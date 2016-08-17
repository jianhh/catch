package com.common.log.newproxy;




/**
 * <p>
 * Title: BizLogContent
 * </p>
 * <p>
 * Description: the log for business,including the mobile ID
 * </p> 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Aspire Technologies
 * </p>
 *
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */

public class BizLogContent
{

    private String mobileID;

    private Object content;

    private int logType;

    /**
     * 该日志的业务时间戳，即业务处理完的时间点
     */
    private long timestamp;

    public BizLogContent()
    {

    }

    public BizLogContent(Object content, String mobileID, int logType)
    {

        this.content = content;
        this.mobileID = mobileID;
        this.logType = logType;
    }

    public BizLogContent(Object content, String mobileID, long timestamp,
                         int logType)
    {

        this.content = content;
        this.mobileID = mobileID;
        this.timestamp = timestamp;
        this.logType = logType;
    }

    public String getMobileID()
    {

        return mobileID;
    }

    public void setMobileID(String mobileID)
    {

        this.mobileID = mobileID;
    }

    public void setContent(Object content)
    {

        this.content = content;
    }

    /**
     * override the default to output the real biz content
     *
     * @return the biz content
     */
    public String toString()
    {

        if (logType == 1)
        { // is biz log
            return content.toString();
        }
        else
        {// is running log
            return mobileID + LogConstants.COLON_SEPERATOR + content.toString();
        }
    }

    public void setLogType(int logType)
    {

        this.logType = logType;
    }

    public long getTimestamp()
    {

        return timestamp;
    }

}
