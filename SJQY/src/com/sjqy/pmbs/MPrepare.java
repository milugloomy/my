package com.sjqy.pmbs;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sjqy.common.DataMigrationAction;

@Service("MPrepare")
public class MPrepare extends DataMigrationAction{
	
	@Override
	public void execute(Map<String,Object> context){
		
		ArrayList<Object[]> cifList=new ArrayList<Object[]>();
		ArrayList<String> cifNoList=(ArrayList<String>) context.get("List");
		for(int i=0;i<cifNoList.size();i++){
			String cifNo=cifNoList.get(i);
			int count =(Integer) this.sqlMapMOBILE.queryForObject("mobile.queryValidCif",cifNo);
			if(count==0){
				log.error("客户号为"+cifNo+"的客户不存在");
				continue;
			}
			String cifSeq=(String) this.sqlMapECIF.queryForObject("ecif.queryCifSeqByCifNo",cifNo);
			String newCif="N";
			if(cifSeq==null){
				cifSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryPCifSeq");
				newCif="Y";
			}
			cifList.add(new Object[]{cifSeq,cifNo,newCif});
		}
		this.batchAction.excuteBatch(sqlMapECIF, insertTMPDATA, cifList);
	}

	private final String insertTMPDATA ="insert into MTMPDATA"
			+ "(CIFSEQ,CIFNO,NEWCIF,USERFLAG,ACCOUNTFLAG,PAYEEFLAG)"
			+ "values(?,?,?,'NT','NT','NT')";

}
