package com.common.db;

import java.io.FileInputStream;
import java.util.Properties;

import com.framework.util.StringUtils;

/**
 * ���ӳ���������
 * 
 * @author tangbiao
 * 
 */
public final class DBParas {

	private String persistenceFileName;// ���ݿ�����������ƣ��磺�û���������

	private String sqlcodeFileName;// sql�����������

	// ���ӳ�����
	private String jdbcDriver;

	private String jdbcUrl;

	private String jdbcUsername;

	private String jdbcPassword;

	private String poolName = "weigouJDBC";// ���ӳ�����
	private int minConnections = 10; // ���гأ���С����������
	private int maxConnections = 200; // ���гأ����������
	private int initConnections = 10;// ��ʼ������������
	private int connTimeOut = 1000;// �ظ�������ӵ�Ƶ��
	private int maxActiveConnections = 300;// ���������������������ݿ��Ӧ����
	private int connectionTimeOut = 10;// ���ӳ�ʱʱ�䣬Ĭ��10����(mm)
	private boolean isCurrentConnection = true; // �Ƿ��õ�ǰ���ӣ�Ĭ��true
	private boolean isCheakPool = true; // �Ƿ�ʱ������ӳ�
	private long lazyCheck = 1000 * 60;// �ӳٶ���ʱ���ʼ ���(ms)
	private long periodCheck = 1000 * 60;// ���Ƶ��(ms)

	public void init(String persistenceFileName, String sqlcodeFileName)
			throws Exception {
		this.persistenceFileName = persistenceFileName;
		this.sqlcodeFileName = sqlcodeFileName;

		Properties persistenceProperties = new Properties();

		persistenceProperties.load(new FileInputStream(persistenceFileName));

		jdbcDriver = persistenceProperties.getProperty("jdbc.mysql.driver");

		jdbcUrl = persistenceProperties.getProperty("jdbc.mysql.url");

		jdbcUsername = persistenceProperties.getProperty("jdbc.mysql.username");

		jdbcPassword = persistenceProperties.getProperty("jdbc.mysql.password");

		poolName = persistenceProperties.getProperty("jdbc.mysql.poolName");
		String minConnectionsStr = persistenceProperties
				.getProperty("jdbc.mysql.minConnections");
		if (StringUtils.isNotEmpty(minConnectionsStr)) {
			minConnections = Integer.parseInt(minConnectionsStr);
		}
		String maxConnectionsStr = persistenceProperties
				.getProperty("jdbc.mysql.maxConnections");
		if (StringUtils.isNotEmpty(maxConnectionsStr)) {
			maxConnections = Integer.parseInt(maxConnectionsStr);
		}
		String initConnectionsStr = persistenceProperties
				.getProperty("jdbc.mysql.initConnections");
		if (StringUtils.isNotEmpty(initConnectionsStr)) {
			initConnections = Integer.parseInt(initConnectionsStr);
		}
		String connTimeOutStr = persistenceProperties
				.getProperty("jdbc.mysql.connTimeOut");
		if (StringUtils.isNotEmpty(connTimeOutStr)) {
			connTimeOut = Integer.parseInt(connTimeOutStr);
		}
		String maxActiveConnectionsStr = persistenceProperties
				.getProperty("jdbc.mysql.maxActiveConnections");
		if (StringUtils.isNotEmpty(maxActiveConnectionsStr)) {
			maxActiveConnections = Integer.parseInt(maxActiveConnectionsStr);
		}
		String connectionTimeOutStr = persistenceProperties
				.getProperty("jdbc.mysql.connectionTimeOut");
		if (StringUtils.isNotEmpty(connectionTimeOutStr)) {
			connectionTimeOut = Integer.parseInt(connectionTimeOutStr);
		}
		String lazyCheckStr = persistenceProperties
				.getProperty("jdbc.mysql.lazyCheck");
		if (StringUtils.isNotEmpty(lazyCheckStr)) {
			lazyCheck = Integer.parseInt(lazyCheckStr);
		}
		String periodCheckStr = persistenceProperties
				.getProperty("jdbc.mysql.periodCheck");
		if (StringUtils.isNotEmpty(periodCheckStr)) {
			periodCheck = Integer.parseInt(periodCheckStr);
		}

		SQLCode.getInstance().init();

	}

	private static DBParas instance = new DBParas();

	private DBParas() {

	}

	public static DBParas getInstance() {
		return instance;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public String getPersistenceFileName() {
		return persistenceFileName;
	}

	public String getSqlcodeFileName() {
		return sqlcodeFileName;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public int getMinConnections() {
		return minConnections;
	}

	public void setMinConnections(int minConnections) {
		this.minConnections = minConnections;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getInitConnections() {
		return initConnections;
	}

	public void setInitConnections(int initConnections) {
		this.initConnections = initConnections;
	}

	public int getConnTimeOut() {
		return connTimeOut;
	}

	public void setConnTimeOut(int connTimeOut) {
		this.connTimeOut = connTimeOut;
	}

	public int getMaxActiveConnections() {
		return maxActiveConnections;
	}

	public void setMaxActiveConnections(int maxActiveConnections) {
		this.maxActiveConnections = maxActiveConnections;
	}

	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public boolean isCurrentConnection() {
		return isCurrentConnection;
	}

	public void setCurrentConnection(boolean isCurrentConnection) {
		this.isCurrentConnection = isCurrentConnection;
	}

	public boolean isCheakPool() {
		return isCheakPool;
	}

	public void setCheakPool(boolean isCheakPool) {
		this.isCheakPool = isCheakPool;
	}

	public long getLazyCheck() {
		return lazyCheck;
	}

	public void setLazyCheck(long lazyCheck) {
		this.lazyCheck = lazyCheck;
	}

	public long getPeriodCheck() {
		return periodCheck;
	}

	public void setPeriodCheck(long periodCheck) {
		this.periodCheck = periodCheck;
	}

	public void setPersistenceFileName(String persistenceFileName) {
		this.persistenceFileName = persistenceFileName;
	}

	public void setSqlcodeFileName(String sqlcodeFileName) {
		this.sqlcodeFileName = sqlcodeFileName;
	}

	public static void setInstance(DBParas instance) {
		DBParas.instance = instance;
	}

}
