package com.sjqy.common;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service("StartAction")
public class StartAction extends DataMigrationAction{
	
	private Map<String,String> modeNameMap;

	public void execute(Map<String,Object> context){
		Timestamp startTime=new Timestamp(System.currentTimeMillis());
		
		System.err.println("------------迁移"+modeNameMap.get(context.get("transCode"))+"开始！-----------");
		System.out.println("起始时间:"+startTime);

		String timeString=startTime.toString();
		timeString=timeString.substring(timeString.indexOf("-")+1);
		timeString=timeString.substring(0, timeString.indexOf("."));
		timeString=timeString.replace(" ", "").replace(":", "").replace("-", "");
		
		String transCode=(String) context.get("transCode");
		
		Map<String,Object> insertMap=new HashMap<String,Object>();
		insertMap.put("StartTime", startTime);
		insertMap.put("TransCode", transCode);
		insertMap.put("Index", timeString);
		
		this.getSqlMapTMP().insert("ecif.insertStartTime",insertMap);
		context.put("TimerString", timeString);
		context.put("StartTime", startTime);
	}

	@PostConstruct
	public void preInit() {
		modeNameMap=new HashMap<String,String>();
		modeNameMap.put("PPrepare","个人网上银行创建临时表");	
		modeNameMap.put("PUser","个人网上银行用户信息");	
		modeNameMap.put("PAccount","个人网上银行账户信息");	
		modeNameMap.put("PPayee","个人网上银行收款人信息");	
		modeNameMap.put("POther","个人网上银行其他信息");	
		modeNameMap.put("EPrepare","企业网上银行创建临时表");	
		modeNameMap.put("Euser","企业网上银行客户信息");	
		modeNameMap.put("EAccount","企业网上银行账户信息");	
		modeNameMap.put("ERole","企业网上银行角色信息");	
		modeNameMap.put("EPayee","企业网上银行收款人信息");	
		modeNameMap.put("EOther","企业网上银行其他信息");
		modeNameMap.put("MPrepare","手机银行创建临时表");	
		modeNameMap.put("MUser","手机银行用户信息");	
		modeNameMap.put("MAccount","手机银行账户信息");	
		modeNameMap.put("MPayee","手机银行收款人信息");	
	}

}
