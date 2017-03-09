package com.imgl.wx.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="payjnl")
public class PayJnl {
	private int pJnlNo;
	private Integer groupId;
	private double amount;
	private User user;
	private Activity activity;
	private String payStatus;
	private Timestamp payTime;
	private Timestamp sendTime;
	private Address address;
	private String deliComp;
	private String deliNo;
	@Id
	@GeneratedValue
	public int getpJnlNo() {
		return pJnlNo;
	}
	public void setpJnlNo(int pJnlNo) {
		this.pJnlNo = pJnlNo;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userSeq")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="activityId")
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Timestamp getPayTime() {
		return payTime;
	}
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="addrSeq")
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getDeliComp() {
		return deliComp;
	}
	public void setDeliComp(String deliComp) {
		this.deliComp = deliComp;
	}
	public String getDeliNo() {
		return deliNo;
	}
	public void setDeliNo(String deliNo) {
		this.deliNo = deliNo;
	}
	
}
