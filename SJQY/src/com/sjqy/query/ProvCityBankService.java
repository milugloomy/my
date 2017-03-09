package com.sjqy.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;
@Service("ProvCityBankService")
public class ProvCityBankService {
	
	@Autowired
	private @Qualifier("SqlMapEip")SqlMapClientTemplate sqlMapEIP;
	private Map<String,Map> cachedCityProvMap;
	private Map<String,Map> cachedBankNameMap;

	public Map queryCityProvByCityCode(String cityCode){
		if(cachedCityProvMap==null)
			cachedCityProvMap=this.sqlMapEIP.queryForMap("ibs.queryAllCITYANDPROV", null,"CITYCD");
		return cachedCityProvMap.get(cityCode);
	}
	
	public Map queryBankNameByBankId(String bankId){
		if(cachedBankNameMap==null)
			cachedBankNameMap=this.sqlMapEIP.queryForMap("ibs.queryAllBANKNAME",null,"BANKNO");
		return cachedBankNameMap.get(bankId);
	}

}
