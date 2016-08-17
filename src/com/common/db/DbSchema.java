package com.common.db;

/**
 * 数据对象
 * 
 * @author tangbiao
 * 
 */
public class DbSchema {
	private String sql;// sql语句
	private Object[] paras;// sql对应的参数

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParas() {
		return paras;
	}

	public void setParas(Object[] paras) {
		this.paras = paras;
	}
}
