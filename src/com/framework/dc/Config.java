package com.framework.dc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.config.ServerInfo;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.dc.constants.Constant;
import com.framework.dc.dao.DataLoadDAO;
import com.framework.dc.vo.ConfigVO;
import com.framework.dc.vo.TableVO;

public class Config {

	static JLogger logger = LoggerFactory.getLogger(Config.class);

	static JLogger dataCacheManagerLog = LoggerFactory
			.getLogger("biz.datacache.manage");

	private List<TableVO> tablesList = null;

	private Map<String, String> tablesMap = null;

	public Config() {

		tablesMap = new HashMap<String, String>();
		tablesList = new ArrayList<TableVO>();
		// ��ȡ����tables
		try {
	        DataLoadDAO dao = new DataLoadDAO();
	        List<ConfigVO> configList = dao.getConfigList(ServerInfo.siteName, "configitem");
	        for(ConfigVO vo : configList){
	            String[] str = vo.getConfigValue().split(";");
                TableVO tableVO = new TableVO();
                tableVO.setTableName(vo.getConfigName());
                tableVO.setTablePk(str[0]);
                tableVO.setRefreshTime(str[1]);
                tableVO.setLoadColumn(str[2]);
                tablesList.add(tableVO);
	        }
			for (int i = 0; i < tablesList.size(); i++) {
				TableVO vo = (TableVO) tablesList.get(i);
				tablesMap.put(vo.getTableName(), vo.getRefreshTime());
			}
		} catch (Exception e) {
			logger.error("cacheTableConfig.xml  read fail", e);
			dataCacheManagerLog.info("cacheTableConfig.xml  read fail", e);
		}
	}

	/**
	 * ��ȡ��Ҫ���ص�dc�ı�
	 * 
	 * @return
	 */
	public List<TableVO> getTableListConfig() {

		return this.tablesList;
	}

	/**
	 * ��ȡ��Ҫ���ص�dc����Ӧ����ʱ����
	 * 
	 * @return
	 */
	public Map<String, String> getTableMapConfig() {

		return this.tablesMap;
	}

	/**
	 * ��ȡָ������Զ�ˢ��ʱ��
	 * 
	 * @return
	 */
	public Long getRefreshTime(String tableName) {

		Long refreshTime = Constant.REFRESHTIME;
		try {
			if (tablesMap.get(tableName) != null) {
				refreshTime = Long.parseLong((String) tablesMap.get(tableName));
				if (refreshTime < 1L) {
					refreshTime = 1L;
				}
			}
		} catch (Exception e) {
			logger.debug(tableName + " ���Զ�ˢ��ʱ�䣬���ַ���ת��Ϊint��ʧ��");
			logger.error(e);
		}
		return refreshTime * 60 * 1000;
	}
}
