package com.sjqy.eibs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;

@Service("EUserBatchTransAction")
public class EUserBatchTransAction extends AbstractParentBatch{

	private @Qualifier("EUser") DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){
		
		super.execute(trans, context);

		this.queryResult("ecif.queryEUserRecord");
	}
	 
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryEUserTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateEUserTmpData, DataUtil.tmpEList(list,"FL"));
		return list;
	}

}