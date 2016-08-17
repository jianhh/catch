
package com.common.log.proxy.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.common.log.util.LogUtils;

/**
 * <p>
 * Title: ErrInfoInitiator
 * </p>
 * <p>
 * Description: Load the error code and message pair when first using
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

public class ErrInfoInitiator
{

    private Properties infoCache = new Properties();

    /*
     * The ErrInfoInitiator constructor loads all of the error code and error
     * message from the error.properties into the infoCache properties object
     *
     */
    public ErrInfoInitiator()
    {

    }

    public void loadErrorInfo(String fileName)
    {

        try
        {
            if ((fileName == null) || fileName.equals(""))
            {
                return;
            }
            FileInputStream fis = new FileInputStream(fileName);
            // InputStream is=this.getClass().getResourceAsStream(fileName);
            infoCache.load(fis);
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            // logger.error("load error.properties error",e);
        }
    }

    public void loadErrorInfo(InputStream is)
    {

        try
        {
            if (is == null)
            {
                return;
            }
            infoCache.load(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            // logger.error("load error.properties error",e);
        }
    }

    public String getErrMsg(int errCode)
    {

        String code = String.valueOf(errCode);
        /* Checking to see if the requested SQL statement is in the sqlCache */
        if (infoCache.containsKey(code))
        {
            String msg = ( String ) infoCache.get(code);
            return LogUtils.ISOtoGBK(msg);
        }
        return "";
    }

}
