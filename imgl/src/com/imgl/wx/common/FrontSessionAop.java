package com.imgl.wx.common;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imgl.wx.business.UserBusiness;
import com.imgl.wx.model.Jnl;
import com.imgl.wx.model.User;
import com.imgl.wx.service.LogService;

@Service("frontSessionAop")
public class FrontSessionAop {
	protected Log log=LogFactory.getLog(getClass());
	@Autowired
	private LogService logService;
	@Autowired
	private UserBusiness userBusiness;
	
	public Object control(ProceedingJoinPoint pjp) throws Throwable{
		Jnl jnl=new Jnl();
		Object retObj=null;

		Object[] args=pjp.getArgs();
		HttpServletRequest request = null;
		for(int i=0;i<args.length;i++)
			if(args[i] instanceof HttpServletRequest){
				request=(HttpServletRequest) args[i];
				break;
			}
		//1 吊炸
		//2 牛叉
		//3 霸气
		//4 纳尼
		User user=userBusiness.queryUser(1);
		request.getSession().setAttribute("User", user);
		
		String transcode=request.getRequestURI().substring(request.getContextPath().length() + 1, request.getRequestURI().lastIndexOf("."));
		String transtype=request.getMethod();
		log.info("begin trans:"+transcode);
		log.info("paramter:"+request.getParameterMap());
		jnl.setTransCode(transcode);
		jnl.setTransType(transtype);
		jnl.setClientIp(request.getRemoteAddr());//浏览器IP
		jnl.setChannel("Android");//渠道
		jnl.setTransTime(new Timestamp(System.currentTimeMillis()));
		try{
			retObj=pjp.proceed(args);
			jnl.setUserSeq(user.getUserSeq());
			jnl.setReturnCode("000000");
		}catch(Exception e){
			log.error(e);
			jnl.setReturnCode("error");
			jnl.setReturnMsg(e.getMessage());
			throw e;
		}finally{
			jnl.setReturnTime(new Timestamp(System.currentTimeMillis()));
//			logService.saveFrontJnl(jnl);
		}
		return retObj;
	}
}
