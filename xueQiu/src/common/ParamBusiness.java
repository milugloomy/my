package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ParamBusiness {
	private static final String path="param.ini";
	public static final boolean test=false;
	private static Properties prop;
	
	public static String getProperty(String key){
		if(prop==null)
			initProp();
		return prop.getProperty(key);
	}

	private static void initProp() {
		prop=new Properties();
		BufferedReader br=null;
		try {
			FileInputStream fis=new FileInputStream(path);
			br=new BufferedReader(new InputStreamReader(fis));
			String s="";
			while((s=br.readLine())!=null){
				if(s.startsWith("#"))
					continue;
				if(s.indexOf("=")==-1)
					throw new RuntimeException("配置文件错误，缺少等号");
				String key=s.substring(0,s.indexOf("="));
				String val=s.substring(s.indexOf("=")+1);
				if(prop.get(key)!=null){
					val=prop.getProperty(key)+"|"+val;
				}
				prop.setProperty(key, val);
			}
		} catch (IOException e) {
			System.err.println(e);
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
	public static void gFile(String html,String fileName) {
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(new File("src/"+fileName));
			fos.write(html.getBytes());
			fos.close();		
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
