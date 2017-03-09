package com.imgl.wx.service;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.imgl.wx.model.Manager;
import com.imgl.wx.model.User;

@Repository("userService")
public class UserService {
	
	@Autowired
	private HibernateTemplate dao;
	
	public List<User> getUserList(){
		return (List<User>) dao.find("from User");
	}
	public User getUser(int id){
		return dao.get(User.class, id);
	}
	public Manager queryManger(String userId, String password) {
		String sql="select * from manager where userid=? and password=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setString(0, userId);
		sqlQuery.setString(1, password);
		Object[] res=(Object[]) sqlQuery.uniqueResult();
		if(res==null)
			return null;
		else{
			Manager manager=new Manager();
			manager.setUserId((String) res[0]);
//			manager.setPassword((String) res[1]);
			return manager;
		}
			
	}
	public void updatePwd(String userId, String password) {
		String sql="update manager set password=? where userId=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setString(0, password);
		sqlQuery.setString(1, userId);
		sqlQuery.executeUpdate();
	}
}
