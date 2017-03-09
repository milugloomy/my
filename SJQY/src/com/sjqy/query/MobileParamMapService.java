package com.sjqy.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;

@Service("MobileParamMapService")
public class MobileParamMapService {

	private Map<String, String> loginTypeMap;
	private Map<String, String> userLockStateMap;
	private Map<String, String> userStateMap;
	private Map<String, String> bankAcTypeMap;
	private Map<String, String> crFlagMap;
	
	public String getLoginTypeMap(String oldLoginType){
		if(loginTypeMap==null){
			loginTypeMap=new HashMap<String, String>();
			loginTypeMap.put("W", "Y");//��ͨת��
			loginTypeMap.put("X", "N");//δ��ͨ
		}
		return loginTypeMap.get(oldLoginType);
	}
	public String getUserLockStateMap(String oldUserLockState){
		if(userLockStateMap==null){
			userLockStateMap=new HashMap<String, String>();
			userLockStateMap.put("0", "N");//����
			userLockStateMap.put("1", "L");//����
		}
		return userLockStateMap.get(oldUserLockState);
	}
	public String getUserState(String oldUserState){
		if(userStateMap==null){
			userStateMap=new HashMap<String, String>();
			userStateMap.put("0", "N");//����
			userStateMap.put("1", "C");//ע��
			userStateMap.put("2", "S");//��ͣ
			userStateMap.put("9", "N");//δ��¼
		}
		return userStateMap.get(oldUserState);
	}
	public String getBankAcType(String oldBankAcType){
		if(bankAcTypeMap==null){
			bankAcTypeMap=new HashMap<String, String>();
			bankAcTypeMap.put("01", "PDBC");//��ǿ�
			bankAcTypeMap.put("02", "PSDP");//����
			bankAcTypeMap.put("03", "PCRC");//���ÿ�
		}
		return bankAcTypeMap.get(oldBankAcType);
	}
	public String getCrFlag(String oldCrFlag){
		if(crFlagMap==null){
			crFlagMap=new DataUtil.MyMap<String, String>("C");
			crFlagMap.put("1","R");//��
			crFlagMap.put("2","C");//��
		}
		return crFlagMap.get(oldCrFlag);
	}
}
