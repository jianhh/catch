package com.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

public class XMLParser {

	private static JLogger logger = LoggerFactory.getLogger(XMLParser.class);

	public static Map<String, Object> parser(Document doc) throws Exception {

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Element root = doc.getRootElement();

		List<Element> l = root.getChildren();
		for (int i = 0; i < l.size(); i++) {
			parse(l.get(i), null, rtnMap);
		}

		return rtnMap;

	}

	// 把所有首字母都改成小写
	private static void parse(Element doc, Map m, Map rtnMap) {

		// TODO Auto-generated method stub
		List<Element> list = doc.getChildren();
		if (list.size() == 0) {
			if (m == null) {
				rtnMap.put(StringUtils.getStringFirstLowerCase(doc.getName()),
						doc.getTextTrim());
			} else {
				m.put(StringUtils.getStringFirstLowerCase(doc.getName()), doc
						.getTextTrim());
			}

		} else {
			Map children = new HashMap();
			for (int i = 0; i < list.size(); i++) {
				parse(list.get(i), children, rtnMap);
			}

			Object value = rtnMap.get(StringUtils.getStringFirstLowerCase(doc
					.getName()));
			if (value != null) {
				((ArrayList) value).add(children);
			} else {
				List l = new ArrayList();
				l.add(children);
				rtnMap.put(StringUtils.getStringFirstLowerCase(doc.getName()),
						l);
			}

		}

	}

}
