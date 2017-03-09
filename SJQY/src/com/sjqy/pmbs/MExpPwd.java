package com.sjqy.pmbs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;

@Service("MExpPwdBatchTransAction")
public class MExpPwd  extends AbstractParentBatch{

	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}
	
	private int number=1000;
	
	@Override
	public void execute(Map<String, Object> context) {
		Properties p=DataUtil.getConfig("/config/dsconfig.properties");
		File file=new File(p.getProperty("mexpPath"));
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("MChannel", "PMBS");
			int total=(Integer) this.sqlMapECIF.queryForObject("ecif.queryTotalMCif",paraMap);
			int startNum=0;
			while(startNum<total){
				StringBuffer sb=new StringBuffer();
				paraMap.put("StartNum", startNum);
				paraMap.put("EndNum", startNum+number);
				List<Map> list=this.sqlMapECIF.queryForList ("ecif.querySecMcif",paraMap);
				for(int i=0;i<list.size();i++){
					Map cifMap=list.get(i);
					BigDecimal cifSeq=(BigDecimal) cifMap.get("CIFSEQ");
					String password=(String) cifMap.get("PASSWORD");
					String cifNo=(String) cifMap.get("CIFNO");
					sb.append(cifSeq+"|"+cifNo+"|"+password+"\n");
				}
				bw.write(sb.toString());
				bw.flush();
				System.out.println("-----ÒÑÐ´Èë"+(startNum+list.size())+"ÐÐ-----");
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
