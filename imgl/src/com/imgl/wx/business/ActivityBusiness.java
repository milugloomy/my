package com.imgl.wx.business;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.imgl.wx.model.Activity;
import com.imgl.wx.model.Address;
import com.imgl.wx.model.GroupJnl;
import com.imgl.wx.model.Images;
import com.imgl.wx.model.PayJnl;
import com.imgl.wx.model.User;
import com.imgl.wx.service.ActivityService;
import com.imgl.wx.service.AddressService;
import com.imgl.wx.service.GroupService;
import com.imgl.wx.service.OrderService;

@Service("activityBusiness")
@Transactional
public class ActivityBusiness {
	
	protected Log log=LogFactory.getLog(getClass());
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private MessageSource messageSource;

	public List<Activity> queryActivityList(int pageNo) {
		List<Activity> list=activityService.queryActivityList(pageNo);
		return list;
	}

	public Activity queryActivity(int activityId) {
		return activityService.queryActivity(activityId);
	}

	/**
	 * 
	 * @param activityId 活动序号
	 * @param user	用户
	 * @param addrSeq	地址序号
	 * @param groupFlag	add表示加入团，open表示开团 
	 * @param groupId	groupFlag为add时，必须传groupId
	 * 
	 */
	public void groupPay(int activityId, User user, int addrSeq, String groupFlag, Integer groupId) {
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		Activity activity = activityService.queryActivity(activityId);
		
		// 判断商品是否还有库存
		if (activity.getRemain() < 1) {
			log.error("productQuantity.not.enough");
			throw new RuntimeException("productQuantity.not.enough");
		}
		//判断是否已组团完成
		GroupJnl groupJnl=null;
		if("add".equals(groupFlag)){
			if(groupId==null||"".equals(groupId)){
				log.error("groupId.cannot.be.null");
				throw new RuntimeException("groupId.cannot.be.null");
			}
			groupJnl=groupService.queryGroupDetail(groupId);
			if(groupJnl.getCount()>=groupJnl.getTotal()){
				log.error("group.already.completd");
				throw new RuntimeException("group.already.completd");
			}
			//判断用户是否已是组员
			boolean exists=orderService.checkUser(groupId,user.getUserSeq());
			if(exists==true){
				log.error("group.already.joind");
				throw new RuntimeException("group.already.joind");
			}
		}
		
		PayJnl payJnl=new PayJnl();
		try{
			//商品数量-1
			activityService.decrQuantity(activity.getActivityId());
			//支付
			try{
				Thread.sleep(1000);//模拟支付
			}catch(Exception e){
				//商品数量回滚
				activityService.addQuantity(activity.getActivityId());
				payJnl.setPayStatus("FL");
			}
			//group日志
			if("open".equals(groupFlag)){
				groupJnl=new GroupJnl();
				groupJnl.setActivity(activity);
				groupJnl.setStatus("BG");//组团中
				groupJnl.setTotal(activity.getGroupVol());
				groupJnl.setCount(1);
				groupJnl.setLeader(user);
				groupJnl.setGroupType("G");//组团
				groupJnl.setStartTime(now);
				groupId=groupService.write(groupJnl);
				payJnl.setPayStatus("APNG");//已支付未成团
			}
			if("add".equals(groupFlag)){
				groupJnl.setCount(groupJnl.getCount()+1);
				if(groupJnl.getTotal()==groupJnl.getCount()){//组团完成
					groupJnl.setStatus("CP");//组团完成
					groupJnl.setEndTime(now);
					payJnl.setPayStatus("AGNS");//已成团未发货
					//更新所有团员支付状态为已成团未发货
					orderService.updateGroupStatus(groupJnl.getGroupId(),"AGNS");
				}else{//组团未完成
					payJnl.setPayStatus("APNG");//已支付未成团
				}
				groupService.write(groupJnl);
			}
		}finally{
			//支付日志
			payJnl.setGroupId(groupId);
			payJnl.setAmount(activity.getGroupPrice());
			payJnl.setActivity(activity);
			payJnl.setPayTime(now);
			Address address=addressService.queryAddr(addrSeq);
			payJnl.setAddress(address);
			payJnl.setUser(user);
			orderService.write(payJnl);
		}
			
	}

