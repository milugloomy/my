package bocai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import bocai.service.UpdateService;


@Controller
public class UpdateController {
	
	@Autowired
	private UpdateService updateService;
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(HttpServletRequest request,HttpServletResponse response){
		String seasonStr=request.getParameter("season");
		String monthStr=request.getParameter("month");
		if(seasonStr!=null && monthStr!=null){
			int season=Integer.valueOf(seasonStr);
			int month=Integer.valueOf(monthStr);
			updateService.update(season,month);
		}else{
			updateService.update();
		}
		return "<script>alert('更新成功');</script>";
	}
	
	@RequestMapping(value = "/updateAll")
	@ResponseBody
	public String updateAll(HttpServletRequest request,HttpServletResponse response){
		updateService.updateAll();
		
		return "<script>alert('更新成功');</script>";
	}
	
	

}
