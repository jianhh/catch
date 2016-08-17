
package com.common.log.proxy;

/**
 * <p>Title: JBLogger</p>
 *
 * <p>Description: the business logger</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: Aspire Technologies</p>
 *
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */
import java.util.*;

import com.common.log.proxy.BizLogContent;

public class JBLogger extends JLogger
{

    private HashMap logMap = new HashMap();

    JBLogger(String logclazz)
    {

        super(logclazz);
    }

    /**
     * override parent info method to lift one level
     *
     * @param msg the business message
     */
    public void info(Object msg)
    {

        super.warn(msg);
    }

    /**
     * override parent info method to lift one level
     *
     * @param msg the business message
     * @param mobileID the mobile ID
     */
    public void info(Object msg, String mobileID)
    {

        BizLogContent bizLog = new BizLogContent(msg, mobileID, type);
        super.warn(bizLog);
    }

    /**
     * 根据业务时间戳精确判断是否写入上一天的日志文件
     *
     * @param msg Object
     * @param timestamp String
     */
    public void info(Object msg, String mobileID, long timestamp)
    {

        BizLogContent bizLog = new BizLogContent(msg, mobileID, timestamp, type);
        super.warn(bizLog);
    }

    public synchronized void push(String msg, String mobileID)
    {

        if (logMap.containsKey(mobileID))
        {// has pushed log before
            ArrayList logArr = ( ArrayList ) logMap.get(mobileID);

            if (logArr != null)
            {
                if (!logArr.contains(msg))
                { // don't add repeatedly
                    logArr.add(msg);
                }
            }
            else
            {
                logArr = new ArrayList();
                logArr.add(msg);
            }
        }
        else
        {// not pushed log before,create a new array and store it
            ArrayList logArr = new ArrayList();
            logArr.add(msg);
            logMap.put(mobileID, logArr);
        }
    }

    public synchronized void pop(String mobileID)
    {

        if (logMap.containsKey(mobileID))
        {// has array log for this ID
            ArrayList logArr = ( ArrayList ) logMap.get(mobileID);
            StringBuffer str = new StringBuffer("MSISDN:");
            str.append(mobileID);
            for (int i = 0; i < logArr.size(); i++)
            {
                str.append(",");
                String msg = ( String ) logArr.get(i);
                str.append(msg);
            }
            info(str.toString());
            logArr.clear();
            logArr = null;
            logMap.remove(mobileID);
        }

    }
}
