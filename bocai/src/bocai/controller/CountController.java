package bocai.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import bocai.bean.Bocai;
import bocai.bean.Row;
import bocai.service.CountService;


@Controller
public class CountController {
	
	private Gson gson=new Gson();
	@Autowired
	private CountService countService;
	
	@RequestMapping(value = "/count")
	@ResponseBody
	public String count(HttpServletRequest request,HttpServletResponse response){
		String bocaiType=request.getParameter("bocaiType");
		long times=Long.valueOf(request.getParameter("times"));//实验次数
		Double corpus=Double.valueOf(request.getParameter("corpus"));//本金，每次下注
		Double odds1=Double.valueOf(request.getParameter("odds1"));//赔率1
		Double rate=Double.valueOf(request.getParameter("rate"));//胜率

		Double earn=0.0;
		List<Row> list=new ArrayList<Row>();
		if("one".equals(bocaiType)){
			Bocai bocai=countService.countOne(times, corpus, rate, odds1);
			earn=bocai.getEarn();
			list.add(new Row("猜对次数",bocai.getWin()));
			list.add(new Row("猜错次数",bocai.getLose()));
		}else if("twoOne".equals(bocaiType)){
			Bocai bocai=countService.countTwoOne(times, corpus, rate, odds1);
			earn=bocai.getEarn();
			list.add(new Row("猜对次数",bocai.getWin()));
			list.add(new Row("猜错次数",bocai.getLose()));
		}else if("threeOne".equals(bocaiType)){
			Bocai bocai=countService.countThreeOne(times, corpus, rate, odds1);
			earn=bocai.getEarn();
			list.add(new Row("猜对次数",bocai.getWin()));
			list.add(new Row("猜错次数",bocai.getLose()));
		}else if("threeTwo".equals(bocaiType)){//3串2  由3个2串1组成
			Bocai bocai=countService.countThreeTwo(times, corpus, rate, odds1);
			earn=bocai.getEarn();
			list.add(new Row("全对次数",bocai.getWin()));
			list.add(new Row("猜对2场",bocai.getWin32_2()));
			list.add(new Row("猜错次数",bocai.getLose()));
		}else if("threeThree".equals(bocaiType)){//四分之一做三串一，四分之三做三串二
			Double odds2=Double.valueOf(request.getParameter("odds2"));//赔率2
			Bocai bocai=countService.countThreeThree(times, corpus, rate, odds1, odds2);
			earn=bocai.getEarn();
			list.add(new Row("三串一对",bocai.getWin31()));
			list.add(new Row("三串二对三场",bocai.getWin32()));
			list.add(new Row("三串二对两场",bocai.getWin32_2()));
		}
		DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
		df.setMaximumFractionDigits(6);
		
		list.add(new Row("总盈利",df.format(earn)));
		list.add(new Row("每次盈利",df.format(earn/times)));
		String gsonStr=gson.toJson(list);
		return gsonStr;
	}
	

}
