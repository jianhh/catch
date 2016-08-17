/*
 * @(#)MultiItemConfig.java        1.2 06/08/04
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
import java.util.*;
import org.jdom.*;
import com.common.config.helper.ConfigUtil;

/**
 * �µĶ�ֵ������ģ��,��Ӧ�����ļ���ArrayConfigItem�ڵ������
 * @author yanfeng
 * @version 1.4.2.0
 */

public class MultiItemConfig extends ItemConfig
{

    /**
     *��ֵ���б�
     */
    private ArrayList arrValue;
    public MultiItemConfig()
    {
        arrValue=new ArrayList();
    }
    public ArrayList getArrValue()
    {
        return arrValue;
    }
    public void resetArrValue()
    {
        arrValue=null;
        arrValue=new ArrayList();
    }
    /**
     * ����һ��ֵ
     * @param av ArrayValue
     */
    public void addValue(ArrayValue av)
    {
        if (av!=null)
        {
            arrValue.add(av);
        }
    }
    /**
     * ��ö�ֵ����
     * @return String[]
     */
    public String[] getValues()
    {
        int size=arrValue.size();
        String[] values=new String[size];
        for (int i = 0; i < size; i++)
        {
            ArrayValue av=(ArrayValue)arrValue.get(i);
            values[i]=av.getValue();
        }
        return values;
    }
    /**
     * ��Jdom��element�м�����������
     * @param ele Element
     */
    public void loadFromElement(Element ele)
    {
        //�п����ظ����ô˷���
        arrValue=null;
        ArrayList tmpArrValue=new ArrayList();
        super.loadFromElement(ele);
        List listItems = ele.getChildren(CONFIG_ARRAYITEM_VALUENAME);
        int size = listItems.size();
        for (int i = 0; i < size; i++)
        { //ȡÿ��ArrayValue
            Element eleValue = (Element) listItems.get(i);
            ArrayValue av = new ArrayValue();
            av.loadFromElement(eleValue);
            tmpArrValue.add(av);
        }
        //���������л�
        arrValue=tmpArrValue;
    }
    /**
     * ���ر�ʾ���ڵ����ݵ��ַ��������ڱ����ļ��������ӡ����
     * @return String
     * @param indent String
     */
    public String toString(String indent)
    {
        StringBuffer sb=new StringBuffer();
        sb.append(super.toString(indent+indent));
        int size=arrValue.size();
        for(int i=0;i<size;i++)
        {
            ArrayValue av=(ArrayValue)arrValue.get(i);
            sb.append(av.toString(indent+LINE_INDENT));
        }
        ArrayList attrs=this.getAttrConfigs();
        return ConfigUtil.getXMLNode(CONFIG_MULTIITEM_NODENAME,attrs,sb.toString(),indent);
    }
    public String toString()
    {
        return toString("");
    }

}
