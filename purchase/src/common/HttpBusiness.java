package common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HttpBusiness {
	
	public static final int timeout = 30*1000;
	
	public String httpPost(String url, Map<String,String> params) throws IOException{
		Response res = Jsoup.connect(url)
				.timeout(HttpBusiness.timeout)
				.data(params)
				.method(Method.POST)
				.execute();
		return res.body();
	}
	
	public String httpPost(String url) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date today=new Date();
		String endTime=sdf.format(today);
		
		Date weekago=new Date(today.getTime()-7*24*60*60*1000);
		String beginTime=sdf.format(weekago);
		
		Map<String,String> paramMap=new HashMap<String,String>();
		paramMap.put("rank","");//市级
		paramMap.put("queryInfo.curPage","0");
		paramMap.put("queryInfo.pageSize","0");
		paramMap.put("queryInfo.TITLE","");//关键词
		paramMap.put("queryInfo.FBRMC","");//
		paramMap.put("queryInfo.GGLX","招标公告");//类型
		paramMap.put("queryInfo.CGLX","");//
		paramMap.put("queryInfo.CGFS","");//
		paramMap.put("queryInfo.BEGINTIME1",beginTime);//
		paramMap.put("queryInfo.ENDTIME1",endTime);//
		paramMap.put("queryInfo.QYBM","420100");//武汉市
		paramMap.put("queryInfo.JHHH","");//
		
		String s=httpPost(url,paramMap);
		return s;
	}
	
}
