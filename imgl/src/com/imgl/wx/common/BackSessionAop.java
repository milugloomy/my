package com.imgl.wx.common;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imgl.wx.model.BackJnl;
import com.imgl.wx.model.Manager;
import com.imgl.wx.service.LogService;

@Service("backSessionAop")
public class BackSessionAop {
	protected Log log=LogFactory.getLog(getClass());
	@Autowired
	private LogService logService;
	
	public Object control(ProceedingJoinPoint pjp) throws Throwable{
		BackJnl backJnl=new BackJnl();
		Object retObj=null;

		Object[] args=pjp.getArgs();
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		for(int i=0;i<args.length;i++){
			if(args[i] instanceof HttpServletRequest)
				request=(HttpServletRequest) args[i];
			if(args[i] instanceof HttpServletResponse)
				response=(HttpServletResponse) args[i];
		}
		//校验session
		String transCode=request.getRequestURI().substring(request.getContextPath().length() + 1, request.getRequestURI().lastIndexOf("."));
		if(!"backLogin".equals(transCode) && request.getSession().getAttribute("Manager")==null){
			response.sendRedirect("/imgl/back/backLogin.jsp");
			return null;
		}
 		log.info("begin trans:"+transCode);
		log.info("paramter:"+request.getParameterMap());


		backJnl.setTransCode(transCode);
		backJnl.setClientIp(request.getRemoteAddr());//浏览器IP
		backJnl.setTransTime(new Timestamp(System.currentTimeMillis()));
		try{
			retObj=pjp.proceed(args);
			if("backLogin".equals(transCode)){//若为登录交易，到此才能取到userid
				Manager manager=(Manager) request.getSession().getAttribute("Manager");
				backJnl.setUserId(manager.getUserId());
			}
			backJnl.setReturnCode("000000");
		}catch(Exception e){
			log.error(e);
			backJnl.setReturnCode("error");
			backJnl.setReturnMsg(e.getMessage());
			throw e;
		}finally{
			if("adminLog".equals(transCode)){
			}else{
//				logService.saveBackJnl(backJnl);
			}
		}
		return retObj;
	}
}
