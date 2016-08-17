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
 * 数据查询
 * 
 * @author tangbiao
 * 
 */
public abstract class QueryTemplate {

	private static Map<Class, PropertyDescriptor[]> propertyDescriptorCache;

	protected static JLogger logger = LoggerFactory
			.getLogger(QueryTemplate.class);

	/**
	 * 增刪改操作
	 * 
	 * @param <T>
	 * @param sql
	 *            操作sql
	 * @param parameters
	 *            参数信息
	 * @throws Exception
	 */
	public boolean executeBySql(String sql, Object[] parameters)
			throws Exception {

		DB db = DB.getInstance();
		int i = db.execute(sql, parameters);

		logger.debug("数据库操作結果: " + i);
		return i > 0 ? true : false;
	}

	/**
	 * 增刪改操作+
	 * 
	 * @param dbList
	 * @throws Exception
	 */
	public boolean executeBySqlList(List<DbSchema> dbList) throws Exception {

		DB db = DB.getInstance();
		return db.executeList(dbList);
	}

	/**
	 * 根据ID获取数据对象
	 * 
	 * @param <T>
	 * @param tableName
	 *            表名称
	 * @param idName
	 *            主键名称
	 * @param dataVersionId
	 *            缓存id
	 * @param idValue
	 *            主键值
	 * @param cla
	 *            数据对象类型
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T queryByIdWithCache(String tableName,
			String idName, String dataVersionId, String idValue, Class<T> cla)
			throws Exception {

		String sql = getQueryByIdSql(tableName, idName, idValue, cla);
		if (StringUtils.isEmpty(dataVersionId)) {// 缓存id为空就直接查数据库
			return this.queryObjectBySql(sql, cla, new String[] { idValue });
		} else {
			return this.queryObjectBySqlWithCache(sql,
					new String[] { idValue }, dataVersionId, null, cla);
		}
	}

	/**
	 * 根据sql查询指定数据对象,并进行缓存
	 * 
	 * @param sql
	 *            查询SQL
	 * @param parameters
	 *            查询参数
	 * @param dataVersionId
	 *            数据版本号Id
	 * @param userKey
	 *            缓存用户自定义键值 (如果此值设置时dataVersionId失效，则可传空)
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T queryObjectBySqlWithCache(final String sql,
			final String[] parameters, final String dataVersionId,
			final String userKey, final Class<T> clas) throws Exception {

		return (T) (new BaseCache() {

			@Override
			public void select() throws Exception {

				// 从数据库中查询数据
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
	 * 根据sql查询对象信息
	 * 
	 * @param <T>
	 * @param sql
	 *            查询询句
	 * @param obj
	 *            返回数据对象
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
				logger.debug("总条数为: 1");
			return t;
		}
		return null;
	}

	/**
	 * 根据sql查询数据list集合,并进行缓存
	 * 
	 * @param sql
	 *            查询SQL
	 * @param parameters
	 *            查询参数
	 * @param dataVersionId
	 *            数据版本号ID
	 * @param clas
	 *            数据对象类型
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> queryListBySqlWithCache(final String sql,
			final String[] parameters, final String dataVersionId,
			final Class<T> clas) throws Exception {

		if (StringUtils.isEmpty(dataVersionId)) {// 缓存id为空就直接查数据库
			return this.queryListBySql(sql, clas, parameters);
		} else {
			return this.queryListBySqlWithCache(sql, parameters, dataVersionId,
					null, clas);
		}
	}

	/**
	 * 根据sql获取字符数据，并进行缓存
	 * 
	 * @param <T>
	 * @param sql
	 *            数据sql
	 * @param dataVersionId
	 *            数据版本号
	 * @param parameters
	 *            参数信息
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
	 * 根据sql获取字符数据，并进行缓存
	 * 
	 * @param <T>
	 * @param sql
	 *            数据sql
	 * @param userKey
	 *            用户自定义主键 (如果此值设置时dataVersionId失效，则可传空)
	 * @param dataVersion
	 *            数据版本号
	 * @param parameters
	 *            参数信息
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
				// 从数据库中查询数据
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
	 * 分页获取数据对象
	 * 
	 * @param <T>
	 * @param sql
	 *            查询SQL
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            分页大小
	 * @param dataVersionId
	 *            数据版本号Id
	 * @param parameters
	 *            查询参数
	 * @param clas
	 *            数据对象类型
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
	 * 分页查询数据信息,并进行数据缓存
	 * 
	 * @param sql
	 *            查询sql
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页多少记录数
	 * @param parameters
	 *            参数信息
	 * @param showtype
	 *            key值子分类
	 * @param sort
	 *            key值主分类
	 * @param userKey
	 *            缓存用户自定义键值 (如果此值设置时dataVersionId失效，则可传空)
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
				// 从数据库中查询数据
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
	 * 根据sql查询数据list集合,并进行缓存
	 * 
	 * @param sql
	 *            查询SQL
	 * @param parameters
	 *            查询参数
	 * @param dataVersionId
	 *            数据版本号ID
	 * @param userKey
	 *            缓存用户自定义键值 (如果此值设置时dataVersionId失效，则可传空)
	 * @param clas
	 *            数据对象类型
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
				// 从数据库中查询数据
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
	 * 根据sql条件查询数据信息
	 * 
	 * @param <T>
	 * @param sql
	 *            查询语句
	 * @param paras
	 *            查询参数
	 * @param cla
	 *            JAVABEAN 类对象
	 * @return
	 */
	public <T extends Object> List<T> queryListBySql(String sql, Class<T> cla,
			Object[] paras) throws Exception {

		PropertyDescriptor[] props = getPropertyDescriptors(cla);
		if (props == null || props.length == 0)
			throw new Exception("不符合归范的BEAN");
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
			logger.debug("总条数为: " + objList.size());
		return objList;
	}

