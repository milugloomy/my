package com.sjqy.psms;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;

@Service("PSmsBatchTransAction")
public class PSmsBatchTransAction extends AbstractParentBatch{
	
	@Autowired
	private @Qualifier("PSms")DataMigrationAction trans;

	@Override
	public void execute(Map<String,Object> context){
		
		super.execute(trans, context);

		this.queryResult("ecif.queryPSmsRecord");
	}
	
	@Override
	public List<Map> beforeThreadRun() {
		List<Map> list = this.queryTMPList("NT","ecif.queryPSmsTmpData",number);
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMUserTmpData, DataUtil.tmpMList(list,"FL"));
		return list;
	}

}
