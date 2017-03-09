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

import com.imgl.wx.business.OrderBusiness;
import com.imgl.wx.model.PayJnl;
import com.imgl.wx.model.User;

@Controller
public class OrderController {
	
	@Autowired
	private OrderBusiness orderBusiness;

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/myOrder")
	public ModelAndView myOrder(HttpServletRequest request,HttpServletResponse response){
		int userSeq=((User)request.getSession().getAttribute("User")).getUserSeq();

		int pageNo=0;
		if(request.getParameter("pageNo")!=null)
			pageNo=Integer.valueOf(request.getParameter("pageNo"));
		List<PayJnl> list= orderBusiness.queryOrderList(pageNo,userSeq);
		
		if(pageNo!=0){
			String sub=request.getParameter("sub");
			if("1".equals(sub))//子页面1
				return new ModelAndView("wx/myorder_list_sub1","orderList",list);
			if("2".equals(sub))//子页面2
				return new ModelAndView("wx/myorder_list_sub2","orderList",list);
		}else
			return new ModelAndView("wx/myorder_list","orderList",list);
		return null;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/queryOrder")
	public ModelAndView queryOrder(HttpServletRequest request,HttpServletResponse response){
		
		int pJnlNo=Integer.valueOf(request.getParameter("pJnlNo"));
		PayJnl payJnl= orderBusiness.queryOrder(pJnlNo);
		
		return new ModelAndView("wx/myorder_detail","payJnl",payJnl);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/queryOrderByGroupId")
	public ModelAndView queryOrderByGroupId(HttpServletRequest request,HttpServletResponse response){
		
		int userSeq=((User)request.getSession().getAttribute("User")).getUserSeq();
		int groupId=Integer.valueOf(request.getParameter("groupId"));
		PayJnl payJnl= orderBusiness.queryOrderBygroupId(groupId,userSeq);
		
		return new ModelAndView("wx/myorder_detail","payJnl",payJnl);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/confirmReceive")
	@ResponseBody
	public String confirmReceive(HttpServletRequest request,HttpServletResponse response){
		
		int pjnlNo=Integer.valueOf(request.getParameter("pjnlNo"));
		orderBusiness.updatePayJnlState(pjnlNo,"AR");//AR已接受状态
		
		return null;
	}
	
}
