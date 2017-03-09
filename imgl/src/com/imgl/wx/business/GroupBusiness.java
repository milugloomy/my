package com.imgl.wx.business;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.model.GroupJnl;
import com.imgl.wx.model.PayJnl;
import com.imgl.wx.service.GroupService;
import com.imgl.wx.service.OrderService;

@Service("groupBusiness")
@Transactional
public class GroupBusiness {
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private OrderService orderService;

	public List<GroupJnl> queryGroupList(int pageNo,int userSeq) {
		List<GroupJnl> groupList=groupService.queryGroupList(pageNo,userSeq);
		return groupList;
	}

	public GroupJnl queryGroupDetail(int groupId) {
		GroupJnl group=groupService.queryGroupDetail(groupId);
		return group;
	}

	public List<GroupJnl> aroundGroup(int pageNo, int activityId, int userSeq) {
		List<GroupJnl> list=groupService.aroundGroup(pageNo,activityId,userSeq);
		return list;
	}

	public ModelAndView queryGroupList(String beginTimeStr, String endTimeStr,
			String status, int pageNo) {
		Timestamp beginTime=null;
		Timestamp endTime=null;
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		if(endTimeStr!=null){
			try {
				cal.setTime(sdf.parse(endTimeStr));
			} catch (ParseException e) {
				throw new RuntimeException("日期格式错误");
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			endTime=new Timestamp(cal.getTimeInMillis());
		}else{
			try {
				cal.setTime(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				throw new RuntimeException("日期格式错误");
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);//取后一天零时
			endTime=new Timestamp(cal.getTimeInMillis());
		}
		if(beginTimeStr!=null){
			try {
				beginTime=new Timestamp(sdf.parse(beginTimeStr).getTime());
			} catch (ParseException e) {
				throw new RuntimeException("日期格式错误");
			}
		}else{
			cal.setTimeInMillis(endTime.getTime());
			cal.add(Calendar.MONTH, -1);
			beginTime=new Timestamp(cal.getTimeInMillis());
		}
		
		List<GroupJnl> list=groupService.queryGroupList(beginTime,endTime,status,pageNo);
		int total=groupService.queryGroupTotal(beginTime,endTime,status);
		ModelAndView mav=new ModelAndView();
		mav.addObject("groupList", list);
		mav.addObject("total", total);
		return mav;
	}

	public void updateUnGroup() {
		//组团超过一天的团
		List<GroupJnl> groupList=groupService.queryOverdueGroup();
		for(int i=0;i<groupList.size();i++){
			GroupJnl group=groupList.get(i);
			//更新组状态为失败
			group.setStatus("FL");
			groupService.updateGroup(group);
			List<PayJnl> orderList=orderService.queryOrderListByGroupId(group.getGroupId());
			for(int j=0;j<orderList.size();j++){
				PayJnl order=orderList.get(j);
				//更新订单状态为失败
				order.setPayStatus("FL");
				orderService.updatePayJnl(order);
				//退钱
				{}
			}
		}
		
	}

	
	
	
}
