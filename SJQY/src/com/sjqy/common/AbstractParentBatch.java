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
	
	int poolNum=0;//线程名字的序号
	
	protected int defaultSize;//执行任务线程数
	protected int queueSize;//排队任务线程数
	
	protected int number;//每次查询待迁移记录数
	
	public ThreadPoolExecutor pool; 
	
	/**将list中客户状态置成FL*/
	public abstract List<Map> beforeThreadRun();
	/**初始化线程池*/
	@PostConstruct
	public void initPool(){
		Properties property=DataUtil.getConfig("/config/dsconfig.properties");
		this.queueSize=this.defaultSize=Integer.valueOf(property.getProperty("maximumPoolSize"));//执行任务线程数
		this.number=Integer.valueOf(property.getProperty("migNumber"));//每次查询待迁移记录数
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
		tmpMap.put("Flag", flag);//DT-不用迁移，NT-未迁移，FL-迁移失败，OK-迁移成功
		tmpMap.put("Number", number);
		return this.getSqlMapTMP().queryForList(sql,tmpMap);
	}
	
	public void queryResult(String sql){
		Map<String, String> queryMap=new HashMap<String, String>();
		//成功记录统计
		queryMap.put("FLAG", "OK");//DT-不用迁移，NT-未迁移，FL-迁移失败，OK-迁移成功
		String succRec=(String) this.getSqlMapTMP().queryForObject(sql,queryMap);
		//失败记录统计
		queryMap.put("FLAG", "FL");//DT-不用迁移，NT-未迁移，FL-迁移失败，OK-迁移成功
		String failRec=(String) this.getSqlMapTMP().queryForObject(sql,queryMap);

		System.out.println("迁移成功："+succRec+"位客户的记录");
		System.out.println("迁移失败："+failRec+"位客户的记录");
	}
}
