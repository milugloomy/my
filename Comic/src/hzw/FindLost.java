package hzw;

import java.io.File;

public class FindLost {
	public static void main(String[] args) {
		File[] hzwDir = new File("hzw").listFiles();
		for (int i = 0; i < hzwDir.length; i++) {
			File dir = hzwDir[i];
			File[] imgList = dir.listFiles();
			if(imgList.length==0)
				continue;
			String maxName=imgList[imgList.length-1].getName();
			int max=Integer.valueOf(maxName.substring(0,maxName.indexOf(".")));
			for(int j=0;j<max;j++){
				String fileName=imgList[j].getName();
				int no=Integer.valueOf(fileName.substring(0,fileName.indexOf(".")));
				if(no==j+1)
					continue;
				else{
					System.out.println("缺少:"+dir+"-----"+fileName);
					break;
				}
			}
		}
	}

}
