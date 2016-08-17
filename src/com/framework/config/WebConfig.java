
package com.framework.config;

import java.util.Hashtable;

import com.common.config.ArrayValue;
import com.common.config.ConfigDataListener;
import com.common.config.ConfigFactory;
import com.common.config.ModuleConfig;
import com.common.config.SystemConfig;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

public class WebConfig implements ConfigDataListener
{

    static JLogger logger = LoggerFactory.getLogger(WebConfig.class);

    private static WebConfig wwwPortalConfig = null;

    private static ModuleConfig webConfig = null;

    private static SystemConfig systemConfig = null;

    /**
     * ��ȡ�̶������·�����ȷ�������
     */
    private static Hashtable myServPWRules = new Hashtable();

    /**
     * һСʱ�����������¼ʧ�ܴ���
     */
    private int imaxLogonErrorTimes = 15;

    /**
     * ����������Чʱ��
     */
    private int iPassValidTime = 1;

    /**
     * �����·�����������С���ʱ��
     */
    private int iPassSendTimeInterval = 1;

    /**
     * ���޶���������Чʱ��(iPassValidTime)������·�����
     */
    private int imaxSendPwdTimes = 5;

    /**
     * ����URL
     */
    private String contentURL = "http://10.1.3.69:19403/dispatcher?ID=";

    /**
     * OTAURL
     */
    private String OTAURL = "http://10.1.3.69:19403/ota?id=";

    /**
     * DIFFEROTAURL
     */
    private String DIFFEROTAURL = "http://10.1.3.69:19403/ota?id=";

    /**
     * DIFFERNotifyURL
     */
    private String DIFFERNotifyURL = "http://10.1.3.69:19403/downloadNotify?id=";

    /**
     * LibraryURL
     */
    private String LibraryURL = "http://10.1.3.69:19403/downloadApp?id=";
    
    /**
     * AppURL
     */
    private String AppURL = "http://10.1.3.69:19403/downloadApp?id=";

    /**
     * WebAppURL
     */
    private String WebAppURL = "http://10.1.3.69:19403/downloadAppForWeb?id=";
    
    /**
     * SDKURL
     */
    private String SDKURL = "http://10.1.3.69:19403/downloadAppForOpen?id=";

    /**
     * NotifyURL
     */
    private String NotifyURL = "http://10.1.3.69:19403/downloadNotify?id=";
    
    /**
     * HTCNotifyURL
     */
    private String HTCNotifyURL = "http://10.1.3.69:19403/downloadNotify?id=";

    private String wapDownURL = "";

    private String portalURL = "";

    /**
     * ���ƽ̨
     */
    private static Hashtable ad = new Hashtable();

    /**
     * ����MOӪ��ƽ̨URL
     */
    private String momasUrl = "";

    private String previewServerConfig = ""; // �Ƿ��ȡԤ��������
    
    private String sqlfileServerConfig = ""; // �Ƿ��ȡsql�ļ�

    /**
     * һСʱ�����������¼ʧ�ܴ���
     */
    public int imaxLogonErrorTimes()
    {

        return imaxLogonErrorTimes;
    }

    /**
     * ����������Чʱ��
     */
    public int iPassValidTime()
    {

        return iPassValidTime;
    }

    /**
     * �����·�����������С���ʱ��
     */
    public int iPassSendTimeInterval()
    {

        return iPassSendTimeInterval;
    }

    /**
     * ���޶���������Чʱ��(iPassValidTime)������·�����
     */
    public int imaxSendPwdTimes()
    {

        return imaxSendPwdTimes;
    }

    /**
     * ��ȡ�̶������·�����ȷ�������
     */
    public int getMyServPWRules(String configName)
    {

        try
        {
            return Integer.parseInt(( String ) myServPWRules.get(configName));
        }
        catch (Exception e)
        {
            logger.error("myServPWRules��" + configName + "���������", e);
            return -1;
        }

    }

    /**
     * ��ȡ����URL
     */
    public String getContentURL()
    {

        return contentURL;
    }

    /**
     * ��ȡOTAURL
     */
    public String getOTAURL()
    {

        return OTAURL;
    }

    /**
     * ��ȡLibraryURL
     */
    public String getLibraryURL()
    {

        return LibraryURL;
    }
    
    /**
     * ��ȡAppURL
     */
    public String getAppURL()
    {

        return AppURL;
    }

    public String getWebAppURL()
    {

        return WebAppURL;
    }

