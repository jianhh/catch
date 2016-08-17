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
	 * ���ݿ��ѯָ���������
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
		// ��ȡ��ǰ������
		try {
			dcTableMap = dao.dataLoad(tableName, tPks, lCols);
		} catch (DAOException e) {
			logger.error(e);
			dataCacheManagerLog.info("��ѯ�������ݱ�: " + tableName + " ʧ��", e);
		}
		return dcTableMap;
	}
}
