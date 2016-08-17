/*
 * @(#)SystemConfig.java        1.2 06/08/04
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.common.config.helper.ConfigDataListener;
import com.common.config.helper.ConfigUtil;
import com.common.config.helper.Constant;
import com.common.config.helper.DuplicateNameException;


/**
 * 新的系统配置模型,对应配置文件中XMLSystemConfig节点的内容
 * @author yanfeng
 * @version 1.4.2.0
 *
 * @CheckItem OPT-huangbigui-20061121 为加载配置文件时出错记录一个hashtable：htErrXMLFile，以便PortalConfig.Server可以通知用户
 */

public class GeneralConfig implements Constant
{

    /**
     * 所有的配置模块.key=模块名，value=模块对象，ModuleConfig
     */
    private HashMap modules;/**
     * 配置项ModuleConfig的列表,单独保存的意义在于list保证顺序和加入时一致
     * 而modules作为HashMap无法保证这点
     */
    private ArrayList arrModules;

    /**
     * 全局的配置总代理
     */
    private static GeneralConfig myInstance;
    /**
     * 是否是单配置文件形式
     */
    private boolean singleFile;
    /**
     * 完整的总配置文件路径
     */
    private String fullPath;
    /**
     * 侦听器listener列表
     */
    private ArrayList arrListener;

    private Hashtable htErrXMLFile;
    /**
     * 为了让PortalConfig Server也能使用它，改构造方法为public,这样可以同时
     * 维护多个系统的配置
     */
    public GeneralConfig()
    {//缺省是单文件形式
        singleFile=true;
        modules=new HashMap();
        arrModules=new ArrayList();
        arrListener=new ArrayList();
        htErrXMLFile = new Hashtable();
    }
    /**
     * 获得全局的配置代理实例
     * @return ConfigProxy
     */
    public static GeneralConfig getInstance()
    {
        if (myInstance==null)
        {
            myInstance=new GeneralConfig();
        }
        return myInstance;
    }
    /**
     * 加入侦听器
     * @param listener ConfigDataListener
     */
    @SuppressWarnings("unchecked")
    public void addConfigDataListener(ConfigDataListener listener)
     {
         if (!arrListener.contains(listener))
         {//不重复加入
             arrListener.add(listener);
         }
     }
     /**
      * 通知侦听器刷新
      */
     public void notifyListeners()
     {
         for(int i=0;i<arrListener.size();i++)
         {
             ConfigDataListener listener =(ConfigDataListener)arrListener.get(i);
             listener.doConfigRefresh();
         }
     }
    /**
     * 根据模块名获得模块对象
     * @param name String
     * @return ModuleConfig
     */
    public ModuleConfig getModuleByName(String name)
    {
        return (ModuleConfig)modules.get(name);
    }
    /**
     * 返回所有的配置模块
     * @return ArrayList
     */
    public ArrayList getModules()
    {
        return arrModules;
    }
    /**
     * 从记录的文件路径加载内容
     * @throws Exception
     * @return boolean
     */
    public boolean loadFromDefaultFile()
    throws Exception
    {
        File file = new File(fullPath);
        return loadFromFile(file);
    }
    /**
     * 直接取出模块的名字，为重新加载配置
     * @param ele Element
     * @return String
     */
    private String parseModuleName(Element ele)
    {
        String name=null;
        if (ele!=null)
        {
            name = ele.getAttribute(CONFIG_MODULE_ATTR_NAME).getValue();
        }
        return name;
    }
    /**
     *从文件加载配置信息,需要兼容两种格式,在新格式下需要调用ModuleConfig的loadFromFile方法
     * @param confFile File
     * @return boolean 加载是否成功
     * @throws Exception
     */
    public boolean loadFromFile(File confFile)
    throws Exception
    {
        boolean loadOK=true;
        htErrXMLFile = new Hashtable();
        //ArrayList tmpArrModules=new ArrayList();
        //HashMap tmpModules=new HashMap();
        //arrModules=null;
        //arrModules=new ArrayList();
        SAXBuilder builder = new SAXBuilder();
        FileInputStream fis = new FileInputStream(confFile);
        Document sysDocument = builder.build(fis);
        //对应文件中的XMLSystemConfig
        Element sysElement = sysDocument.getRootElement();
        List list = sysElement.getChildren(CONFIG_MODULE_NODENAME);
        int size=list.size();
        for(int i=0;i<size;i++)
        {
            Element eleModule=(Element)list.get(i);
            //先得到模块名
            String name=parseModuleName(eleModule);
            if (name==null)
            {
                loadOK=false;
                continue;
            }
            //此处为了兼容以前使用者缓存ModuleConfig的情况
            //modified by yanfeng at 20061026
            ModuleConfig mc=(ModuleConfig)modules.get(name);
            //是否是新的模块
            boolean newModule=false;
            if (mc==null)
            {//是新模块
                newModule=true;
                mc=new ModuleConfig();
            }
            mc.setModuleElement(eleModule);
            Attribute attrRef=eleModule.getAttribute(CONFIG_MODULE_ATTR_REF);
            if (attrRef!=null)
            {//ModuleConfig有Ref属性，则为多文件形式
                singleFile=false;
                String relativeFile=attrRef.getValue();
                File moduleFile=ConfigUtil.getAbsoluteFile(confFile,relativeFile);
                try
                {
                    if (mc!=null)
                    {
                        mc.loadFromFile(moduleFile, eleModule);
                    }

                }
                catch (Exception ex)
                {//某个模块加载失败，继续下个模块的加载
                    loadOK=false;
                    String errorMsg = "导入子配置文件错误！文件名：" + moduleFile.getName() + "，异常：" + ex.getMessage();
                    htErrXMLFile.put(moduleFile.getName(), errorMsg);
                }
            }
            else
            {//单文件格式
                singleFile=true;
                try
                {
                    mc.loadFromElement(eleModule);
                }
                catch (Exception ex)
                {//某个模块加载失败，继续下个模块的加载
                    ex.printStackTrace(System.out);
                    loadOK=false;
                }
            }
            if (newModule)
            {//是新的模块，则重新加入
                modules.put(name, mc);
                arrModules.add(mc);
            }
        }
        fis.close();
        //加载完再切换
        //modules=tmpModules;
        //arrModules=tmpArrModules;
        //加载后通知更新
        notifyListeners();
        return loadOK;
    }

