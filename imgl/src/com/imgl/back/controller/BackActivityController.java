package com.imgl.back.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.imgl.wx.business.ActivityBusiness;
import com.imgl.wx.model.Activity;

@Controller
public class BackActivityController {
	
	@Autowired
	private ActivityBusiness activityBusiness;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivityList")
	public ModelAndView backActivityList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=1;
		ModelAndView mav=activityBusiness.queryBackActivityList(pageNo);
		mav.setViewName("/back/activity/activityList");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivitySubList")
	public ModelAndView backActivitySubList(HttpServletRequest request,HttpServletResponse response){
		int pageNo=Integer.valueOf(request.getParameter("pageNo"));
		
		ModelAndView mav=activityBusiness.queryBackActivityList(pageNo);
		mav.setViewName("/back/activity/activityList_sub");
		return mav;
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivityDetail")
	public ModelAndView backActivityDetail(HttpServletRequest request,HttpServletResponse response){
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		Activity activity=activityBusiness.queryActivity(activityId);
		return new ModelAndView("/back/activity/activityDetail","activity",activity);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivityEditPre")
	public ModelAndView backActivityEditPre(HttpServletRequest request,HttpServletResponse response){
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		Activity activity=activityBusiness.queryActivity(activityId);
		return new ModelAndView("/back/activity/activityEditPre","activity",activity);
	}
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivityEdit")
	public ModelAndView backActivityEdit(HttpServletRequest request,HttpServletResponse response){
		String[] imgNameArr=request.getParameterValues("imgUrl");
		String defaultImgName=request.getParameter("options");
		Activity activity=new Activity();
		activity.setActivityId(Integer.valueOf(request.getParameter("activityId")));
		activity.setProductName(request.getParameter("productName"));
		activity.setTitle(request.getParameter("title"));
		activity.setOriginPrice(Double.valueOf(request.getParameter("originPrice")));
		activity.setSinglePrice(Double.valueOf(request.getParameter("singlePrice")));
		activity.setDiscount(Double.valueOf(request.getParameter("discount")));
		activity.setGroupPrice(Double.valueOf(request.getParameter("groupPrice")));
		activity.setGroupVol(Integer.valueOf(request.getParameter("groupVol")));
		activity.setTotalQuantity(Integer.valueOf(request.getParameter("totalQuantity")));
		activity.setRemain(Integer.valueOf(request.getParameter("remain")));
		activity.setIntroduction(request.getParameter("introduction"));
		activityBusiness.updateActivity(activity,imgNameArr,defaultImgName);
		return backActivityDetail(request,response);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/backActivityNew")
	public ModelAndView backActivityNew(HttpServletRequest request,HttpServletResponse response){
		String[] imgNameArr=request.getParameterValues("imgUrl");
		String defaultImgName=request.getParameter("options");
		Activity activity=new Activity();
		activity.setProductName(request.getParameter("productName"));
		activity.setTitle(request.getParameter("title"));
		activity.setOriginPrice(Double.valueOf(request.getParameter("originPrice")));
		activity.setSinglePrice(Double.valueOf(request.getParameter("singlePrice")));
		activity.setDiscount(Double.valueOf(request.getParameter("discount")));
		activity.setGroupPrice(Double.valueOf(request.getParameter("groupPrice")));
		activity.setGroupVol(Integer.valueOf(request.getParameter("groupVol")));
		activity.setTotalQuantity(Integer.valueOf(request.getParameter("totalQuantity")));
		activity.setRemain(Integer.valueOf(request.getParameter("remain")));
		activity.setIntroduction(request.getParameter("introduction"));
		activityBusiness.newActivity(activity,imgNameArr,defaultImgName);
		
		Activity newActivity=activityBusiness.queryActivity(activity.getActivityId());
		return new ModelAndView("/back/activity/activityDetail","activity",newActivity);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteActivity")
	public ModelAndView deleteActivity(HttpServletRequest request,HttpServletResponse response){
		int activityId=Integer.valueOf(request.getParameter("activityId"));
		activityBusiness.deleteActivity(activityId);
		
		return backActivityList(request,response);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/imgUpload")
	@ResponseBody
	public String imgUpload(HttpServletRequest request,HttpServletResponse response){
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> fileList=null;
		try {
			fileList = upload.parseRequest(new ServletRequestContext(request));
		} catch (FileUploadException e2) {
			e2.printStackTrace();
		}
		FileItem fileItem = null;
		String fileName="";
		for(int i=0;i<fileList.size();i++){
			if(fileList.get(i).isFormField()==false){//表示是文件
				fileItem=fileList.get(i);
				fileName=activityBusiness.getFileName();
				String suffix=fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
				fileName=fileName+suffix;
				File file=new File(messageSource.getMessage("tempPath",null,Locale.SIMPLIFIED_CHINESE)+fileName);
				try {
					fileItem.write(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("imgName", fileName);
		return new Gson().toJson(map);
	}
	
}	
