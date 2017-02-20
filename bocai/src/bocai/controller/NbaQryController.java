package bocai.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import bocai.service.NbaQryService;


@Controller
public class NbaQryController {
	
	@Autowired
	private NbaQryService nbaQryService;
	private Gson gson=new Gson();
	
	@RequestMapping(method ={RequestMethod.POST},value = "/totalRateQry")
	@ResponseBody
	public String totalRateQry(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map=nbaQryService.totalRateQry();
		
		return gson.toJson(map);
	}
	
	@RequestMapping(method ={RequestMethod.POST},value = "/teamRateQry")
	@ResponseBody
	public String teamRateQry(HttpServletRequest request,HttpServletResponse response){
		String teamName=request.getParameter("teamName");
		Map<String,Object> map=nbaQryService.teamRateQry(teamName);
		
		return gson.toJson(map);
	}
	

}
