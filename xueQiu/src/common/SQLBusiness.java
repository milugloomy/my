package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SQLBusiness {
	
	private Connection getConn(){
		Connection connection = null;
	    try {
			Class.forName("org.sqlite.JDBC");
			connection =DriverManager.getConnection("jdbc:sqlite:xueQiu.db");
	    } catch (SQLException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		return connection;
	}

	public List<XueQiu> compare(List<XueQiu> xqList) {
		Connection conn=getConn();
		List<XueQiu> newList=new ArrayList<XueQiu>();
		
		try{
			for(XueQiu xueQiu:xqList){
				XueQiu retweet=xueQiu.getRetweet();
				if(retweet!=null){
					insertXueQiu(conn,retweet);
				}
				boolean isNew=insertXueQiu(conn,xueQiu);
				if(isNew){
					newList.add(xueQiu);
				}
			}
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return newList;
	}
	private boolean insertXueQiu(Connection conn,XueQiu xueQiu){
		String id=xueQiu.getId();
		String userId=xueQiu.getUserId();
		String title=xueQiu.getTitle();
		String target=xueQiu.getTarget();
		String text=xueQiu.getText();
		String info=xueQiu.getInfo();
		String source=xueQiu.getSource();
		String nickName =xueQiu.getNickName();
		List<String> imgUrlList=xueQiu.getImgUrlList();
		String retweetId=null;
		if(xueQiu.getRetweet()!=null)
			retweetId=xueQiu.getRetweet().getId();
	    PreparedStatement ps;
		try {
			ps = conn.prepareStatement("select count(*) from xueQiu where id=?");
		    ps.setString(1, id);
		    ResultSet rs=ps.executeQuery();
		    rs.next();
		    int count=rs.getInt(1);
		    if(count==0){//新微博 插入数据库
		    	String insertSql="insert into xueQiu(id,userId,title,target,text,info,source,nickName,retweetId)"
		    			+ " values (?,?,?,?,?,?,?,?,?)";
		    	PreparedStatement psInsert=conn.prepareStatement(insertSql);
		    	psInsert.setString(1, id);
		    	psInsert.setString(2, userId);
		    	psInsert.setString(3, title);
		    	psInsert.setString(4, target);
		    	psInsert.setString(5, text);
		    	psInsert.setString(6, info);
		    	psInsert.setString(7, source);
		    	psInsert.setString(8, nickName);
		    	psInsert.setString(9, retweetId);
		    	if(!ParamBusiness.test){
			    	//插入XueQiu
			    	psInsert.executeUpdate();
			    	//插入图片
			    	insertImg(conn,id,imgUrlList);
		    	}
	    		return true;
		    }
		} catch (SQLException e) {
			System.err.println(e);
		}
		return false;
	}

	private void insertImg(Connection conn,String id,List<String> imgUrlList) {
		if(imgUrlList==null)
			return;
		for(int i=0;i<imgUrlList.size();i++){
			String imgUrl=imgUrlList.get(i);
			String insertSql="insert into imgUrl(id,imgUrl)"
	    			+ " values (?,?)";
	    	try {
		    	PreparedStatement psInsert=conn.prepareStatement(insertSql);
		    	psInsert.setString(1, id);
		    	psInsert.setString(2, imgUrl);
				psInsert.executeUpdate();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}
	
}
