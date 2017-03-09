package com.sjqy.pibs;

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

@Service("PPrepareBatchTransAction")
public class PPrepareBatchTransAction extends AbstractParentBatch{

	@Autowired
	private @Qualifier("PPrepare")DataMigrationAction pPrepare;

	@Override
	public void execute(Map<String,Object> context){
		System.out.println("--插入需迁移个人网银客户信息--");
		insertPibsCifNo(context);
		System.out.println("Initiallize complete");
	}
	private void insertPibsCifNo(Map context){
		String fileName="PUser.txt";
		ArrayList<String> strList=new ArrayList<String>();
		try {
			File file=new File("PUser.txt");
			InputStreamReader in=new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(in);
			String str="";
			int n=1;
			while((str = br.readLine()) != null){
				//去除UTF-8文件的头三个bytes
				if(n==1&&(str.getBytes()[0]==-17&&str.getBytes()[1]==-69&&str.getBytes()[2]==-65)){
					str=new String(Arrays.copyOfRange(str.getBytes(),3,str.getBytes().length));
				}
				if("".equals(str))//不算空行
					continue;
				strList.add(str);
				if(n%number==0){
					final Map<String,Object> trsContext=new HashMap<String,Object>();
					trsContext.put("List", strList);
					trsContext.put("transCode", context.get("transCode"));
					this.pool.execute(new DataThread(pPrepare, trsContext));
					strList=new ArrayList<String>();
				}
				n++;
			}
			System.out.println(n);
			if(strList.size()>0){
				final Map<String,Object> trsContext=new HashMap<String,Object>();
				trsContext.put("List", strList);
				trsContext.put("transCode", context.get("transCode"));
				this.pool.execute(new DataThread(pPrepare, trsContext));
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
