package com.work.businesses.bo;

import java.util.List;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.work.businesses.dao.TestPhplDAO;
import com.work.businesses.schema.PhpSchema;

/**
 * PHP����bo
 * 
 * @author tangbiao
 * 
 */
public class TestPhpBO {

	static JLogger logger = LoggerFactory.getLogger(TestPhpBO.class);

	private TestPhplDAO testPhplDAO;

	public TestPhpBO() {

		this.testPhplDAO = TestPhplDAO.getInstance();
	}

	/**
	 * ����id��ѯ
	 * 
	 * @param id
	 * @throws Exception
	 */
	public PhpSchema getTestMysqlById(String id) throws Exception {

		return this.testPhplDAO.getTestMysqlById(id);
	}

	/**
	 * �������Ʋ�ѯ����
	 * 
	 * @param name
	 *            ����
	 * @throws Exception
	 */
	public List<PhpSchema> getTestMysqlByName(String name) throws Exception {

		return this.testPhplDAO.getTestMysqlByName(name);
	}

	/**
	 * ��ѯ����
	 * 
	 * @throws Exception
	 */
	public List<PhpSchema> getTestMysql() throws Exception {

		return this.testPhplDAO.getTestMysql();
	}

	/**
	 * ����
	 * 
	 * @param name
	 * @param age
	 * @param sex
	 * @return
	 * @throws Exception
	 */
	public boolean addTestMysql(String name, String age, String sex)
			throws Exception {

		return this.testPhplDAO.addTestMysql(name, age, sex);
	}

	/**
	 * ɾ��
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delTestMysqlById(String id) throws Exception {

		return this.testPhplDAO.delTestMysqlById(id);
	}

	/**
	 * �޸�
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean updateTestMysql(String id, String name) throws Exception {

		return this.testPhplDAO.updateTestMysql(id, name);
	}
}
