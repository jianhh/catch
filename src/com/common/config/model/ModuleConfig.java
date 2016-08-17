/*
 * @(#)ModuleConfig.java        1.2 06/08/04
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
import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import com.common.config.helper.Constant;
import com.common.config.helper.DuplicateNameException;
import com.common.config.helper.ConfigUtil;

/**
 * �µ�ģ������ģ��,��ʾ�����ļ���ModuleConfig�е�ģ��
* ������ģ���ڵ���������Ϣ����Ҳ����ģ�鱾����Ϣ��
* ģ�鱾����Ϣ��ID,Desc����Ϊ���ļ���ʽ�����������ļ�,ģ���ļ��е�ģ����Ϣ�������ļ�
 * @author yanfeng
 * @version 1.4.2.0
 */

public class ModuleConfig implements Constant
{

    /**
     *ģ�������е�����������,key=name,value=ItemConfig
     */
    private HashMap items;
    /**
     * ������ItemConfig���б�,�����������������list��֤˳��ͼ���ʱһ��
     * ��items��ΪHashMap�޷���֤���
     */
    private ArrayList arrItems;
    /**
     * ���ļ�������ģ���ID,��ʼֵ���������Ƿ��и�ֵ
     */
    private int ID=-1;
    /**
     * ����ģ�������
     */
    private String name;
    /**
     * ����ģ�������
     */
    private String desc;
    /**
     * ���ļ�������ģ������·��
     */
    private String ref;
    
    //��ӦModuleConfig�ڵ��Element
    private Element moduleElement = null;
        
    public Element getModuleElement()
    { 
        return moduleElement;
    }
    
    public void setModuleElement(Element moduleElement)
    {
        this.moduleElement = moduleElement;
    }
    public ModuleConfig()
    {
        items=new HashMap();
        arrItems=new ArrayList();

    }
    /**
     * ����������ȡ��������
     * @param name String
     * @return ItemConfig
     */
    public ItemConfig getItemByName(String name)
    {
        return (ItemConfig)items.get(name);
    }
    /**
     * �����������������ֵ,ֻ�����ڵ������ļ�
     * @param name String
     * @return String
     */
    public String getItemValueByName(String name)
    {
        ItemConfig item=getItemByName(name);
        if (item!=null)
        {
            if (item instanceof SingleItemConfig)
            {
                SingleItemConfig myItem=(SingleItemConfig)item;
                return myItem.getValue();
            }
        }
        return null;
    }
    /**
     * �����������������ֵ,�����������ʱ�����ز���defaultVal��Ϊȱʡֵ
     * @param name String
     * @param defaultVal String
     * @return String
     */
    public String getItemValueByName(String name,String defaultVal)
    {
        String value=getItemValueByName(name);
        if (value==null)
        {
            return defaultVal;
        }
        return value;
    }
    /**
     * �����������������ֵ,�����������ʱ�����ز���defaultVal��Ϊȱʡֵ
     * @param name String
     * @param defaultVal String
     * @return String
     */
    public int getItemValueByName(String name, int defaultVal)
    {
        String value=getItemValueByName(name);
        if (value==null)
        {
            return defaultVal;
        }
        return Integer.parseInt(value);
    }
    /**
     * �����������������ֵ,�����������ʱ�����ز���defaultVal��Ϊȱʡֵ
     * @param name String
     * @param defaultVal String
     * @return String
     */
    public boolean getItemValueByName(String name, boolean defaultVal)
    {
        String value=getItemValueByName(name);
        if (value==null)
        {
            return defaultVal;
        }//��Сд������
        return "true".equalsIgnoreCase(value);
    }
    /**
     * ��������ģ��ĸ�Ҫ��Ϣ
     * @param ele Element
     */
    private void loadModuleInfo(Element ele)
    {
        if (ele!=null)
        {
            //ArrayList arrAttrs = ConfigUtil.getAttrs(ele);
            Attribute temp = ele.getAttribute(CONFIG_MODULE_ATTR_ID);
            if (temp != null)
            { //ID�����ļ���ʽ�в��С����ļ�û��
                String strID = temp.getValue();
                ID = Integer.parseInt(strID);
            }
            name = ele.getAttribute(CONFIG_MODULE_ATTR_NAME).getValue();
            desc = ele.getAttribute(CONFIG_MODULE_ATTR_DESC).getValue();
            temp = ele.getAttribute(CONFIG_MODULE_ATTR_REF);
            if (temp != null)
            { //Ref�����ļ���ʽ�в��С����ļ�û��
                ref = temp.getValue();
            }
        }
    }
    /**
     * ��ģ�������ֵ��Ϣ��װΪAttributeConfig���б�
     * @return ArrayList
     */
    private ArrayList getModuleAttributes()
    {
        ArrayList arrAttrs=new ArrayList();
        if (ID!=-1)
        {//��IDֵ
            arrAttrs.add(new AttributeConfig(CONFIG_MODULE_ATTR_ID,String.valueOf(ID)));
        }
        arrAttrs.add(new AttributeConfig(CONFIG_MODULE_ATTR_NAME,name));
        arrAttrs.add(new AttributeConfig(CONFIG_MODULE_ATTR_DESC,desc));
        if (ref!=null)
        {
            arrAttrs.add(new AttributeConfig(CONFIG_MODULE_ATTR_REF,ref));
        }
        return arrAttrs;
    }

