package com.sjqy.common;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;

@Service("DataMigrationAction")
public abstract class DataMigrationAction{
	protected Log log=LogFactory.getLog(getClass());

	@Autowired
	protected BatchAction batchAction;
	
	@Autowired
	protected @Qualifier("SqlMapOld")SqlMapClientTemplate sqlMapOLD;

	@Autowired
	protected @Qualifier("SqlMapMobile")SqlMapClientTemplate sqlMapMOBILE;

	@Autowired
	protected @Qualifier("SqlMapEcif")SqlMapClientTemplate sqlMapECIF;

	@Autowired
	protected @Qualifier("SqlMapEip")SqlMapClientTemplate sqlMapEIP;

	@Autowired
	protected @Qualifier("SqlMapIbs")SqlMapClientTemplate sqlMapIBS;

	//临时表所在数据源
	public SqlMapClientTemplate getSqlMapTMP(){
		return this.sqlMapECIF;
	}

	//子类需重写此方法
	public abstract void execute(Map<String,Object> context);
}
