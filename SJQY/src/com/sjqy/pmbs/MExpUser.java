package com.sjqy.pmbs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sjqy.common.AbstractParentBatch;

@Service("MExpUserBatchTransAction")
public class MExpUser  extends AbstractParentBatch{

	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}
	
	private int number=1000;
	
	@Override
	public void execute(Map<String, Object> context) {
		BufferedWriter bw=null;
		try {
			File file=new File("MUser.txt");
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("MChannel", "PMBS");
			int total=(Integer) this.sqlMapOLD.queryForObject("old.queryMoveCifCount",paraMap);
			int startNum=0;
			while(startNum<total){
				StringBuffer sb=new StringBuffer();
				paraMap.put("StartNum", startNum);
				paraMap.put("EndNum", startNum+number);
				List<Map> list=this.sqlMapOLD.queryForList ("old.queryMoveCif",paraMap);
				for (Map map : list) {
					String cifNo=(String) map.get("CUST_NO");
					sb.append(cifNo+"\n");
					
				}
				bw.write(sb.toString());
				bw.flush();
				System.out.println("-----已写入客户名单"+(startNum+list.size())+"行-----");
				startNum+=number;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
		
}
