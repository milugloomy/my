package com.imgl.front.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.ActivityBusiness;
import com.imgl.wx.business.AddressBusiness;
import com.imgl.wx.business.GroupBusiness;
import com.imgl.wx.business.UserBusiness;
import com.imgl.wx.model.Activity;
import com.imgl.wx.model.Address;
import com.imgl.wx.model.User;

@Controller
public class ActivityController {
	
	@Autowired
	private ActivityBusiness activityBusiness;
	@Autowired
	private UserBusiness userBusiness;
	@Autowired
	private AddressBusiness addressBusiness;
	@Autowired
	private GroupBusiness groupBusiness;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/activityList")
	public ModelAndView activityList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=0;
		if(request.getParameter("pageNo")!=null)
			pageNo=Integer.valueOf(request.getParameter("pageNo"));
		
		List<Activity> list=activityBusiness.queryActivityList(pageNo);
		if(pageNo==0)
			return new ModelAndView("wx/activity_list","activityList",list);
		else
			return new ModelAndView("wx/activity_list_sub","activityList",list);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/queryActivity")
	public ModelAndView queryActivity(HttpServletRequest request,HttpServletResponse response){
		
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		
		Activity activity=activityBusiness.queryActivity(activityId);
		
		return new ModelAndView("wx/activity_detail","activity",activity);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/payPre")
	public ModelAndView payPre(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav=new ModelAndView("wx/activity_pay");

		int activityId=Integer.valueOf(request.getParameter("activityId"));
		int userSeq=((User)request.getSession().getAttribute("User")).getUserSeq();
		Activity activity=activityBusiness.queryActivity(activityId);
		Address address=null;
		if(request.getParameter("addrSeq")!=null){
			int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
			address=addressBusiness.queryAddr(addrSeq);
		}else{
			address=addressBusiness.defaultAddress(userSeq);
		}
		//购买类型 单独购买或拼团
		String buyType=request.getParameter("buyType");
		if(buyType.equals("single")){//单独购买
			mav.addObject("price", activity.getSinglePrice());
			mav.addObject("buyType", "single");
		}else{//团购
			mav.addObject("price", activity.getGroupPrice());
			mav.addObject("buyType", "group");
		}
		if(request.getParameter("groupId")!=null && !"".equals(request.getParameter("groupId"))){
			String groupId=request.getParameter("groupId");
			mav.addObject("groupId", groupId);
			mav.addObject("buyType", "group");
		}
		mav.addObject("activity", activity);
		mav.addObject("address", address);
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/groupPay")
	@ResponseBody
	public String groupPay(HttpServletRequest request,HttpServletResponse response){
		
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		User user=(User)request.getSession().getAttribute("User");
		String groupFlag="open";
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		Integer groupId=null;
		if(!"".equals(request.getParameter("groupId")) && request.getParameter("groupId")!=null){
			groupId=Integer.valueOf(request.getParameter("groupId"));
			groupFlag="add";
		}
		activityBusiness.groupPay(activityId,user,addrSeq,groupFlag,groupId);
		
		return null;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/singlePay")
	@ResponseBody
	public String singlePay(HttpServletRequest request,HttpServletResponse response){
		
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		User user=(User)request.getSession().getAttribute("User");
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		activityBusiness.singlePay(activityId,user,addrSeq);
		
		return null;
	}
	
}	