    public String getSDKURL()
    {

        return SDKURL;
    }
    /**
     * ��ȡNotifyURL
     */
    public String getNotifyURL()
    {

        return NotifyURL;
    }

    /**
     * ��ȡ�ն��Ż��ľ���URL
     */
    public String getPortalURL()
    {

        return portalURL;
    }

    public String getAd(String configName)
    {

        return ( String ) ad.get(configName);
    }

    public void doConfigRefresh()
    {

        imaxLogonErrorTimes = readConfigIntValue("imaxLogonErrorTimes", 15);
        iPassValidTime = readConfigIntValue("iPassValidTime", 1);
        iPassSendTimeInterval = readConfigIntValue("iPassSendTimeInterval", 1);
        imaxSendPwdTimes = readConfigIntValue("imaxSendPwdTimes", 5);
        contentURL = readConfigItem("contentURL");
        OTAURL = readConfigItem("OTAURL");
        AppURL = readConfigItem("AppURL");
        try
        {
            LibraryURL = readConfigItem("LibraryURL");
        }
        catch (Exception ex)
        {
            logger.error("LibraryURL ���ó���.");
        }
        
        WebAppURL = readConfigItem("WebAppURL");
        SDKURL = readConfigItem("SDKURL");
        NotifyURL = readConfigItem("NotifyURL");
        HTCNotifyURL = readConfigItem("HTCNotifyURL");
        DIFFEROTAURL = readConfigItem("DIFFEROTAURL");
        DIFFERNotifyURL = readConfigItem("DIFFERNotifyURL");

        momasUrl = readConfigItem("momasUrl");
        previewServerConfig = readConfigItem("previewServerConfig");
        sqlfileServerConfig = readConfigItem("sqlfileServerConfig");
        

        wapDownURL = readConfigItem("wapDownURL");
        portalURL = readConfigItem("portalURL");

        try
        {
            ArrayValue[] ruleVale = webConfig.getArrayItem("servPasswordRules")
                                             .getArrayValues();
            for (int i = 0; i < ruleVale.length; i++)
            {
                myServPWRules.put(ruleVale[i].getReserved(),
                                  ruleVale[i].getValue());
            }
        }
        catch (Exception ex)
        {
            logger.error("servPasswordRules ���ó���.");
        }

        try
        {
            ArrayValue[] ruleVale = webConfig.getArrayItem("ad")
                                             .getArrayValues();
            for (int i = 0; i < ruleVale.length; i++)
            {
                ad.put(ruleVale[i].getReserved(), ruleVale[i].getValue());
            }
        }
        catch (Exception ex)
        {
            logger.error("ad ���ó���.");
        }
    }

    private WebConfig()
    {

        try
        {
            systemConfig = ConfigFactory.getSystemConfig();
            webConfig = ConfigFactory.getSystemConfig()
                                     .getModuleConfig("WebPortal");
            systemConfig.addConfigDataListener(this);
            doConfigRefresh();

        }
        catch (Exception ex)
        {
            logger.error("WebPortal load error!!", ex);
        }

    }

    public static WebConfig getInstants()
    {

        synchronized (WebConfig.class)
        {
            if (wwwPortalConfig == null)
            {
                wwwPortalConfig = new WebConfig();
            }
        }
        return wwwPortalConfig;
    }

    public String readConfigItem(String itemName)
    {

        try
        {
            String strItemValue = webConfig.getItemValue(itemName);
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

    public int readConfigIntValue(String itemName, int defaultValue)
    {

        int iItemValue = defaultValue;
        try
        {
            String strItemValue = webConfig.getItemValue(itemName);
            iItemValue = Integer.parseInt(strItemValue);
            if (logger.isDebugEnabled())
            {
                logger.debug("Get " + itemName + " from CFG: " + iItemValue);
            }

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

        return "WebPortal_Config";
    }

    public String getMomasUrl()
    {

        return momasUrl;
    }

    public String getWapDownURL()
    {

        return wapDownURL;
    }

    public String getPreviewServerConfig()
    {

        return previewServerConfig;
    }
    
    public String getSqlfileServerConfig(){
        return sqlfileServerConfig;
    }

    public String getDIFFEROTAURL()
    {

        return DIFFEROTAURL;
    }

    public String getDIFFERNotifyURL()
    {

        return DIFFERNotifyURL;
    }

    
    public String getHTCNotifyURL()
    {
    
        return HTCNotifyURL;
    }
}
