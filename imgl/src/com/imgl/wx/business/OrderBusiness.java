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

import com.imgl.wx.model.PayJnl;
import com.imgl.wx.service.OrderService;

@Service("orderBusiness")
@Transactional
public class OrderBusiness {
	
	@Autowired
	private OrderService orderService;

	public List<PayJnl> queryOrderList(int pageNo,int userSeq) {
		List<PayJnl> list=orderService.queryOrderList(pageNo,userSeq);
		return list;
	}

	public void updatePayJnlState(int pjnlNo, String payStatus) {
		orderService.updatePayJnlState(pjnlNo,payStatus);
		
	}

	public PayJnl queryOrder(int pJnlNo) {
		PayJnl pauJnl=orderService.queryOrder(pJnlNo);
		return pauJnl;
	}


	public PayJnl queryOrderBygroupId(int groupId, int userSeq) {
		int pJnlNo=orderService.queryPJnlNoByGroupId(groupId,userSeq);
		PayJnl pauJnl=orderService.queryOrder(pJnlNo);
		return pauJnl;
	}
	
	public List<PayJnl> queryOrderListByGroupId(int groupId) {
		List<PayJnl> orderList=orderService.queryOrderListByGroupId(groupId);
		return orderList;
	}

	public ModelAndView queryOrderList(String beginTimeStr,String endTimeStr,String payStatus,int pageNo) {
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
		
		List<PayJnl> list=orderService.queryOrderList(beginTime,endTime,payStatus,pageNo);
		int total=orderService.queryOrderTotal(beginTime,endTime,payStatus);
		ModelAndView mav=new ModelAndView();
		mav.addObject("orderList", list);
		mav.addObject("total", total);
		return mav;
	}

	public PayJnl updatePayJnl(int pJnlNo, String deliComp, String deliNo) {
		PayJnl payJnl=orderService.queryOrder(pJnlNo);
		payJnl.setDeliComp(deliComp);
		payJnl.setDeliNo(deliNo);
		payJnl.setPayStatus("AS");
		if(payJnl.getSendTime()==null)
			payJnl.setSendTime(new Timestamp(System.currentTimeMillis()));
		orderService.updatePayJnl(payJnl);
		return payJnl;
	}
	
	
}
