package gif;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownGif {
	
	private static String base="http://www.nhcun.com";

	public static void main(String[] args) throws IOException{
		File gifDir=new File("xegif");
		if(!gifDir.exists())gifDir.mkdir();
		
		String html="http://www.nhcun.com/show/40238_6.html";
		while(true){
			Response gifRes=Jsoup.connect(html)
					.method(Method.GET).execute();
			Document gifDoc=Jsoup.parse(gifRes.body());
			Element imgDiv=gifDoc.getElementsByClass("contentimg").get(0);
			Elements imgs=imgDiv.getElementsByTag("img");
			if(imgs.size()>0){
				Element img=imgs.get(0);
				String href=img.attr("src");
				String picName=href.substring(href.lastIndexOf("/"));
				if(picName.endsWith(".gif")){
					downloadImage(href,gifDir.getAbsolutePath()+picName);
					System.out.println(href);
				}
			}
			String next=gifDoc.getElementsByClass("nextbtn").get(0).attr("href");
			if(next.indexOf("/")==-1){
				html=html.substring(0, html.lastIndexOf("/")+1)+next;
			}else{
				html=base+next;
			}
			System.out.println(html);
		}
	}

	private static void downloadImage(String imageSrc, String path){
		URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(path);
        try{
            urlfile = new URL(imageSrc);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
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
        }catch (SocketException e){
        	downloadImage(imageSrc,path);
        }catch (Exception e){
            System.err.println(e);
        }finally{
			try {
	        	if(bis!=null)
	        		bis.close();
	        	if(bos!=null)
	        		bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
