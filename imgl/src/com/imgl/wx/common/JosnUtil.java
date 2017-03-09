package com.imgl.wx.common;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
@Component
public class JosnUtil {
	private static Gson gson=null;
	
	@PostConstruct
	private void inti(){
		gson=new Gson();
	}
	
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
}
