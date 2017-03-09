package com.sjqy.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;

@Service("AbstractParentBatch")
public abstract class AbstractParentBatch extends DataMigrationAction{
	
	int poolNum=0;//�߳����ֵ����
	
	protected int defaultSize;//ִ�������߳���
	protected int queueSize;//�Ŷ������߳���
	
	protected int number;//ÿ�β�ѯ��Ǩ�Ƽ�¼��
	
	public ThreadPoolExecutor pool; 
	
	/**��list�пͻ�״̬�ó�FL*/
	public abstract List<Map> beforeThreadRun();
	/**��ʼ���̳߳�*/
	@PostConstruct
	public void initPool(){
		Properties property=DataUtil.getConfig("/config/dsconfig.properties");
		this.queueSize=this.defaultSize=Integer.valueOf(property.getProperty("maximumPoolSize"));//ִ�������߳���
		this.number=Integer.valueOf(property.getProperty("migNumber"));//ÿ�β�ѯ��Ǩ�Ƽ�¼��
		this.pool= new ThreadPoolExecutor(
				0, this.defaultSize, 20L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(this.queueSize),
				new ThreadFactory(){
			        public Thread newThread(Runnable runnable){
			            return new Thread(runnable, "BatchThreadPool"+"-"+poolNum++);
			        }
			    },
		        new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	public void execute(DataMigrationAction trans,Map<String,Object> context){
		if(trans==null)
			throw new RuntimeException("trans can not be null");
		while(true){
			List<Map> list=beforeThreadRun();
			if(list!=null && list.size()>0){
				final Map<String,Object> trsContext=new HashMap<String,Object>();
				trsContext.put("List", list);
				trsContext.put("transCode", context.get("transCode"));
				this.pool.execute(new DataThread(trans, trsContext));
			}else{
				break;
			}
		}
		
		while(this.pool.getActiveCount()>0){
			try {
				this.pool.awaitTermination(5000l, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Map> queryTMPList(String flag,String sql,int number){
		Map<String, Object> tmpMap=new HashMap<String, Object>();
		tmpMap.put("Flag", flag);//DT-����Ǩ�ƣ�NT-δǨ�ƣ�FL-Ǩ��ʧ�ܣ�OK-Ǩ�Ƴɹ�
		tmpMap.put("Number", number);
		return this.getSqlMapTMP().queryForList(sql,tmpMap);
	}
	
	public void queryResult(String sql){
		Map<String, String> queryMap=new HashMap<String, String>();
		//�ɹ���¼ͳ��
		queryMap.put("FLAG", "OK");//DT-����Ǩ�ƣ�NT-δǨ�ƣ�FL-Ǩ��ʧ�ܣ�OK-Ǩ�Ƴɹ�
		String succRec=(String) this.getSqlMapTMP().queryForObject(sql,queryMap);
		//ʧ�ܼ�¼ͳ��
		queryMap.put("FLAG", "FL");//DT-����Ǩ�ƣ�NT-δǨ�ƣ�FL-Ǩ��ʧ�ܣ�OK-Ǩ�Ƴɹ�
		String failRec=(String) this.getSqlMapTMP().queryForObject(sql,queryMap);

		System.out.println("Ǩ�Ƴɹ���"+succRec+"λ�ͻ��ļ�¼");
		System.out.println("Ǩ��ʧ�ܣ�"+failRec+"λ�ͻ��ļ�¼");
	}
}
