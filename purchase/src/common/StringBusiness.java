package common;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StringBusiness {
	
	public List<Announce> parseString(String res){
		Document doc=Jsoup.parse(res);
		Element newsContent=doc.getElementsByClass("news_content").get(0);
		Elements lis=newsContent.getElementsByTag("li");

		List<Announce> announceList=new ArrayList<Announce>();
		for(Element li:lis){
			Announce announce=new Announce();
			Element a=li.getElementsByTag("a").get(0);

			String href=a.attr("href");
			href="http://www.ccgp-hubei.gov.cn"+href;
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
		
		return announceList;
	}
}
