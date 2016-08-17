package com.common.db;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.RowSet;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.sun.rowset.CachedRowSetImpl;

/**
 * <p>
 * 事务数据库操作类
 * </p>
 * <p>
 * Copyright (c) 2003-2006 ASPire TECHNOLOGIES (SHENZHEN) LTD All Rights
 * Reserved
 * </p>
 * 
 * @author shidr
 * @version 1.0.1.0
 * @since 1.0.1.0
 */
public class DB {

	/**
	 * 日志
	 */
	private static final JLogger LOG = LoggerFactory.getLogger(DB.class);

	/**
	 * singleton模式
	 */
	private static DB instance = new DB();

	/**
	 * 事物场景下的链接
	 */
	Connection transactionConn = null;

	/**
	 * 私有构建器
	 */
	DB() {

	}

	/**
	 * 获取一个非事务场景的实例
	 * 
	 * @return TransactionDB
	 */
	public static DB getInstance() {

		return instance;
	}

	public void init(String persistenceFileName, String sqlcodeFileName)
			throws DAOException {

		try {
			DBParas.getInstance().init(persistenceFileName, sqlcodeFileName);// 初始化数据库连接参数
			// ConnectionPoolManager.getInstance().init();// 初始化所有的连接池
		} catch (DAOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new DAOException("TransactionContext init failed!", ex);
		}
	}

	/**
	 * 执行多条sql语句
	 * 
	 * @param dbList
	 * @return boolean
	 * @throws Exception
	 */
	public boolean executeList(List<DbSchema> dbList) throws Exception {

		Connection conn = null;
		PreparedStatement statement = null;
		boolean flag = false;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);// 取消自动提交
			for (DbSchema db : dbList) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("execute sql is:" + db.getSql());
				}
				statement = conn.prepareStatement(db.getSql());
				this.setParas(statement, db.getParas());
				int result = statement.executeUpdate();
				if (LOG.isDebugEnabled()) {
					LOG.debug("数据库操作結果: " + result);
				}
			}
			flag = true;
			conn.commit();// 提交
		} catch (Exception e) {
			conn.rollback();// 回滚
			throw new DAOException("execute failed.", e);
		} finally {
			// 释放资源
			this.releaseDBResource(conn, statement, null);
		}
		return flag;
	}

	/**
	 * 执行一个sql语句
	 * 
	 * @param sql
	 *            String,sql语句
	 * @param paras
	 *            Object[],sql语句需要的参数，如果没有就为null
	 * @return int,几条数据库记录被更新。
	 * @throws DAOException
	 */
	public int execute(String sql, Object[] paras) throws DAOException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("execute sql is:" + sql);
		}
		Connection conn = null;
		PreparedStatement statement = null;
		int result = 0;
		try {
			conn = this.getConnection();
			statement = conn.prepareStatement(sql);
			this.setParas(statement, paras);
			result = statement.executeUpdate();
		} catch (Exception e) {
			// if (num < 10) {
			// LOG.error("SocketException:" + num, e);
			// num += 1;
			// this.execute(sql, paras);
			// } else {
			// throw new DAOException("execute failed." + num, e);
			// }
			throw new DAOException("execute failed.", e);
		} finally {
			// 释放资源
			this.releaseDBResource(conn, statement, null);
		}
		return result;
	}

	/**
	 * 执行一个sql语句
	 * 
	 * @param sql
	 *            String,sql语句
	 * @param paras
	 *            Object[],sql语句需要的参数，如果没有就为null
	 * @return ResultSet,查询到的结果集合
	 * @throws DAOException
	 */
	public RowSet query(String sql, Object[] paras) throws DAOException {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		CachedRowSetImpl crs = null;
		try {
			conn = this.getConnection();
			if (LOG.isDebugEnabled()) {
				LOG.debug("query sql is:" + sql);
			}
			statement = conn.prepareStatement(sql);
			this.setParas(statement, paras);
			rs = statement.executeQuery();
			crs = new CachedRowSetImpl();
			crs.populate(new ResultSetWrapper(rs));

			LOG.debug("crs.size():" + crs.size());
		} catch (Exception e) {
			// if (num < 10) {
			// LOG.error("SocketException:" + num, e);
			// num += 1;
			// this.execute(sql, paras);
			// } else {
			throw new DAOException("execute failed.", e);
			// }
		} finally {
			// 释放资源
			this.releaseDBResource(conn, statement, rs);
		}
		return crs;
	}

	/**
	 * 释放资源
	 * 
	 * @param conn
	 *            Connection，数据库链接
	 * @param statement
	 *            PreparedStatement，statement执行器
	 * @param rs
	 *            ResultSet，记录集
	 */
	public void releaseDBResource(Connection conn, Statement statement,
			ResultSet rs) {

		// PooledDBA.getInstance().close(rs);
		// PooledDBA.getInstance().close(statement);
		// PooledDBA.getInstance().close(conn);

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				LOG.error(ex);
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ex) {
				LOG.error(ex);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ex) {
				throw new java.lang.RuntimeException("close connection error",
						ex);

			}
		}
	}

	/**
	 * 设置PreparedStatement的sql参数
	 * 
	 * @param statement
	 *            PreparedStatement
	 * @param paras
	 *            Object[]
	 * @throws SQLException
	 */
	private void setParas(PreparedStatement statement, Object[] paras)
			throws SQLException {

		if (paras != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("sql paras is:[");
			for (int i = 0; i < paras.length; i++) {
				if (i > 0) {
					sb.append(",");
				}
				if (paras[i] instanceof byte[]) // 二进制BLOB数据处理有点特殊，单独处理
				{
					sb.append(paras[i]);
					byte[] data = (byte[]) paras[i];
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					statement.setBinaryStream(i + 1, bais, data.length);
				} else if (paras[i] instanceof ParameterClob) {
					sb.append(paras[i]);
					ParameterClob parameterClob = (ParameterClob) paras[i];
					Reader reader = new StringReader(parameterClob.getText());
					statement.setCharacterStream(i + 1, reader, parameterClob
							.getLength());
				} else {
					sb.append(paras[i]);
					if (paras[i] == null) {
						statement.setString(i + 1, null);
					} else {
						statement.setObject(i + 1, paras[i]);
					}
				}
			}
			sb.append("]");
			if (LOG.isDebugEnabled()) {
				LOG.debug(sb.toString());
			}
		}
	}

	/**
	 * 获取链接
	 * 
	 * @return Connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws Exception {

		/*
		 * huangqinfang String driver = DBParas.getInstance().getJdbcDriver(); //
		 * 1.加载驱动程序。 Class.forName(driver); // 2.建立连接 String url =
		 * DBParas.getInstance().getJdbcUrl(); String username =
		 * DBParas.getInstance().getJdbcUsername(); String password =
		 * DBParas.getInstance().getJdbcPassword(); Connection conn =
		 * DriverManager.getConnection(url, username, password); // PooledDBA
		 * dba = new PooledDBA(); // Connection conn =
		 * PooledDBA.getInstance().getConnection();
		 * 
		 */
		Connection conn = ConnectionManager.getInstance().getConnection();
		return conn;
	}

}