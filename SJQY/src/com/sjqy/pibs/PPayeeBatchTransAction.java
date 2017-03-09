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

@Service("PPayeeBatchTransAction")
public class PPayeeBatchTransAction extends AbstractParentBatch{

	@Autowired
	private @Qualifier("PPayee")DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){

		super.execute(trans, context);

		this.queryResult("ecif.queryPPayeeRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryPPayeeTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updatePPayeeTmpData, DataUtil.tmpPList(list,"FL"));
		return list;
	}

}
