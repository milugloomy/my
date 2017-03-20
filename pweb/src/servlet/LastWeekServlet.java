package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import bean.Announce;

/**
 * Servlet implementation class LastWeekServlet
 */
public class LastWeekServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static String url="http://www.ccgp-hubei.gov.cn/fnoticeAction!listFNoticeInfos_n.action";
   /**
     * @see HttpServlet#HttpServlet()
     */
    public LastWeekServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		String endTime=sdf.format(c.getTimeInMillis());
		String beginTime=sdf.format(c.getTimeInMillis()-1000*3600*24*7);//7天前
		Map<String,String> map=new HashMap<String,String>();
		map.put("rank","");
		map.put("queryInfo.curPage","1");
		map.put("queryInfo.pageSize","1000");//查全部
		map.put("queryInfo.TITLE","");
		map.put("queryInfo.FBRMC","");
		map.put("queryInfo.GGLX","招标公告");
		map.put("queryInfo.CGLX","");
		map.put("queryInfo.CGFS","");
		map.put("queryInfo.BEGINTIME1",beginTime);
		map.put("queryInfo.ENDTIME1",endTime);
		map.put("queryInfo.QYBM","420100");
		map.put("queryInfo.JHHH","");
		Response res = Jsoup.connect(url)
				.timeout(30*1000)//30秒
				.method(Method.POST)
				.data(map)
				.execute();
		Document doc=Jsoup.parse(res.body());
		Element newsContent=doc.getElementsByClass("news_content").get(0);
		Elements lis=newsContent.getElementsByTag("li");
		List<Announce> announceList=new ArrayList<Announce>();
		for(Element li:lis){
			Announce announce=new Announce();
			Element a=li.getElementsByTag("a").get(0);

			String href=a.attr("href").replace("../", "");
			href="http://www.ccgp-hubei.gov.cn/"+href;
			announce.setHref(href);
			
			String name=a.text();
			announce.setName(name);

			String time=li.select("font[color=\"#000000\"]").get(0).text();
			time=time.replace("[发布时间 ", "").replace(" ]", "");
			announce.setTime(time);
			
			String type=li.select("font[color=\"blue\"]").get(0).text();
			announce.setType(type);
			
			announceList.add(announce);
		}
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter pw=response.getWriter();
		pw.write(new Gson().toJson(announceList));
		pw.flush();
		pw.close();
	}

}
