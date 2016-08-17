
package com.framework.page;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.framework.util.CommonUtil;

public class PageUtil
{

    /**
     * 设置分页请求参数信息
     * 
     * @param req 请求对象
     * @param resp 返回对象
     */
    public static Map<String, String> setPageParamter(Req req, Resp resp)
    {

        Map<String, String> pageMap = new HashMap();
        try
        {
            Iterator<String> keys = req.getParameterMap().keySet().iterator();
            String key = "";
            while (keys.hasNext())
            {
                key = keys.next();
                pageMap.put(key, CommonUtil.getParameter(req, key));

            }
            resp.setAttributes("paramter", pageMap);
        }
        catch (Exception ex)
        {

        }
        return pageMap;
    }
}
