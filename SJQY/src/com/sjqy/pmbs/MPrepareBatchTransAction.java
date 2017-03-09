package com.sjqy.pmbs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.common.DataThread;

@Service("MPrepareBatchTransAction")
public class MPrepareBatchTransAction extends AbstractParentBatch{

	@Autowired
	private @Qualifier("MPrepare")DataMigrationAction mPrepare;

	@Override
	public void execute(Map<String,Object> context){
		System.out.println("--������Ǩ���ֻ��ͻ���Ϣ--");
		insertMobileCifNo(context);
		System.out.println("Initiallize complete");
	}
	private void insertMobileCifNo(Map context){
		ArrayList<String> strList=new ArrayList<String>();
		try {
			File file=new File("MUser.txt");
			InputStreamReader in=new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(in);
			String str="";
			int n=1;
			while((str = br.readLine()) != null){
				//ȥ��UTF-8�ļ���ͷ����bytes
				if(n==1&&(str.getBytes()[0]==-17&&str.getBytes()[1]==-69&&str.getBytes()[2]==-65)){
					str=new String(Arrays.copyOfRange(str.getBytes(),3,str.getBytes().length));
				}
				if("".equals(str))//�������
					continue;
				strList.add(str);
				if(n%number==0){
					final Map<String,Object> trsContext=new HashMap<String,Object>();
					trsContext.put("List", strList);
					trsContext.put("transCode", context.get("transCode"));
					this.pool.execute(new DataThread(mPrepare, trsContext));
					strList=new ArrayList<String>();
				}
				n++;
			}
			System.out.println(n);
			if(strList.size()>0){
				final Map<String,Object> trsContext=new HashMap<String,Object>();
				trsContext.put("List", strList);
				trsContext.put("transCode", context.get("transCode"));
				this.pool.execute(new DataThread(mPrepare, trsContext));
			}
			
		} catch (FileNotFoundException e){
			System.err.println("-----�ļ�MUser.txt������------");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		/*int total=(Integer) this.sqlMapMOBILE.queryForObject("mobile.queryTotalCif");
//		total=500;//���Բ���500���ͻ���¼
		int startNum=0;
		while(startNum<total){
			final Map<String,Object> trsContext=new HashMap<String,Object>();
			trsContext.put("StartNum", startNum);
			trsContext.put("EndNum", startNum+number);
			this.pool.execute(new DataThread(mPrepare, trsContext));
			startNum+=number;
		}

		while(this.pool.getActiveCount()>0){
			try {
				this.pool.awaitTermination(5000l, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}

}
