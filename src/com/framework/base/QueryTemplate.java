package com.framework.base;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.cache.vo.CachedVOList;
import com.common.db.DB;
import com.common.db.DbSchema;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.BaseCache;
import com.framework.util.DateUtil;
import com.framework.util.StringUtils;

/**
 * ���ݲ�ѯ
 * 
 * @author tangbiao
 * 
 */
public abstract class QueryTemplate {

	private static Map<Class, PropertyDescriptor[]> propertyDescriptorCache;

	protected static JLogger logger = LoggerFactory
			.getLogger(QueryTemplate.class);

	/**
	 * ���h�Ĳ���
	 * 
	 * @param <T>
	 * @param sql
	 *            ����sql
	 * @param parameters
	 *            ������Ϣ
	 * @throws Exception
	 */
	public boolean executeBySql(String sql, Object[] parameters)
			throws Exception {

		DB db = DB.getInstance();
		int i = db.execute(sql, parameters);

		logger.debug("���ݿ�����Y��: " + i);
		return i > 0 ? true : false;
	}

	/**
	 * ���h�Ĳ���+
	 * 
	 * @param dbList
	 * @throws Exception
	 */
	public boolean executeBySqlList(List<DbSchema> dbList) throws Exception {

		DB db = DB.getInstance();
		return db.executeList(dbList);
	}

	/**
	 * ����ID��ȡ���ݶ���
	 * 
	 * @param <T>
	 * @param tableName
	 *            ������
	 * @param idName
	 *            ��������
	 * @param dataVersionId
	 *            ����id
	 * @param idValue
	 *            ����ֵ
	 * @param cla
	 *            ���ݶ�������
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T queryByIdWithCache(String tableName,
			String idName, String dataVersionId, String idValue, Class<T> cla)
			throws Exception {

		String sql = getQueryByIdSql(tableName, idName, idValue, cla);
		if (StringUtils.isEmpty(dataVersionId)) {// ����idΪ�վ�ֱ�Ӳ����ݿ�
			return this.queryObjectBySql(sql, cla, new String[] { idValue });
		} else {
			return this.queryObjectBySqlWithCache(sql,
					new String[] { idValue }, dataVersionId, null, cla);
		}
	}

	/**
	 * ����sql��ѯָ�����ݶ���,�����л���
	 * 
	 * @param sql
	 *            ��ѯSQL
	 * @param parameters
	 *            ��ѯ����
	 * @param dataVersionId
	 *            ���ݰ汾��Id
	 * @param userKey
	 *            �����û��Զ����ֵ (�����ֵ����ʱdataVersionIdʧЧ����ɴ���)
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T queryObjectBySqlWithCache(final String sql,
			final String[] parameters, final String dataVersionId,
			final String userKey, final Class<T> clas) throws Exception {

		return (T) (new BaseCache() {

			@Override
			public void select() throws Exception {

				// �����ݿ��в�ѯ����
				this.cacheObj = (Serializable) queryObjectBySql(sql, clas,
						parameters);
			}

			public Object query() throws Exception {

				this.setKey(getKey(userKey, sql, dataVersionId, parameters));
				this.setExpired(DateUtil.getCacheDate(), 1);
				this.loadData();
				return this.cacheObj;
			}

		}.query());
	}

	/**
	 * ����sql��ѯ������Ϣ
	 * 
	 * @param <T>
	 * @param sql
	 *            ��ѯѯ��
	 * @param obj
	 *            �������ݶ���
	 * @throws Exception
	 */
	public <T extends Object> T queryObjectBySql(String sql, Class<T> cla,
			Object[] parameters) throws Exception {

		PropertyDescriptor[] props = getPropertyDescriptors(cla);
		DB db = DB.getInstance();
		ResultSet rs = db.query(sql.toString(), parameters);
		ResultSetMetaData metaData = rs.getMetaData();
		if (rs.next()) {
			T t = cla.newInstance();
			getDataFromResult(rs, t, getWriterMethodByColumnName(metaData,
					props), metaData.getColumnCount());
			if (logger.isDebugEnabled())
				logger.debug("������Ϊ: 1");
			return t;
		}
		return null;
	}

