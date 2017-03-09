package common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class StringBusiness {

	private Gson gson = new Gson();

	public List<XueQiu> parseString(String res) {
		List<XueQiu> list=new ArrayList<XueQiu>();
		
		Document doc = Jsoup.parse(res);
		Elements scripts = doc.getElementsByTag("script");
		String scriptStr = null;
		for (int i = 0; i < scripts.size(); i++) {
			String scriptText = scripts.get(i).html();
			if (scriptText.indexOf("SNB.data.statuses") != -1) 
				scriptStr = scripts.get(i).html();
		}
		int start = scriptStr.indexOf("SNB.data.statuses") + 20;
		int end = scriptStr.indexOf("};", start);

		JsonObject eJsonAll=gson.fromJson(scriptStr.substring(start, end + 1), JsonObject.class);
		JsonArray ja=eJsonAll.get("statuses").getAsJsonArray();
		for(int i=0;i<ja.size();i++){
			XueQiu xueQiu=new XueQiu();
			JsonObject eJson=gson.fromJson(ja.get(i),JsonObject.class);
			setXueQiu(eJson,xueQiu);
			
			String retweet_status_id = eJson.get("retweet_status_id").getAsString();
			if(!"0".equals(retweet_status_id)){//引用
				XueQiu retweet=new XueQiu();
				JsonObject retweetJson=gson.fromJson(eJson.get("retweeted_status"), JsonObject.class);;
				setXueQiu(retweetJson,retweet);
				
				xueQiu.setRetweet(retweet);
			}
			list.add(xueQiu);
		}

//		ParamBusiness.gFile(scriptStr.substring(start, end + 1), "b.html");
		return list;
	}
	
	private void setXueQiu(JsonObject eJson,XueQiu xueQiu){
		String id=eJson.get("id").getAsString();
		String userId = eJson.get("user_id").getAsString();
		
		String title = eJson.get("title").getAsString();
		String target = eJson.get("target").getAsString();
		String text = eJson.get("text").getAsString();
		String info = eJson.get("timeBefore").getAsString();
		info=geneInfo(info);
		String source = eJson.get("source").getAsString();
		JsonObject user=gson.fromJson(eJson.get("user"), JsonObject.class);;
		String nickName = user.get("screen_name").getAsString();
		xueQiu.setId(id);
		xueQiu.setUserId(userId);
		xueQiu.setTitle(title);
		xueQiu.setTarget(target);
		xueQiu.setText(geneText(text));
		xueQiu.setInfo(info);
		xueQiu.setSource(source);
		xueQiu.setNickName(nickName);
		if (eJson.get("pic")!=JsonNull.INSTANCE && 
				!"".equals(eJson.get("pic").getAsString())) {//包含图片
			List<String> imgUrlList = genePic(eJson.get("pic").getAsString());
			xueQiu.setImgUrlList(imgUrlList);
		}
	}

	private String geneInfo(String info) {
		String out="";
		if(info.indexOf("前")!=-1){
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
			if(info.indexOf("秒")!=-1){
				out=sdf.format(date);
			}else if(info.indexOf("分")!=-1){
				String minBeforeStr="";
				Pattern p = Pattern.compile("[0-9]+");
				Matcher m=p.matcher(info);
				if(m.find()){
					minBeforeStr+=m.group(0);
				}
				int minBefore=Integer.valueOf(minBeforeStr);
				long mills=date.getTime()-minBefore*60*1000;
				date.setTime(mills);
				out=sdf.format(date);
			}
		}else{
			out=info;
		}
		return out;
	}

	private String geneText(String text) {
		String out=text;
		out=out.replaceAll("<img[^>]+>", "");//+重复1次或多次
		out=out.replaceAll("<p></p>", "");//+重复1次或多次
		out=out.replaceAll("<br[^>]*>", "");//*重复0次或多次
		return out;
	}

	private List<String> genePic(String str) {
		List<String> imgUrlList = new ArrayList<String>();
		String[] picArr = str.split(",");
		for (int i = 0; i < picArr.length; i++) {
			imgUrlList.add("http:" + picArr[i].replace("!thumb.jpg", ""));
		}
		return imgUrlList;
	}

}
