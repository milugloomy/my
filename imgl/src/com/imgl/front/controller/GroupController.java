package com.imgl.front.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.ActivityBusiness;
import com.imgl.wx.business.GroupBusiness;
import com.imgl.wx.business.OrderBusiness;
import com.imgl.wx.model.Activity;
import com.imgl.wx.model.GroupJnl;
import com.imgl.wx.model.PayJnl;
import com.imgl.wx.model.User;

@Controller
public class GroupController {
	
	@Autowired
	private GroupBusiness groupBusiness;
	@Autowired
	private OrderBusiness orderBusiness;
	@Autowired
	private ActivityBusiness activityBusiness;

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/myGroup")
	public ModelAndView myGroup(HttpServletRequest request,HttpServletResponse response){
		int userSeq=((User)request.getSession().getAttribute("User")).getUserSeq();
		int pageNo=0;
		if(request.getParameter("pageNo")!=null)
			pageNo=Integer.valueOf(request.getParameter("pageNo"));
		List<GroupJnl> groupList=groupBusiness.queryGroupList(pageNo,userSeq);
		if(pageNo==0)
			return new ModelAndView("wx/mygroup_list","groupList",groupList);
		else
			return new ModelAndView("wx/mygroup_list_sub","groupList",groupList);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/groupDetail")
	public ModelAndView groupDetail(HttpServletRequest request,HttpServletResponse response){
		int groupId=Integer.valueOf(request.getParameter("groupId"));
		GroupJnl group=groupBusiness.queryGroupDetail(groupId);
		List<PayJnl> orderList=orderBusiness.queryOrderListByGroupId(groupId);
		ModelAndView mav=new ModelAndView();
		mav.setViewName("wx/mygroup_detail");
		mav.addObject("group", group);
		mav.addObject("orderList", orderList);
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/aroundGroup")
	public ModelAndView aroundGroup(HttpServletRequest request,HttpServletResponse response){
		
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		Activity activity=activityBusiness.queryActivity(activityId);
		User user=(User)request.getSession().getAttribute("User");
		
		int pageNo=0;
		if(request.getParameter("pageNo")!=null)
			pageNo=Integer.valueOf(request.getParameter("pageNo"));
		
		List<GroupJnl> groupList=groupBusiness.aroundGroup(pageNo,activityId,user.getUserSeq());
		ModelAndView mav=new ModelAndView();
		if(pageNo==0)
			mav.setViewName("wx/group_around");
		else
			mav.setViewName("wx/group_around_sub");
		mav.addObject("activity", activity);
		mav.addObject("groupList", groupList);
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/joinGroup")
	public ModelAndView joinGroup(HttpServletRequest request,HttpServletResponse response){
		int groupId=Integer.valueOf(request.getParameter("groupId"));
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		Activity activity=activityBusiness.queryActivity(activityId);
		ModelAndView mav=new ModelAndView();
		mav.setViewName("wx/group_join");
		mav.addObject("groupId", groupId);
		mav.addObject("activity", activity);
		return mav;
	}
	
}
