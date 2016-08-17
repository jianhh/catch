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
 * 新的系统配置侦听器,用于侦听配置的修改
 * @author yanfeng
 * @version 1.4.2.0
 */

public interface ConfigDataListener
{
    /**
     * 回调的方法，只有注册时指定的模块改变了，该方法才会执行
     */
    public void doConfigRefresh();
}
