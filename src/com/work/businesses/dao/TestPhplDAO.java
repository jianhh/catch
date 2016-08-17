package com.work.businesses.dao;

import java.util.List;

import com.common.db.DbSchema;
import com.common.db.SQLCode;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.page.Page;
import com.work.businesses.schema.PhpSchema;

/**
 * PHP����dao
 * 
 * @author tangbiao
 * 
 */
public class TestPhplDAO extends QueryTemplate {

	static JLogger logger = LoggerFactory.getLogger(TestPhplDAO.class);

	private static TestPhplDAO instance = new TestPhplDAO();

	public static TestPhplDAO getInstance() {

		return instance;
	}

	public enum mysqltype {
		byName, list, add, del, update
	}

	/**
	 * ����id��ѯ
	 * 
	 * @param id
	 * @throws Exception
	 */
	public PhpSchema getTestMysqlById(String id) throws Exception {

		return this
				.queryByIdWithCache("t_php", "id", null, id, PhpSchema.class);
	}

	/**
	 * �������Ʋ�ѯ����
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<PhpSchema> getTestMysqlByName(String name) throws Exception {

		String sql = this.getSQLById(mysqltype.byName);
		String[] paras = new String[] { name };
		return this.queryListBySql(sql, PhpSchema.class, paras);

	}

	/**
	 * �������Ʋ�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void getVideoSort(Page page, String name) throws Exception {

		String sql = this.getSQLById(mysqltype.byName);
		String[] paras = new String[] { name };

		page.excuteWithCache(sql, paras, PhpSchema.class, null);

	}

	/**
	 * ��ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PhpSchema> getTestMysql() throws Exception {

		String sql = this.getSQLById(mysqltype.list);
		return this.queryListBySql(sql, PhpSchema.class);

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

		String sql = this.getSQLById(mysqltype.add);
		Object[] paras = { name, age, sex };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ��
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delTestMysqlById(String id) throws Exception {

		String sql = this.getSQLById(mysqltype.del);
		Object[] paras = { id };
		return this.executeBySql(sql, paras);
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

		String sql = this.getSQLById(mysqltype.update);
		Object[] paras = { name, id };
		return this.executeBySql(sql, paras);
	}

	public DbSchema addDB(String name, String age, String sex) throws Exception {
		DbSchema db = new DbSchema();
		String sql = this.getSQLById(mysqltype.add);
		Object[] paras = { name, age, sex };
		db.setSql(sql);
		db.setParas(paras);
		return db;
	}

	// ��sql
	private String getSQLById(mysqltype type) throws Exception {

		return SQLCode.getInstance().getSQLStatement(
				"com.businesses.jsoup.dao.TestPhplDAO." + type.toString());
	}

}
