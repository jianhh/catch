
package com.common.log.proxy.config;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import com.common.log.proxy.BizLogContent;
import com.common.log.util.LogUtils;

/**
 * <p>
 * Title: UserHpIDFilter
 * </p>
 * <p>
 * Description: The filter for User Handphone ID filter
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Aspire
 * </p>
 *
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */

public class UserMobileIDFilter extends Filter
{

    private String mobileID;

    public void setMobileID(String mobileID)
    {

        this.mobileID = LogUtils.trim(mobileID);

    }

    public String getMobileID()
    {

        return mobileID;
    }

    /**
     * @param event the logging event
     * @return is there string match.
     */
    public int decide(LoggingEvent event)
    {

        String id = "xxx";

        Object obj = event.getMessage();
        if (obj instanceof BizLogContent)
        { // is a biz log
            BizLogContent bizObj = ( BizLogContent ) obj;
            id = LogUtils.trim(bizObj.getMobileID());
            if (LogUtils.trim(mobileID).equals((id)))
            {
                return Filter.ACCEPT;
            }
        }
        return Filter.DENY;
        // return Filter.ACCEPT;//this is for test
    }

}
