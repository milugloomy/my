package com.dataUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp.BasicDataSource;

public class ImpBankCode {
	private int poolNum=0;
	private ThreadPoolExecutor pool;
	private static Connection c=null;
	private int threadCount=25;
	
	public ImpBankCode() throws SQLException{
		pool= new ThreadPoolExecutor(
				0, threadCount, 20L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(threadCount),
				new ThreadFactory(){
			        public Thread newThread(Runnable runnable){
			            return new Thread(runnable, "BatchThreadPool"+"-"+poolNum++);
			        }
			    },
		        new ThreadPoolExecutor.CallerRunsPolicy());
		
		BasicDataSource ds=new BasicDataSource();
		ds.setDriverClassName("oracle.jdbc.OracleDriver");
		ds.setUrl("jdbc:oracle:thin:@20.100.24.12:1521/wzccb");
		ds.setUsername("eip");
		ds.setPassword("eip");
		ds.setMaxActive(threadCount);
		c=ds.getConnection();
	}
	public static void main(String[] args) throws SQLException, IOException{
		ImpBankCode ibc=new ImpBankCode();
		try{
//			ibc.insertApsBank();
//			ibc.insertApsCity();
//			ibc.insertApsProvince();
			ibc.insertApsRtgsNode();
//			ibc.insertIbpsNode();
		}finally{
			c.close();
		}
	}
	public void insertApsBank() throws IOException{
		String sql="insert into MCAPSBANK(BANKNO,BANKNAME)values(?,?)";
		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpBankCode.class.getResourceAsStream("mcapsbank.txt")));
		String s=br.readLine();
		while((s=br.readLine())!=null){
			String[] arr=s.replace("\"", "").split("\t");
			list.add(new String[]{arr[0],arr[1]});
		}
		br.close();
		batch(c,sql,list);
	}
	public void insertApsCity() throws IOException{
		String sql="insert into MCAPSCITY(CITYCD,CITYNAME,PROVCD)values(?,?,?)";
		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpBankCode.class.getResourceAsStream("mcapscity.txt")));
		String s=br.readLine();
		while((s=br.readLine())!=null){
			String[] arr=s.replace("\"", "").split("\t");
			list.add(new String[]{arr[0],arr[1],arr[2]});
		}
		br.close();
		batch(c,sql,list);
	}
	public void insertApsProvince() throws IOException{
		String sql="insert into MCAPSPROVINCE(PROVCD,PROVNAME,PROVTYPE)values(?,?,?)";
		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpBankCode.class.getResourceAsStream("mcapsprovince.txt")));
		String s=br.readLine();
		while((s=br.readLine())!=null){
			String[] arr=s.replace("\"", "").split("\t");
			list.add(new String[]{arr[0],arr[1],arr[2]});
		}
		br.close();
		batch(c,sql,list);
	}
	public void insertApsRtgsNode() throws IOException{
		final String sql="insert into MCAPSRTGSNODE"
				+ "(BANKCODE,STATUS,CATEGORY,CLSCODE,DRECCODE,NODECODE,SUPRLIST,PBCCODE,CITYCODE,"
				+ " ACCTSTATUS,ASALTDT,ASALTTM,LNAME,SNAME,ADDR,POSTCODE,TEL,EMAIL,"
				+ " EFFDATE,INVDATE,ALTDATE,ALTTYPE,ALTISSNO,REMARK,ALTFLAG,LARGEAMOUNTFLAG,SMALLAMOUNTFLAG)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpBankCode.class.getResourceAsStream("mcapsrtgsnode.txt")));
		String s=br.readLine();
		int count=0;
		while((s=br.readLine())!=null){
			String[] arr=s.split("\t");
			list.add(new String[]{arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],
					arr[9],arr[10],arr[11],arr[12],arr[13],arr[14],arr[15],arr[16],arr[17],
					arr[18],arr[19],arr[20],arr[21],arr[22],arr[23],arr[24],arr[25],arr[26]
					});
			count++;
			if(count%1000==0){
				final ArrayList<String[]> runList=new ArrayList<String[]>();
				runList.addAll(list);
				pool.execute(new Runnable(){
					public void run() {
						runList.size();
						batch(c,sql,runList);
					}
				});
				list=new ArrayList<String[]>();
			}
		}
		br.close();
		batch(c,sql,list);
	}
	public void insertIbpsNode() throws IOException{
		String sql="insert into MCIBPSNODE(BANKID,BANKNAME,BANKALIASNAME,BANKCTGYCODE,"
				+ " BANKCTGYNAME,JOINTYPE,AGENTBANKCODE,EFFECTIVEDATE,STATUS)"
				+ "values(?,?,?,?,?,?,?,?,?)";
		ArrayList<String[]> list=new ArrayList<String[]>();
		BufferedReader br=new BufferedReader(new InputStreamReader(ImpBankCode.class.getResourceAsStream("mcibpsnode.txt")));
		String s=br.readLine();
		while((s=br.readLine())!=null){
			String[] arr=s.replace("\"", "").split("\t");
			list.add(new String[]{arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8]});
		}
		br.close();
		batch(c,sql,list);
	}
	
	private void batch(Connection c,String sql,List<String[]> list){
		PreparedStatement ps=null;
		try {
			c.setAutoCommit(false);
			ps=c.prepareStatement(sql);
			for(int i=0;i<list.size();i++){
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
