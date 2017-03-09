package com.imgl.wx.service;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.imgl.wx.model.Address;
import com.imgl.wx.model.City;
import com.imgl.wx.model.District;
import com.imgl.wx.model.Province;

@Repository("addressService")
public class AddressService {
	
	@Autowired
	private HibernateTemplate dao;
	
	public List<Address> addrList(int userSeq) {
		List<Address> list=(List<Address>) dao.find("from Address where userSeq=? and addrState='N'",new Object[]{userSeq});
		return list;
	}
	public void updateUnDefaultAddr(int userSeq) {
		String sql="update Address set mainAddr=0 where userSeq=? and mainAddr=1";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, userSeq);
		sqlQuery.executeUpdate();
	}
	public void updateDefaultAddr(int userSeq,int addrSeq) {
		String sql="update Address set mainAddr=1 where userSeq=? and addrSeq=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, userSeq);
		sqlQuery.setInteger(1, addrSeq);
		sqlQuery.executeUpdate();
	}
	public Address queryAddr(int addrSeq) {
		return dao.get(Address.class, addrSeq);
	}
	public List<Province> provinceList() {
		return (List<Province>) dao.find("from Province");
	}
	public List<City> cityList(int provinceId) {
		return (List<City>) dao.find("from City where provinceId=?",new Object[]{provinceId});
	}
	public List<City> cityList(String provinceName) {
		return (List<City>) dao.find("from City where provinceId=(select provinceId from Province where provinceName=?)",
				new Object[]{provinceName});
	}
	public List<District> districtList(int cityId) {
		return (List<District>) dao.find("from District where cityId=?",new Object[]{cityId});
	}
	public List<District> districtList(String cityName) {
		return (List<District>) dao.find("from District where cityId=(select cityId from City where cityName=?)",
				new Object[]{cityName});
	}
	public void updateAddress(Address address) {
		Address currentAddress=queryAddr(address.getAddrSeq());
		BeanUtils.copyProperties(address, currentAddress, new String[]{"mainAddr","addrState"});
		dao.update(currentAddress);
	}
	public void deleteAddress(int addrSeq) {
		Address address=queryAddr(addrSeq);
		if(address.getMainAddr().equals("1"))
			throw new RuntimeException("默认地址不允许删除");
		String sql="update Address set addrState='C' where addrSeq=?";
		SQLQuery sqlQuery=dao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		sqlQuery.setInteger(0, addrSeq);
		sqlQuery.executeUpdate();
	}
	public void newAddress(Address address) {
		dao.save(address);
	}
	public Address defaultAddress(int userSeq) {
		List<Address> addressList=(List<Address>) dao.find("from Address where userSeq=? and mainAddr='1' and addrState='N'", userSeq);
		if(addressList.size()>0)
			return addressList.get(0);
		else
			return null;
	}
}
