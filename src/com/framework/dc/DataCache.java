package com.framework.dc;

import java.util.HashMap;
import java.util.Map;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * 数据缓存，存储类
 * 
 * @author rongcunhao
 * 
 */
public class DataCache {

	static JLogger logger = LoggerFactory.getLogger(DataCache.class);

	static JLogger dataCacheManagerLog = LoggerFactory
			.getLogger("biz.datacache.manage");

	/**
	 * 所有缓存数据表
	 */
	private static Map<String, Map> dcMap = new HashMap<String, Map>();

	/**
	 * 将一个表的数据插入缓存
	 * 
	 * @param tableName
	 * @param dcMap
	 */
	public static void addDataToCache(String tableName, Map map) {

		dcMap.put(tableName, map);
	}

	/**
	 * 获取整个表的缓存数据
	 * 
	 * @return
	 */
	public static Map<String, Map> getDataFromCache(String tableName) {

		return (HashMap) dcMap.get(tableName);
	}

	/**
	 * 获取指定表中的一行数据
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
	 * 获取指定表中的一行数据中某一列的值
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
	 * 获取所有列
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
