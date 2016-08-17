
package com.common.log.newproxy;

/**
 * <p>
 * Title: LogConstants
 * </p>
 * <p>
 * Description: the constants value for Log module
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

public class LogConstants
{

    public LogConstants()
    {

    }

    /**
     * the suffix of various system's log center used in token mark
     */
    public static final String LOG_MODULE = "_LogCenter";

    /**
     * the suffix of backup log file for transferring
     */
    public final static String LOG_BACKUP_SUFFIX = ".bak";

    /**
     * the line seperator according to various OS
     */
    public final static String NEXT_LINE = System.getProperty("line.separator");

    /**
     * the file path seperator according to various OS
     */
    public final static String FILE_SEP = System.getProperty("file.separator");

    /**
     * the mark in error message for substitute
     */
    public final static String REP_MARK = "?";

    /**
     * the subscription ID for creating Durable Subscriber in JMS
     */
    public final static String JMS_SUBSID = "myLog";

    /**
     * the biz log prefix
     */
    public final static String BIZ_LOG_TYPE = "biz";

    /**
     * the prefix of all the code of us
     */
    public final static String ASPIRE_LOG_TYPE = "com";

    /**
     * the seperator of mobile ID and other log content also seperate error code
     * and error message
     */
    public final static String COLON_SEPERATOR = ":";

}
