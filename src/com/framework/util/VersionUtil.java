
package com.framework.util;

public class VersionUtil
{

    /**
     * 如果是预览服务器准现网，则不查询现网数据库获取分页数量
     * 
     * @return
     */
    public static boolean isPreviewServerConfig()
    {

        // WebConfig.getInstants().getPreviewServerConfig();
        String previewServerConfig = "YES";

        if (null != previewServerConfig && previewServerConfig.equals("YES"))
        {
            return true;
        }
        return false;
    }
}
