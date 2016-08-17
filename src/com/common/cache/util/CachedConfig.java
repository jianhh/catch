
package com.common.cache.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.cache.constants.Constants;
import com.common.config.ArrayValue;
import com.common.config.ConfigDataListener;
import com.common.config.ConfigFactory;
import com.common.config.ModuleConfig;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;


public class CachedConfig implements ConfigDataListener
{

    private static JLogger logger = LoggerFactory.getLogger(CachedConfig.class);

    private String initConnet;

    private String minConnet;

    private String maxConnet;

    private String maxIdle;

    private String nagle;

    private String socketTo;

    private String socketConnectTO;

    private String compressEnable;

    private String compressThreshold;

    private String mainSleepSize;

    private String isOpen;
    private String timeValue;

//    private String[] service;
    
    private Map<String,List<String>> serviceMap=new HashMap();
    private Map<String,List<Integer>> weightsMap=new HashMap();
//    private Integer[] weights;
    private String strategy;
    
    String modelName = "cached";

    private static CachedConfig config = new CachedConfig();
  
    
    public static synchronized CachedConfig getInstants()
    {

        if (config == null)
            config = new CachedConfig();
        return config;
    }

    private CachedConfig()
    {

        this.init();
    }

    /**
     * 初始化
     */
    private void init()
    {

        // 将本实例注册到配置更新通知列表中
        // ConfigFactory.getSystemConfig().addConfigDataListener(this);
        readConfig();
    }

    /**
     * 读取配置
     */
    private void readConfig()
    {

        ModuleConfig myCFG = ConfigFactory.getSystemConfig()
                                          .getModuleConfig(modelName);
        this.initConnet = readConfigItem(myCFG, "initConnet");
        this.minConnet = readConfigItem(myCFG, "minConnet");
        this.maxConnet = readConfigItem(myCFG, "maxConnet");
        this.maxIdle = readConfigItem(myCFG, "maxIdle");
        this.nagle = readConfigItem(myCFG, "nagle");
        this.socketTo = readConfigItem(myCFG, "socketTo");
        this.socketConnectTO = readConfigItem(myCFG, "socketConnectTO");
        this.compressEnable = readConfigItem(myCFG, "compressEnable");
        this.compressThreshold = readConfigItem(myCFG, "compressThreshold");
        this.mainSleepSize = readConfigItem(myCFG, "mainSleepSize");
        this.isOpen = readConfigItem(myCFG, "isOpen");
        this.strategy = readConfigItem(myCFG, "strategy");
        this.timeValue = readConfigItem(myCFG, "timeValue");
        ArrayValue[] services = readConfigArrayItem(myCFG, "service");
//        service = new String[services.length];
        String key=Constants.DEFAULTMEMCACHENAME;
        for (int i = 0; i < services.length; i++)
        {
            if (services[i].getReserved()!=null&&!"".equals(services[i].getReserved()))
                key=services[i].getReserved();
            List<String> serviceList=serviceMap.get(key);
            if (serviceList==null)
            {
                serviceList=new ArrayList<String>();
                serviceMap.put(key,serviceList);
            }
            serviceList.add(services[i].getValue());
        }
        key=Constants.DEFAULTMEMCACHENAME;
        ArrayValue[] weight = readConfigArrayItem(myCFG, "weights");
//        weights = new Integer[weight.length];
        for (int i = 0; i < weight.length; i++)
        {
            if (weight[i]!=null&&!"".equals(weight[i].getReserved()))
                key=weight[i].getReserved();
            List<Integer> weightList=weightsMap.get(key);
            if (weightList==null)
            {
                weightList=new ArrayList<Integer>();
                weightsMap.put(key,weightList);
            }
            weightList.add(Integer.valueOf(weight[i].getValue()));
        }
    }

    /**
     * 读取配置项
     * 
     * @param myCFG ModuleConfig
     * @param strConfigItemName String
     * @return String
     */
    private ArrayValue[] readConfigArrayItem(ModuleConfig myCFG,
                                             String strConfigItemName)
    {

        try
        {
            ArrayValue[] strTmp = myCFG.getArrayItem(strConfigItemName)
                                       .getArrayValues();
            logger.info("Get " + strConfigItemName + " from CFG: " + strTmp);
            return strTmp;
        }
        catch (Exception ex)
        {
            logger.error("Get Common CFG item " + strConfigItemName + " error:",
                         ex);
            // 初始化读配置失败, 在启动日志中输出失败信息, 供部署人员检查
            return null;
        }
    }

