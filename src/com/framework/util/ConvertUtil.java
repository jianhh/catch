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
	 * ��MAP����ת��ΪBEAN(key,value���Ǽ򵥵�STRING)
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
			logger.error("����ҵ�����Mobile Marketƽ̨�ӿ��ж�MAP ת��ΪBean���̴���!");
			logger.error("Ҫת����MAP��" + map);
			logger.error("ҪתΪ��BEAN��" + clazz);
			logger.error(e);

		}
		return t;
	}

	// ��MAP����ת��ΪBEAN(value�а���List<Map>)
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
							.getName().substring(4));// ������ĸ���ĳ�Сд
					subKey = StringUtils.getStringFirstLowerCase(subKey);// ������ĸ���ĳ�Сд
					if (key.equals(subKey)) {

						m.invoke(t, convertList(((List<Map>) map.get(subKey)),
								subClazz));
					} else {
						m.invoke(t, map.get(key));
					}

				}
			}

		} catch (Exception e) {
			logger.error("����ҵ�����Mobile Marketƽ̨�ӿ��ж�MAP ת��ΪBean���̴���!");
			logger.error("��MAP����ת��ΪBEAN(value�а���List<Map>)");
			logger.error("Ҫת����MAP��" + map);
			logger.error("ҪתΪ��BEAN��" + clazz);
			logger.error("ҪתΪ��subKey��" + subKey);
			logger.error("ҪתΪ��subClazz��" + subClazz);
			logger.error(e);

		}
		return t;
	}

	/**
	 * ��List<Map>����ת��ΪList<BEAN>
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
	 * ������
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
