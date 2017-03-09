package com.sjqy.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataThread extends BatchAction implements Runnable{
	private Log log=LogFactory.getLog(getClass());
	
	private DataMigrationAction dma;
	private List<DataMigrationAction> transList;
	private Map<String,Object> trsContext;
	
	public DataThread(List<DataMigrationAction> transList,Map<String,Object> trsContext) {
		this.transList=transList;
		this.trsContext=trsContext;
	}
	
	public DataThread(DataMigrationAction dma,Map<String,Object> trsContext) {
		this.dma=dma;
		this.trsContext=trsContext;
	}

	public void run() {
		try {
			trsContext.put("currentThread", Thread.currentThread().getName());
			if(transList!=null){
				for (int j = 0; j < transList.size(); j++) {
					DataMigrationAction transAction = transList.get(j);
					transAction.execute(trsContext);
				}
			}else{
				dma.execute(trsContext);
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
