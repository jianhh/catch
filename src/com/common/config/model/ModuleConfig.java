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
 * 新的模块配置模型,表示配置文件中ModuleConfig中的模型
* 即包含模块内的配置项信息集合也包含模块本身信息：
* 模块本身信息如ID,Desc，如为多文件格式，则来自总文件,模块文件中的模块信息来自总文件
 * @author yanfeng
 * @version 1.4.2.0
 */

public class ModuleConfig implements Constant
{

    /**
     *模块配置中的所有配置项,key=name,value=ItemConfig
     */
    private HashMap items;
    /**
     * 配置项ItemConfig的列表,单独保存的意义在于list保证顺序和加入时一致
     * 而items作为HashMap无法保证这点
     */
    private ArrayList arrItems;
    /**
     * 多文件中配置模块的ID,初始值用于区分是否有赋值
     */
    private int ID=-1;
    /**
     * 配置模块的名字
     */
    private String name;
    /**
     * 配置模块的描述
     */
    private String desc;
    /**
     * 多文件中配置模块的相对路径
     */
    private String ref;
    
    //对应ModuleConfig节点的Element
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
     * 根据配置名取得配置项
     * @param name String
     * @return ItemConfig
     */
    public ItemConfig getItemByName(String name)
    {
        return (ItemConfig)items.get(name);
    }
    /**
     * 根据配置名获得配置值,只适用于单配置文件
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
     * 根据配置名获得配置值,当配置项不存在时，返回参数defaultVal作为缺省值
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
     * 根据配置名获得配置值,当配置项不存在时，返回参数defaultVal作为缺省值
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
     * 根据配置名获得配置值,当配置项不存在时，返回参数defaultVal作为缺省值
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
        }//大小写不敏感
        return "true".equalsIgnoreCase(value);
    }
    /**
     * 加载配置模块的概要信息
     * @param ele Element
     */
    private void loadModuleInfo(Element ele)
    {
        if (ele!=null)
        {
            //ArrayList arrAttrs = ConfigUtil.getAttrs(ele);
            Attribute temp = ele.getAttribute(CONFIG_MODULE_ATTR_ID);
            if (temp != null)
            { //ID，多文件格式中才有。单文件没有
                String strID = temp.getValue();
                ID = Integer.parseInt(strID);
            }
            name = ele.getAttribute(CONFIG_MODULE_ATTR_NAME).getValue();
            desc = ele.getAttribute(CONFIG_MODULE_ATTR_DESC).getValue();
            temp = ele.getAttribute(CONFIG_MODULE_ATTR_REF);
            if (temp != null)
            { //Ref，多文件格式中才有。单文件没有
                ref = temp.getValue();
            }
        }
    }
    /**
     * 把模块的属性值信息包装为AttributeConfig的列表
     * @return ArrayList
     */
    private ArrayList getModuleAttributes()
    {
        ArrayList arrAttrs=new ArrayList();
        if (ID!=-1)
        {//有ID值
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
     * 单文件格式下从总文件加载模块内容和概要信息
     * @param ele Element
     * @throws Exception
     */
    public void loadFromElement(Element ele)
    throws Exception
    {

        loadItemsInModule(ele);
        //最后加载模块本身的信息
        loadModuleInfo(ele);

    }
    /**
     *当模块配置为单独文件表示时，从文件加载
     * @param confFile File
     * @param ele Element,总配置文件模块对应的节点
     * boolean 加载是否成功
     * @throws Exception
     */
    public void loadFromFile(File confFile,Element ele)
    throws Exception
    {

        SAXBuilder builder = new SAXBuilder();
        FileInputStream fis = new FileInputStream(confFile);
        Document sysDocument = builder.build(fis);
        //对应文件中的XMLSystemConfig
        Element sysElement = sysDocument.getRootElement();
        Element eleModule = sysElement.getChild(CONFIG_MODULE_NODENAME);
        loadItemsInModule(eleModule);
        //最后加载模块本身的信息
        loadModuleInfo(ele);
        fis.close();
    }

    /**
     * 返回模块本身信息，用于新格式下总配置文件的内容
     * @param indent String
     * @return String
     */
    public String getModuleInfoString(String indent)
    {
        //没有值，只有模块本身信息
        String strModule=ConfigUtil.getXMLNode(CONFIG_MODULE_NODENAME,getModuleAttributes(),null,indent);
        return strModule;
    }
    /**
     * 返回本身内容的string，用于保存为文件
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
     * 把本模块的内容保存在文件中,适用于新格式
     * @param confFile File
     * @throws Exception
     */
    public void save2File(File confFile)
    throws Exception
    {
        //先获取模块内容
        //保存时需要再把&替换回&amp;
        String strModule=toString("");
        String replaced=ConfigUtil.replaceToken(strModule,AND_TOKEN,AND_TOKEN_FULL);
        String strXML=ConfigUtil.getXMLNode(CONFIG_GENERAL_NODENAME,null,replaced,"");
        StringBuffer strFile=new StringBuffer();
        strFile.append(FILE_HEAD);//头:<?xml version="1.0" encoding="GB2312"?>
        strFile.append(LINE_SEP);//换行
        strFile.append(strXML);//内容
        FileOutputStream fos=new FileOutputStream(confFile);
        fos.write(strFile.toString().getBytes(FILE_ENCODING));
        fos.close();
    }
    /**
     * 当模块配置为总文件表示时，从节点加载
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
        {//取每个ConfigItem或ArrayConfigItem
            Element eleItem=(Element)listItems.get(i);
            ItemConfig ic=null;
            String eleName=eleItem.getName();
            if (eleName.equals(CONFIG_SINGLEITEM_NODENAME))
            {//单值配置
                ic=new SingleItemConfig();

            }
            else if (eleName.equals(CONFIG_MULTIITEM_NODENAME))
            {//多值配置
                ic=new MultiItemConfig();
            }
            else
            {//不认识的Item节点名，跳过
                continue;
            }
            //加载配置项内容,注意每个配置项的加载方法都捕获错误
            //即某个配置项的加载错误不影响下个配置项的加载
            ic.loadFromElement(eleItem);
            //把配置项加入缓存，key=配置名,value=配置对象
            String itemName = ic.getName();
            //log.debug("add item:"+itemName);
            tmpItems.put(itemName, ic);
            tmpArrItems.add(ic);
            //log.debug("item:"+ic);
        }
        //加载完后再切换
        items=tmpItems;
        arrItems=tmpArrItems;
    }
    /**
     * 返回所有的配置项
     * @return ArrayList
     */
    public ArrayList getItems()
    {
        return arrItems;
    }
    /**
     * 去掉一个配置项
     * @param name String
     */
    public void removeItem(String name)
    {
        if (name!=null)
        {
            Object item = items.remove(name);
            if (item != null)
            { //存在对应的配置项
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
     * 模型中加入一个配置项
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
            {//当前模块下重名，抛出异常
                throw new DuplicateNameException();
            }
            items.put(name,itemConfig);
            arrItems.add(itemConfig);
        }
    }
    /**
     * 删除配置项
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
