package com.common.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public final class ConnectionManager {
	
	private static JLogger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	private static ConnectionManager instance;

	public ComboPooledDataSource ds;
	
	//public final static String FS = System.getProperty("file.separator");

	private static String c3p0Properties;
	
	public void init(String persistenceFile) throws Exception {
		c3p0Properties=persistenceFile;
		logger.debug("persistenceFile配置文件地址:"+c3p0Properties);
		
		Properties p = new Properties();
		
		File propertiesFile=new File(c3p0Properties);
		InputStream in=new FileInputStream(propertiesFile);
		
		p.load(in);

		ds = new ComboPooledDataSource("metkb");
		ds.setUser(p.getProperty("jdbc.mysql.username"));
		ds.setPassword(p.getProperty("jdbc.mysql.password"));
		ds.setJdbcUrl(p.getProperty("jdbc.mysql.url"));
		ds.setDriverClass(p.getProperty("jdbc.mysql.driver"));
	}
	
	private ConnectionManager(){}

	public static final ConnectionManager getInstance() {
		if (instance == null) {
			try {
				instance = new ConnectionManager();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return instance;
	}

	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		DataSources.destroy(ds); // 关闭datasource
		super.finalize();
	}
	
}