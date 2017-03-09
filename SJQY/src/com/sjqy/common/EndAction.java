package com.sjqy.common;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("EndAction")
public class EndAction extends DataMigrationAction {

	@Override
	public void execute(Map<String,Object> context){
		Timestamp endTime=new Timestamp(System.currentTimeMillis());
		Timestamp startTime=(Timestamp) context.get("StartTime");
		Double totalMinute=(double)Math.round((endTime.getTime()-startTime.getTime())/60000.00*100)/100;
		
		Map<String,Object> updateMap=new HashMap<String,Object>();
		updateMap.put("EndTime", endTime);
		updateMap.put("Index", context.get("TimerString"));
		updateMap.put("TotalMinute", totalMinute);
		
		this.getSqlMapTMP().update("ecif.updateTMPTIME",updateMap);
		
		System.out.println("结束时间:"+endTime);
		System.out.println("总用时："+totalMinute+"分钟");
		System.err.println("----------------迁移结束----------------");
	}
}
