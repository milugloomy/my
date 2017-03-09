package dmzj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Down {
	public static void main(String[] args) throws IOException{
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setThrowExceptionOnScriptError(false);//关键，忽略JS报错
		File kzgj=new File("kzgj");
		if(!kzgj.exists())kzgj.mkdir();
		
		Response kzgjRes=Jsoup.connect("http://manhua.dmzj.com/kzgj/")
				.method(Method.GET).execute();
		Document kzgjDoc=Jsoup.parse(kzgjRes.body());
		Elements chapters=kzgjDoc.getElementsByClass("cartoon_online_border")
				.first().getElementsByTag("a");
		for(int i=0;i<chapters.size();i++){//每一章
			Element chapter=chapters.get(i);
			String title=chapter.text();
			File dir=new File(kzgj.getPath()+"/"+title);
			if(!dir.exists())dir.mkdir();
			
			String chapterHref="http://manhua.dmzj.com"+chapter.attr("href");
			HtmlPage page = webClient.getPage(chapterHref);
			DomNodeList<HtmlElement> optionList=page.getElementById("page_select").getElementsByTagName("option");
			for(int j=0;j<optionList.size();j++){
				HtmlElement h=optionList.get(j);
				String imageSrc=h.getAttribute("value");
				String imageName=String.valueOf(j+1);
				if((j+1)<10)
					imageName="0"+imageName;
				downloadImage(imageSrc,dir.getPath()+"\\"+imageName+".jpg",chapterHref);
			}
		}
		webClient.close();
	}

	private static void downloadImage(String imageSrc, String path, String referer) throws IOException {
		URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(path);
        try{
            urlfile = new URL(imageSrc);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.setRequestMethod("GET");
            httpUrl.addRequestProperty("Referer", referer);
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            new String(b);
            while ((len = bis.read(b)) != -1){
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e){
            System.err.println(e);
        }
        finally{
            bis.close();
            bos.close();
        }
	}
}
