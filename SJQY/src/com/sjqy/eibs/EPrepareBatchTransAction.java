package com.sjqy.eibs;

import java.io.BufferedReader;
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

import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.common.DataThread;

@Service("EPrepareBatchTransAction")
public class EPrepareBatchTransAction extends AbstractParentBatch{

	@Autowired
	private @Qualifier("EPrepare")DataMigrationAction ePrepare;

	@Override
	public void execute(Map<String,Object> context){
		System.out.println("--插入需迁移企业客户信息--");
		insertECifNo(context);
		System.out.println("Initiallize complete");
	}
	
	public void insertECifNo(Map context){
		String fileName="EUser.txt";
		ArrayList<String> strList=new ArrayList<String>();
		try {
			//jar包中须用InputStreamReader才读的到文件
			InputStreamReader in=new InputStreamReader(DataUtil.class.getResourceAsStream(fileName),"UTF-8");
			BufferedReader br = new BufferedReader(in);
			String str="";
			int n=1;
			while((str = br.readLine()) != null){
				//去除UTF-8文件的头三个bytes
				if(n==1&&(str.getBytes()[0]==-17&&str.getBytes()[1]==-69&&str.getBytes()[2]==-65)){
					str=new String(Arrays.copyOfRange(str.getBytes(),3,str.getBytes().length));
				}
				strList.add(str);
				if(n%number==0){
					final Map<String,Object> trsContext=new HashMap<String,Object>();
					trsContext.put("List", strList);
					trsContext.put("transCode", context.get("transCode"));
					this.pool.execute(new DataThread(ePrepare, trsContext));
					strList=new ArrayList<String>();
				}
				n++;
			}
			if(strList.size()>0){
				final Map<String,Object> trsContext=new HashMap<String,Object>();
				trsContext.put("List", strList);
				trsContext.put("transCode", context.get("transCode"));
				this.pool.execute(new DataThread(ePrepare, trsContext));
			}
			
		} catch (FileNotFoundException e){
			System.err.println("-----文件"+fileName+"不存在------");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}

}
