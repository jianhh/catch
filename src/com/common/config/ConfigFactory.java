package com.common.config;

public class ConfigFactory
{
    private static SystemConfig systemConfig = null;

    private ConfigFactory()
    {
    }

    public static synchronized void init(String configFile)
    {
        systemConfig = new SystemConfig();
        systemConfig.setConfigFile(configFile);
        systemConfig.load();
    }

    public static SystemConfig getSystemConfig()
    {
        return systemConfig;
    }

}
