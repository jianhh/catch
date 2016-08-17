package com.work.businesses.dao;

import com.common.db.SQLCode;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.log.LogUtil;
import com.work.businesses.schema.BusinessesInfoSchema;
import com.work.businesses.table.Table;

/**
 * 商家信息dao
 * 
 * @author tangbiao
 * 
 */
public class BusinessesInfoDAO extends QueryTemplate {

	static JLogger logger = LoggerFactory.getLogger(BusinessesInfoDAO.class);

	private static BusinessesInfoDAO instance = new BusinessesInfoDAO();

	public static BusinessesInfoDAO getInstance() {

		return instance;
	}

	public enum mysqltype {
		addBusinessesInfo
	}

	/**
	 * 增加商家信息
	 * 
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	public boolean addBusinessesInfo(BusinessesInfoSchema schema)
			throws Exception {

		BusinessesInfoSchema info = getTestBusinessesInfoByUid(schema.getUid());
		if (info != null) {
			LogUtil.writeBusinessesLog(schema.getUid() + " 域名存在 ");
			return false;
		}
		String sql = this.getSQLById(mysqltype.addBusinessesInfo);
		Object[] paras = { schema.getUid(), schema.getCompany_name(),
				schema.getName(), schema.getPhone(), schema.getNumber(),
				schema.getAddress(), schema.getShop_address(),
				schema.getBusiness_number(), schema.getZipcode(),
				schema.getCompanyUrl(), schema.getKeyword(),
				schema.getCreate_time(), schema.getGetUrl() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 根据uid查询
	 * 
	 * @param uid
	 *            域名
	 * @return
	 * @throws Exception
	 */
	public BusinessesInfoSchema getTestBusinessesInfoByUid(String uid)
			throws Exception {

		return this.queryByIdWithCache(Table.T_BUSINESSES_INFO, "uid", null,
				uid, BusinessesInfoSchema.class);
	}

	// 组sql
	private String getSQLById(mysqltype type) throws Exception {

		return SQLCode.getInstance()
				.getSQLStatement(
						"com.businesses.jsoup.dao.BusinessesInfoDAO."
								+ type.toString());
	}

}
