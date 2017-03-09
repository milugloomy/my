package com.imgl.wx.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class MyFilter
 */
public class BackReWriteFilter implements Filter {

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)arg0;
		HttpServletResponse response=(HttpServletResponse)arg1;
		String requestUri=request.getRequestURI();
		requestUri=requestUri.substring(requestUri.indexOf("/imgl")+5);
		if(requestUri.endsWith(".jsp")){
			if(request.getSession().getAttribute("Manager")!=null)
				//request.getServletContext()跳转绝对路径
				request.getServletContext().getRequestDispatcher(requestUri).forward(request, response);
			else
				request.getServletContext().getRequestDispatcher("/back/backLogin.jsp").forward(request, response);
		}else{
			if(request.getSession().getAttribute("Manager")!=null)
				request.getServletContext().getRequestDispatcher("/back/backIndex.jsp").forward(request, response);
			else
				request.getServletContext().getRequestDispatcher("/back/backLogin.jsp").forward(request, response);
		}
		
//		chain.doFilter(arg0, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
