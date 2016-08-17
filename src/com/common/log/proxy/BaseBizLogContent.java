
package com.common.log.proxy;

import java.util.ArrayList;

import com.common.log.constants.LogConstants;

/**
 * <p>
 * Title: BaseBizLogContent
 * </p>
 * <p>
 * Description: The abstract base business log content for others to extends
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Aspire
 * </p>
 *
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */

public class BaseBizLogContent
{

    /**
     * the place to hold log fields
     */
    ArrayList fields = new ArrayList();

    public BaseBizLogContent()
    {

    }

    /**
     * @param index the index of field,start from 0
     * @return the field value
     */
    public Object get(int index)
    {

        return fields.get(index);
    }

    /**
     * @param obj the value of one filed
     */
    public void add(Object obj)
    {

        fields.add(obj);
    }

    /**
     * @return the fields' number
     */
    public int size()
    {

        return fields.size();
    }

    public String toString()
    {

        StringBuffer buf = new StringBuffer(50);
        for (int i = 0; i < fields.size(); i++)
        {
            buf.append(fields.get(i));
            buf.append(LogConstants.COLON_SEPERATOR);
        }

        return buf.substring(0, buf.length() - 1);
    }
}
