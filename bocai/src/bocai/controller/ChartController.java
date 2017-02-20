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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import bocai.bean.Bocai;
import bocai.bean.Section;
import bocai.service.CountService;


@Controller
public class ChartController {
	
	public static int step=100;//一组菠菜次数
	public static int secCount=20;//统计时数据分组数
	private Gson gson=new Gson();
	@Autowired
	private CountService countService;
	
	@RequestMapping(method ={RequestMethod.POST},value = "/gChart")
	@ResponseBody
	public String gChart(HttpServletRequest request,HttpServletResponse response){
		String bocaiType=request.getParameter("bocaiType");
		long times=Long.valueOf(request.getParameter("times"));//实验次数
		Double corpus=Double.valueOf(request.getParameter("corpus"));//本金，每次下注
		Double odds1=Double.valueOf(request.getParameter("odds1"));//赔率1
		Double rate=Double.valueOf(request.getParameter("rate"));//胜率
		
		long groups=times/step;
		Double[] earn=new Double[new Long(groups).intValue()];
		if("one".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countOne(step, corpus, rate, odds1);
				earn[i]=bocai.getEarn();
			}
		}else if("twoOne".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countTwoOne(step, corpus, rate, odds1);
				earn[i]=bocai.getEarn();
			}
		}else if("threeOne".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeOne(step, corpus, rate, odds1);
				earn[i]=bocai.getEarn();
			}
		}else if("threeTwo".equals(bocaiType)){//3串2  由3个2串1组成
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeTwo(step, corpus, rate, odds1);
				earn[i]=bocai.getEarn();
			}
		}else if("threeThree".equals(bocaiType)){//四分之一做三串一，四分之三做三串二
			Double odds2=Double.valueOf(request.getParameter("odds2"));//赔率2
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeThree(step, corpus, rate, odds1, odds2);
				earn[i]=bocai.getEarn();
			}
		}
		List<Section> list=toList(earn);
		String gsonStr=gson.toJson(list);
		return gsonStr;
	}
	
	@RequestMapping(method ={RequestMethod.POST},value = "/npChart")
	@ResponseBody
	public String npChart(HttpServletRequest request,HttpServletResponse response){
		String bocaiType=request.getParameter("bocaiType");
		long times=Long.valueOf(request.getParameter("times"));//实验次数
		Double corpus=Double.valueOf(request.getParameter("corpus"));//本金，每次下注
		Double odds1=Double.valueOf(request.getParameter("odds1"));//赔率1
		Double rate=Double.valueOf(request.getParameter("rate"));//胜率
		
		long groups=times/step;
		Double[] earnMax=new Double[new Long(groups).intValue()];
		Double[] earnMin=new Double[new Long(groups).intValue()];
		
		if("one".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countOne(step, corpus, rate, odds1);
				earnMax[i]=bocai.getEarnMax();
				earnMin[i]=bocai.getEarnMin();
			}
		}else if("twoOne".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countTwoOne(step, corpus, rate, odds1);
				earnMax[i]=bocai.getEarnMax();
				earnMin[i]=bocai.getEarnMin();
			}
		}else if("threeOne".equals(bocaiType)){
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeOne(step, corpus, rate, odds1);
				earnMax[i]=bocai.getEarnMax();
				earnMin[i]=bocai.getEarnMin();
			}
		}else if("threeTwo".equals(bocaiType)){//3串2  由3个2串1组成
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeTwo(step, corpus, rate, odds1);
				earnMax[i]=bocai.getEarnMax();
				earnMin[i]=bocai.getEarnMin();
			}
		}else if("threeThree".equals(bocaiType)){//四分之一做三串一，四分之三做三串二
			Double odds2=Double.valueOf(request.getParameter("odds2"));//赔率2
			for(int i=0;i<groups;i++){
				Bocai bocai=countService.countThreeThree(step, corpus, rate, odds1, odds2);
				earnMax[i]=bocai.getEarnMax();
				earnMin[i]=bocai.getEarnMin();
			}
		}
		
		List<Section> maxList=toList(earnMax);
		List<Section> minList=toList(earnMin);
		JsonObject jo=new JsonObject();
		jo.addProperty("maxList", gson.toJson(maxList));
		jo.addProperty("minList", gson.toJson(minList));
		return jo.toString();
	}
	
	private List<Section> toList(Double[] earn){
		List<Section> list=new ArrayList<Section>();
		DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);
		
		Double max=earn[0];
		Double min=earn[0];
		for(int i=0;i<earn.length;i++){
			if(earn[i]>max)
				max=earn[i];
			if(earn[i]<min)
				min=earn[i];
		}
		Double secWidth=(max-min)/secCount;
		for(int i=0;i<secCount;i++){
			Section section=new Section();
			section.setStart(min+i*secWidth);
			section.setEnd(min+(i+1)*secWidth);
			Double avg=(section.getStart()+section.getEnd())/2;
			section.setAvg(new Double(df.format(avg).replace(",", "")));
			list.add(section);
		}
		for(int i=0;i<earn.length;i++){
			for(int j=0;j<secCount;j++){
				Section section=list.get(j);
				Double start=section.getStart();
				Double end=section.getEnd();
				if( earn[i]<=end && earn[i]>=start ){
					section.setCount(section.getCount()+1);
					break;
				}
			}
		}
		return list;
	}

}
