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
 * �µ�ϵͳ����ģ��,��Ӧ�����ļ���XMLSystemConfig�ڵ������
 * @author yanfeng
 * @version 1.4.2.0
 *
 * @CheckItem OPT-huangbigui-20061121 Ϊ���������ļ�ʱ�����¼һ��hashtable��htErrXMLFile���Ա�PortalConfig.Server����֪ͨ�û�
 */

public class GeneralConfig implements Constant
{

    /**
     * ���е�����ģ��.key=ģ������value=ģ�����ModuleConfig
     */
    private HashMap modules;/**
     * ������ModuleConfig���б�,�����������������list��֤˳��ͼ���ʱһ��
     * ��modules��ΪHashMap�޷���֤���
     */
    private ArrayList arrModules;

    /**
     * ȫ�ֵ������ܴ���
     */
    private static GeneralConfig myInstance;
    /**
     * �Ƿ��ǵ������ļ���ʽ
     */
    private boolean singleFile;
    /**
     * �������������ļ�·��
     */
    private String fullPath;
    /**
     * ������listener�б�
     */
    private ArrayList arrListener;

    private Hashtable htErrXMLFile;
    /**
     * Ϊ����PortalConfig ServerҲ��ʹ�������Ĺ��췽��Ϊpublic,��������ͬʱ
     * ά�����ϵͳ������
     */
    public GeneralConfig()
    {//ȱʡ�ǵ��ļ���ʽ
        singleFile=true;
        modules=new HashMap();
        arrModules=new ArrayList();
        arrListener=new ArrayList();
        htErrXMLFile = new Hashtable();
    }
    /**
     * ���ȫ�ֵ����ô���ʵ��
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
     * ����������
     * @param listener ConfigDataListener
     */
    @SuppressWarnings("unchecked")
    public void addConfigDataListener(ConfigDataListener listener)
     {
         if (!arrListener.contains(listener))
         {//���ظ�����
             arrListener.add(listener);
         }
     }
     /**
      * ֪ͨ������ˢ��
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
     * ����ģ�������ģ�����
     * @param name String
     * @return ModuleConfig
     */
    public ModuleConfig getModuleByName(String name)
    {
        return (ModuleConfig)modules.get(name);
    }
    /**
     * �������е�����ģ��
     * @return ArrayList
     */
    public ArrayList getModules()
    {
        return arrModules;
    }
    /**
     * �Ӽ�¼���ļ�·����������
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
     * ֱ��ȡ��ģ������֣�Ϊ���¼�������
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
     *���ļ�����������Ϣ,��Ҫ�������ָ�ʽ,���¸�ʽ����Ҫ����ModuleConfig��loadFromFile����
     * @param confFile File
     * @return boolean �����Ƿ�ɹ�
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
        //��Ӧ�ļ��е�XMLSystemConfig
        Element sysElement = sysDocument.getRootElement();
        List list = sysElement.getChildren(CONFIG_MODULE_NODENAME);
        int size=list.size();
        for(int i=0;i<size;i++)
        {
            Element eleModule=(Element)list.get(i);
            //�ȵõ�ģ����
            String name=parseModuleName(eleModule);
            if (name==null)
            {
                loadOK=false;
                continue;
            }
            //�˴�Ϊ�˼�����ǰʹ���߻���ModuleConfig�����
            //modified by yanfeng at 20061026
            ModuleConfig mc=(ModuleConfig)modules.get(name);
            //�Ƿ����µ�ģ��
            boolean newModule=false;
            if (mc==null)
            {//����ģ��
                newModule=true;
                mc=new ModuleConfig();
            }
            mc.setModuleElement(eleModule);
            Attribute attrRef=eleModule.getAttribute(CONFIG_MODULE_ATTR_REF);
            if (attrRef!=null)
            {//ModuleConfig��Ref���ԣ���Ϊ���ļ���ʽ
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
                {//ĳ��ģ�����ʧ�ܣ������¸�ģ��ļ���
                    loadOK=false;
                    String errorMsg = "�����������ļ������ļ�����" + moduleFile.getName() + "���쳣��" + ex.getMessage();
                    htErrXMLFile.put(moduleFile.getName(), errorMsg);
                }
            }
            else
            {//���ļ���ʽ
                singleFile=true;
                try
                {
                    mc.loadFromElement(eleModule);
                }
                catch (Exception ex)
                {//ĳ��ģ�����ʧ�ܣ������¸�ģ��ļ���
                    ex.printStackTrace(System.out);
                    loadOK=false;
                }
            }
            if (newModule)
            {//���µ�ģ�飬�����¼���
                modules.put(name, mc);
                arrModules.add(mc);
            }
        }
        fis.close();
        //���������л�
        //modules=tmpModules;
        //arrModules=tmpArrModules;
        //���غ�֪ͨ����
        notifyListeners();
        return loadOK;
    }

    /**
     * ���汾���ļ�
     * @param confFile File
     * @throws Exception
     */
    private void saveLocalFile(File confFile)throws Exception
    {
        StringBuffer strFile = new StringBuffer();
        String content = toString();
        strFile.append(FILE_HEAD); //ͷ:<?xml version="1.0" encoding="GB2312"?>
        strFile.append(LINE_SEP); //����
        //����ʱ��Ҫ�ٰ�&�滻��&amp;
        strFile.append(ConfigUtil.replaceToken(content,AND_TOKEN,AND_TOKEN_FULL)); //����
        FileOutputStream fos = new FileOutputStream(confFile);
        fos.write(strFile.toString().getBytes(FILE_ENCODING));
        fos.close();
    }
    /**
     * �����ڴ�ģ�����ݵ��ļ�
     * @param confFile File
     * @throws Exception
     */
    public void save2File(File confFile)throws Exception
    {
        saveLocalFile(confFile);
        if (!singleFile)
        {//���ļ��¸�ʽ
            for(int i=0;i<arrModules.size();i++)
            { //ÿ������ģ��ֱ𱣴�
                ModuleConfig mc = (ModuleConfig) arrModules.get(i);
                String relativeFile=mc.getRef();
                File moduleFile=ConfigUtil.getAbsoluteFile(confFile,relativeFile);
                mc.save2File(moduleFile);
            }
        }
        //����ɹ���֪ͨ����
        notifyListeners();
    }
    /**
     * �����������ݵ�ȱʡ���ļ���
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
        {//����ÿ������ģ������
            ModuleConfig mc = (ModuleConfig) arrModules.get(i);
            if (singleFile)
            { //���ļ��ϸ�ʽ
                sb.append(mc.toString(LINE_INDENT));
            }
            else
            {//���ļ��¸�ʽ
                sb.append(mc.getModuleInfoString(LINE_INDENT));
            }
        }
        return ConfigUtil.getXMLNode(CONFIG_GENERAL_NODENAME,null,sb.toString(),"");
    }
    /**
     * �ж������ļ��Ƿ��ǵ��ļ���ʽ
     * @return boolean
     */
    public boolean isSingleFile()
    {
        return singleFile;
    }
    /**
     * ����һ������ģ��,�����������׳�DuplicateNameException
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
     * ȥ��һ������ģ��
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
