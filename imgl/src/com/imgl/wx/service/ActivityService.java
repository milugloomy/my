package com.imgl.wx.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.imgl.wx.model.Activity;

@Repository("activityService")
public class ActivityService {
	
	private int pageLimit=2;
	private int backPageLimit=10;
	
	@Autowired
	private HibernateTemplate dao;

	public List<Activity> queryActivityList(int pageNo) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sql="from Activity where ?>startTime and status='N' order by startTime desc";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setTimestamp(0, now);
		query.setFirstResult(pageNo*pageLimit);
		query.setMaxResults(pageLimit);
		List<Activity> list=query.list();
		return list;
	}

	public Activity queryActivity(int activityid) {
		Activity activity=dao.get(Activity.class, activityid);
		return activity;
	}

	public void addQuantity(int activityId) {
		String sql="update Activity set remain=remain+1 where activityId=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, activityId);
		sqlQuery.executeUpdate();
	}

	public void decrQuantity(int activityId) {
		String sql="update Activity set remain=remain-1 where activityId=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, activityId);
		sqlQuery.executeUpdate();
	}

	public List<Activity> queryBackActivityList(int pageNo) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sql="from Activity where ?>startTime and status='N' order by startTime desc";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setTimestamp(0, now);
		query.setFirstResult((pageNo-1)*backPageLimit);
		query.setMaxResults(backPageLimit);
		List<Activity> list=query.list();
		return list;
	}
	public int queryActivityTotal() {
		String sql="select count(*) from Activity";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}

	public void deleteActivity(int activityId) {
		String sql="update Activity set status='C' where activityId=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, activityId);
		sqlQuery.executeUpdate();
	}

	public void updateActivity(Activity currentActivity) {
		dao.update(currentActivity);
	}

	public void addActivity(Activity activity) {
		dao.save(activity);
	}

}
