package com.imgl.wx.business;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public abstract class AutoDataRefresh{

	@Autowired
	protected MessageSource messageSource;

	private Long lastLoadTime;
	protected Long period;
	protected Log log;
	private ReentrantReadWriteLock rwl;
	
	public AutoDataRefresh() {
		log=LogFactory.getLog(getClass());
		lastLoadTime=0l;
		rwl=new ReentrantReadWriteLock();
	}
	
	@PostConstruct
	public abstract void setPeriod();
	public abstract void updateCache();
	
	public void lockCache(){
		if((System.currentTimeMillis()-lastLoadTime)>period){
			rwl.writeLock().lock();
			try{
				lastLoadTime=System.currentTimeMillis();
				updateCache();
			}finally{
				rwl.writeLock().unlock();
			}
		}
		rwl.readLock().lock();
	}
	public void unlockCache(){
		rwl.readLock().unlock();
	}

	public void setLastLoadTime(Long lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}

}
