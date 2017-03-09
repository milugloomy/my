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
			connection =DriverManager.getConnection("jdbc:sqlite:purchase.db");
	    } catch (SQLException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		return connection;
	}

	public List<Announce> compare(List<Announce> announceList) {
		List<Announce> newList=new ArrayList<Announce>();
		Connection conn=null;
		try{
			conn=getConn();
			for(Announce announce:announceList){
				String time=announce.getTime();
				String type=announce.getType();
				String href=announce.getHref();
				String name=announce.getName();
			    PreparedStatement ps=conn.prepareStatement("select count(*) from announce where href=?");
			    ps.setString(1, href);
			    ResultSet rs=ps.executeQuery();
			    rs.next();
			    int count=rs.getInt(1);
			    if(count==0){//新公告 插入数据库
			    	String insertSql="insert into announce(time,type,href,name) values (?,?,?,?)";
			    	PreparedStatement psInsert=conn.prepareStatement(insertSql);
			    	psInsert.setString(1, time);
			    	psInsert.setString(2, type);
			    	psInsert.setString(3, href);
			    	psInsert.setString(4, name);
			    	psInsert.executeUpdate();
			    	newList.add(announce);
			    }
			}
		}catch(SQLException e){
			System.err.println(e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		
		return newList;
	}
	
}