	/**
	 * ����sql��ѯ����list����,�����л���
	 * 
	 * @param sql
	 *            ��ѯSQL
	 * @param parameters
	 *            ��ѯ����
	 * @param dataVersionId
	 *            ���ݰ汾��ID
	 * @param clas
	 *            ���ݶ�������
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> queryListBySqlWithCache(final String sql,
			final String[] parameters, final String dataVersionId,
			final Class<T> clas) throws Exception {

		if (StringUtils.isEmpty(dataVersionId)) {// ����idΪ�վ�ֱ�Ӳ����ݿ�
			return this.queryListBySql(sql, clas, parameters);
		} else {
			return this.queryListBySqlWithCache(sql, parameters, dataVersionId,
					null, clas);
		}
	}

	/**
	 * ����sql��ȡ�ַ����ݣ������л���
	 * 
	 * @param <T>
	 * @param sql
	 *            ����sql
	 * @param dataVersionId
	 *            ���ݰ汾��
	 * @param parameters
	 *            ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public String queryStrBySqlWithCache(final String sql,
			final String dataVersionId, final String[] parameters)
			throws Exception {

		return this
				.queryStrBySqlWithCache(sql, null, dataVersionId, parameters);
	}

	/**
	 * ����sql��ȡ�ַ����ݣ������л���
	 * 
	 * @param <T>
	 * @param sql
	 *            ����sql
	 * @param userKey
	 *            �û��Զ������� (�����ֵ����ʱdataVersionIdʧЧ����ɴ���)
	 * @param dataVersion
	 *            ���ݰ汾��
	 * @param parameters
	 *            ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public String queryStrBySqlWithCache(final String sql,
			final String userKey, final String dateVersionId,
			final String[] parameters) throws Exception {

		return (new BaseCache() {

			@Override
			public void select() throws Exception {

				String str = queryStrBySql(sql, parameters);
				BaseType baseType = new BaseType();
				baseType.setS(str);
				// �����ݿ��в�ѯ����
				this.cacheObj = baseType;
			}

			public String query() throws Exception {

				// this.buildKeyBySaveKey(_key, dataVersion);
				this.setKey(getKey(userKey, sql, dateVersionId, parameters));
				this.setExpired(DateUtil.getCacheDate(), 1);
				this.loadData();
				return ((BaseType) this.cacheObj).getS();
			}

		}.query());
	}

	/**
	 * ��ҳ��ȡ���ݶ���
	 * 
	 * @param <T>
	 * @param sql
	 *            ��ѯSQL
	 * @param currentPage
	 *            ��ǰҳ
	 * @param pageSize
	 *            ��ҳ��С
	 * @param dataVersionId
	 *            ���ݰ汾��Id
	 * @param parameters
	 *            ��ѯ����
	 * @param clas
	 *            ���ݶ�������
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> pageQueryWithCache(final String sql,
			final int currentPage, final int pageSize,
			final String dataVersionId, final String[] parameters,
			final Class<T> clas) throws Exception {

		return pageQueryWithCache(sql, currentPage, pageSize, parameters,
				dataVersionId, null, clas);

	}

	/**
	 * ��ҳ��ѯ������Ϣ,���������ݻ���
	 * 
	 * @param sql
	 *            ��ѯsql
	 * @param currentPage
	 *            ��ǰҳ��
	 * @param pageSize
	 *            ÿҳ���ټ�¼��
	 * @param parameters
	 *            ������Ϣ
	 * @param showtype
	 *            keyֵ�ӷ���
	 * @param sort
	 *            keyֵ������
	 * @param userKey
	 *            �����û��Զ����ֵ (�����ֵ����ʱdataVersionIdʧЧ����ɴ���)
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> pageQueryWithCache(final String sql,
			final int currentPage, final int pageSize,
			final String[] parameters, final String dataVersionId,
			final String userKey, final Class<T> clas) throws Exception {

		return (List<T>) (new BaseCache() {

			@Override
			public void select() throws Exception {

				CachedVOList voList = new CachedVOList();
				voList.setVoList((ArrayList<Serializable>) queryListBySql(sql,
						clas, parameters));
				// �����ݿ��в�ѯ����
				this.cacheObj = voList;
			}

			public List<T> query() throws Exception {

				String key = getPageQueryKey(userKey, sql, dataVersionId,
						parameters, currentPage, pageSize);
				this.setKey(key);
				this.setExpired(DateUtil.getCacheDate(), 1);
				this.loadData();
				return ((CachedVOList) this.cacheObj).getVoList();
			}

		}.query());

	}

	/**
	 * ����sql��ѯ����list����,�����л���
	 * 
	 * @param sql
	 *            ��ѯSQL
	 * @param parameters
	 *            ��ѯ����
	 * @param dataVersionId
	 *            ���ݰ汾��ID
	 * @param userKey
	 *            �����û��Զ����ֵ (�����ֵ����ʱdataVersionIdʧЧ����ɴ���)
	 * @param clas
	 *            ���ݶ�������
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> queryListBySqlWithCache(final String sql,
			final String[] parameters, final String dataVersionId,
			final String userKey, final Class<T> clas) throws Exception {

		return (List<T>) (new BaseCache() {

			@Override
			public void select() throws Exception {

				CachedVOList voList = new CachedVOList();
				voList.setVoList((ArrayList<Serializable>) queryListBySql(sql,
						clas, parameters));
				// �����ݿ��в�ѯ����
				this.cacheObj = voList;
			}

			public List<T> query() throws Exception {

				// this.buildKeyBySaveKey(_key, sort.toString() + showtype);
				this.setKey(getKey(userKey, sql, dataVersionId, parameters));
				this.setExpired(DateUtil.getCacheDate(), 1);
				this.loadData();
				return ((CachedVOList) this.cacheObj).getVoList();
			}

		}.query());
	}

	/**
	 * ����sql������ѯ������Ϣ
	 * 
	 * @param <T>
	 * @param sql
	 *            ��ѯ���
	 * @param paras
	 *            ��ѯ����
	 * @param cla
	 *            JAVABEAN �����
	 * @return
	 */
	public <T extends Object> List<T> queryListBySql(String sql, Class<T> cla,
			Object[] paras) throws Exception {

		PropertyDescriptor[] props = getPropertyDescriptors(cla);
		if (props == null || props.length == 0)
			throw new Exception("�����Ϲ鷶��BEAN");
		DB db = DB.getInstance();
		ResultSet rs = db.query(sql, paras);
		List<T> objList = new ArrayList();
		ResultSetMetaData metaData = rs.getMetaData();
		Map<Integer, PropertyDescriptor> propersMap = getWriterMethodByColumnName(
				metaData, props);
		while (rs.next()) {
			T obj = cla.newInstance();
			for (int i = 0; i < props.length; i++) {
				getDataFromResult(rs, obj, propersMap, metaData
						.getColumnCount());
			}
			objList.add(obj);
		}
		if (logger.isDebugEnabled())
			logger.debug("������Ϊ: " + objList.size());
		return objList;
	}

