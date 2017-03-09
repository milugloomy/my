package com.imgl.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.LogBusiness;
import com.imgl.wx.business.UserBusiness;
import com.imgl.wx.model.Manager;


@Controller
public class BackUserController {
	
	@Autowired
	private UserBusiness userBusiness;
	@Autowired
	private LogBusiness logBusiness;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backLogin")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
		String userId=request.getParameter("adminName");
		String password=request.getParameter("password");
		Manager manager=userBusiness.backLogin(userId, password);
		request.getSession().setAttribute("Manager", manager);
		return new ModelAndView("back/backIndex");
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/modifyPwd")
	public ModelAndView modifyPwd(HttpServletRequest request,HttpServletResponse response){
		Manager manager=(Manager) request.getSession().getAttribute("Manager");
		String userId=manager.getUserId();
		String password=(String) request.getParameter("oldPassword");
		String newPassword=(String) request.getParameter("newPassword");
		userBusiness.updatePwd(userId, password, newPassword);
		return new ModelAndView("back/userManage/modifyPwd");
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/logOut")
	public ModelAndView logOut(HttpServletRequest request,HttpServletResponse response){
		request.getSession().removeAttribute("Manager");
		return new ModelAndView("back/backLogin");
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/adminLog")
	public ModelAndView adminLog(HttpServletRequest request,HttpServletResponse response){
		int pageNo=1;
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		
		ModelAndView mav=logBusiness.queryBackLog(beginTimeStr,endTimeStr,pageNo);
		mav.addObject("beginDate", beginTimeStr);
		mav.addObject("endDate", endTimeStr);
		mav.setViewName("back/userManage/adminLog");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/adminLogSub")
	public ModelAndView adminLogSub(HttpServletRequest request,HttpServletResponse response){
		int pageNo=Integer.valueOf(request.getParameter("pageNo"));
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		
		ModelAndView mav=logBusiness.queryBackLog(beginTimeStr,endTimeStr,pageNo);
		mav.setViewName("back/userManage/adminLog_sub");
		return mav;
	}
}
