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
 * ������Ĺ���ģ��,����ConfigItem��ArrayConfigItem�Ĺ�ͬ����
 * @author yanfeng
 * @version 1.4
 */

public abstract class ItemConfig implements Constant
{

    /**
     * ���
     */
    private int ID;
    /**
     * ������
     */
    private String name;

    /**
     * ��������
     */
    private String desc;

    /**
     * ���ȼ�:0,�ظģ�1���߼���-1:����
     */
    private int priority;
    /**
     * ��Ч���ڣ�0,������1��������-1���¸���������
     */
    private int effectMode;
    /**
     * ����������ԣ���<ArrayConfigItem needHSQL="1" needHash="0" sqlWhereClause="">
     * ��AttributeConfig�б����
     */
    private ArrayList attrConfigs=new ArrayList();

    public ItemConfig()
    {
        //ȱʡû������
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
     * ��Jdom��Element�����������
     * @param ele Element
     */
    public  void loadFromElement(Element ele)
    {
        attrConfigs=ConfigUtil.getAttrs(ele);
        name = ele.getChild(CONFIG_ITEM_NAME).getText();
        Element temp = ele.getChild(CONFIG_ITEM_ID);
        if (temp != null)
        { //ID�����ļ���ʽ��ֻ�е�ֵ�������С���ֵ����û��
            //��ID����ʱ��ȱʡΪ0
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
        { //��Чʱ�䣬ֻ���¸�ʽ����
            effectMode = Integer.parseInt(temp.getText());
        }

        temp = ele.getChild(CONFIG_ITEM_PRIORITY);
        if (temp != null)
        { //���ȼ���ֻ���¸�ʽ����
            priority = Integer.parseInt(temp.getText());
        }
    }
    /**
     * ���ر�ʾ���ڵ����ݵ��ַ��������ڱ����ļ��������ӡ����
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
     * �������б��л�ȡ��Ӧ���ֵ�����ֵ
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
