package com.common.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * c3p0Á¬½Ó³Ø
 * 
 * @author tangbiao
 * 
 */
public class PooledDBA {
	private static JLogger logger = LoggerFactory.getLogger(PooledDBA.class);

	private ComboPooledDataSource cpds = null;
	private static String DBClassName = null;
	private static String DBUrl = null;
	private static String DBUser = null;
	private static String DBPassword = null;
	private static PooledDBA instance = null;

	public static synchronized PooledDBA getInstance() {
		if (instance == null) {
			instance = new PooledDBA();
		}
		return instance;
	}

	protected PooledDBA() {
		cpds = new ComboPooledDataSource("metkb");
		try {
			cpds.setDriverClass(DBClassName);
		} catch (PropertyVetoException e) {
			logger.error(e);
		}
		cpds.setJdbcUrl(DBUrl);
		cpds.setUser(DBUser);
		cpds.setPassword(DBPassword);
	}

	static {
		try {
			DBClassName = DBParas.getInstance().getJdbcDriver();
			DBUrl = DBParas.getInstance().getJdbcUrl();
			DBUser = DBParas.getInstance().getJdbcUsername();
			DBPassword = DBParas.getInstance().getJdbcPassword();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public synchronized Connection getConnection() throws SQLException,
			ClassNotFoundException, InterruptedException {
		return cpds.getConnection();
	}

	public synchronized void close() {
		try {
			DataSources.destroy(cpds);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public synchronized void close(Statement stat) {
		try {
			if (stat != null) {
				stat.close();
				stat = null;
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public synchronized void close(ResultSet rest) {
		try {
			if (rest != null) {
				rest.close();
				rest = null;
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}
}