	/**
	 * 根据sql条件查询数据信息
	 * 
	 * @param sql
	 *            查询语句
	 * @param cla
	 *            JAVABEAN 类对象
	 * @return
	 */
	public <T extends Object> List<T> queryListBySql(String sql, Class<T> cla)
			throws Exception {

		return queryListBySql(sql, cla, null);
	}

	/**
	 * 查询单个数据信息
	 * 
	 * @param sql
	 *            数据查询SQL
	 * @param parameters
	 *            参数信息
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
			logger.debug("查询到的字符为: " + str);
		}
		return str;
	}

	/**
	 * 根据ID获取查询sql
	 * 
	 * @param <T>
	 * @param tableName
	 *            表名称
	 * @param idName
	 *            id名称
	 * @param idValue
	 *            id值
	 * @param cla
	 *            数据对象类型
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
	 * 根据数据库schame生成sql列查询语句
	 * 
	 * @param <T>
	 * @param tableName
	 *            表名称
	 * @param cla
	 *            schame类型
	 * @return
	 * @throws Exception
	 */
	protected <T extends Object> StringBuffer getSqlPrefix(String tableName,
			Class<T> cla) throws Exception {

		PropertyDescriptor[] props = getPropertyDescriptors(cla);
		return getSqlPrefixByPro(tableName, props);
	}

	/**
	 * 根据VO对象信息构造sql查询返回条件字符串
	 * 
	 * @param tableName
	 *            表名
	 * @param props
	 *            JAVABEAN属性描述对象信息
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
	 * 获取列名称字符串
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
	 * 将数据库结果集转换成JAVABEAN
	 * 
	 * @param rs
	 *            数据库结果集
	 * @param obj
	 *            javaBEAN对象
	 * @param props
	 *            javaBean 属性信息
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
	 * 为指定对象写入数据值信息
	 * 
	 * @param proper
	 *            属性描术信息
	 * @param obj
	 *            JAVABEAN对象
	 * @param propertyValue
	 *            属性值
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
	 * 获取查询对应列列的BEAN对象的WRITER方法
	 * 
	 * @param metaData
	 *            数据库信息对象
	 * @param props
	 *            bean属性信息
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, PropertyDescriptor> getWriterMethodByColumnName(
			ResultSetMetaData metaData, PropertyDescriptor[] props)
			throws Exception {

		// 获取所有列数
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
