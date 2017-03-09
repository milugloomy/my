package com.sjqy.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;
 

@Service("DeptService")
public class DeptService{

	@Autowired
	private @Qualifier("SqlMapEip")SqlMapClientTemplate sqlMapEIP;
	private Map<String,Map> cachedDeptMap;
	
	public Map queryDeptById(String id){
		id=id.trim();
		if(cachedDeptMap==null)
			cachedDeptMap=sqlMapEIP.queryForMap("eip.queryAllDept",null,"DEPTID");
		
		return cachedDeptMap.get(id);
	}

}
