package com.sjqy.common;

public class TransObject {
	
	private String sql;
	private Object[] objects;
	
	public TransObject() {}
	
	public TransObject(String sql, Object[] objects) {
		this.sql = sql;
		this.objects = objects;
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object[] getObjects() {
		return objects;
	}
	public void setObjects(Object[] objects) {
		this.objects = objects;
	}
}
