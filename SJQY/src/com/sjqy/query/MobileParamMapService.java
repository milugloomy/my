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
			loginTypeMap.put("W", "Y");//开通转账
			loginTypeMap.put("X", "N");//未开通
		}
		return loginTypeMap.get(oldLoginType);
	}
	public String getUserLockStateMap(String oldUserLockState){
		if(userLockStateMap==null){
			userLockStateMap=new HashMap<String, String>();
			userLockStateMap.put("0", "N");//正常
			userLockStateMap.put("1", "L");//被锁
		}
		return userLockStateMap.get(oldUserLockState);
	}
	public String getUserState(String oldUserState){
		if(userStateMap==null){
			userStateMap=new HashMap<String, String>();
			userStateMap.put("0", "N");//正常
			userStateMap.put("1", "C");//注销
			userStateMap.put("2", "S");//暂停
			userStateMap.put("9", "N");//未登录
		}
		return userStateMap.get(oldUserState);
	}
	public String getBankAcType(String oldBankAcType){
		if(bankAcTypeMap==null){
			bankAcTypeMap=new HashMap<String, String>();
			bankAcTypeMap.put("01", "PDBC");//借记卡
			bankAcTypeMap.put("02", "PSDP");//存折
			bankAcTypeMap.put("03", "PCRC");//信用卡
		}
		return bankAcTypeMap.get(oldBankAcType);
	}
	public String getCrFlag(String oldCrFlag){
		if(crFlagMap==null){
			crFlagMap=new DataUtil.MyMap<String, String>("C");
			crFlagMap.put("1","R");//汇
			crFlagMap.put("2","C");//钞
		}
		return crFlagMap.get(oldCrFlag);
	}
}
