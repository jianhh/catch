package com.framework.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.aspire.dps.jvsc.Req;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.httpsend.HttpSend;

public class ConvertUtil {

	private static JLogger logger = LoggerFactory.getLogger(ConvertUtil.class);

	/**
	 * 把MAP数据转换为BEAN(key,value都是简单的STRING)
	 * 
	 * @param <T>
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T convertBean(Map map, Class<T> clazz) {

		if (map == null || (null != map && 0 == map.size()))
			return null;
		T t = null;
		try {
			t = clazz.newInstance();

			Method[] methods = clazz.getMethods();
			String key;
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					key = m.getName().substring(3, 4).toLowerCase()
							+ m.getName().substring(4);
					m.invoke(t, map.get(key));
				}

			}
		} catch (Exception e) {
			logger.error("基地业务接入Mobile Market平台接口中对MAP 转换为Bean过程错误!");
			logger.error("要转换的MAP：" + map);
			logger.error("要转为的BEAN：" + clazz);
			logger.error(e);

		}
		return t;
	}

	// 把MAP数据转换为BEAN(value中包含List<Map>)
	public static <T, TT> T convertBeanList(Map map, Class<T> clazz,
			String subKey, Class<TT> subClazz) {

		if (map == null || (null != map && 0 == map.size()))
			return null;
		T t = null;
		try {
			t = clazz.newInstance();
			Method[] methods = clazz.getMethods();

			String key = null;

			for (Method m : methods) {
				if (m.getName().startsWith("set")) {

					key = (m.getName().substring(3, 4).toLowerCase() + m
							.getName().substring(4));// 把首字母都改成小写
					subKey = StringUtils.getStringFirstLowerCase(subKey);// 把首字母都改成小写
					if (key.equals(subKey)) {

						m.invoke(t, convertList(((List<Map>) map.get(subKey)),
								subClazz));
					} else {
						m.invoke(t, map.get(key));
					}

				}
			}

		} catch (Exception e) {
			logger.error("基地业务接入Mobile Market平台接口中对MAP 转换为Bean过程错误!");
			logger.error("把MAP数据转换为BEAN(value中包含List<Map>)");
			logger.error("要转换的MAP：" + map);
			logger.error("要转为的BEAN：" + clazz);
			logger.error("要转为的subKey：" + subKey);
			logger.error("要转为的subClazz：" + subClazz);
			logger.error(e);

		}
		return t;
	}

	/**
	 * 把List<Map>数据转换为List<BEAN>
	 * 
	 * @param <T>
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> convertList(List<Map> list, Class<T> clazz) {

		List<T> rtnList = new ArrayList<T>();
		if (list != null) {
			for (Map m : list) {
				rtnList.add(convertBean(m, clazz));
			}
		}
		return rtnList;

	}

	/**
	 * 解析流
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getXmlMap(Req request) {

		try {
			InputStream in = request.getInputStream();
			byte[] tempb = HttpSend.InputStreamToByte(in);

			logger.debug("requestXml-----------"
					+ new String(tempb, "UTF-8").toString());

			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(new ByteArrayInputStream(tempb));
			return XMLParser.parser(doc);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

}
