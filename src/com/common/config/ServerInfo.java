
package com.common.config;

public class ServerInfo
{

    public static String FS = System.getProperty("file.separator");

    public static String hostName = null;

    public static String localIP = null;

    public static String serverName = null;

    public static String systemName = null;

    public static String siteName = null;

    public static int serverPort;

    public static String appRootPath = null;

    private ServerInfo()
    {

    }

    public static String getSiteName()
    {

        return siteName;
    }

    public static void setSiteName(String siteName)
    {

        ServerInfo.siteName = siteName;
    }

    public static String getAppRootPath()
    {

        return appRootPath;
    }

    public static void setAppRootPath(String appRootPath)
    {

        ServerInfo.appRootPath = appRootPath;
    }

    public static String getFS()
    {

        return FS;
    }

    public static void setFS(String fs)
    {

        FS = fs;
    }

    public static String getHostName()
    {

        return hostName;
    }

    public static void setHostName(String hostName)
    {

        ServerInfo.hostName = hostName;
    }

    public static String getLocalIP()
    {

        return localIP;
    }

    /**
     * 适应 DCManager里写的错别字
     * 
     * @return
     */
    public static String getLoaclIP()
    {

        return localIP;
    }

    public static void setLocalIP(String localIP)
    {

        ServerInfo.localIP = localIP;
    }

    public static String getServerName()
    {

        return serverName;
    }

    public static void setServerName(String serverName)
    {

        ServerInfo.serverName = serverName;
    }

    public static int getServerPort()
    {

        return serverPort;
    }

    public static void setServerPort(int serverPort)
    {

        ServerInfo.serverPort = serverPort;
    }

    public static String getSystemName()
    {

        return systemName;
    }

    public static void setSystemName(String systemName)
    {

        ServerInfo.systemName = systemName;
    }

}
