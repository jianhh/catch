
package com.framework.dc.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.dc.DataCache;

/**
 * dc数据获取方法，都放到这里
 * 
 * @author rongcunhao
 * 
 */
public class DCUtil
{

    static JLogger logger = LoggerFactory.getLogger(DCUtil.class);

    private static DCUtil config = new DCUtil();

    public static DCUtil getInstance()
    {
        return config;
    }

    /**
     * 获取sql
     * @param sqlCode
     * @return
     */
    public String getSQLValue(String sqlCode)
    {

        return DataCache.getColDataFromCache("T_PPS_CONFIG_SQL",
                                             sqlCode,
                                             "SQL_VALUE");
    }

    /**
     * 根据ID获取数据版本号
     * 
     * @param id 版本号ID
     * @return
     */
    public String getDataVersion(String id)
    {

        return DataCache.getColDataFromCache("CACHE_DATAVERSION",
                                             id,
                                             "DATAVERSION");

    }
    /**
     * 获取T_CONFIG里面的值
     * @param siteName
     * @param typeName
     * @param configName
     * @return
     */
    public String getConfigSQLValue(String siteName, String typeName, String configName)
    {

        String key = siteName + "$" + typeName + "$" + configName;
        return DataCache.getColDataFromCache("T_CONFIG",
                                             key,
                                             "CONFIGVALUE");
    }
}
