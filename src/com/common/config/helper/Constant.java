/*
 * @(#)Constant.java        1.2 06/08/31
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
package com.common.config.helper;
/**
 * 新的系统配置模型中使用到的常量
 * @author yanfeng
 * @version 1.4.2.0
 */

public interface Constant
{
    /**
     * 配置文件的头
     */
    public static final String FILE_HEAD="<?xml version=\"1.0\" encoding=\"GB2312\"?>";
    /**
     * 配置文件的编码
     */
    public static final String FILE_ENCODING="GB2312";
    /**
     * 文件路径的分隔符
     */
    public static final String FILE_SEP=System.getProperty("file.separator");
    /**
     * 文件内容的换行符
     */
    public static final String LINE_SEP=System.getProperty("line.separator");
    /**
     * XML节点的缩进
     */
    public static final String LINE_INDENT="  ";
    /**
     * 配置文件中表示配置模块的节点名
     */
    public static final String CONFIG_MODULE_NODENAME="ModuleConfig";
    /**
     * 配置文件中的根节点名
     */
    public static final String CONFIG_GENERAL_NODENAME="XMLSystemConfig";
    /**
     * 配置文件中表示配置模块的名字属性名
     */
    public static final String CONFIG_MODULE_ATTR_NAME="Name";
    /**
     * 配置文件中表示配置模块的ID属性名
     */
    public static final String CONFIG_MODULE_ATTR_ID="Id";
    /**
     * 配置模块的描述属性名
     */
    public static final String CONFIG_MODULE_ATTR_DESC="Description";
    /**
     * 新格式里配置模块文件的路径属性名
     */
    public static final String CONFIG_MODULE_ATTR_REF="Ref";
    /**
     * 配置文件中表示单值配置的节点名
     */
    public static final String CONFIG_SINGLEITEM_NODENAME="ConfigItem";
    /**
     * 配置文件中表示多值配置的节点名
     */
    public static final String CONFIG_MULTIITEM_NODENAME="ArrayConfigItem";
    /**
     * 配置项的名字属性名
     */
    public static final String CONFIG_ITEM_NAME="Name";
    /**
     * 配置项的ID属性名
     */
    public static final String CONFIG_ITEM_ID="Id";
    /**
     * 配置项的描述属性名
     */
    public static final String CONFIG_ITEM_DESC="Description";
    /**
     * 配置项的值属性名
     */
    public static final String CONFIG_ITEM_VALUE="Value";
    /**
     * 配置项的保留值属性名
     */
    public static final String CONFIG_ITEM_RESERVED="Reserved";
    /**
     * 配置项的优先级属性名
     */
    public static final String CONFIG_ITEM_PRIORITY="Priority";
    /**
     * 配置项的生效时间属性名
     */
    public static final String CONFIG_ITEM_EFFECTMODE="EffectTime";
    /**
     * 多值配置项的值节点名
     */
    public static final String CONFIG_ARRAYITEM_VALUENAME="ArrayValue";
    /**
     * 优先级:0,必改，1：高级；-1:保留
     */
    public static final int CONFIG_PRIORITY_MUST=0;
    /**
     * 优先级:0,必改，1：高级；-1:保留
     */
    public static final int CONFIG_PRIORITY_ADVANCED=1;
    /**
     * 优先级:0,必改，1：高级；-1:保留
     */
    public static final int CONFIG_PRIORITY_RESERVED=-1;
    /**
     * 优先级:-2，还没有赋值，兼容旧配置中没有此项节点的情况
     */
    public static final int CONFIG_PRIORITY_UNSET=-2;
    /**
     * 生效周期：0,重启，1，立即，-1：下个运行周期
     */
    public static final int CONFIG_EFFECTMODE_RESTRAT=0;
    /**
     * 生效周期：0,重启，1，立即，-1：下个运行周期
     */
    public static final int CONFIG_EFFECTMODE_NOW=1;
    /**
     * 生效周期：0,重启，1，立即，-1：下个运行周期
     */
    public static final int CONFIG_EFFECTMODE_NEXTRUN=-1;
    /**
     * 生效周期:-2，还没有赋值，兼容旧配置中没有此项节点的情况
     */
    public static final int CONFIG_EFFECTMODE_UNSET=-2;
    /**
     * 文本中的&amp;被SAXBuilder读入后变为&
     */
    public static final String AND_TOKEN="&";
    /**
     * 保存时需要再把&替换回&amp;
     */
    public static final String AND_TOKEN_FULL="&amp;";

}
