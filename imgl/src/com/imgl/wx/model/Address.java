package com.imgl.wx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="address")
@DynamicUpdate(true)
public class Address {
	private int addrSeq;
	private int userSeq;
	private String name;
	private String phoneNo;
	private String provinceName;
	private String cityName;
	private String districtName;
	private String detail;
	private String addrType;
	private String addrState;
	private String mainAddr;
	@Id
	@GeneratedValue
	public int getAddrSeq() {
		return addrSeq;
	}
	public void setAddrSeq(int addrSeq) {
		this.addrSeq = addrSeq;
	}
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAddrType() {
		return addrType;
	}
	public void setAddrType(String addrType) {
		this.addrType = addrType;
	}
	public String getAddrState() {
		return addrState;
	}
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	public String getMainAddr() {
		return mainAddr;
	}
	public void setMainAddr(String mainAddr) {
		this.mainAddr = mainAddr;
	}
}
