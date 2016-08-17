package com.framework.dc.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.db.DAOException;
import com.common.db.DB;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.dc.vo.ConfigVO;

public class DataLoadDAO extends QueryTemplate {

	static JLogger logger = LoggerFactory.getLogger(DataLoadDAO.class);

	static JLogger dataCacheManagerLog = LoggerFactory
			.getLogger("biz.datacache.manage");

	public DataLoadDAO() {

	}

	/**
	 * 查询指定表的数据,将数据放入map中，此map中key为tablepk，value为存储当前行所有数据的map
	 * 
	 * @param tableName
	 * @param String[]
	 *            tPks
	 * @param String[]
	 *            lCols
	 */
	public Map<String, Map> dataLoad(String tableName, String[] tPks,
			String[] lCols) throws DAOException {
		String sql = "select * from " + tableName;
		Map<String, Map> dcTableMap = new HashMap<String, Map>();
		try {
			ResultSet rs = DB.getInstance().query(sql, null);
			String[] name = null;
			if (null != lCols) {
				name = lCols;
			} else {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				name = new String[count];
				for (int i = 0; i < count; i++) {
					name[i] = rsmd.getColumnName(i + 1);
				}
			}
			while (rs.next()) {
				String tPkStr = "";
				try {
					Map<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < name.length; i++) {
						map.put(name[i], rs.getString(name[i]));
					}
					for (int i = 0; i < tPks.length; i++) {
						if (i == (tPks.length - 1)) {
							tPkStr = tPkStr + rs.getString(tPks[i]);
						} else {
							tPkStr = tPkStr + rs.getString(tPks[i]) + "$";
						}
					}
					dcTableMap.put(tPkStr, map);
				} catch (Exception e) {
					logger.error("数据表 " + tableName + " 组装map失败", e);
					dataCacheManagerLog.info("数据表 " + tableName + " row "
							+ tPkStr + " 组装map失败", e);
				}
			}
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
		return dcTableMap;
	}

	public List<ConfigVO> getConfigList(String siteName, String typeName)
			throws Exception {

		String sql = "select * from t_config where sitename=? and typename=?";
		String[] paras = new String[] { siteName, typeName };
		return this.queryListBySqlWithCache(sql, paras, null, ConfigVO.class);
	}
}
