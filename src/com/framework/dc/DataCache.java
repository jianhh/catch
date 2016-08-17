package com.framework.dc;

import java.util.HashMap;
import java.util.Map;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * ���ݻ��棬�洢��
 * 
 * @author rongcunhao
 * 
 */
public class DataCache {

	static JLogger logger = LoggerFactory.getLogger(DataCache.class);

	static JLogger dataCacheManagerLog = LoggerFactory
			.getLogger("biz.datacache.manage");

	/**
	 * ���л������ݱ�
	 */
	private static Map<String, Map> dcMap = new HashMap<String, Map>();

	/**
	 * ��һ��������ݲ��뻺��
	 * 
	 * @param tableName
	 * @param dcMap
	 */
	public static void addDataToCache(String tableName, Map map) {

		dcMap.put(tableName, map);
	}

	/**
	 * ��ȡ������Ļ�������
	 * 
	 * @return
	 */
	public static Map<String, Map> getDataFromCache(String tableName) {

		return (HashMap) dcMap.get(tableName);
	}

	/**
	 * ��ȡָ�����е�һ������
	 * 
	 * @param tableName
	 * @param tablePk
	 * @return
	 */
	public static Map<String, String> getRowDataFromCache(String tableName,
			String tablePk) {
		if (dcMap.get(tableName) != null) {
			return (HashMap) ((HashMap) dcMap.get(tableName)).get(tablePk);
		}
		return new HashMap();
	}

	/**
	 * ��ȡָ�����е�һ��������ĳһ�е�ֵ
	 * 
	 * @param tableName
	 * @param tablePk
	 * @return
	 */
	public static String getColDataFromCache(String tableName, String tablePk,
			String colName) {
		logger.debug("getColDataFromCache in....");
		Map<String, String> rowMap = new HashMap<String, String>();
		String colValue = null;
		if (dcMap.get(tableName) != null) {
			rowMap = (HashMap) ((HashMap) dcMap.get(tableName)).get(tablePk);
		}
		if (rowMap != null) {
			colValue = (String) rowMap.get(colName);
		}
		logger.debug("Get Table " + tableName + " " + tablePk + " " + colName
				+ " colValue " + colValue);
		if (colValue == null || "".equals(colValue)) {
//			dataCacheManagerLog.info("Get Table " + tableName + " " + tablePk
//					+ " " + colName + " colValue is null");
		} else {
			// dataCacheManagerLog.info("Get Table " + tableName + " " + tablePk
			// + " " + colName + " colValue " + colValue);
		}
		return colValue;
	}

	/***************************************************************************
	 * ��ȡ������
	 * 
	 * @param tableName
	 * @return
	 */
	public static Object[] getRows(String tableName) {
		if (dcMap.get(tableName) != null) {

			return dcMap.get(tableName).keySet().toArray();
		}
		return null;
	}

}
