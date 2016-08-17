package com.common.config;

/**
 * <p>
 * Title: xPortal1.6 Program
 * </p>
 * <p>
 * Description: 读取，保存配置文件中的所有宏定义。进行宏替换
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Aspire Technologies
 * </p>
 * 
 * @author He Chengshou
 * @version 1.6
 */
// @CheckItem@ OPT-hecs-20040202 增加宏定义功能
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public class MarcoDef {
	private static MarcoDef instance = null;
	private static Hashtable marcos = new Hashtable();
	private static Hashtable marcoDesp = new Hashtable();
	private org.jdom.Element eleMarco = null;

	/**
	 * 构造器，初始化宏定义类。
	 * 
	 * @param ele
	 *            宏定义DOM块
	 */
	public MarcoDef(Element ele) {
		eleMarco = ele;
		this.readData();
		instance = this;
	}

	/**
	 * 客户端程序调用接口，得到当前MarcoDef的实例
	 * 
	 * @return MarcoDef的当前实例
	 */
	public static MarcoDef getInstance() {
		return instance;
	}

	/**
	 * 返回当前的宏定义标签模块
	 * 
	 * @return 宏Element
	 */
	public Element getMarcoRootElement() {
		return this.eleMarco;
	}

	/**
	 * Set the Marco Element
	 * 
	 * @param ele
	 *            The true Element of MarcoDefine
	 */
	public void setElement(Element ele) {
		eleMarco = ele;
		marcos.clear();
		this.readData();
	}

	/**
	 * Read all marco data to hashtable
	 */
	private void readData() {
		if (eleMarco == null) {
			return;
		}
		List list = eleMarco.getChildren("Marco");
		Iterator itrts = list.iterator();
		String name = null;
		String value = null;
		String desc = null;
		while (itrts.hasNext()) {
			Element element = (Element) itrts.next();
			name = element.getAttributeValue("name");
			value = element.getAttributeValue("value");
			desc = element.getAttributeValue("descripiton");
			if (desc == null) {
				desc = "";
			}
			marcos.put(name, value);
			marcoDesp.put(name, desc);
		}

	}

	/**
	 * 查询一个宏定义
	 * 
	 * @param name
	 *            宏名称
	 * @return 宏定义Element或null
	 */
	private Element getMarcoElement(String name) {
		Element ele = null;
		List list = eleMarco.getChildren("Marco");
		Iterator itrts = list.iterator();
		while (itrts.hasNext()) {
			Element element = (Element) itrts.next();
			if (element.getAttributeValue("name").equalsIgnoreCase(name)) {
				ele = element;
				break;
			}
		}
		return ele;
	}

	/**
	 * 返回内存中的宏定义
	 * 
	 * @return
	 */
	public Hashtable getMarcos() {
		return marcos;
	}

	/**
	 * 返回内存中的宏描述
	 * 
	 * @return
	 */
	public Hashtable getMarcoDesps() {
		return marcoDesp;
	}

	/**
	 * 取得指定的宏值
	 * 
	 * @param name
	 *            宏名称
	 * @return 宏值
	 */
	public String getMarcoValue(String name) {
		return (String) marcos.get(name);
	}

	/**
	 * 检查该宏名称是否已经被定义
	 * 
	 * @param name
	 *            宏名称
	 * @return true：已经定义，false：未定义
	 */
	public boolean isDefined(String name) {
		return marcos.containsKey(name);
	}

	/**
	 * 删除一个宏定义
	 * 
	 * @param name
	 *            宏名称
	 */
	public synchronized void removeMarco(String name) {
		marcos.remove(name);
		marcoDesp.remove(name);
		this.eleMarco.removeContent(this.getMarcoElement(name));
	}

	/**
	 * 增加一个宏定义
	 * 
	 * @param name
	 *            宏名称
	 * @param value
	 *            宏值
	 * @throws ConfigException
	 */
	public synchronized void addMarco(String name, String value, String desc)
			throws Exception {
		if (this.getMarcoElement(name) != null) {
			throw new Exception("Duplicated marco name.");
		} else {
			marcos.put(name, value);
			marcoDesp.put(name, desc);
			Element ele = new Element("Marco");
			ele.setAttribute("name", name);
			ele.setAttribute("value", value);
			ele.setAttribute("description", desc);
			this.eleMarco.addContent(ele);
		}
	}

	/**
	 * 修改一个宏定义
	 * 
	 * @param name
	 *            宏名称
	 * @param value
	 *            宏值
	 */
	public synchronized void modifyMarco(String name, String value, String desc) {
		marcos.remove(name);
		marcos.put(name, value);
		marcoDesp.remove(name);
		marcoDesp.put(name, desc);
		Element ele = this.getMarcoElement(name);
		ele.setAttribute("value", value);
		ele.setAttribute("description", desc);
	}

	/**
	 * 将字符串中的宏用实际值替换，若字符串中不包含宏，则直接返回。
	 * 
	 * @param value
	 *            源字符串
	 * @return 替换后的字符串。
	 */
	public String replaceByMarco(String value) {
		String res = value;
		if (value.indexOf("${") == -1) { // 如果不包含宏，直接返回。
			return res;
		}
		res = replaceAll(res, marcos);
		return res;
	}

	/**
	 * 将字符串source中的所有的字符串orig替换为字符串real
	 * 
	 * @param source
	 *            源字符串
	 * @param orig
	 *            被替换的字符串
	 * @param real
	 *            用以提供orig的字符串
	 * @return 被替换后的字符串
	 */
	public static String replaceAll(String source, Hashtable mcs) {
		String res = source;
		String name = null;
		String marcoValue = null;
		Iterator it = mcs.keySet().iterator();
		while (it.hasNext()) {
			name = (String) it.next();
			if (source.indexOf("${" + name + "}") == -1) {
				continue; // 如果此宏没定义，则跳过
			}
			marcoValue = (String) mcs.get(name);
			res = replaceString(res, "${" + name + "}", marcoValue);
		}
		return res;

	}

	/**
	 * 将字符串source中的所有orig替换为real。
	 * 
	 * @param source
	 *            源字符串
	 * @param orig
	 *            被替换的的字符串
	 * @param real
	 *            替换为的字符串。
	 * @return
	 */
	public static String replaceString(String source, String orig, String real) {
		String res = "";
		String prefix = null;
		String suffix = source;
		int index = source.indexOf(orig);
		if (index == -1) { // 没发现对应的宏
			return source;
		}
		while (index != -1) {
			prefix = suffix.substring(0, index);
			res = res + prefix + real;
			suffix = suffix.substring(index + orig.length());
			index = suffix.indexOf(orig);
			if (index == -1) {
				res += suffix;
			}
		}
		return res;
	}

}