	public void singlePay(int activityId, User user, int addrSeq) {
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		Activity activity = activityService.queryActivity(activityId);
		
		// 判断商品是否还有库存
		if (activity.getRemain() < 1) {
			log.error("productQuantity.not.enough");
			throw new RuntimeException("productQuantity.not.enough");
		}
		PayJnl payJnl=new PayJnl();
		try{
			//商品数量-1
			activityService.decrQuantity(activity.getActivityId());
			//支付
			try{
				Thread.sleep(1000);//模拟支付
				payJnl.setPayStatus("AGNS");//已确认未发货
			}catch(Exception e){
				//商品数量回滚
				activityService.addQuantity(activity.getActivityId());
				payJnl.setPayStatus("FL");
			}
		}finally{
			//支付日志
			payJnl.setGroupId(null);//单独购买没有团id
			payJnl.setAmount(activity.getGroupPrice());
			payJnl.setActivity(activity);
			payJnl.setPayTime(now);
			Address address=addressService.queryAddr(addrSeq);
			payJnl.setAddress(address);
			payJnl.setUser(user);
			orderService.write(payJnl);
		}
	}

	public ModelAndView queryBackActivityList(int pageNo) {
		List<Activity> list=activityService.queryBackActivityList(pageNo);
		int total=activityService.queryActivityTotal();
		ModelAndView mav=new ModelAndView();
		mav.addObject("activityList", list);
		mav.addObject("total", total);
		return mav;
	}

	public void deleteActivity(int activityId) {
		activityService.deleteActivity(activityId);
	}
	public void updateActivity(Activity activity, String[] imgNameArr, String defaultImgName){
		Activity currentActivity=activityService.queryActivity(activity.getActivityId());
		currentActivity.setProductName(activity.getProductName());
		currentActivity.setTitle(activity.getTitle());
		currentActivity.setOriginPrice(activity.getOriginPrice());
		currentActivity.setSinglePrice(activity.getSinglePrice());
		currentActivity.setDiscount(activity.getDiscount());
		currentActivity.setGroupPrice(activity.getGroupPrice());
		currentActivity.setGroupVol(activity.getGroupVol());
		currentActivity.setTotalQuantity(activity.getTotalQuantity());
		currentActivity.setRemain(activity.getRemain());
		currentActivity.setIntroduction(activity.getIntroduction());
		List<Images> imgList=currentActivity.getImgUrlList();
		for(int i=imgList.size()-1;i>(imgNameArr.length-1);i--){//去除删掉的图片
			imgList.remove(i);
		}
		for(int i=imgList.size();i<imgNameArr.length;i++){//添加新添加的图片
			Images img=new Images();
			img.setActivityId(currentActivity.getActivityId());
			imgList.add(img);
		}

		for(int i=0;i<imgNameArr.length;i++){
			//设置默认
			Images img=imgList.get(i);
			img.setImgUrl(imgNameArr[i]);
			if(imgNameArr[i].equals(defaultImgName))
				img.setMainPic(1);
			else
				img.setMainPic(0);
			//将新增图片从imgTemp移至img
			File newFile=new File(messageSource.getMessage("imgPath",null,Locale.SIMPLIFIED_CHINESE)+imgNameArr[i]);
			if(!newFile.exists()){
				File file=new File(messageSource.getMessage("tempPath",null,Locale.SIMPLIFIED_CHINESE)+imgNameArr[i]);
				file.renameTo(newFile);
			}
			
		}
		currentActivity.setImgUrlList(imgList);
		
		activityService.updateActivity(currentActivity);
	}

	public String getFileName() {
		Date now=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(now);
	}

	public void newActivity(Activity activity, String[] imgNameArr,
			String defaultImgName) {
		activity.setStartTime(new Timestamp(System.currentTimeMillis()));
		activity.setStatus("N");
		activityService.addActivity(activity);
		List<Images> imgList=new ArrayList<Images>();
		for(int i=0; i<imgNameArr.length;i++){
			Images img=new Images();
			img.setImgUrl(imgNameArr[i]);
			if(imgNameArr[i].equals(defaultImgName))
				img.setMainPic(1);
			else
				img.setMainPic(0);
			//将新增图片从imgTemp移至img
			File newFile=new File(messageSource.getMessage("imgPath",null,Locale.SIMPLIFIED_CHINESE)+imgNameArr[i]);
			if(!newFile.exists()){
				File file=new File(messageSource.getMessage("tempPath",null,Locale.SIMPLIFIED_CHINESE)+imgNameArr[i]);
				file.renameTo(newFile);
			}
			img.setActivityId(activity.getActivityId());
			imgList.add(img);
		}
		activity.setImgUrlList(imgList);
	}
}
