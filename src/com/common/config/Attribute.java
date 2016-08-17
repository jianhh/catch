package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: Portal OAM Program file</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 增加宏定义功能

public class Attribute
    implements java.io.Serializable
{
    private org.jdom.Attribute attribute = null;
    private String name = null;
    private String value = null;

    private com.common.config.model.AttributeConfig newAttr;
    public Attribute(String name, String value)
    {
        newAttr = new com.common.config.model.AttributeConfig(name, value);
    }
    public Attribute(com.common.config.model.AttributeConfig newAttr)
    {
        this.newAttr = newAttr;
    }

    public String getName()
    {
        return newAttr.getAttrName();
    }

    /**
     * 返回宏替换前的属性值
     * @return
     */
    public String getOrigValue()
    {
        return newAttr.getAttrValue();
    }

    /**
     * 返回宏替换后的属性值
     * @return
     */
    public String getValue()
    {
         return newAttr.getAttrValue();
    }

    public void setValue(String value)
    {
        newAttr.setAttrValue(value);
    }

}
