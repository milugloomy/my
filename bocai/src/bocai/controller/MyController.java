package bocai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

import bocai.service.NbaQryService;

	

@Controller
public class MyController {
	
	@Autowired
	private NbaQryService nbaQryService;
	
	@RequestMapping(value = "/calculate")
	@ResponseBody
	public String calculate(HttpServletRequest request,HttpServletResponse response){
		String teamName=request.getParameter("teamName");//球队名
		String rhName=request.getParameter("rhName");//场次 最近五场 整个赛季
		String hvName=request.getParameter("hvName");//主客场
		String wlName=request.getParameter("wlName");//得失分
		
		Double res=nbaQryService.caculate(teamName,rhName,hvName,wlName);
		
		JsonObject jo=new JsonObject();
		jo.addProperty("result", res);
		
		return jo.toString();
	}

}
