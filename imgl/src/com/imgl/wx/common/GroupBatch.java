package com.imgl.wx.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imgl.wx.business.GroupBusiness;

@Service("groupBatch")
public class GroupBatch {
	@Autowired
	private GroupBusiness groupBusiness;
	
	public void execute(){
		//检查超过一天还未成团的团，团状态置为失败，订单状态置为失败，退款
		groupBusiness.updateUnGroup();
	}
}
