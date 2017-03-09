package com.dataUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DataUtil {
	public static final int step=1000;
	
	//default1 有值    map取不到值 返回default1
	//default1 无值   若key为空或者null 返回null map取不到值 返回key
	public static class MyMap<key,value> extends HashMap<key,value>{
		private static final long serialVersionUID = 8137209854493952714L;
		
		Object default1;
		public MyMap(){
			this.default1=null;
		}
		public MyMap(Object default1){
			this.default1=default1;
		}
		
		public value get(Object oKey){
			if(oKey==null || "".equals(oKey))
				return (value) default1;
			value oValue=super.get(oKey);
			if(oValue!=null){
				return oValue;
			}else{
				if(default1==null)
					return (value) oKey;
				else
					return (value) default1;
			}
		}
	}
	
	public static List<Object[]> tmpMList(List<Map> list,String state){
		List<Object[]> FLList=new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			FLList.add(new Object[]{state,list.get(i).get("CIFSEQ")});
		}
		return FLList;
	}	

	public static List<Object[]> tmpPList(List<Map> list,String state){
		List<Object[]> FLList=new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			FLList.add(new Object[]{state,list.get(i).get("CIFSEQ")});
		}
		return FLList;
	}	
	
	public static List<Object[]> tmpEList(List<Map> list,String state){
		List<Object[]> FLList=new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			FLList.add(new Object[]{state,list.get(i).get("CIFSEQ"),"ent"});
		}
		return FLList;
	}
	
	public static void printObjList(List<Object[]> List){
		for(int n=0;n<List.size();n++){
			System.out.print("(");
			Object[] obj=List.get(n);
			for(int m=0;m<obj.length;m++){
				System.out.print(obj[m]);
				if(n!=obj.length-1){
					System.out.print(",");
				}
			}
			if(n!=List.size()-1){
				System.out.print(",");
			}
			System.out.println(")");
		}
	}
	
	public static Properties getConfig(String fileName){
		Properties config=new Properties();
		try {
			InputStreamReader in=new InputStreamReader(DataUtil.class.getResourceAsStream(fileName),"UTF-8");
			config.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}

	/**18位身份证客户号转15位身份证客户号*/
	public static String get15CifNo(String cifNo) {
		return cifNo.substring(0,7)+cifNo.substring(9,18);
	}
	/**身份证最后一位换位小写*/
	public static String getxCifNo(String cifNo) {
		cifNo.replace("X", "x");
		return cifNo;
	}

	public static String listToString(Object[] objects) {
		StringBuffer err=new StringBuffer();
		for(int i=0;i<objects.length;i++){
			if(objects[i]!=null)
				err.append(objects[i].toString()+",");
		}
		return err.toString();
	}

	public static String getAcNo(String acNo,int charLength) {
		int sLength=charLength-acNo.length();
		StringBuffer suffix=new StringBuffer();
		for(int i=0;i<sLength;i++)
			suffix.append(" ");
		return acNo+suffix;
	}
	
}
