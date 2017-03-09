package com.sjqy.pibs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;

@Service("PUserBatchTransAction")
public class PUserBatchTransAction extends AbstractParentBatch{
	
	@Autowired
	private @Qualifier("PUser")DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){
		
		super.execute(trans, context);

		this.queryResult("ecif.queryPUserRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryPUserTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updatePUserTmpData, DataUtil.tmpPList(list,"FL"));
		return list;
	}

}
