package bocai.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import bocai.common.Util;

@Service("updateService")
public class UpdateService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public void update(){
		Calendar calendar = Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH)+1;
		int season=Util.getSeason();
		update(season,month);
	}
	
	public void updateAll(){
		//全部删除
		String deleteSql="delete from score";
		jdbcTemplate.update(deleteSql);
		//重新插入
		int[] season=new int[]{2014,2015,2016};
		int[] months=new int[]{10,11,12,01,02,03,04};
		for(int s=0;s<season.length;s++){
			for(int m=0;m<months.length;m++){
				update(season[s],months[m]);
			}
		}
	}
	
	public void update(int season,int month){
		int year=season;
		String matchMonth="";
		if(month<8){
			year=season+1;
			matchMonth=year+"0"+month;
		}else{
			matchMonth=year+""+month;
		}
		
		//大于本月
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.YEAR)>year || 
				(c.get(Calendar.YEAR)==year && month>(c.get(Calendar.MONTH)+1))
				){
			System.out.println("日期大于本月");
			return;
		}
		
		String url="http://app.gooooal.com/bks/schedule.do?lid=1021&sid="+season+"&st=1&lang=en&month="+matchMonth;
		Response res=null;
		try {
			res = Jsoup.connect(url).timeout(1000*20)
					.method(Method.GET).execute();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Document doc=Jsoup.parse(res.body());
		Element table=doc.getElementsByClass("dataSheet").get(0);
		Elements trs=table.getElementsByTag("tr");
		
		//存在则不插入
		String sql="insert into score "
				+ "select ?,?,?,?,?,?,?,?,? from dual "
				+ "where not exists(select 1 from score where id=?)";
		for(int i=1;i<trs.size();i++){
			Elements tds=trs.get(i).getElementsByTag("td");
			String timeStr=tds.get(0).text();
			Timestamp matchTime=getDate(timeStr);
			String state=tds.get(1).text();
			String homeTeam=tds.get(2).text();
			Integer id=getId(tds.get(3));
			if(id==null)
				continue;
			String score=tds.get(3).text();
			String visitTeam=tds.get(4).text();
			String halfScore=tds.get(5).text();
			String[] bocai=tds.get(6).text().split("/");
			Double letPoints=Double.valueOf(bocai[0]);
			Double totalPoints=Double.valueOf(bocai[1]);
			jdbcTemplate.update(sql, new Object[]{id,matchTime,homeTeam,visitTeam,score,halfScore,letPoints,totalPoints,state,id});
		}
		System.out.println(matchMonth);
	}
	

	private Integer getId(Element e){
		Elements as=e.getElementsByTag("a");
		if(as.size()>0){
			Element a=e.getElementsByTag("a").get(0);
			String href=a.attr("href");
			int start=href.indexOf("m=")+2;
			int end=href.indexOf("&");
			String id=href.substring(start, end);
			return Integer.valueOf(id);
		}else
			return null;
	}
	
	private Timestamp getDate(String timeStr){
		try {
			java.util.Date date=sdf.parse(timeStr);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}


