package common;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class StringBusiness {
	
	private Gson gson=new Gson();
	
	public List<WB> parseString(Response res){
		String contentHTML = null;//微博正文HTML
		
		Elements rootEles=Jsoup.parse(res.body()).getElementsByTag("script");
		for(Element e:rootEles){
			if(e.toString().indexOf("<script>FM.view")==-1)
				continue;
			String eJsonStr=e.toString()
					.replace("<script>FM.view(", "")
					.replace(")</script>", "")
					.replace(");</script>","");
			JsonObject eJson=gson.fromJson(eJsonStr, JsonObject.class);
			String domid=eJson.get("domid").getAsString();
			if(domid.startsWith("Pl_Official_MyProfileFeed")){
				contentHTML=eJson.get("html").getAsString();
			}
		}
		Document doc=Jsoup.parse(contentHTML);
		//每一条微博
		Elements eles=doc.select("div[action-type=feed_list_item]");
		
		List<WB> wbList=new ArrayList<WB>();
		for(Element e:eles){
			WB wb=new WB();
			String mid=e.attr("mid");
			setWB(wb,e,mid);

			//引用其他微博
			String omid=e.attr("omid");
			if(!"".equals(omid)){
				Element oe=e.select("div[node-type=feed_list_forwardContent]").get(0);
				WB owb=new WB();
				setWB(owb,oe,omid);
				//链接引用微博
				wb.setOwb(owb);
				wb.setImgUrlList(null);
				wb.setSimgUrlList(null);
				wb.setVideoUrl(null);
			}
			wbList.add(wb);
		}
		return wbList;
	}
	
	private void setWB(WB wb,Element e,String mid){
		String name=e.getElementsByClass("WB_info").get(0).text();
		Elements tagAs=e.getElementsByClass("WB_from").get(0)
				.getElementsByTag("a");
		String time=tagAs.get(0).attr("title");
		if(tagAs.size()>1){
			time+="  来自  "+tagAs.get(1).text();
		}
		String target="http://weibo.com"+tagAs.get(0).attr("href");
		String content=e.getElementsByClass("WB_text").get(0).html().trim();
		wb.setTarget(target);
		wb.setMid(mid);
		wb.setName(name);
		wb.setTime(time);
		wb.setContent(content);
		
		Elements mediaBox=e.getElementsByClass("media_box");
		if(mediaBox.size()!=0){
			//图片
			Elements imgEles=mediaBox.get(0).getElementsByTag("img");
			if(imgEles.size()>0){
				List<String> simgUrlList=new ArrayList<String>();
				List<String> imgUrlList=new ArrayList<String>();
				for(Element imgEle:imgEles){
					String surl=imgEle.attr("src");//小图链接
					simgUrlList.add(surl);
					int s1=surl.indexOf("/",8)+1;
					int s2=surl.indexOf("/",s1);
					String url=surl.substring(0,s1)+"mw690"+surl.substring(s2);//大图链接
					imgUrlList.add(url);
				}
				wb.setSimgUrlList(simgUrlList);
				wb.setImgUrlList(imgUrlList);
			}
			//视频
			Elements videoEles=mediaBox.get(0).getElementsByClass("WB_video");
			if(videoEles.size()>0){
				String actionData=videoEles.get(0).attr("action-data");
				String objectId=getUrlParam(actionData,"objectid");
				String videoUrl="http://video.weibo.com/player/"+objectId+"/v.swf";
				wb.setVideoUrl(videoUrl);
			}
		}
	}
	
	private String getUrlParam(String url, String key) {
	    String[] params = url.split("&");
	    for (int i = 0; i < params.length; i++) {
	        String[] p = params[i].split("=");
	        if (p.length == 2) {
	        	if(p[0].equals(key))
	            	return p[1];
	        }
	    }
	    return "";
	}
	
}
