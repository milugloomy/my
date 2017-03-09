package com.sjqy.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;
@Service("LimitService")
public class LimitService {
	
	@Autowired
	private @Qualifier("SqlMapEip")SqlMapClientTemplate sqlMapEIP;
	private Map<String,Map> cachedCifLimitMap;
	private List<Map> cachedCifLimitList;
	
	
	public Map queryCifLimit(String limitType,String mchannel){
		if(cachedCifLimitMap==null){
			cachedCifLimitMap=new HashMap<String,Map>();
			cachedCifLimitList=sqlMapEIP.queryForList("eip.queryAllCifLimit",null);
			for (int i = 0; i < cachedCifLimitList.size(); i++) {
				String key=(String)cachedCifLimitList.get(i).get("MCHANNELID")+(String)cachedCifLimitList.get(i).get("LIMITTYPE");
				cachedCifLimitMap.put(key, cachedCifLimitList.get(i));
			}
		}
		
		return cachedCifLimitMap.get(mchannel+limitType);
	}
}
