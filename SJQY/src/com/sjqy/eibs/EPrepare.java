package com.sjqy.eibs;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sjqy.common.DataMigrationAction;

@Service("EPrepare")
public class EPrepare extends DataMigrationAction{
	
	@Override
	public void execute(Map<String,Object> context){
		
		//只迁移客户状态user_status01为0、1的客户，卡类型为31','92','95','9B','AA'：的客户。
		ArrayList<Object[]> cifList=new ArrayList<Object[]>();
		ArrayList<String> strList=(ArrayList<String>) context.get("List");
		for(int i=0;i<strList.size();i++){
			String[] content=strList.get(i).split("\\|");
			String cifNo=content[0];
			//没有有效账户类型的账户的客户不迁移
			//专业版客户
			int res=(Integer) this.sqlMapOLD.queryForObject("old.queryValidECif1", cifNo);
			if(res==0){
				//大众版客户
				res=(Integer) this.sqlMapOLD.queryForObject("old.queryValidECif2", cifNo);
				if(res==0)
					continue;
			}
			String idType="E00";
			String idNo=cifNo;
			if(content.length>2){
				idType=content[1];
				idNo=content[2];
			}
			cifList.add(new Object[]{cifNo,idType,idNo});
		}
		
		this.batchAction.excuteBatch(sqlMapECIF, insertTMPDATA, cifList);

	}

	private final String insertTMPDATA = "insert into TMPDATA"
			+ "(CIFSEQ,CIFNO,IDTYPE,IDNO,MODULEID,USERFLAG,ACCOUNTFLAG,PAYEEFLAG,ROLEFLAG)"
			+ "values(CIFSEQ.NEXTVAL,?,?,?,'ent','NT','NT','NT','NT')";

}
