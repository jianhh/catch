/*
 * @(#)ConfigDataListener.java        1.2 06/09/30
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
 * �µ�ϵͳ����������,�����������õ��޸�
 * @author yanfeng
 * @version 1.4.2.0
 */

public interface ConfigDataListener
{
    /**
     * �ص��ķ�����ֻ��ע��ʱָ����ģ��ı��ˣ��÷����Ż�ִ��
     */
    public void doConfigRefresh();
}
