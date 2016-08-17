/*
 * @(#)ItemConfig.java        1.2 06/08/04
 *
 * Copyright (c) 2003-2006 ASPire Technologies, Inc.
 * 6/F,IER BUILDING, SOUTH AREA,SHENZHEN HI-TECH INDUSTRIAL PARK Mail Box:11# 12#.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ASPire Technologies, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Aspire.
 */
package com.common.config.model;
import org.jdom.*;
import java.util.*;
import com.common.config.helper.Constant;
import com.common.config.helper.ConfigUtil;
/**
 * 配置项的公共模型,包含ConfigItem和ArrayConfigItem的共同特性
 * @author yanfeng
 * @version 1.4
 */

public abstract class ItemConfig implements Constant
{

    /**
     * 序号
     */
    private int ID;
    /**
     * 配置名
     */
    private String name;

    /**
     * 配置描述
     */
    private String desc;

    /**
     * 优先级:0,必改，1：高级；-1:保留
     */
    private int priority;
    /**
     * 生效周期：0,重启，1，立即，-1：下个运行周期
     */
    private int effectMode;
    /**
     * 配置项的属性，如<ArrayConfigItem needHSQL="1" needHash="0" sqlWhereClause="">
     * 由AttributeConfig列表组成
     */
    private ArrayList attrConfigs=new ArrayList();

    public ItemConfig()
    {
        //缺省没有设置
        priority=CONFIG_PRIORITY_UNSET;
        effectMode=CONFIG_EFFECTMODE_UNSET;
    }

    public void addAttribute(String name,String value)
    {
        if ((name!=null)&&!name.equals(""))
        {
            attrConfigs.add(new AttributeConfig(name,value));
        }
    }
    public void removeAttribute(String name)
    {
        if ( (name != null) && !name.equals(""))
        {
            int index=-1;
            for(int i=0;i<attrConfigs.size();i++)
            {
                AttributeConfig ac = (AttributeConfig) attrConfigs.get(i);
                if (ac.getAttrName().equals(name))
                {
                    index=i;
                    break;
                }
            }
            if (index!=-1)
            {
                attrConfigs.remove(index);
            }

        }
    }

    public AttributeConfig getAttribute(String name)
    {
        for(int i=0;i<attrConfigs.size();i++)
        {
            AttributeConfig ac=(AttributeConfig)attrConfigs.get(i);
            if (ac.getAttrName().equals(name))
            {
                return ac;
            }
        }
        return null;
    }
    public int getID()
    {
        return ID;
    }
    public void setID(int ID)
    {
        this.ID = ID;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public int getPriority()
    {
        return priority;
    }
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    public int getEffectMode()
    {
        return effectMode;
    }
    public void setEffectMode(int effectMode)
    {
        this.effectMode = effectMode;
    }
    /**
     * 从Jdom的Element对象加载数据
     * @param ele Element
     */
    public  void loadFromElement(Element ele)
    {
        attrConfigs=ConfigUtil.getAttrs(ele);
        name = ele.getChild(CONFIG_ITEM_NAME).getText();
        Element temp = ele.getChild(CONFIG_ITEM_ID);
        if (temp != null)
        { //ID，单文件格式中只有单值参数才有。多值参数没有
            //当ID错误时，缺省为0
            String strID = ele.getChild(CONFIG_ITEM_ID).getText();
            try
            {
                ID = Integer.parseInt(strID);
            }
            catch (NumberFormatException ex)
            {
                ex.printStackTrace(System.out);
            }
        }

        desc = ele.getChild(CONFIG_ITEM_DESC).getText();

        temp = ele.getChild(CONFIG_ITEM_EFFECTMODE);
        if (temp != null)
        { //生效时间，只有新格式才有
            effectMode = Integer.parseInt(temp.getText());
        }

        temp = ele.getChild(CONFIG_ITEM_PRIORITY);
        if (temp != null)
        { //优先级，只有新格式才有
            priority = Integer.parseInt(temp.getText());
        }
    }
    /**
     * 返回表示本节点内容的字符串，用于保存文件和输出打印测试
     * @param indent String
     * @return String
     */
    public String toString(String indent)
    {
        StringBuffer sb=new StringBuffer();
        sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_ID,ID,indent));
        sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_NAME,name,indent));
        sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_DESC,desc,indent));
        if (effectMode!=CONFIG_EFFECTMODE_UNSET)
        {
            sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_EFFECTMODE,effectMode,indent));
        }
        if (priority!=CONFIG_PRIORITY_UNSET)
        {
            sb.append(ConfigUtil.getXMLLine(CONFIG_ITEM_PRIORITY,priority,indent));
        }
        return sb.toString();
    }
    public String toString()
    {
        return toString("");
    }
    public ArrayList getAttrConfigs()
    {
        return attrConfigs;
    }
    public void resetAttrConfigs()
    {
        attrConfigs=null;
        attrConfigs=new ArrayList();
    }

    /**
     * 从属性列表中获取对应名字的属性值
     * @param attrName String
     * @return String
     */
    public String getAttributeValue(String attrName)
    {
        if (attrConfigs!=null)
        {
            for(int i=0;i<attrConfigs.size();i++)
            {
                AttributeConfig ac=(AttributeConfig)attrConfigs.get(i);
                if (ac.getAttrName().equals(attrName))
                {
                    return ac.getAttrValue();
                }
            }
        }
        return null;
    }

}
