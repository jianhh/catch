
package com.common.log.proxy.config;

/**
 * <p>Title: ConfigModifier</p>
 * <p>Description: modify the config file of logging</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire</p>
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */
import org.apache.log4j.Logger;
import org.apache.log4j.net.JMSAppender;
import org.apache.log4j.spi.Filter;

import com.common.log.constants.LogConstants;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

public class ConfigModifier implements ConfigModifierMBean
{

    private String configFile;

    private JMSAppender lastJMS;

    private Logger bizLog;

    private Logger runLog;

    static JLogger log = LoggerFactory.getLogger(ConfigModifier.class);

    private static String mobileIDTraced = "";

    public ConfigModifier()
    {

    }

    /**
     * constructor with the config file
     *
     * @param configFile the config file for log
     */
    public ConfigModifier(String configFile)
    {

        this.configFile = configFile;
    }

    /**
     * enable log realtime trace
     *
     * @param range "11":both biz and running log traced,"10":biz traced
     *            "01":running log traced,"00":do nothing
     * @param userMobileID the traced mobile ID
     */
    public void enableFilterAndTrace(String range, String userMobileID)
    {

        log.debug("Now into enableFilterAndTrace via code");
        log.debug("range:" + range);

        if (range.length() != 2)
        {
            log.error("range.length()!=2");
        }
        else
        {
            if (range.charAt(0) == '1')
            {// biz log bit enabled
                bizLog = Logger.getLogger(LogConstants.BIZ_LOG_TYPE);
            }
            if (range.charAt(1) == '1')
            {// running log bit enabled
                runLog = Logger.getLogger(LogConstants.ASPIRE_LOG_TYPE);
            }
        }
        if (lastJMS == null)
        {// don't add duplicatedly
            JMSAppender JMS = makeJMSAppender(userMobileID);
            lastJMS = JMS;
            if (bizLog != null)
            {
                bizLog.addAppender(lastJMS);
            }
            if (runLog != null)
            {
                runLog.addAppender(lastJMS);
            }
            mobileIDTraced = userMobileID;
        }
    }

    /**
     * disable user's realtime filter and trace function it means remove the
     * channel for publishing log message
     *
     * @param range the name of logger
     */
    public void disableFilterAndTrace()
    {

        if (lastJMS != null)
        {// remove if attached
            if (bizLog != null)
            {
                bizLog.removeAppender(lastJMS);
                bizLog = null;
            }
            if (runLog != null)
            {
                runLog.removeAppender(lastJMS);
                runLog = null;
            }
            lastJMS.close();
            log.debug("remove JMS Appender:" + lastJMS);
            lastJMS = null;
            mobileIDTraced = "";
        }
    }

    /**
     * check the status of tracing,if is traced,return mobileID,else return ""
     *
     * @return mobileID or ""
     */
    public static String checkFilterAndTrace()
    {

        return mobileIDTraced;
    }

    public String getConfigFile()
    {

        return configFile;
    }

    public void setConfigFile(String configFile)
    {

        this.configFile = configFile;
    }

    private JMSAppender makeJMSAppender(String userMobileID)
    {

        JMSAppender JMS = new JMSAppender();
        JMS.setTopicConnectionFactoryBindingName("");
        // JMS.setTopicConnectionFactoryBindingName(config.getJMSSendTCFName());
        JMS.setTopicBindingName("");
        // JMS.setTopicBindingName(config.getJMSSendTopicName());
        JMS.activateOptions();
        JMS.addFilter(makeMobileIDFilter(userMobileID));
        //JMS.addFilter(makeInfoFilter());
        return JMS;
    }

    private Filter makeMobileIDFilter(String mobileID)
    {

        UserMobileIDFilter filter = new UserMobileIDFilter();
        filter.setMobileID(mobileID);
        return filter;
    }

}
