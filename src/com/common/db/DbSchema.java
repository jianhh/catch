package com.common.db;

/**
 * ���ݶ���
 * 
 * @author tangbiao
 * 
 */
public class DbSchema {
	private String sql;// sql���
	private Object[] paras;// sql��Ӧ�Ĳ���

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