	/**
	 * ����sql������ѯ������Ϣ
	 * 
	 * @param sql
	 *            ��ѯ���
	 * @param cla
	 *            JAVABEAN �����
	 * @return
	 */
	public <T extends Object> List<T> queryListBySql(String sql, Class<T> cla)
			throws Exception {

		return queryListBySql(sql, cla, null);
	}

	/**
	 * ��ѯ����������Ϣ
	 * 
	 * @param sql
	 *            ���ݲ�ѯSQL
	 * @param parameters
	 *            ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public String queryStrBySql(String sql, Object[] parameters)
			throws Exception {

		DB db = DB.getInstance();
		ResultSet rs = db.query(sql.toString(), parameters);
		String str = "";
		if (rs.next()) {
			str = rs.getString(1);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("��ѯ�����ַ�Ϊ: " + str);
		}
		return str;
	}

	/**
	 * ����ID��ȡ��ѯsql
	 * 
	 * @param <T>
	 * @param tableName
	 *            ������
	 * @param idName
	 *            id����
	 * @param idValue
	 *            idֵ
	 * @param cla
	 *            ���ݶ�������
	 * @return
	 * @throws Exception
	 */
	private <T extends Object> String getQueryByIdSql(String tableName,
			String idName, String idValue, Class<T> cla) throws Exception {

		StringBuffer sqlBuffer = getSqlPrefix(tableName, cla).append(
				" where 1=1 ");
		sqlBuffer.append(" and ").append(idName).append(" = ").append("?");
		return sqlBuffer.toString();
	}

