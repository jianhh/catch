
package com.common.log.proxy.config;

/**
 * <p>
 * Title: ConfigModifierMBean
 * </p>
 * <p>
 * Description: the JMX interface for configuring the config file
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Aspire Technologies
 * </p>
 * @author x_biran
 * @version 1.0 history: created at 3/11/2007
 */

public interface ConfigModifierMBean
{

    public void disableFilterAndTrace();

    public void enableFilterAndTrace(String range, String userMobileID);
}
