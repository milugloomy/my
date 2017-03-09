package com.sjqy.eibs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;

@Service("EPayeeBatchTransAction")
public class EPayeeBatchTransAction extends AbstractParentBatch{

	private @Qualifier("EPayee") DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){

		super.execute(trans, context);

		this.queryResult("ecif.queryEPayeeRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryEPayeeTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateEPayeeTmpData, DataUtil.tmpEList(list,"FL"));
		return list;
	}

}