	/**
	 * �������ݿ�schame����sql�в�ѯ���
	 * 
	 * @param <T>
	 * @param tableName
	 *            ������
	 * @param cla
	 *            schame����
	 * @return
	 * @throws Exception
	 */
	protected <T extends Object> StringBuffer getSqlPrefix(String tableName,
			Class<T> cla) throws Exception {

		PropertyDescriptor[] props = getPropertyDescriptors(cla);
		return getSqlPrefixByPro(tableName, props);
	}

	/**
	 * ����VO������Ϣ����sql��ѯ���������ַ���
	 * 
	 * @param tableName
	 *            ����
	 * @param props
	 *            JAVABEAN��������������Ϣ
	 * @return
	 */
	private StringBuffer getSqlPrefixByPro(String tableName,
			PropertyDescriptor[] props) {

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select ").append(getColumnNameStr(props));
		sqlBuffer.append(" from ").append(tableName);
		sqlBuffer.append(" t ");
		return sqlBuffer;
	}

	/**
	 * ��ȡ�������ַ���
	 * 
	 * @param props
	 * @return
	 */
	private String getColumnNameStr(PropertyDescriptor[] props) {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < props.length; i++) {
			PropertyDescriptor proper = props[i];
			buffer.append("t.");
			buffer.append(proper.getName()).append(" ");
			if (i != props.length - 1)
				buffer.append(",");
		}
		return buffer.toString();
	}

	/**
	 * �����ݿ�����ת����JAVABEAN
	 * 
	 * @param rs
	 *            ���ݿ�����
	 * @param obj
	 *            javaBEAN����
	 * @param props
	 *            javaBean ������Ϣ
	 * @throws Exception
	 */
	private void getDataFromResult(ResultSet rs, Object obj,
			Map<Integer, PropertyDescriptor> properMap, int columnCount)
			throws Exception {

		if (properMap == null || properMap.size() == 0)
			return;

		for (int i = 1; i <= columnCount; i++) {
			writerDataToBean(properMap.get(i), obj, rs.getString(i));
		}

	}

	/**
	 * Ϊָ������д������ֵ��Ϣ
	 * 
	 * @param proper
	 *            ����������Ϣ
	 * @param obj
	 *            JAVABEAN����
	 * @param propertyValue
	 *            ����ֵ
	 */
	private void writerDataToBean(PropertyDescriptor proper, Object obj,
			Object propertyValue) throws Exception {

		if (proper == null)
			return;
		Method me = proper.getWriteMethod();
		me.invoke(obj, propertyValue);
	}

	private synchronized PropertyDescriptor[] getPropertyDescriptors(Class cla)
			throws Exception {

		try {
			if (propertyDescriptorCache == null) {
				propertyDescriptorCache = new HashMap<Class, PropertyDescriptor[]>();
			}

			PropertyDescriptor[] props = propertyDescriptorCache.get(cla);

			if (props == null) {
				props = Introspector.getBeanInfo(cla, Object.class)
						.getPropertyDescriptors();
				propertyDescriptorCache.put(cla, props);
			}

			return props;
		} catch (IntrospectionException e) {
			throw e;
		}

	}

	/**
	 * ��ȡ��ѯ��Ӧ���е�BEAN�����WRITER����
	 * 
	 * @param metaData
	 *            ���ݿ���Ϣ����
	 * @param props
	 *            bean������Ϣ
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, PropertyDescriptor> getWriterMethodByColumnName(
			ResultSetMetaData metaData, PropertyDescriptor[] props)
			throws Exception {

		// ��ȡ��������
		int columnCount = metaData.getColumnCount();
		Map<Integer, PropertyDescriptor> properMap = new HashMap();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			for (int k = 0; k < props.length; k++) {
				PropertyDescriptor proper = props[k];
				if (proper.getName().equalsIgnoreCase(columnName)) {
					properMap.put(i, proper);
					break;
				}
			}
		}
		return properMap;
	}
}
