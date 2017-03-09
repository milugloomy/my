package com.imgl.wx.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imgl.wx.model.Manager;
import com.imgl.wx.model.User;
import com.imgl.wx.service.UserService;

@Transactional
@Service("userBusiness")
public class UserBusiness {
	
	@Autowired
	private UserService userService;
	
	public Manager backLogin(String userId,String password){
		Manager manager=userService.queryManger(userId,password);
		if(manager==null)
			throw new RuntimeException("用户名或密码错误");
		return manager;
	}

	public void updatePwd(String userId, String password, String newPassword) {
		Manager manager=userService.queryManger(userId,password);
		if(manager==null)
			throw new RuntimeException("旧密码错误");
		userService.updatePwd(userId,newPassword);
	}
	
	public User queryUser(int userSeq) {
		User user=userService.getUser(userSeq);
		return user;
	}

}
