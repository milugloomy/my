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
			connection =DriverManager.getConnection("jdbc:sqlite:readWB.db");
	    } catch (SQLException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		return connection;
	}

	public List<WB> compare(List<WB> wbList) {
		Connection conn=getConn();
		List<WB> newList=new ArrayList<WB>();
		try{
			for(WB wb:wbList){
				WB owb=wb.getOwb();
				if(owb!=null){
					insertWB(conn,owb);
				}
				boolean isNew=insertWB(conn,wb);
				if(isNew){
					newList.add(wb);
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
	private boolean insertWB(Connection conn,WB wb){
		String mid=wb.getMid();
		String time=wb.getTime();
		String name=wb.getName();
		String target=wb.getTarget();
		String content=wb.getContent();
		List<String> imgUrlList=wb.getImgUrlList();
		List<String> simgUrlList=wb.getSimgUrlList();
		String videoUrl=wb.getVideoUrl();
		String omid=null;
		if(wb.getOwb()!=null)
			omid=wb.getOwb().getMid();
	    PreparedStatement ps;
		try {
			ps = conn.prepareStatement("select count(*) from wb where mid=?");
		    ps.setString(1, mid);
		    ResultSet rs=ps.executeQuery();
		    rs.next();
		    int count=rs.getInt(1);
		    if(count==0){//新微博 插入数据库
		    	String insertSql="insert into wb(mid,time,name,target,content,videoUrl,omid)"
		    			+ " values (?,?,?,?,?,?,?)";
		    	PreparedStatement psInsert=conn.prepareStatement(insertSql);
		    	psInsert.setString(1, mid);
		    	psInsert.setString(2, time);
		    	psInsert.setString(3, name);
		    	psInsert.setString(4, target);
		    	psInsert.setString(5, content);
		    	psInsert.setString(6, videoUrl);
		    	psInsert.setString(7, omid);
		    	if(!ParamBusiness.test){
			    	//插入WB
			    	psInsert.executeUpdate();
			    	//插入图片
			    	insertImg(conn,mid,simgUrlList,imgUrlList);
		    	}
	    		return true;
		    }
		} catch (SQLException e) {
			System.err.println(e);
		} 
		return false;
	}

	private void insertImg(Connection conn,String mid,
			List<String> simgUrlList,List<String> imgUrlList) {
		if(simgUrlList==null||imgUrlList==null)
			return;
		for(int i=0;i<simgUrlList.size();i++){
			String simgUrl=simgUrlList.get(i);
			String imgUrl=imgUrlList.get(i);
			String insertSql="insert into imgUrl(mid,simgUrl,imgUrl)"
	    			+ " values (?,?,?)";
	    	try {
		    	PreparedStatement psInsert=conn.prepareStatement(insertSql);
		    	psInsert.setString(1, mid);
		    	psInsert.setString(2, simgUrl);
		    	psInsert.setString(3, imgUrl);
				psInsert.executeUpdate();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}
	
}
