package com.sjqy.common;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainEntry {
	
	public static void main(String[] args) throws Exception{
		AbstractApplicationContext ioc=null;
        ioc = new ClassPathXmlApplicationContext("/config/config.xml");
		if(args.length==0){
	        System.err.println("please input args");
			
		//执行单个进程
		}else{
	        Controller mc=(Controller) ioc.getBean("Controller");
	        mc.start(args[0]);
		}
		
        //关闭程序
        ioc.registerShutdownHook();

	}

}
