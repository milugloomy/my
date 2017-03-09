package com.sjqy.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;


@Service("BankProdService")
public class BankProdService{

	private @Qualifier("SqlMapEip")SqlMapClientTemplate sqlMapEIP;
	private Map<String,Map> cachedBankProdMap;
	
	public void updateCache(){
		cachedBankProdMap=sqlMapEIP.queryForMap("eip.queryAllEBankProd",null,"PRDID");
	}
	
	public Map queryBankProdById(String id){
		if(cachedBankProdMap==null)
			updateCache();
		
		return cachedBankProdMap.get(id);
	}
	

}
