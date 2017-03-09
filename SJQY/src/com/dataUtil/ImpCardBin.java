package com.dataUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;

public class ImpCardBin {
	private static Connection c=null;
	private int threadCount=25;
	
	public ImpCardBin() throws SQLException{
		BasicDataSource ds=new BasicDataSource();
		ds.setDriverClassName("oracle.jdbc.OracleDriver");
		ds.setUrl("jdbc:oracle:thin:@20.100.24.12:1521/wzccb");
		ds.setUsername("eip");
		ds.setPassword("eip");
		ds.setMaxActive(threadCount);
		c=ds.getConnection();
	}
	public static void main(String[] args) throws SQLException, IOException{
		ImpCardBin icb=new ImpCardBin();
		try{
			icb.insertMcCardBin();
		}finally{
			c.close();
		}
	}
	public void insertMcCardBin() throws IOException{
		String sql="insert into MCCARDBIN(BINNO,BANKTYPE,ISSUEBANK,DEPTID,CARDTYPE,"
				+ " CARDNOLEN,CARDLEVEL,CARDCURRENCYTYPE,CARDCURRENCY,BANKNAME)"
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		

		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpCardBin.class.getResourceAsStream("IN2015111001YWBIN")));
		String s=br.readLine();
		Set<String> set=new HashSet<String>();
		while((s=br.readLine())!=null){
			StringBuffer sb=new StringBuffer();
			String[] arr=new String[7];
			int j=0;
			try{
				for(int i=0;i<s.length()-1;i++){
					if(j==1){//不能用空格来区分  
						if(s.charAt(i+1)<'0' || s.charAt(i+1)>'9'){
							sb.append(s.charAt(i));
						}else{
							arr[j++]=sb.toString().trim();
							sb=new StringBuffer();
						}
					}else if(s.charAt(i)!=' '){//下一个是空格
						sb.append(s.charAt(i));
						if(s.charAt(i+1)==' '){
							arr[j++]=sb.toString();
							sb=new StringBuffer();
						}
					}
				}
			}catch(Exception e){
				System.out.println(s);
			}
			String deptId=arr[0];
			String issueBank=arr[1];
			String cardNoLen=arr[2];
			String binNo=arr[3];
			String bankType=arr[4];
			String cardType=arr[5];
			list.add(new String[]{
					binNo,bankType,issueBank,deptId,cardType,cardNoLen,null,"S","CNY",issueBank
			});
		}
		br.close();
		batch(c,sql,list);
	}
	
	private void batch(Connection c,String sql,List<String[]> list){
		PreparedStatement ps=null;
		int in=0;
		try {
			c.setAutoCommit(false);
			ps=c.prepareStatement(sql);
			for(int i=0;i<list.size();i++){
				in=i;
				for(int j=0;j<list.get(i).length;j++){
					if(list.get(i)[j]!=null)
						ps.setObject(j+1, list.get(i)[j].replace("\"", ""));
					else
						ps.setNull(j+1, 12);
				}
				ps.addBatch();
			}
			ps.executeBatch();
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e.getMessage());
			}
			throw new RuntimeException(e.getMessage());
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
}
