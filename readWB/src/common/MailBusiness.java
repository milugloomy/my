package common;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailBusiness {
	
	private static String host="smtp.163.com";
	private static String user="milugloomy";
	private static String password="nitai2le";
	private static String mailFrom="milugloomy@163.com";

	public void send(List<WB> newList) {
		if(newList.size()==0){
			System.out.println("本次查询无更新，不发送邮件");
			return;
		}else{
			System.out.println("本次查询有新微博"+newList.size()+"条");
		}
        Properties prop = new Properties();
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(false);
        //2、通过session得到transport对象
        Transport ts = null;
		try {
			ts = session.getTransport();
	        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
	        ts.connect(host, user, password);
	        //4、创建邮件
	        Message message = createSimpleMail(session,newList);
	        //5、发送邮件
	        ts.sendMessage(message, message.getAllRecipients());
	        System.out.println("邮件发送成功");
		} catch (Exception e) {
			System.err.println(e);
		} finally{
			try {
				if(ts!=null)
					ts.close();
			} catch (MessagingException e) {
				System.err.println(e);
			}
		}
	}
	
    public MimeMessage createSimpleMail(Session session, List<WB> newList)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(mailFrom));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        String[] mailTo=ParamBusiness.getProperty("mailTo").split("\\|");
        for(int i=0;i<mailTo.length;i++)
        	message.addRecipient(Message.RecipientType.TO,
        			new InternetAddress(mailTo[i]));
        
        String body="";
        String title="";
        if(newList.size()==1){
        	WB wb=newList.get(0);
        	title="(新微博)"+wb.getTime()+"--"+wb.getName();
        	body=geneBody(wb);
        }else{
        	String interval=ParamBusiness.getProperty("interval");
        	title="(新微博)"+interval+"分钟内一共有"+newList.size()+"条新微博";
        	body=title+"<br/>";
        	int m=1;
            for(WB wb:newList){
            	body+="【"+m+"】:<br/>";
            	body+=geneBody(wb);
            	body+="<hr/>";
            	m++;
            }
        }
        //邮件标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(body, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
    
	private String geneBody(WB wb){
		String body="";
		body+="<div style=\"font-size:14px\">"+wb.getName()+"</div>";
    	body+="<div style=\"font-size:12px;color:#919191\">"+wb.getTime()+"</div>";
    	body+="<div style=\"font-size:14px\">"+wb.getContent()+"</div>";
    	if(wb.getSimgUrlList()!=null){
        	for(int i=0;i<wb.getSimgUrlList().size();i++){
        		body+="<a href=\""+wb.getImgUrlList().get(i)+"\" target=\"_blank\">";
            	body+="<img src=\""+wb.getSimgUrlList().get(i)+"\"/>";
            	body+="</a>";
        	}
        	body+="<br/>";
    	}
    	if(wb.getVideoUrl()!=null){
    		body+="视频链接："+wb.getVideoUrl()+"<br/>";
    	}
    	if(wb.getOwb()!=null){
    		String quote="<div style=\"padding:8px;background-color:#f2f2f5;border-left:2px solid;\">"
    				+geneBody(wb.getOwb())+"</div>";
    		body+=quote;
    	}
    	body+="<div style=\"font-size:14px;font-weight:bold;\">"
    			+"<a style=\"text-decoration:none\" href=\""+wb.getTarget()+"\">"
    			+"查看原文</a></div>";
    	return body;
	}
}
