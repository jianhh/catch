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
 * �µ�ϵͳ����ģ����ʹ�õ��ĳ���
 * @author yanfeng
 * @version 1.4.2.0
 */

public interface Constant
{
    /**
     * �����ļ���ͷ
     */
    public static final String FILE_HEAD="<?xml version=\"1.0\" encoding=\"GB2312\"?>";
    /**
     * �����ļ��ı���
     */
    public static final String FILE_ENCODING="GB2312";
    /**
     * �ļ�·���ķָ���
     */
    public static final String FILE_SEP=System.getProperty("file.separator");
    /**
     * �ļ����ݵĻ��з�
     */
    public static final String LINE_SEP=System.getProperty("line.separator");
    /**
     * XML�ڵ������
     */
    public static final String LINE_INDENT="  ";
    /**
     * �����ļ��б�ʾ����ģ��Ľڵ���
     */
    public static final String CONFIG_MODULE_NODENAME="ModuleConfig";
    /**
     * �����ļ��еĸ��ڵ���
     */
    public static final String CONFIG_GENERAL_NODENAME="XMLSystemConfig";
    /**
     * �����ļ��б�ʾ����ģ�������������
     */
    public static final String CONFIG_MODULE_ATTR_NAME="Name";
    /**
     * �����ļ��б�ʾ����ģ���ID������
     */
    public static final String CONFIG_MODULE_ATTR_ID="Id";
    /**
     * ����ģ�������������
     */
    public static final String CONFIG_MODULE_ATTR_DESC="Description";
    /**
     * �¸�ʽ������ģ���ļ���·��������
     */
    public static final String CONFIG_MODULE_ATTR_REF="Ref";
    /**
     * �����ļ��б�ʾ��ֵ���õĽڵ���
     */
    public static final String CONFIG_SINGLEITEM_NODENAME="ConfigItem";
    /**
     * �����ļ��б�ʾ��ֵ���õĽڵ���
     */
    public static final String CONFIG_MULTIITEM_NODENAME="ArrayConfigItem";
    /**
     * �����������������
     */
    public static final String CONFIG_ITEM_NAME="Name";
    /**
     * �������ID������
     */
    public static final String CONFIG_ITEM_ID="Id";
    /**
     * �����������������
     */
    public static final String CONFIG_ITEM_DESC="Description";
    /**
     * �������ֵ������
     */
    public static final String CONFIG_ITEM_VALUE="Value";
    /**
     * ������ı���ֵ������
     */
    public static final String CONFIG_ITEM_RESERVED="Reserved";
    /**
     * ����������ȼ�������
     */
    public static final String CONFIG_ITEM_PRIORITY="Priority";
    /**
     * ���������Чʱ��������
     */
    public static final String CONFIG_ITEM_EFFECTMODE="EffectTime";
    /**
     * ��ֵ�������ֵ�ڵ���
     */
    public static final String CONFIG_ARRAYITEM_VALUENAME="ArrayValue";
    /**
     * ���ȼ�:0,�ظģ�1���߼���-1:����
     */
    public static final int CONFIG_PRIORITY_MUST=0;
    /**
     * ���ȼ�:0,�ظģ�1���߼���-1:����
     */
    public static final int CONFIG_PRIORITY_ADVANCED=1;
    /**
     * ���ȼ�:0,�ظģ�1���߼���-1:����
     */
    public static final int CONFIG_PRIORITY_RESERVED=-1;
    /**
     * ���ȼ�:-2����û�и�ֵ�����ݾ�������û�д���ڵ�����
     */
    public static final int CONFIG_PRIORITY_UNSET=-2;
    /**
     * ��Ч���ڣ�0,������1��������-1���¸���������
     */
    public static final int CONFIG_EFFECTMODE_RESTRAT=0;
    /**
     * ��Ч���ڣ�0,������1��������-1���¸���������
     */
    public static final int CONFIG_EFFECTMODE_NOW=1;
    /**
     * ��Ч���ڣ�0,������1��������-1���¸���������
     */
    public static final int CONFIG_EFFECTMODE_NEXTRUN=-1;
    /**
     * ��Ч����:-2����û�и�ֵ�����ݾ�������û�д���ڵ�����
     */
    public static final int CONFIG_EFFECTMODE_UNSET=-2;
    /**
     * �ı��е�&amp;��SAXBuilder������Ϊ&
     */
    public static final String AND_TOKEN="&";
    /**
     * ����ʱ��Ҫ�ٰ�&�滻��&amp;
     */
    public static final String AND_TOKEN_FULL="&amp;";

}
