/*
 * @(#)TimeRollingFileAppender.java        1.6 04/10/21
 *
 * Copyright (c) 2003-2005 ASPire Technologies, Inc.
 * 6/F,IER BUILDING, SOUTH AREA,SHENZHEN HI-TECH INDUSTRIAL PARK Mail Box:11# 12#.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ASPire Technologies, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Aspire.
 */

package com.common.log.proxy;

import java.io.File;

import org.apache.log4j.DailyRollingFileAppender;

import com.common.log.constants.LogConstants;

/**
 * the appender only rolling by time
 *
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */
public class TimeRollingFileAppender extends DailyRollingFileAppender
{

    public TimeRollingFileAppender()
    {

        super();
    }

    /**
     * onli override this method to 1,set the relative path 2,create path if not
     * exist
     *
     * @param file String
     */
    public void setFile(String file)
    {

        // Trim spaces from both ends. The users probably does not want
        // trailing spaces in file names.
        String val = file.trim();
        String tmpfileName = val.replace('/', LogConstants.FILE_SEP.charAt(0));
        if (!tmpfileName.startsWith(LogConstants.FILE_SEP))
        {
            tmpfileName = LogConstants.FILE_SEP + tmpfileName;
        }
        fileName = LoggerFactory.getAppRootPath() + tmpfileName;
        // create non-exist path

        int index = fileName.lastIndexOf(System.getProperty("file.separator"));
        if (index > 0)
        {
            String sPath = fileName.substring(0, index);
            File path = new File(sPath);
            if (!path.exists())
            {
                path.mkdirs();
            }
        }

    }

}
