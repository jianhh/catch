package com.framework.dc.bo;

import java.util.HashMap;
import java.util.Map;

import com.common.db.DAOException;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.dc.dao.DataLoadDAO;

public class DataLoadBO {

	static JLogger logger = LoggerFactory.getLogger(DataLoadBO.class);

	static JLogger dataCacheManagerLog = LoggerFactory
			.getLogger("biz.datacache.manage");

	public DataLoadBO() {

	}

	/**
	 * 数据库查询指定表的数据
	 * 
	 * @param tableName
	 * @param tablePk
	 * @param loadColumn
	 */
	public Map<String, Map> dataLoad(String tableName, String tablePk,
			String loadColumn) {

		DataLoadDAO dao = new DataLoadDAO();
		Map<String, Map> dcTableMap = new HashMap<String, Map>();
		String[] tPks = tablePk.split("\\$");
		String[] lCols = null;
		if (!"".equals(loadColumn) && null != loadColumn) {
			lCols = loadColumn.split("\\$");
		}
		// 获取当前表数据
		try {
			dcTableMap = dao.dataLoad(tableName, tPks, lCols);
		} catch (DAOException e) {
			logger.error(e);
			dataCacheManagerLog.info("查询缓存数据表: " + tableName + " 失败", e);
		}
		return dcTableMap;
	}
}
