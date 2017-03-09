package com.sjqy.pibs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;

@Service("PImpPwdBatchTransAction")
public class PImpPwd  extends AbstractParentBatch{

	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}
	
	private int number=1000;
	
	@Override
	public void execute(Map<String, Object> context) {
		List<Object[]> list=new ArrayList<Object[]>();
		/*for(int i=0;i<5020;i++){
			list.add(new Object[]{i+1,"a"+i});
		}
		this.batchAction.excuteBatch(sqlMapECIF, "insert into TTTT values(?,?)", list);*/
		/*for(int i=0;i<5020;i++){
			list.add(new Object[]{"b"+i,i+1});
		}
		this.batchAction.excuteBatch(sqlMapECIF, "update TTTT set name=? where id=?", list);*/
		
		Properties p=DataUtil.getConfig("/config/dsconfig.properties");
		File file=new File(p.getProperty("pimpPath"));
		BufferedReader br=null;
		List<Object[]> cifList=new ArrayList<Object[]>();
		try {
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String s="";
			int count=0;
			while((s=br.readLine())!=null){
				count++;
				String[] para=s.split("\\|");
				String cifSeq=para[0];
				String succFlag=para[1];
				if(succFlag.equals("S")){
					String password=para[2].toLowerCase();
					cifList.add(new Object[]{password,cifSeq});
				}else{
					log.error("客户号为"+cifSeq+"的密码加密失败");
				}
				if(count%number==0){//每number条插入
					this.batchAction.excuteBatch(sqlMapECIF, updateECUSRMCH, cifList);
					cifList=new ArrayList<Object[]>();
					System.out.println(count+"行已更新");
				}
			}
			this.batchAction.excuteBatch(sqlMapECIF, updateECUSRMCH, cifList);
			System.out.println(count+"行已更新");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private final String updateECUSRMCH ="update ECUSRMCH set "
			+ "password=? where mchannelid='PIBS' "
			+ "and userseq =(select userseq from ecusr where cifseq=?)";
}
