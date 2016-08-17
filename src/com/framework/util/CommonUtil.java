
package com.framework.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.aspire.dps.jvsc.Req;

/**
 * ������
 * 
 * @author x_nijiangli
 * 
 */
public class CommonUtil
{

    private static final JLogger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * �������л�ȡ���������Ϊnull�ͷ��ؿ��ַ���""
     * 
     * @param request http����
     * @param key �����Ĺؼ���
     * @return ����ֵ
     */
    public static String getParameter(Req request, String key)
    {

        String value = request.getParameter(key);
        if (value == null)
        {
            value = "";
        }
        return value;
    }

    /**
     * �������л�ȡ���������Ϊnull�ͷ����Զ���ֵ
     * 
     * @param request http����
     * @param key �����Ĺؼ���
     * @param v �Զ���ֵ
     * @return
     */
    public static String getParameter(Req request, String key, String v)
    {

        String value = getParameter(request, key);

        return value == null ? v : value;
    }

    /**
     * ƴװmemcached��keyֵ
     * 
     * @param str
     * @return
     */
    public static String getMemcachedKey(String str[])
    {

        if (str == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++)
        {
            sb.append("{");
            sb.append(str[i] == null ? "" : str[i]);
            sb.append("}");
        }
        String key = sb.toString();
        if (key.length() >= 250)
        {
            key = String.valueOf(key.hashCode());
        }
        return key;
    }
}
