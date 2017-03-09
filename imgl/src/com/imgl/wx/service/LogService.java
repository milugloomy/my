package com.imgl.wx.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.imgl.wx.model.BackJnl;
import com.imgl.wx.model.Jnl;
import com.imgl.wx.model.PayJnl;

@Repository("logService")
@Transactional
public class LogService {
	private int pageLimit=10;
	@Autowired
	private HibernateTemplate dao;
	
	public void saveBackJnl(BackJnl backJnl){
		dao.save(backJnl);
	}

	public List<BackJnl> queryBackLog(Timestamp beginTime, Timestamp endTime, int pageNo) {
		String sql="from BackJnl where transtime>? and transtime<?";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setTimestamp(0, beginTime);
		query.setTimestamp(1, endTime);
		query.setFirstResult((pageNo-1)*pageLimit);
		query.setMaxResults(pageLimit);
		List<BackJnl> list=query.list();
		return list;
	}
	
	public int queryBackLogTotal(Timestamp beginTime, Timestamp endTime) {
		String sql="select count(*) from BackJnl where transtime>? and transtime<?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setTimestamp(0, beginTime);
		sqlQuery.setTimestamp(1, endTime);
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}

	public void saveFrontJnl(Jnl jnl) {
		dao.save(jnl);
	}


}
