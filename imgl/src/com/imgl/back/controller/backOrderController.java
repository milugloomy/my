package com.imgl.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.business.OrderBusiness;
import com.imgl.wx.model.PayJnl;

@Controller
public class backOrderController {
	
	@Autowired
	private OrderBusiness orderBusiness;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backOrderList")
	public ModelAndView backOrderList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=1;
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		String payStatus=request.getParameter("payStatus");
		
		ModelAndView mav=orderBusiness.queryOrderList(beginTimeStr,endTimeStr,payStatus,pageNo);
		mav.addObject("beginDate", beginTimeStr);
		mav.addObject("endDate", endTimeStr);
		mav.addObject("payStatus", payStatus);
		mav.setViewName("/back/activity/orderList");
		return mav;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backOrderSubList")
	public ModelAndView backOrderSubList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=Integer.valueOf(request.getParameter("pageNo"));
		String beginTimeStr=request.getParameter("beginDate");
		String endTimeStr=request.getParameter("endDate");
		String payStatus=request.getParameter("payStatus");
		
		ModelAndView mav=orderBusiness.queryOrderList(beginTimeStr,endTimeStr,payStatus,pageNo);
		mav.setViewName("back/activity/orderList_sub");
		return mav;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backOrderDetail")
	public ModelAndView backOrderDetail(HttpServletRequest request,HttpServletResponse response){
		int pJnlNo=Integer.valueOf(request.getParameter("pJnlNo"));
		PayJnl payJnl=orderBusiness.queryOrder(pJnlNo);
		return new ModelAndView("back/activity/orderDetail","payJnl",payJnl);
	}

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backOrderEditPre")
	public ModelAndView backOrderEditPre(HttpServletRequest request,HttpServletResponse response){
		int pJnlNo=Integer.valueOf(request.getParameter("pJnlNo"));
		PayJnl payJnl=orderBusiness.queryOrder(pJnlNo);
		return new ModelAndView("back/activity/orderEditPre","payJnl",payJnl);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backOrderEdit")
	public ModelAndView backOrderEdit(HttpServletRequest request,HttpServletResponse response){
		int pJnlNo=Integer.valueOf(request.getParameter("pJnlNo"));
		String deliComp=request.getParameter("deliComp");
		String deliNo=request.getParameter("deliNo");
		PayJnl payJnl=orderBusiness.updatePayJnl(pJnlNo, deliComp, deliNo);
		
		return new ModelAndView("back/activity/orderDetail","payJnl",payJnl);
	}

}	
