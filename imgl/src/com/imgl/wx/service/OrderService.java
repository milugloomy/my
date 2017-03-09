package com.imgl.wx.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.imgl.wx.model.PayJnl;

@Repository("orderService")
public class OrderService {
	
	private int pageLimit=2;
	private int backPageLimit=10;
	
	@Autowired
	private HibernateTemplate dao;

	public List<PayJnl> queryOrderList(int pageNo,int userSeq) {
		String sql="from PayJnl where userseq=? order by payTime desc";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setInteger(0, userSeq);
		query.setFirstResult(pageNo*pageLimit);
		query.setMaxResults(pageLimit);
		List<PayJnl> list=query.list();
		return list;
	}
	public List<PayJnl> queryOrderList(Timestamp beginTime, Timestamp endTime, String payStatus, int pageNo) {
		Query query=null;
		if(payStatus==null || payStatus.equals("ALL")){
			String sql="from PayJnl where payTime>? and payTime<? order by payTime desc";
			query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
			query.setTimestamp(0, beginTime);
			query.setTimestamp(1, endTime);
		}else{
			String sql="from PayJnl where payTime>? and payTime<? and payStatus=? order by payTime desc";
			query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
			query.setTimestamp(0, beginTime);
			query.setTimestamp(1, endTime);
			query.setString(2, payStatus);
		}
		query.setFirstResult((pageNo-1)*backPageLimit);
		query.setMaxResults(backPageLimit);
		List<PayJnl> list=query.list();
		return list;
	}

	public void updatePayJnlState(int pjnlNo, String payStatus) {
		String sql="update payjnl set payStatus=? where pjnlno=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setString(0, payStatus.toString());
		sqlQuery.setInteger(1, pjnlNo);
		sqlQuery.executeUpdate();
	}

	public PayJnl queryOrder(int pJnlNo) {
		PayJnl payJnl=(PayJnl) dao.get(PayJnl.class, pJnlNo);
		return payJnl;
	}

	public int queryPJnlNoByGroupId(int groupId, int userSeq) {
		String sql="select pJnlNo from payjnl where groupId=? and userSeq=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, groupId);
		sqlQuery.setInteger(1, userSeq);
		int pJnlNO=(int) sqlQuery.uniqueResult();
		return pJnlNO;
	}
	public List<PayJnl> queryOrderListByGroupId(int groupId) {
		List<PayJnl> list=(List<PayJnl>) dao.find("from PayJnl where groupId=? order by payTime", new Object[]{groupId});
		return list;
	}

	public void write(PayJnl payJnl) {
		dao.save(payJnl);
	}

	public boolean checkUser(Integer groupId, int userSeq) {
		String sql="from PayJnl where groupId=? and userSeq=?";
		List<PayJnl> list=(List<PayJnl>) dao.find(sql, new Object[]{groupId,userSeq});
		if(list.size()==0)
			return false;
		else
			return true;
	}

	public void updateGroupStatus(int groupId,String payStatus) {
		String sql="update payjnl set payStatus=? where groupId=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setString(0, payStatus);
		sqlQuery.setInteger(1, groupId);
		sqlQuery.executeUpdate();
		
	}
	public int queryOrderTotal(Timestamp beginTime, Timestamp endTime, String payStatus) {
		SQLQuery sqlQuery=null;
		if(payStatus==null || payStatus.equals("ALL")){
			String sql="select count(*) from PayJnl where payTime>? and payTime<?";
			sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			sqlQuery.setTimestamp(0, beginTime);
			sqlQuery.setTimestamp(1, endTime);
		}else{
			String sql="select count(*) from PayJnl where payTime>? and payTime<? and payStatus=?";
			sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			sqlQuery.setTimestamp(0, beginTime);
			sqlQuery.setTimestamp(1, endTime);
			sqlQuery.setString(2, payStatus);
		}
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}
	public void updatePayJnl(PayJnl payJnl) {
		dao.save(payJnl);
	}

}
