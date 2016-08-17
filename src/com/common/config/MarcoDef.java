package com.common.config;

/**
 * <p>
 * Title: xPortal1.6 Program
 * </p>
 * <p>
 * Description: ��ȡ�����������ļ��е����к궨�塣���к��滻
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
// @CheckItem@ OPT-hecs-20040202 ���Ӻ궨�幦��
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
	 * ����������ʼ���궨���ࡣ
	 * 
	 * @param ele
	 *            �궨��DOM��
	 */
	public MarcoDef(Element ele) {
		eleMarco = ele;
		this.readData();
		instance = this;
	}

	/**
	 * �ͻ��˳�����ýӿڣ��õ���ǰMarcoDef��ʵ��
	 * 
	 * @return MarcoDef�ĵ�ǰʵ��
	 */
	public static MarcoDef getInstance() {
		return instance;
	}

	/**
	 * ���ص�ǰ�ĺ궨���ǩģ��
	 * 
	 * @return ��Element
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
	 * ��ѯһ���궨��
	 * 
	 * @param name
	 *            ������
	 * @return �궨��Element��null
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
	 * �����ڴ��еĺ궨��
	 * 
	 * @return
	 */
	public Hashtable getMarcos() {
		return marcos;
	}

	/**
	 * �����ڴ��еĺ�����
	 * 
	 * @return
	 */
	public Hashtable getMarcoDesps() {
		return marcoDesp;
	}

	/**
	 * ȡ��ָ���ĺ�ֵ
	 * 
	 * @param name
	 *            ������
	 * @return ��ֵ
	 */
	public String getMarcoValue(String name) {
		return (String) marcos.get(name);
	}

	/**
	 * ���ú������Ƿ��Ѿ�������
	 * 
	 * @param name
	 *            ������
	 * @return true���Ѿ����壬false��δ����
	 */
	public boolean isDefined(String name) {
		return marcos.containsKey(name);
	}

	/**
	 * ɾ��һ���궨��
	 * 
	 * @param name
	 *            ������
	 */
	public synchronized void removeMarco(String name) {
		marcos.remove(name);
		marcoDesp.remove(name);
		this.eleMarco.removeContent(this.getMarcoElement(name));
	}

	/**
	 * ����һ���궨��
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ��ֵ
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
	 * �޸�һ���궨��
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ��ֵ
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
	 * ���ַ����еĺ���ʵ��ֵ�滻�����ַ����в������꣬��ֱ�ӷ��ء�
	 * 
	 * @param value
	 *            Դ�ַ���
	 * @return �滻����ַ�����
	 */
	public String replaceByMarco(String value) {
		String res = value;
		if (value.indexOf("${") == -1) { // ����������ֱ꣬�ӷ��ء�
			return res;
		}
		res = replaceAll(res, marcos);
		return res;
	}

	/**
	 * ���ַ���source�е����е��ַ���orig�滻Ϊ�ַ���real
	 * 
	 * @param source
	 *            Դ�ַ���
	 * @param orig
	 *            ���滻���ַ���
	 * @param real
	 *            �����ṩorig���ַ���
	 * @return ���滻����ַ���
	 */
	public static String replaceAll(String source, Hashtable mcs) {
		String res = source;
		String name = null;
		String marcoValue = null;
		Iterator it = mcs.keySet().iterator();
		while (it.hasNext()) {
			name = (String) it.next();
			if (source.indexOf("${" + name + "}") == -1) {
				continue; // ����˺�û���壬������
			}
			marcoValue = (String) mcs.get(name);
			res = replaceString(res, "${" + name + "}", marcoValue);
		}
		return res;

	}

	/**
	 * ���ַ���source�е�����orig�滻Ϊreal��
	 * 
	 * @param source
	 *            Դ�ַ���
	 * @param orig
	 *            ���滻�ĵ��ַ���
	 * @param real
	 *            �滻Ϊ���ַ�����
	 * @return
	 */
	public static String replaceString(String source, String orig, String real) {
		String res = "";
		String prefix = null;
		String suffix = source;
		int index = source.indexOf(orig);
		if (index == -1) { // û���ֶ�Ӧ�ĺ�
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
