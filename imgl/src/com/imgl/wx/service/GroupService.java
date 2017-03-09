package com.imgl.wx.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.imgl.wx.model.GroupJnl;

@Repository("groupService")
public class GroupService {
	
	private int pageLimit=5;
	private int backPageLimit=10;
	
	@Autowired
	private HibernateTemplate dao;

	public List<GroupJnl> queryGroupList(int pageNo,int userSeq) {
		String sql="from GroupJnl a where a.groupId in"
				+ "(select groupId from PayJnl where userSeq=?) order by startTime desc";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setInteger(0, userSeq);
		query.setFirstResult(pageNo*pageLimit);
		query.setMaxResults(pageLimit);
		List<GroupJnl> list=query.list();
		return list;
	}
	
	public List<GroupJnl> queryGroupList(Timestamp beginTime, Timestamp endTime, String status, int pageNo) {
		Query query=null;
		if(status==null || status.equals("ALL")){
			String sql="from GroupJnl where startTime>? and startTime<? order by startTime desc";
			query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
			query.setTimestamp(0, beginTime);
			query.setTimestamp(1, endTime);
		}else{
			String sql="from GroupJnl where startTime>? and startTime<? and status=? order by startTime desc";
			query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
			query.setTimestamp(0, beginTime);
			query.setTimestamp(1, endTime);
			query.setString(2, status);
		}
		query.setFirstResult((pageNo-1)*backPageLimit);
		query.setMaxResults(backPageLimit);
		List<GroupJnl> list=query.list();
		return list;
	}

	public GroupJnl queryGroupDetail(int groupId) {
		GroupJnl group=dao.get(GroupJnl.class, groupId);
		return group;
	}

	public int write(GroupJnl groupJnl) {
		dao.save(groupJnl);
		return groupJnl.getGroupId();
	}

	public List<GroupJnl> aroundGroup(int pageNo, int activityId, int userSeq) {
		String sql="from GroupJnl a where a.activity.activityId=? and a.status='BG' and "
				+ "a.groupId not in(select groupId from PayJnl where userSeq=?) "
				+ "order by a.count/a.total desc,a.startTime";
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(sql);
		query.setInteger(0, activityId);
		query.setInteger(1, userSeq);
		query.setFirstResult(pageNo*pageLimit);
		query.setMaxResults(pageLimit);
		List<GroupJnl> list=query.list();
		return list;
	}

	public int queryGroupTotal(Timestamp beginTime, Timestamp endTime,
			String status) {
		SQLQuery sqlQuery=null;
		if(status==null || status.equals("ALL")){
			String sql="select count(*) from GroupJnl where startTime>? and startTime<?";
			sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			sqlQuery.setTimestamp(0, beginTime);
			sqlQuery.setTimestamp(1, endTime);
		}else{
			String sql="select count(*) from GroupJnl where startTime>? and startTime<? and status=?";
			sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			sqlQuery.setTimestamp(0, beginTime);
			sqlQuery.setTimestamp(1, endTime);
			sqlQuery.setString(2, status);
		}
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}

	public List<GroupJnl> queryOverdueGroup() {
		String hql="from GroupJnl where TIMESTAMPDIFF(second,startTime,?)>24*3600";
		Timestamp now=new Timestamp(System.currentTimeMillis());
		List<GroupJnl> list=(List<GroupJnl>) dao.find(hql,new Object[]{now});
		return list;
	}

	public void updateGroup(GroupJnl groupJnl) {
		dao.update(groupJnl);
	}

	
}
