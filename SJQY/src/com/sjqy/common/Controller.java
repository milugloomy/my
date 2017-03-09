package com.sjqy.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("Controller")
public class Controller implements ApplicationContextAware{
	protected Log  log = LogFactory.getLog(getClass());
	protected ApplicationContext applicationContext;
	
	@Autowired
	private @Qualifier("StartAction")DataMigrationAction startAction;
	@Autowired
	private @Qualifier("EndAction")DataMigrationAction endAction;
	
	public void start(String trans) throws Exception{
		System.out.println("Controller start");
		Map<String,Object> context = new HashMap<String,Object>();
		context.put("transCode", trans);
		DataMigrationAction transAction=(DataMigrationAction)applicationContext.getBean(trans+"BatchTransAction");
		
		startAction.execute(context);
		transAction.execute(context);
		endAction.execute(context);
		
	}

	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext=applicationContext;
	}
	
}
