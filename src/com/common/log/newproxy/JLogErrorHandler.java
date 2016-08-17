
package com.common.log.newproxy;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <p>
 * Title:JLogErrorHandler
 * </p>
 * <p>
 * Description: It handlers some errors occured when appenders outputting the
 * log, it send an alert
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

public class JLogErrorHandler implements ErrorHandler, ErrorCode
{

    public JLogErrorHandler()
    {

    }

    /**
     * <em>Adds</em> the logger passed as parameter to the list of loggers
     * that we need to search for in case of appender failure.
     */
    public void setLogger(Logger logger)
    {

    }

    /**
     * No options to activate.
     */
    public void activateOptions()
    {

    }

    /**
     * Prints the message and the stack trace of the exception on
     * <code>System.err</code>.
     */
    public void error(String message, Exception e, int errorCode,
                      LoggingEvent event)
    {

        error(message, e, errorCode);
    }

    public void error(String message)
    {

    }

    /**
     * The appender to which this error handler is attached.
     */
    public void setAppender(Appender primary)
    {

    }

    /**
     * Set the backup appender.
     */
    public void setBackupAppender(Appender backup)
    {

    }

    /**
     * For some fatal error ,report to alert system,others just print it and
     * change to other channels
     *
     * @param message the error message
     * @param e exception
     * @param errorCode error code defined by log4j
     */
    public void error(String message, Exception e, int errorCode)
    {

        System.err.println(message);
        if (e != null)
        {
            e.printStackTrace();
        }
    }

}
