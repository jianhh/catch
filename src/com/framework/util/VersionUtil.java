
package com.framework.util;

public class VersionUtil
{

    /**
     * �����Ԥ��������׼�������򲻲�ѯ�������ݿ��ȡ��ҳ����
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
