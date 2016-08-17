
package com.framework.dc.vo;

import java.io.Serializable;

public class ConfigVO implements Serializable
{

    private static final long serialVersionUID = 201033331L;

    /**
     * 站点名称
     */
    private String siteName = "";

    /**
     * 类型名称
     */
    private String typeName = "";

    /**
     * 配置项
     */
    private String configName = "";

    /**
     * 配置项值
     */
    private String configValue = "";

    /**
     * 配置项描述
     */
    private String descs = "";

    /**
     * 最后修改时间
     */
    private String lupdDate = "";

    /**
     * 最后修改人
     */
    private String lupdUser = "";

    public String getSiteName()
    {

        return siteName;
    }

    public void setSiteName(String siteName)
    {

        this.siteName = siteName;
    }

    public String getTypeName()
    {

        return typeName;
    }

    public void setTypeName(String typeName)
    {

        this.typeName = typeName;
    }

    public String getConfigName()
    {

        return configName;
    }

    public void setConfigName(String configName)
    {

        this.configName = configName;
    }

    public String getConfigValue()
    {

        return configValue;
    }

    public void setConfigValue(String configValue)
    {

        this.configValue = configValue;
    }

    public String getDescs()
    {

        return descs;
    }

    public void setDescs(String descs)
    {

        this.descs = descs;
    }

    public String getLupdDate()
    {

        return lupdDate;
    }

    public void setLupdDate(String lupdDate)
    {

        this.lupdDate = lupdDate;
    }

    public String getLupdUser()
    {

        return lupdUser;
    }

    public void setLupdUser(String lupdUser)
    {

        this.lupdUser = lupdUser;
    }

}
