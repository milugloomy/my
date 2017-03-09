package com.dataUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class CheckCifNo {
	private static Connection c=null;

	public CheckCifNo() throws SQLException{
	}

	public static void main(String[] args) throws IOException, SQLException {
		BasicDataSource ds=new BasicDataSource();
		ds.setDriverClassName("oracle.jdbc.OracleDriver");
		ds.setUrl("jdbc:oracle:thin:@20.100.24.12:1521/wzccb");
		ds.setUsername("datamove");
		ds.setPassword("datamove");
		c=ds.getConnection();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpCardBin.class.getResourceAsStream("MUser.txt")));
		String s="";
		String sql="select count(*) from extr_mbcustinfo where cust_no=?";
		while((s=br.readLine())!=null){
			PreparedStatement ps=c.prepareStatement(sql);
			ps.setString(1, s);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int count=rs.getInt(1);
			if(count==0)
				System.out.println(s);
		}
	}

}