    /**
     * 保存本身文件
     * @param confFile File
     * @throws Exception
     */
    private void saveLocalFile(File confFile)throws Exception
    {
        StringBuffer strFile = new StringBuffer();
        String content = toString();
        strFile.append(FILE_HEAD); //头:<?xml version="1.0" encoding="GB2312"?>
        strFile.append(LINE_SEP); //换行
        //保存时需要再把&替换回&amp;
        strFile.append(ConfigUtil.replaceToken(content,AND_TOKEN,AND_TOKEN_FULL)); //内容
        FileOutputStream fos = new FileOutputStream(confFile);
        fos.write(strFile.toString().getBytes(FILE_ENCODING));
        fos.close();
    }
    /**
     * 保存内存模型数据到文件
     * @param confFile File
     * @throws Exception
     */
    public void save2File(File confFile)throws Exception
    {
        saveLocalFile(confFile);
        if (!singleFile)
        {//多文件新格式
            for(int i=0;i<arrModules.size();i++)
            { //每个配置模块分别保存
                ModuleConfig mc = (ModuleConfig) arrModules.get(i);
                String relativeFile=mc.getRef();
                File moduleFile=ConfigUtil.getAbsoluteFile(confFile,relativeFile);
                mc.save2File(moduleFile);
            }
        }
        //保存成功后通知更新
        notifyListeners();
    }
    /**
     * 保存配置内容到缺省的文件里
     * @throws Exception
     */
    public void save2DefaultFile()
        throws Exception
    {
        File confFile=new File(fullPath);
        save2File(confFile);
    }
    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        int size=arrModules.size();
        for(int i=0;i<size;i++)
        {//加上每个配置模块内容
            ModuleConfig mc = (ModuleConfig) arrModules.get(i);
            if (singleFile)
            { //单文件老格式
                sb.append(mc.toString(LINE_INDENT));
            }
            else
            {//多文件新格式
                sb.append(mc.getModuleInfoString(LINE_INDENT));
            }
        }
        return ConfigUtil.getXMLNode(CONFIG_GENERAL_NODENAME,null,sb.toString(),"");
    }
    /**
     * 判断配置文件是否是单文件格式
     * @return boolean
     */
    public boolean isSingleFile()
    {
        return singleFile;
    }
    /**
     * 增加一个配置模块,如有重名则抛出DuplicateNameException
     * @param module ModuleConfig
     * @throws Exception
     */
    public void addModule(ModuleConfig module)
    throws Exception
    {
        if (module!=null)
        {
            String name=module.getName();
            if (modules.containsKey(name))
            {
                throw new DuplicateNameException();
            }
            modules.put(name,module);
            arrModules.add(module);
        }
    }
    /**
     * 去掉一个配置模块
     * @param name String
     */
    public void removeModule(String name)
    {
        if (name!=null)
        {
            Object obj=modules.remove(name);
            if (obj!=null)
            {
                arrModules.remove(obj);
            }
        }
    }
    public String getFullPath()
    {
        return fullPath;
    }
    public void setFullPath(String fullPath)
    {
        this.fullPath = fullPath;
    }
    /**
     * @return Returns the htErrXMLFile.
     */
    public Hashtable getHtErrXMLFile() {
        return htErrXMLFile;
    }
}