    /**
     * ���ļ���ʽ�´����ļ�����ģ�����ݺ͸�Ҫ��Ϣ
     * @param ele Element
     * @throws Exception
     */
    public void loadFromElement(Element ele)
    throws Exception
    {

        loadItemsInModule(ele);
        //������ģ�鱾�����Ϣ
        loadModuleInfo(ele);

    }
    /**
     *��ģ������Ϊ�����ļ���ʾʱ�����ļ�����
     * @param confFile File
     * @param ele Element,�������ļ�ģ���Ӧ�Ľڵ�
     * boolean �����Ƿ�ɹ�
     * @throws Exception
     */
    public void loadFromFile(File confFile,Element ele)
    throws Exception
    {

        SAXBuilder builder = new SAXBuilder();
        FileInputStream fis = new FileInputStream(confFile);
        Document sysDocument = builder.build(fis);
        //��Ӧ�ļ��е�XMLSystemConfig
        Element sysElement = sysDocument.getRootElement();
        Element eleModule = sysElement.getChild(CONFIG_MODULE_NODENAME);
        loadItemsInModule(eleModule);
        //������ģ�鱾�����Ϣ
        loadModuleInfo(ele);
        fis.close();
    }

    /**
     * ����ģ�鱾����Ϣ�������¸�ʽ���������ļ�������
     * @param indent String
     * @return String
     */
    public String getModuleInfoString(String indent)
    {
        //û��ֵ��ֻ��ģ�鱾����Ϣ
        String strModule=ConfigUtil.getXMLNode(CONFIG_MODULE_NODENAME,getModuleAttributes(),null,indent);
        return strModule;
    }
    /**
     * ���ر������ݵ�string�����ڱ���Ϊ�ļ�
     * @param indent String
     * @return String
     */
    public String toString(String indent)
    {
        int size=arrItems.size();
//        log.debug("size:"+size);
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<size;i++)
        {

            ItemConfig item=(ItemConfig)arrItems.get(i);
            //log.debug("ItemConfig:"+item);
            sb.append(item.toString(indent+LINE_INDENT));
        }
        String strModule=ConfigUtil.getXMLNode(CONFIG_MODULE_NODENAME,getModuleAttributes(),sb.toString(),indent);
        return strModule;
    }
    /**
     * �ѱ�ģ������ݱ������ļ���,�������¸�ʽ
     * @param confFile File
     * @throws Exception
     */
    public void save2File(File confFile)
    throws Exception
    {
        //�Ȼ�ȡģ������
        //����ʱ��Ҫ�ٰ�&�滻��&amp;
        String strModule=toString("");
        String replaced=ConfigUtil.replaceToken(strModule,AND_TOKEN,AND_TOKEN_FULL);
        String strXML=ConfigUtil.getXMLNode(CONFIG_GENERAL_NODENAME,null,replaced,"");
        StringBuffer strFile=new StringBuffer();
        strFile.append(FILE_HEAD);//ͷ:<?xml version="1.0" encoding="GB2312"?>
        strFile.append(LINE_SEP);//����
        strFile.append(strXML);//����
        FileOutputStream fos=new FileOutputStream(confFile);
        fos.write(strFile.toString().getBytes(FILE_ENCODING));
        fos.close();
    }
    /**
     * ��ģ������Ϊ���ļ���ʾʱ���ӽڵ����
     * @param eleModule Element
     * @throws Exception
     */
    private void loadItemsInModule(Element eleModule)
    throws Exception
    {
        ArrayList tmpArrItems=new ArrayList();
        HashMap tmpItems=new HashMap();
        List listItems=eleModule.getChildren();
        int size=listItems.size();
        for(int i=0;i<size;i++)
        {//ȡÿ��ConfigItem��ArrayConfigItem
            Element eleItem=(Element)listItems.get(i);
            ItemConfig ic=null;
            String eleName=eleItem.getName();
            if (eleName.equals(CONFIG_SINGLEITEM_NODENAME))
            {//��ֵ����
                ic=new SingleItemConfig();

            }
            else if (eleName.equals(CONFIG_MULTIITEM_NODENAME))
            {//��ֵ����
                ic=new MultiItemConfig();
            }
            else
            {//����ʶ��Item�ڵ���������
                continue;
            }
            //��������������,ע��ÿ��������ļ��ط������������
            //��ĳ��������ļ��ش���Ӱ���¸�������ļ���
            ic.loadFromElement(eleItem);
            //����������뻺�棬key=������,value=���ö���
            String itemName = ic.getName();
            //log.debug("add item:"+itemName);
            tmpItems.put(itemName, ic);
            tmpArrItems.add(ic);
            //log.debug("item:"+ic);
        }
        //����������л�
        items=tmpItems;
        arrItems=tmpArrItems;
    }
    /**
     * �������е�������
     * @return ArrayList
     */
    public ArrayList getItems()
    {
        return arrItems;
    }
    /**
     * ȥ��һ��������
     * @param name String
     */
    public void removeItem(String name)
    {
        if (name!=null)
        {
            Object item = items.remove(name);
            if (item != null)
            { //���ڶ�Ӧ��������
                arrItems.remove(item);
            }
        }
    }
    public int getID()
    {
        return ID;
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
    public String getRef()
    {
        return ref;
    }
    public void setRef(String ref)
    {
        this.ref = ref;
    }
    public void setID(int ID)
    {
        this.ID = ID;
    }
    /**
     * ģ���м���һ��������
     * @param itemConfig ItemConfig
     * @throws DuplicateNameException
     */
    public void addItem(ItemConfig itemConfig)
    throws DuplicateNameException
    {
        if (itemConfig!=null)
        {
            String name=itemConfig.getName();
            if (items.containsKey(name))
            {//��ǰģ�����������׳��쳣
                throw new DuplicateNameException();
            }
            items.put(name,itemConfig);
            arrItems.add(itemConfig);
        }
    }
    /**
     * ɾ��������
     * @param name String
     */
    public void deleteItem(String name)
    {
       if (name!=null)
       {
           Object obj=items.remove(name);
           if (obj!=null)
           {
               arrItems.remove(obj);
           }
       }
    }
}
