package com.imgl.front.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.UserBusiness;
import com.imgl.wx.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserBusiness userBusiness;
	
	@RequestMapping(method = RequestMethod.GET, value = "/userCenter")
	public ModelAndView userCenter(HttpServletRequest request,HttpServletResponse response){
		
		return new ModelAndView("wx/usercenter");
	}
	
	
}
