package com.work.businesses.bo;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.work.businesses.dao.BusinessesInfoDAO;
import com.work.businesses.schema.BusinessesInfoSchema;

/**
 * �̼���Ϣbo
 * 
 * @author tangbiao
 * 
 */
public class BusinessesInfoBO {

	static JLogger logger = LoggerFactory.getLogger(BusinessesInfoBO.class);

	private BusinessesInfoDAO businessesInfoDAO;

	public BusinessesInfoBO() {

		this.businessesInfoDAO = BusinessesInfoDAO.getInstance();
	}

	/**
	 * �����̼���Ϣ
	 * 
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	public boolean addBusinessesInfo(BusinessesInfoSchema schema)
			throws Exception {
		return this.businessesInfoDAO.addBusinessesInfo(schema);
	}
}
