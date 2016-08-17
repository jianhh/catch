/*
 * @(#)DuplicateNameException.java        1.2 06/09/11
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
 * 新的系统配置模型中在同一配置模块下重名的配置项
* 或同名的配置模块异常
 * @author yanfeng
 * @version 1.4.1.004
 */

public class DuplicateNameException extends Exception
{
    public DuplicateNameException()
    {
        super();
    }
    public DuplicateNameException(String msg)
    {
        super(msg);
    }
}
