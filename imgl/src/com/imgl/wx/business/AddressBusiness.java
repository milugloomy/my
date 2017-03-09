package com.imgl.wx.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imgl.wx.model.Address;
import com.imgl.wx.model.City;
import com.imgl.wx.model.District;
import com.imgl.wx.model.Province;
import com.imgl.wx.model.User;
import com.imgl.wx.service.AddressService;

@Service("addressBusiness")
@Transactional
public class AddressBusiness extends AutoDataRefresh{
	
	@Autowired
	private AddressService addressService;
	
	private List<Province> provinceList;
	
	public List<Address> addrList(int userSeq) {
		List<Address> list=addressService.addrList(userSeq);
		return list;
	}

	public void setDefaultAddr(int userSeq, int addrSeq) {
		addressService.updateUnDefaultAddr(userSeq);
		addressService.updateDefaultAddr(userSeq, addrSeq);
	}

	public Address queryAddr(int addrSeq) {
		Address addr=addressService.queryAddr(addrSeq);
		return addr;
	}

	public List<Province> provinceList() {
		try{
			lockCache();
			return provinceList;
		}finally{
			unlockCache();
		}
	}
	public List<City> cityList(int provinceId) {
		return addressService.cityList(provinceId);
	}
	public List<City> cityList(String provinceName) {
		return addressService.cityList(provinceName);
	}
	public List<District> districtList(int cityId) {
		return addressService.districtList(cityId);
	}
	public List<District> districtList(String cityName) {
		return addressService.districtList(cityName);
	}
	
	public void setPeriod() {
		period=24*60*60*1000l;//默认1天
	}

	@Override
	public void updateCache() {
		provinceList=addressService.provinceList();
	}

	public void updateAddress(HttpServletRequest request) {
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		int addrSeq=Integer.valueOf(request.getParameter("addrSeq"));
		String districtName=request.getParameter("district");
		String cityName=request.getParameter("city");
		String provinceName=request.getParameter("province");
		String detail=request.getParameter("detail");
		String addrType=request.getParameter("addrType");
		String phoneNo=request.getParameter("phoneNo");
		String name=request.getParameter("name");
		Address address=new Address();
		address.setAddrSeq(addrSeq);
		address.setAddrType(addrType);
		address.setCityName(cityName);
		address.setDetail(detail);
		address.setDistrictName(districtName);
		address.setName(name);
		address.setPhoneNo(phoneNo);
		address.setProvinceName(provinceName);
		address.setUserSeq(userSeq);
		addressService.updateAddress(address);
	}

	public void deleteAddress(int addrSeq) {
		addressService.deleteAddress(addrSeq);
	}

	public void newAddress(HttpServletRequest request) {
		User user=(User) request.getSession().getAttribute("User");
		int userSeq=user.getUserSeq();
		String districtName=request.getParameter("district");
		String cityName=request.getParameter("city");
		String provinceName=request.getParameter("province");
		String detail=request.getParameter("detail");
		String addrType=request.getParameter("addrType");
		String phoneNo=request.getParameter("phoneNo");
		String name=request.getParameter("name");
		Address address=new Address();
		address.setAddrType(addrType);
		address.setAddrType("N");//正常状态
		address.setCityName(cityName);
		address.setDetail(detail);
		address.setDistrictName(districtName);
		address.setName(name);
		address.setPhoneNo(phoneNo);
		address.setProvinceName(provinceName);
		Address defaultAddress=addressService.defaultAddress(userSeq);
		if(defaultAddress==null)//若没有默认地址，这个设为默认
			address.setMainAddr("1");
		else
			address.setMainAddr("0");
		address.setUserSeq(userSeq);
		addressService.newAddress(address);
	}

	public Address defaultAddress(int userSeq) {
		Address address=addressService.defaultAddress(userSeq);
		return address;
	}
}
