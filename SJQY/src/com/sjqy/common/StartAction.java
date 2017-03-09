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
		
		System.err.println("------------Ǩ��"+modeNameMap.get(context.get("transCode"))+"��ʼ��-----------");
		System.out.println("��ʼʱ��:"+startTime);

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
		modeNameMap.put("PPrepare","�����������д�����ʱ��");	
		modeNameMap.put("PUser","�������������û���Ϣ");	
		modeNameMap.put("PAccount","�������������˻���Ϣ");	
		modeNameMap.put("PPayee","�������������տ�����Ϣ");	
		modeNameMap.put("POther","������������������Ϣ");	
		modeNameMap.put("EPrepare","��ҵ�������д�����ʱ��");	
		modeNameMap.put("Euser","��ҵ�������пͻ���Ϣ");	
		modeNameMap.put("EAccount","��ҵ���������˻���Ϣ");	
		modeNameMap.put("ERole","��ҵ�������н�ɫ��Ϣ");	
		modeNameMap.put("EPayee","��ҵ���������տ�����Ϣ");	
		modeNameMap.put("EOther","��ҵ��������������Ϣ");
		modeNameMap.put("MPrepare","�ֻ����д�����ʱ��");	
		modeNameMap.put("MUser","�ֻ������û���Ϣ");	
		modeNameMap.put("MAccount","�ֻ������˻���Ϣ");	
		modeNameMap.put("MPayee","�ֻ������տ�����Ϣ");	
	}

}