    /**
     * 读取配置项
     * 
     * @param myCFG ModuleConfig
     * @param strConfigItemName String
     * @return String
     */
    private String readConfigItemReserved(ModuleConfig myCFG,
                                          String strConfigItemName)
    {

        try
        {

            String strTmp = myCFG.getItem(strConfigItemName).getReserved();
            logger.info("Get " + strConfigItemName + " Reserved from CFG: "
                        + strTmp);
            return strTmp;
        }
        catch (Exception ex)
        {
            logger.error("Get Common CFG item " + strConfigItemName
                         + " Reserved  error:", ex);
            // 初始化读配置失败, 在启动日志中输出失败信息, 供部署人员检查
            return null;
        }
    }

    /**
     * 重新初始化
     */
    private void reInit()
    {

        readConfig();
    }

    /**
     * 读取配置项
     * 
     * @param myCFG ModuleConfig
     * @param strConfigItemName String
     * @return String
     */
    private String readConfigItem(ModuleConfig myCFG, String strConfigItemName)
    {

        try
        {
            String strTmp = myCFG.getItemValue(strConfigItemName);
            logger.info("Get " + strConfigItemName + " from CFG: " + strTmp);
            return strTmp;
        }
        catch (Exception ex)
        {
            logger.error("Get Common CFG item " + strConfigItemName + " error:",
                         ex);
            // 初始化读配置失败, 在启动日志中输出失败信息, 供部署人员检查
            return null;
        }
    }

    public void doConfigRefresh()
    {

        reInit();
    }

    public String getListenerName()
    {

        return modelName;
    }

    public static JLogger getLogger()
    {

        return logger;
    }

    public static void setLogger(JLogger logger)
    {

        CachedConfig.logger = logger;
    }

    public String getCompressEnable()
    {

        return compressEnable;
    }

    public void setCompressEnable(String compressEnable)
    {

        this.compressEnable = compressEnable;
    }

    public String getCompressThreshold()
    {

        return compressThreshold;
    }

    public void setCompressThreshold(String compressThreshold)
    {

        this.compressThreshold = compressThreshold;
    }

    public String getInitConnet()
    {

        return initConnet;
    }

    public void setInitConnet(String initConnet)
    {

        this.initConnet = initConnet;
    }

    public String getMainSleepSize()
    {

        return mainSleepSize;
    }

    public void setMainSleepSize(String mainSleepSize)
    {

        this.mainSleepSize = mainSleepSize;
    }

    public String getMaxConnet()
    {

        return maxConnet;
    }

    public void setMaxConnet(String maxConnet)
    {

        this.maxConnet = maxConnet;
    }

    public String getMaxIdle()
    {

        return maxIdle;
    }

    public void setMaxIdle(String maxIdle)
    {

        this.maxIdle = maxIdle;
    }

    public String getMinConnet()
    {

        return minConnet;
    }

    public void setMinConnet(String minConnet)
    {

        this.minConnet = minConnet;
    }

    public String getNagle()
    {

        return nagle;
    }

    public void setNagle(String nagle)
    {

        this.nagle = nagle;
    }

    public String getSocketConnectTO()
    {

        return socketConnectTO;
    }

    public void setSocketConnectTO(String socketConnectTO)
    {

        this.socketConnectTO = socketConnectTO;
    }

    public String getSocketTo()
    {

        return socketTo;
    }

    public void setSocketTo(String socketTo)
    {

        this.socketTo = socketTo;
    }

//    public String[] getService()
//    {
//
//        return service;
//    }
//
//    public void setService(String[] service)
//    {
//
//        this.service = service;
//    }
//
//    public Integer[] getWeights()
//    {
//
//        return weights;
//    }

    
    public String getIsOpen()
    {
    
        return isOpen;
    }

    
    public String getStrategy()
    {
    
        return strategy;
    }

    
    public String getTimeValue()
    {
    
        return timeValue;
    }

    
    public void setTimeValue(String timeValue)
    {
    
        this.timeValue = timeValue;
    }

    
    public Map<String, List<String>> getServiceMap()
    {
    
        return serviceMap;
    }

    
    public void setServiceMap(Map<String, List<String>> serviceMap)
    {
    
        this.serviceMap = serviceMap;
    }

    
    public Map<String, List<Integer>> getWeightsMap()
    {
    
        return weightsMap;
    }

    
    public void setWeightsMap(Map<String, List<Integer>> weightsMap)
    {
    
        this.weightsMap = weightsMap;
    }

}
