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
	 * ��ѯָ���������,�����ݷ���map�У���map��keyΪtablepk��valueΪ�洢��ǰ���������ݵ�map
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
					logger.error("���ݱ� " + tableName + " ��װmapʧ��", e);
					dataCacheManagerLog.info("���ݱ� " + tableName + " row "
							+ tPkStr + " ��װmapʧ��", e);
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
