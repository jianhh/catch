/*
 * @(#)SingleItemConfig.java        1.2 06/08/04
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
import org.jdom.*;
import java.util.*;
import com.common.config.helper.ConfigUtil;

/**
 * 新的单值配置项模型,对应配置文件中ConfigItem节点的内容
 * @author yanfeng
 * @version 1.4.2.0
 */

public class SingleItemConfig extends ItemConfig
{


    /**
     * 配置值
     */
    private String value;
    /**
     * 保留值
     */
    private String reserved;

    public SingleItemConfig()
    {
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    public String getReserved()
    {
        return reserved;
    }
    public void setReserved(String reserved)
    {
        this.reserved = reserved;
    }
    /**
     * 从Jdom的element中加载配置数据
     * @param ele Element
     */
    public void loadFromElement(Element ele)
    {
        super.loadFromElement(ele);
        value = ele.getChild(CONFIG_ITEM_VALUE).getText();
        reserved = ele.getChild(CONFIG_ITEM_RESERVED).getText();

    }
    /**
     * 返回表示本节点内容的字符串，用于保存文件和输出打印测试
     * @return String
     * @param indent String
     */
    public String toString(String indent)
    {
        StringBuffer sb=new StringBuffer();
        sb.append(super.toString(indent+LINE_INDENT));
        sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_VALUE,value,indent+LINE_INDENT));
        sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_RESERVED,reserved,indent+LINE_INDENT));
        ArrayList attrs=this.getAttrConfigs();
        return ConfigUtil.getXMLNode(CONFIG_SINGLEITEM_NODENAME,attrs,sb.toString(),indent);
    }
    public String toString()
    {
        return toString("");
    }
}
