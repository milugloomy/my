package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainEntry {
	public static void main(String[] args){
		Timer timer=new Timer();
		String interval=ParamBusiness.getProperty("interval");
		timer.scheduleAtFixedRate(new MyTask(), 0, 
				Integer.valueOf(interval)*60*1000);//interval分钟查询一次
	}

	static class MyTask extends TimerTask{
		public MyTask(){
			http=new HttpBusiness();
			sb=new StringBusiness();
			sql=new SQLBusiness();
			word=new WordBusiness();
			mail=new MailBusiness();
		}
		
		public void run(){
			System.out.println(new Date()+"    执行任务：");

			String[] url=ParamBusiness.getProperty("url").split("\\|");
			List<Announce> sendList=new ArrayList<Announce>();
			for(int i=0;i<url.length;i++){
				try{
					String res = http.httpPost(url[i]);
		
					//处理返回的String
					List<Announce> announceList=sb.parseString(res);
		
					//和数据库比较 摘出最新的
					List<Announce> newList=sql.compare(announceList);
		
					//生成word文档
					word.geneWord(newList);
					
					//新的添加到sendList
					sendList.addAll(newList);
				}catch(Exception e){//忽略报错，继续执行
					System.err.println(e);
					continue;
				}
			}
				//有新公告则发送邮件
			try{
				mail.send(sendList);
			}catch(Exception e){//忽略报错，继续执行
				System.err.println(e);
			}
		}
		private HttpBusiness http;
		private StringBusiness sb;
		private SQLBusiness sql;
		private WordBusiness word;
		private MailBusiness mail;
	}
}
