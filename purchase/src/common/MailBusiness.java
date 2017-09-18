package common;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class MailBusiness {
	
	private static String host="smtp.126.com";
	private static String user="milugloomy";
	private static String password="hahaha";
	private static String mailFrom="milugloomy@126.com";

	public void send(List<Announce> newList) {
		if(newList.size()==0){
			System.out.println("本次查询无更新，不发送邮件");
			return;
		}else{
			System.out.println("本次查询有新公告"+newList.size()+"条");
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
	
    public static MimeMessage createSimpleMail(Session session, List<Announce> newList)
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
        Multipart mp = new MimeMultipart();  
    	//body
    	MimeBodyPart mbpBody = new MimeBodyPart();  
        mp.addBodyPart(mbpBody);
        if(newList.size()==1){
        	Announce announce=newList.get(0);
            //添加附件
        	File tempFile=new File("temp/"+announce.getName()+".docx");
        	if(tempFile.exists()){
	            FileDataSource fds=new FileDataSource("temp/"+announce.getName()+".docx"); //得到数据源  
	        	MimeBodyPart mbpAttach = new MimeBodyPart();  
	            mbpAttach.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
	            mbpAttach.setFileName(MimeUtility.encodeWord(fds.getName()));
	            mp.addBodyPart(mbpAttach);  
        	}

            title="(新公告)"+announce.getName();
        	body+="名称："+announce.getName()+"<br/>";
        	body+="时间："+announce.getTime()+"<br/>";
        	body+="类型："+announce.getType()+"<br/>";
        	body+="链接："+announce.getHref()+"<br/>";
        }else{
        	title="一共有"+newList.size()+"条新公告";
        	body=title+"<br/>";
        	int i=1;
            for(Announce announce:newList){
            	body+="【"+i+"】:<br/>";
            	i++;
            	body+="名称："+announce.getName()+"<br/>";
            	body+="时间："+announce.getTime()+"<br/>";
            	body+="类型："+announce.getType()+"<br/>";
            	body+="链接："+announce.getHref()+"<br/>";
            	body+="<br/>";
            	body+="<hr/>";
            	//添加附件
            	File tempFile=new File("temp/"+announce.getName()+".docx");
            	if(tempFile.exists()){
    	            FileDataSource fds=new FileDataSource("temp/"+announce.getName()+".docx"); //得到数据源  
    	        	MimeBodyPart mbpAttach = new MimeBodyPart();  
    	            mbpAttach.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
    	            mbpAttach.setFileName(MimeUtility.encodeWord(fds.getName()));
    	            mp.addBodyPart(mbpAttach);  
            	}
            }
       }
    	//添加body
        mbpBody.setContent(body.toString(), "text/html;charset=UTF-8");  
        //邮件标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(mp);
        //返回创建好的邮件对象
        return message;
    }
}
