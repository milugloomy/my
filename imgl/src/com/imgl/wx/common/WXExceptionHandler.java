package com.imgl.wx.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


@Service("exceptionHandler")
public class WXExceptionHandler implements HandlerExceptionResolver{
	protected Log log=LogFactory.getLog(getClass());
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
			Exception ex) { 
		ex.printStackTrace();
		log.error(ex);
		Map<String, Object> model = new HashMap<String, Object>();  
		model.put("message", ex.getMessage());  

		// 根据不同错误转向不同页面  
		if(ex instanceof RuntimeException) {  
			return new ModelAndView("back/error-runtime", model);  
		} else {  
			return new ModelAndView("error", model);  
		}  
	}

}
