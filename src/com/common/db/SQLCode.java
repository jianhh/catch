package com.common.db;

/**
 * <p>Title: aspire xPortal project</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: www.aspire-tech.com</p>
 * @author achang
 * @version 1.0
 *
 * The SQLCode class is a helper class that loads all of the SQL
 * code used within the persistence framework into a private properties
 * object stored within the SQLCode class.
 */

import java.io.FileInputStream;
import java.util.Properties;

public class SQLCode {

	private static SQLCode sqlCode = null;

	private static Properties sqlCache = new Properties();

	static {
		sqlCode = new SQLCode();
	}

	private SQLCode() {

	}

	public static SQLCode getInstance() {

		return sqlCode;
	}

	public void init() throws Exception {

		String sqlFileName = DBParas.getInstance().getSqlcodeFileName();
		sqlCache.load(new FileInputStream(sqlFileName));
	}
	
	/*
	 * huangqinfang
	 */
	public void init(String sqlcodeFileName) throws Exception {
		sqlCache.load(new FileInputStream(sqlcodeFileName));
	}
	
	public String getSQLStatement(String pSQLKeyName) throws DAOException {
		if (sqlCache.containsKey(pSQLKeyName)) {
			return (String) sqlCache.get(pSQLKeyName);
		} else {
			throw new DAOException("SQL not found for:" + pSQLKeyName);
		}

	}

}
