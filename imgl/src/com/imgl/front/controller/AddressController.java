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

import com.imgl.wx.business.AddressBusiness;
import com.imgl.wx.common.JosnUtil;
import com.imgl.wx.model.Address;
import com.imgl.wx.model.City;
import com.imgl.wx.model.District;
import com.imgl.wx.model.Province;
import com.imgl.wx.model.User;

@Controller
public class AddressController {
	@Autowired
	private AddressBusiness addressBusiness;
	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = {"/addrPayList","/myAddress"})
	public ModelAndView addrList(HttpServletRequest request,HttpServletResponse response){
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		List<Address> addrList=addressBusiness.addrList(userSeq);

		String uri=request.getRequestURI();
		ModelAndView mav= new ModelAndView();
		mav.addObject("addrList", addrList);
		if(uri.endsWith("addrPayList.do"))
			mav.setViewName("wx/addr_pay_list");
		if(uri.endsWith("myAddress.do"))
			mav.setViewName("wx/addr_list");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/setDefaultAddr")
	@ResponseBody
	public String setDefaultAddr(HttpServletRequest request,HttpServletResponse response){
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		addressBusiness.setDefaultAddr(userSeq,addrSeq);
		return JosnUtil.toJson(user);
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = {"/newAddressPre","/addrPayNewPre"})
	public ModelAndView newAddressPre(HttpServletRequest request,HttpServletResponse response){
		List<Province> provinceList=addressBusiness.provinceList();
		String uri=request.getRequestURI();
		ModelAndView mav= new ModelAndView();
		mav.addObject("provinceList", provinceList);
		if(uri.endsWith("newAddressPre.do"))
			mav.setViewName("wx/addr_new");
		if(uri.endsWith("addrPayNewPre.do"))
			mav.setViewName("wx/addr_pay_new");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = {"/newAddress","/addrPayNew"})
	public ModelAndView newAddress(HttpServletRequest request,HttpServletResponse response){
		addressBusiness.newAddress(request);
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		List<Address> addrList=addressBusiness.addrList(userSeq);
		
		String uri=request.getRequestURI();
		ModelAndView mav= new ModelAndView();
		mav.addObject("addrList", addrList);
		if(uri.endsWith("newAddress.do"))
			mav.setViewName("wx/addr_list");
		if(uri.endsWith("addrPayNew.do"))
			mav.setViewName("wx/addr_pay_list");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/modifyAddrPre")
	public ModelAndView modifyAddrPre(HttpServletRequest request,HttpServletResponse response){
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		Address addr=addressBusiness.queryAddr(addrSeq);
		List<Province> provinceList=addressBusiness.provinceList();
		List<City> cityList=addressBusiness.cityList(addr.getProvinceName());
		List<District> districtList=addressBusiness.districtList(addr.getCityName());
		ModelAndView mav=new ModelAndView("wx/addr_detail");
		mav.addObject("address", addr);
		mav.addObject("provinceList", provinceList);
		mav.addObject("cityList", cityList);
		mav.addObject("districtList", districtList);
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/modifyAddr")
	public ModelAndView modifyAddr(HttpServletRequest request,HttpServletResponse response){
		addressBusiness.updateAddress(request);
		
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		List<Address> addrList=addressBusiness.addrList(userSeq);
		return new ModelAndView("wx/addr_list","addrList",addrList);
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/deleteAddress")
	public ModelAndView deleteAddress(HttpServletRequest request,HttpServletResponse response){
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		addressBusiness.deleteAddress(addrSeq);
		
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		List<Address> addrList=addressBusiness.addrList(userSeq);
		return new ModelAndView("wx/addr_list","addrList",addrList);
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/cityList")
	@ResponseBody
	public String cityList(HttpServletRequest request,HttpServletResponse response){
		String provinceName=request.getParameter("provinceName");
		List<City> list=addressBusiness.cityList(provinceName);
		return JosnUtil.toJson(list);
	}
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, value = "/districtList")
	@ResponseBody
	public String districtList(HttpServletRequest request,HttpServletResponse response){
		String cityName=request.getParameter("cityName");
		List<District> list=addressBusiness.districtList(cityName);
		return JosnUtil.toJson(list);
	}

}
