package com.framework.dc.vo;


public class TableVO {

	/**
	 * ����
	 */
	private String tableName = "";

	/**
	 * ����
	 */
	private String tablePk = "";

	/**
	 * ���ؼ��ʱ�䣬��λ�����ӣ�
	 */
	private String refreshTime = "";

	/**
	 * ��������
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
