package com.imgl.back.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.GroupBusiness;
import com.imgl.wx.business.OrderBusiness;
import com.imgl.wx.model.GroupJnl;
import com.imgl.wx.model.PayJnl;

@Controller
public class backGroupController {
	
	@Autowired
	private GroupBusiness groupBusiness;
	@Autowired
	private OrderBusiness orderBusiness;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backGroupList")
	public ModelAndView backGroupList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=1;
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		String status=request.getParameter("status");
		
		ModelAndView mav=groupBusiness.queryGroupList(beginTimeStr,endTimeStr,status,pageNo);
		mav.addObject("beginDate", beginTimeStr);
		mav.addObject("endDate", endTimeStr);
		mav.addObject("status", status);
		mav.setViewName("/back/activity/groupList");
		return mav;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backGroupSubList")
	public ModelAndView backGroupSubList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=Integer.valueOf(request.getParameter("pageNo"));
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		String status=request.getParameter("status");
		
		ModelAndView mav=groupBusiness.queryGroupList(beginTimeStr,endTimeStr,status,pageNo);
		mav.setViewName("back/activity/groupList_sub");
		return mav;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backGroupDetail")
	public ModelAndView backGroupDetail(HttpServletRequest request,HttpServletResponse response){
		int groupId=Integer.valueOf(request.getParameter("groupId"));
		GroupJnl groupJnl=groupBusiness.queryGroupDetail(groupId);
		List<PayJnl> orderList=orderBusiness.queryOrderListByGroupId(groupId);
		ModelAndView mav=new ModelAndView("back/activity/groupDetail");
		mav.addObject("groupJnl",groupJnl);
		mav.addObject("orderList",orderList);
		return mav;
	}

}	
