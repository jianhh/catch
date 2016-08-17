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
 * 新的多值配置中的一个值的模型,表示配置文件中ArrayValue的结构
 * 
 * @author yanfeng
 * @version 1.4.2.0
 */

public class ArrayValue implements Constant {
	/**
	 * 序号
	 */
	private int ID;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 保留值
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
	 * 从Jdom的element中加载配置数据
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
	 * 返回表示本节点内容的字符串，用于保存文件和输出打印测试
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
