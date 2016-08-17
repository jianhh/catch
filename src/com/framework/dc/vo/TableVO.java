package com.framework.dc.vo;


public class TableVO {

	/**
	 * 表名
	 */
	private String tableName = "";

	/**
	 * 主键
	 */
	private String tablePk = "";

	/**
	 * 加载间隔时间，单位（分钟）
	 */
	private String refreshTime = "";

	/**
	 * 加载列名
	 */
	private String loadColumn = "";

	public String getTableName() {

		return tableName;
	}

	public void setTableName(String tableName) {

		this.tableName = tableName;
	}

	public String getTablePk() {

		return tablePk;
	}

	public void setTablePk(String tablePk) {

		this.tablePk = tablePk;
	}

	public String getRefreshTime() {

		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {

		this.refreshTime = refreshTime;
	}

	public String getLoadColumn() {

		return loadColumn;
	}

	public void setLoadColumn(String loadColumn) {

		this.loadColumn = loadColumn;
	}

}
