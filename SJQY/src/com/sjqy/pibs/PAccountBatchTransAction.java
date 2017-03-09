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

@Service("PAccountBatchTransAction")
public class PAccountBatchTransAction extends AbstractParentBatch{
	
	@Autowired
	private @Qualifier("PAccount")DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){
		
		super.execute(trans, context);

		this.queryResult("ecif.queryPAccountRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryPAccountTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updatePAccountTmpData, DataUtil.tmpPList(list,"FL"));
		return list;
	}

}
