/*
 * @(#)ChkfileCreatorObserver.java        1.4 07/05/22
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

package com.common.log.proxy.config;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.log4j.helpers.LogLog;

import com.common.log.proxy.BizStaticFileAppender;



/**
 * 对帐文件生成的定时检查，可被BizStaticFileAppender 注册
 */
public class ChkfileCreatorObserver
    extends TimerTask
{

	/**
	 * 每两次检查之间的等待时间，默认为 5min
	 */
    static public final long DEFAULT_PERIOD = 1 * 60000;
    //定时器启动延迟时间 默认 5s
    static public final long DEFAULT_DELAY = 1000L;

    protected Vector appendersToListen = new Vector();

    private static ChkfileCreatorObserver observer = null;
    
    private ChkfileCreatorObserver()
    {
//        setDaemon(true);
        //checkAndRemind();
    }
    
    public static void addToListen(BizStaticFileAppender appender)
    {
    	LogLog.debug("adding a new appender to listen");
    	if(observer == null)
    	{
    		LogLog.debug("new ChkfileCreatorObserver() instance");
    		observer = new ChkfileCreatorObserver();
            Timer time = new Timer();
            time.schedule(observer, DEFAULT_DELAY, DEFAULT_PERIOD);
    	}
        observer.appendersToListen.remove(appender);
        observer.appendersToListen.add(appender);
    }
    
    public static void removeFromListen(BizStaticFileAppender appender)
    {
    	observer.appendersToListen.remove(appender);
    }
    
    protected void checkAndRemind()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    for(int i=0; i<appendersToListen.size(); i++)
                    {
                        BizStaticFileAppender appender = (BizStaticFileAppender)appendersToListen.get(i);
                        LogLog.debug("call appender.subAppend()");
                        appender.subAppend(null);
                    }
                }
                catch (Exception e)
                {
                    //nop
                }
            }
            
        }.start();
       
    }

    public void run()
    {
        checkAndRemind();
    }
}
