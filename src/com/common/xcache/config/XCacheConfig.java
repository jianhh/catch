
package com.common.xcache.config;


import com.common.config.ConfigDataListener;
import com.common.config.ConfigFactory;
import com.common.config.ModuleConfig;
import com.common.config.ServerInfo;
import com.common.config.SystemConfig;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.dc.util.DCUtil;

public class XCacheConfig implements ConfigDataListener
{

    String modelName = "xcached";      

    private String servies; // /  集群配置

    private int connectionPoolSize = 1; // /nio连接池大小，默认为1

    private int weights = 3 ; // // 与servers对应的节点的权重，权重越高，连接数越多

    private int compressionSize = 1024; // //压缩比例 默认为1K : 对大对象进行压缩

    private boolean sanitizeKeysFlag = true; ////// 默认为true..如果和其他client进行兼容则false

    private int timeout = 10; // //连接超时时间

    private int expire = 60 * 60 * 24; // /////缓存Obj的有效时间 单位：s
    
    private int recBuffer = 64;   ///////接收缓冲大小
    
    private int setBuffer = 32;    /////发送缓冲大小

    static JLogger logger = LoggerFactory.getLogger(XCacheConfig.class);

    private static XCacheConfig xConfig = null;

    private static ModuleConfig moduleConfig = null;

    private static SystemConfig systemConfig = null;

    public void doConfigRefresh()
    {

        this.servies = DCUtil.getInstance().getConfigSQLValue(ServerInfo.siteName, "xcached", "serviesPool");
        this.weights =readConfigIntValue("weights",3);
        this.compressionSize =readConfigIntValue("compressionSize",1024);
        this.connectionPoolSize= readConfigIntValue("connectionPoolSize",1);
        this.timeout =readConfigIntValue("timeout",10000);
        this.expire=readConfigIntValue("expire",86400);
        this.recBuffer=readConfigIntValue("recBuffer",64);
        this.setBuffer=readConfigIntValue("setBuffer",32);
    }

    public String readConfigItem(String itemName)
    {

        try
        {
            String strItemValue = moduleConfig.getItemValue(itemName);
            if (logger.isDebugEnabled())
            {
                logger.debug("Get " + itemName + " from CFG: " + strItemValue);
            }
            return strItemValue;
        }
        catch (Exception ex)
        {
            logger.error("ConfigItem[" + itemName + "] is not proper configed!",
                         ex);
            return null;
        }
    }

    private XCacheConfig()
    {

        try
        {

            this.moduleConfig = ConfigFactory.getSystemConfig()
                                     .getModuleConfig(modelName);
            
           /// systemConfig.addConfigDataListener(this);
            doConfigRefresh();

        }
        catch (Exception ex)
        {
            logger.error("xCached load error!!", ex);
        }

    }

    public static XCacheConfig getInstants()
    {

        synchronized (XCacheConfig.class)
        {
            if (xConfig == null)
            {
                xConfig = new XCacheConfig();
            }
        }
        return xConfig;
    }

    public int readConfigIntValue(String itemName, int defaultValue)
    {

        int iItemValue = defaultValue;
        try
        {
            String strItemValue = DCUtil.getInstance().getConfigSQLValue(ServerInfo.siteName, "xcached", itemName);
            iItemValue = Integer.parseInt(strItemValue);
        }
        catch (Exception ex)
        {
            logger.error("ConfigItem[" + itemName + "] is not proper configed!",
                         ex);
        }
        return iItemValue;
    }

    public String getListenerName()
    {

        return "xcached";
    }

    
    public String getModelName()
    {
    
        return modelName;
    }

    
    public String getServies()
    {
    
        return servies;
    }

    
    public int getConnectionPoolSize()
    {
    
        return connectionPoolSize;
    }

    
    public int getWeights()
    {
    
        return weights;
    }

    
    public int getCompressionSize()
    {
    
        return compressionSize;
    }

    
    public boolean isSanitizeKeysFlag()
    {
    
        return sanitizeKeysFlag;
    }

    
    public int getTimeout()
    {
    
        return timeout;
    }

    
    public int getExpire()
    {
    
        return expire;
    }

    
    public static ModuleConfig getModuleConfig()
    {
    
        return moduleConfig;
    }

    
    public static SystemConfig getSystemConfig()
    {
    
        return systemConfig;
    }

    
    public int getRecBuffer()
    {
    
        return recBuffer;
    }

    
    public int getSetBuffer()
    {
    
        return setBuffer;
    }

}
