package com.sjqy.pibs;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;

@Service("PPrepare")
public class PPrepare extends DataMigrationAction{
	
	public void execute(Map<String,Object> context){
		
		ArrayList<Object[]> cifList=new ArrayList<Object[]>();
		ArrayList<String> cifNoList=(ArrayList<String>) context.get("List");
		for(int i=0;i<cifNoList.size();i++){
			String cifNo=cifNoList.get(i);
			int count =(Integer) this.sqlMapOLD.queryForObject("old.queryValidCif",cifNo);
			if(count==0){
				count=(Integer) this.sqlMapOLD.queryForObject("old.queryValidCif",DataUtil.get15CifNo(cifNo));
			}
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
	//NT未迁移  FL迁移失败  OK迁移成功
	private final String insertTMPDATA = "insert into PTMPDATA"
			+ "(CIFSEQ,CIFNO,NEWCIF,USERFLAG,ACCOUNTFLAG,PAYEEFLAG)"
			+ "values(?,?,?,'NT','NT','NT')";

}
