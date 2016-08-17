
package com.framework.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.aspire.dps.jvsc.Req;

/**
 * 工具类
 * 
 * @author x_nijiangli
 * 
 */
public class CommonUtil
{

    private static final JLogger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 从请求中获取参数，如果为null就返回空字符串""
     * 
     * @param request http请求
     * @param key 参数的关键字
     * @return 参数值
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
     * 从请求中获取参数，如果为null就返回自定义值
     * 
     * @param request http请求
     * @param key 参数的关键字
     * @param v 自定义值
     * @return
     */
    public static String getParameter(Req request, String key, String v)
    {

        String value = getParameter(request, key);

        return value == null ? v : value;
    }

    /**
     * 拼装memcached的key值
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
