/*
 * @(#)ModuleConfig.java        1.2 06/08/04
 *
 * Copyright (c) 2003-2006 ASPire Technologies, Inc.
 * 6/F,IER BUILDING, SOUTH AREA,SHENZHEN HI-TECH INDUSTRIAL PARK Mail Box:11# 12#.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ASPire Technologies, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Aspire.
 */
package com.common.config.model;

import org.jdom.Element;

import com.common.config.helper.ConfigUtil;
import com.common.config.helper.Constant;

/**
 * �µĶ�ֵ�����е�һ��ֵ��ģ��,��ʾ�����ļ���ArrayValue�Ľṹ
 * 
 * @author yanfeng
 * @version 1.4.2.0
 */

public class ArrayValue implements Constant {
	/**
	 * ���
	 */
	private int ID;
	/**
	 * ֵ
	 */
	private String value;
	/**
	 * ����ֵ
	 */
	private String reserved;

	public ArrayValue() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReserved() {
		if (reserved == null) {
			return "";
		}
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	/**
	 * ��Jdom��element�м�����������
	 * 
	 * @param ele
	 *            Element
	 */
	public void loadFromElement(Element ele) {
		String temp = ele.getChild(CONFIG_ITEM_ID).getText();
		ID = Integer.parseInt(temp);
		value = ele.getChild(CONFIG_ITEM_VALUE).getText();
		reserved = ele.getChild(CONFIG_ITEM_RESERVED).getText();
	}

	/**
	 * ���ر�ʾ���ڵ����ݵ��ַ��������ڱ����ļ��������ӡ����
	 * 
	 * @param indent
	 *            String
	 * @return String
	 */
	public String toString(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_ID, ID, indent
				+ LINE_INDENT));
		sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_VALUE, value, indent
				+ LINE_INDENT));
		sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_RESERVED, reserved, indent
				+ LINE_INDENT));
		return ConfigUtil.getXMLNode(CONFIG_ARRAYITEM_VALUENAME, null, sb
				.toString(), indent);
	}

}
