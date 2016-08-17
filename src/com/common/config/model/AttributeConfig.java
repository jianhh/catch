/*
 * @(#)AttributeConfig.java        1.2 06/09/07
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
/**
 * �µ�ϵͳ����ģ����ʹ�õ�ģ�����û������������
* ��������<ArrayConfigItem needHSQL="1" needHash="0" sqlWhereClause="">
* ����ģ��<ModuleConfig Name="Common" Description="ͨ��������">
 * @author yanfeng
 * @version 1.4.1.004
 */

public class AttributeConfig
{
    /**
     * ������
     */
    private String attrName;
    /**
     * ����ֵ
     */
    private String attrValue;
    public AttributeConfig()
    {
    }
    public AttributeConfig(String name,String value)
    {
        this.attrName=name;
        this.attrValue=value;
    }

    public String getAttrName()
    {
        return attrName;
    }
    public void setAttrName(String attrName)
    {
        this.attrName = attrName;
    }
    public String getAttrValue()
    {
        return attrValue;
    }
    public void setAttrValue(String attrValue)
    {
        this.attrValue = attrValue;
    }
}
