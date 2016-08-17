
package com.framework.tempate;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.aspire.dps.template.DefaultTempatePath;

/**
 * 终端页面转发规则
 * 
 * @author x_nijiangli
 * 
 */
public class DeviceTempatePath extends DefaultTempatePath
{

    private static JLogger log = LoggerFactory.getLogger(DeviceTempatePath.class);

    public String getPath(Req req, Resp resp)
    {

        try
        {
            String path = "/defaultSite/template/"
                          + this.getPageFromConfig(req, resp) + ".shtml";

            if (log.isDebugEnabled())
                log.debug("获取的文件路径为: " + path);
            return path;
        }
        catch (Exception ex)
        {
            if (log.isDebugEnabled())
            {
                log.debug("获取路径信息失败");
            }
            log.error("获取路径信息失败", ex);
        }
        return null;
    }
}
