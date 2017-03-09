package hzw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class Dow {

	public static String baseDir="hzw/";
	public static final int timeout = 30*1000;
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Document hzw=Jsoup.connect("http://op.hanhande.com/mh/")
				.timeout(timeout).get();
		Elements chapters=hzw.getElementsByClass("mh_list_li").get(0).getElementsByTag("a");
		for(int i=chapters.size()-1;i>=0;i--){
			String chapterHref=chapters.get(i).attr("href");
			String chapterName=chapters.get(i).text();
			String chapterDirName=geneChapterName(chapterName,chapters.size()-i);
			
			File chapterDir=new File(baseDir+chapterDirName+"/");
			if(!chapterDir.exists()){
				chapterDir.mkdir();
			}
			
			String baseUrl=chapterHref.substring(0,chapterHref.lastIndexOf("/")+1);
			Document chapterDoc=null;
			try{
				chapterDoc=Jsoup.connect(chapterHref)
					.timeout(timeout).get();
			}catch(EOFException e){
				System.err.println(e);
				System.err.println("报错章节："+chapterDir);
				continue;
			}
			Elements pages=chapterDoc.getElementById("p__select").getElementsByTag("option");
			for(int j=0;j<pages.size();j++){
				Element page=pages.get(j);
				String pageHref=baseUrl+page.attr("value");
				String pageNo=genePageNo(page.text());
				Document pageDoc=null;
				try{
					pageDoc=Jsoup.connect(pageHref)
						.timeout(timeout).get();
				}catch(EOFException e){
					System.err.println(e);
					System.err.println("报错图片："+chapterDir+"----"+pageNo);
					continue;
				}
				Element img=pageDoc.getElementById("pictureContent").getElementsByTag("img").get(0);
				String imgSrc=img.attr("src");
				String imgName=chapterDir+"\\"+pageNo+imgSrc.substring(imgSrc.lastIndexOf("."));
				
				downloadImage(imgSrc,imgName);
			}
		}
		
	}
	
	private static void downloadImage(String imageSrc, String path) throws IOException {
		URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(path);
        try{
            urlfile = new URL(imageSrc);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.setConnectTimeout(30*1000);
            httpUrl.setRequestMethod("GET");
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
	private static String genePageNo(String pageNo){
		int noLength=pageNo.length();
		for(int m=3;m>noLength;m--){
			pageNo="0"+pageNo;
		}
		return pageNo;
	}
	
	private static String geneChapterName(String chapterName,int chapterNo){
		if(!chapterName.startsWith("海贼王"))
			chapterName="海贼王"+chapterName;
		String no=String.valueOf(chapterNo);
		int noLength=no.length();
		for(int m=3;m>noLength;m--){
			no="0"+no;
		}
		chapterName=no+"."+chapterName;
		return chapterName;
	}

}
