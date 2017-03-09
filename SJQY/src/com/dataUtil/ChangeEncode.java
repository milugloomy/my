package com.dataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class ChangeEncode {
	public static void main(String[] args) throws Exception {
		File root = new File("E:\\WZdataWorkSpace\\SJQY\\src");
		ChangeEncode ce=new ChangeEncode();
		ce.showAllFiles(root);
	}

	public void showAllFiles(File dir) throws Exception{
		File[] fs = dir.listFiles();
		for(int i=0; i<fs.length; i++){
			if(fs[i].isDirectory()){
				showAllFiles(fs[i]);
			}else{
				String filePath=fs[i].getAbsolutePath();
				if(filePath.endsWith(".sql")){
					System.out.println(filePath);
					FileInputStream fis=new FileInputStream(fs[i]);
					byte[] b=new byte[fis.available()];
					fis.read(b);
					fis.close();
					String fileContent=new String(b,"UTF-8");//按照该编码格式读
					FileOutputStream fos=new FileOutputStream(fs[i]);
					fos.write(fileContent.getBytes("GBK"));//按照该编码格式写
					fos.close();
				}
			}
		}
	}
}