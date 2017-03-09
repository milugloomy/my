package com.sjqy.common;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;

@Service("BatchAction")
public class BatchAction{
	protected Log log = LogFactory.getLog(getClass());
	
	//list���¼һ��һ���������ݿ�
	public void excuteSingle(SqlMapClientTemplate smc,String sql,List<Object[]> list){
		Connection c = null;
		PreparedStatement ps=null;
		int index = 0;
		try {
			c = smc.getDataSource().getConnection();
			c.setAutoCommit(true);
			ps=c.prepareStatement(sql);
			for(int i=0;i<list.size();i++){
				index=i;
				for(int j=0;j<list.get(i).length;j++){
					if(list.get(i)[j]!=null)
						ps.setObject(j+1, list.get(i)[j]);
					else
						ps.setNull(j+1, 12);
				}
				try{
					ps.execute();
				}catch(SQLException e1){
					log.error("��������Ϊ��"+DataUtil.listToString(list.get(index)));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally{
			try {
				ps.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//list���¼�����������ݿ� DataUtil.step��һ��������
	public void excuteBatch(SqlMapClientTemplate smc,String sql,List<Object[]> list){
		int size=list.size();
		int start=0;
		while(size>start){
			int num=(size-start)>DataUtil.step?DataUtil.step:(size-start);
			List<Object[]> subList=list.subList(start, start+num);
			
			batch(smc,sql,subList);
			
			start+=DataUtil.step;
		}
	}
	
	
	private void batch(SqlMapClientTemplate smc,String sql,List<Object[]> list){
		Connection c = null;
		PreparedStatement ps=null;
		try {
			c = smc.getDataSource().getConnection();
			c.setAutoCommit(false);
			ps=c.prepareStatement(sql);
			for(int i=0;i<list.size();i++){
				for(int j=0;j<list.get(i).length;j++){
					if(list.get(i)[j]!=null)
						ps.setObject(j+1, list.get(i)[j]);
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
			//�ҳ���һ��List����������
			excuteSingle(smc, sql, list);
			throw new RuntimeException(e.getMessage());
		}finally{
			try {
				ps.close();
				c.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	/**һ��һ������һ������*/
	public void executeSingleTrans(SqlMapClientTemplate smc, ArrayList<TransObject> transList){
		Connection c = null;
		PreparedStatement ps=null;
		try {
			c = smc.getDataSource().getConnection();
			c.setAutoCommit(false);
			for(int i=0;i<transList.size();i++){
				TransObject transObject=transList.get(i);
				ps=c.prepareStatement(transObject.getSql());
				Object[] objects=transObject.getObjects();
				for(int j=0;j<objects.length;j++){
					if(objects[j]!=null)
						ps.setObject(j+1, objects[j]);
					else
						ps.setNull(j+1, 12);
				}
				ps.execute();
			}
			c.commit();
		}catch (SQLException e) {
			e.printStackTrace();
			try {
				c.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1.getMessage());
			}
		}finally{
			try {
				ps.close();
				c.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	/**batch��ʽ����һ������*/
	public void executeTrans(SqlMapClientTemplate smc, ArrayList<TransObject> transList){
		if(transList.size()==0)
			return;
		Connection c = null;
		Statement ps=null;
		try {
			c = smc.getDataSource().getConnection();
			c.setAutoCommit(false);
			for(int i=0;i<transList.size();i++){
				ps=c.createStatement();
				TransObject transObject=transList.get(i);
				String sql=transObject.getSql();
				Object[] objects=transObject.getObjects();
				for(int j=0;j<objects.length;j++){
					if(objects[j]==null)
						sql=sql.replaceFirst("\\?", "null");
					else
						sql=sql.replaceFirst("\\?", "'"+objects[j].toString().trim()+"'");
				}
				try{
					ps.addBatch(sql);
				}catch(SQLException e1){
					log.error("����SQL��"+transObject.getSql());
					throw new SQLException(e1);
				}
			}
			int[] i=ps.executeBatch();
			System.out.println(i);
			System.out.println(ps.EXECUTE_FAILED);
			c.commit();
		}catch (SQLException e) {
			e.printStackTrace();
			try {
				c.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1.getMessage());
			}
		}finally{
			try {
				ps.close();
				c.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}
