package com.imgl.wx.business;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.model.BackJnl;
import com.imgl.wx.service.LogService;

@Service("logBusiness")
public class LogBusiness {
	@Autowired
	private LogService logService;
	
	public ModelAndView queryBackLog(String beginTimeStr,String endTimeStr,int pageNo){
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
		List<BackJnl> list=logService.queryBackLog(beginTime,endTime,pageNo);
		int total=logService.queryBackLogTotal(beginTime,endTime);

		ModelAndView mav=new ModelAndView();
		mav.addObject("backJnlList", list);
		mav.addObject("total", total);
		return mav;
	}

}
