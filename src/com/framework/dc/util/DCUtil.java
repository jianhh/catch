
package com.framework.dc.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.dc.DataCache;

/**
 * dc���ݻ�ȡ���������ŵ�����
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
     * ��ȡsql
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
     * ����ID��ȡ���ݰ汾��
     * 
     * @param id �汾��ID
     * @return
     */
    public String getDataVersion(String id)
    {

        return DataCache.getColDataFromCache("CACHE_DATAVERSION",
                                             id,
                                             "DATAVERSION");

    }
    /**
     * ��ȡT_CONFIG�����ֵ
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
