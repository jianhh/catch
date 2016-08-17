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
 * �������ݿ������
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
	 * ��־
	 */
	private static final JLogger LOG = LoggerFactory.getLogger(DB.class);

	/**
	 * singletonģʽ
	 */
	private static DB instance = new DB();

	/**
	 * ���ﳡ���µ�����
	 */
	Connection transactionConn = null;

	/**
	 * ˽�й�����
	 */
	DB() {

	}

	/**
	 * ��ȡһ�������񳡾���ʵ��
	 * 
	 * @return TransactionDB
	 */
	public static DB getInstance() {

		return instance;
	}

	public void init(String persistenceFileName, String sqlcodeFileName)
			throws DAOException {

		try {
			DBParas.getInstance().init(persistenceFileName, sqlcodeFileName);// ��ʼ�����ݿ����Ӳ���
			// ConnectionPoolManager.getInstance().init();// ��ʼ�����е����ӳ�
		} catch (DAOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new DAOException("TransactionContext init failed!", ex);
		}
	}

	/**
	 * ִ�ж���sql���
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
			conn.setAutoCommit(false);// ȡ���Զ��ύ
			for (DbSchema db : dbList) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("execute sql is:" + db.getSql());
				}
				statement = conn.prepareStatement(db.getSql());
				this.setParas(statement, db.getParas());
				int result = statement.executeUpdate();
				if (LOG.isDebugEnabled()) {
					LOG.debug("���ݿ�����Y��: " + result);
				}
			}
			flag = true;
			conn.commit();// �ύ
		} catch (Exception e) {
			conn.rollback();// �ع�
			throw new DAOException("execute failed.", e);
		} finally {
			// �ͷ���Դ
			this.releaseDBResource(conn, statement, null);
		}
		return flag;
	}

	/**
	 * ִ��һ��sql���
	 * 
	 * @param sql
	 *            String,sql���
	 * @param paras
	 *            Object[],sql�����Ҫ�Ĳ��������û�о�Ϊnull
	 * @return int,�������ݿ��¼�����¡�
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
			// �ͷ���Դ
			this.releaseDBResource(conn, statement, null);
		}
		return result;
	}

	/**
	 * ִ��һ��sql���
	 * 
	 * @param sql
	 *            String,sql���
	 * @param paras
	 *            Object[],sql�����Ҫ�Ĳ��������û�о�Ϊnull
	 * @return ResultSet,��ѯ���Ľ������
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
			// �ͷ���Դ
			this.releaseDBResource(conn, statement, rs);
		}
		return crs;
	}

	/**
	 * �ͷ���Դ
	 * 
	 * @param conn
	 *            Connection�����ݿ�����
	 * @param statement
	 *            PreparedStatement��statementִ����
	 * @param rs
	 *            ResultSet����¼��
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
	 * ����PreparedStatement��sql����
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
				if (paras[i] instanceof byte[]) // ������BLOB���ݴ����е����⣬��������
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
	 * ��ȡ����
	 * 
	 * @return Connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws Exception {

		/*
		 * huangqinfang String driver = DBParas.getInstance().getJdbcDriver(); //
		 * 1.������������ Class.forName(driver); // 2.�������� String url =
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