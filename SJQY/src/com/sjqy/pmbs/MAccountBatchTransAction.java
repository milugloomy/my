package com.sjqy.pmbs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;

@Service("MAccountBatchTransAction")
public class MAccountBatchTransAction extends AbstractParentBatch{
	
	@Autowired
	private @Qualifier("MAccount")DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){
		
		super.execute(trans, context);

		this.queryResult("ecif.queryMAccountRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryMAccountTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMAccountTmpData, DataUtil.tmpMList(list,"FL"));
		return list;
	}

}
