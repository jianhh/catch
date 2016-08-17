/*
 * @(#)ConfigUtil.java        1.2 06/09/07
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
package com.common.config.helper;
import java.util.*;
import java.io.*;
import org.jdom.*;
import com.common.config.model.AttributeConfig;

/**
 * 新的系统配置模型中使用到的辅助方法
 * @author yanfeng
 * @version 1.4.1.004
 */

public class ConfigUtil implements Constant
{
    public ConfigUtil()
    {
    }
    /**
     * 根据名字和值构造XML格式的字符串，如<Name>username</Name>
     * 结尾加上换行符
     * @param name String
     * @param attrs ArrayList
     * @param value String
     * @param indent String
     * @param oneLine boolean
     * @return String
     */
    private static String getXMLString(String name,ArrayList attrs,String value,String indent,
                                      boolean oneLine)
    {
        if ((name==null)||name.trim().equals(""))
        {//没有名字返回空
            return "";
        }
        StringBuffer sb=new StringBuffer();
        sb.append(indent);
        sb.append("<");
        sb.append(name);
        if (attrs!=null)
        {
            for(int i=0;i<attrs.size();i++)
            {//加入属性
                AttributeConfig attr=(AttributeConfig)attrs.get(i);
                sb.append(LINE_INDENT);
                sb.append(attr.getAttrName());
                sb.append("=\"");
                sb.append(attr.getAttrValue());
                sb.append("\"");
            }
        }
        if ((value!=null)&&!value.equals(""))
        {//节点有值
            sb.append(">");
            if (!oneLine)
            { //需要换行
                sb.append(LINE_SEP);
            }
            sb.append(value);
            if (!oneLine)
            { //新行需要缩进
                sb.append(indent);
//            sb.append(LINE_SEP);
            }
            sb.append("</");
            sb.append(name);
            sb.append(">");
        }
        else
        {//节点无值，直接加/
            sb.append(" />");
        }
        sb.append(LINE_SEP);
        return sb.toString();
    }
    /**
     * 获得一行XML节点
     * @param name String
     * @param value int
     * @param indent String
     * @return String
     */
    public static String getXMLLine(String name,int value,String indent)
    {
        return getXMLString(name,null,String.valueOf(value),indent,true);
    }
    /**
     * 获得一行XML节点
     * @param name String
     * @param value String
     * @param indent String
     * @return String
     */
    public static String getXMLLine(String name,String value,String indent)
    {
        return getXMLString(name,null,value,indent,true);
    }

    /**
     * 根据名字和值构造XML格式的一个节点字符串，如
     * <ConfigItem>
            <Name>username</Name>
     * </ConfigItem>
     * @param name String
     * @param attrs ArrayList
     * @param value String
     * @param indent String
     * @return String
     */
    public static String getXMLNode(String name,ArrayList attrs,String value,String indent)
    {
        return getXMLString(name,attrs,value,indent,false);
    }
    /**
     * 获得Element中的属性列表
     * @param ele Element
     * @return ArrayList
     */
    public static ArrayList getAttrs(Element ele)
    {
        ArrayList arrAtt=new ArrayList();
        List attrs=ele.getAttributes();
        if (attrs!=null)
        {
            int size=attrs.size();
            for(int i=0;i<size;i++)
            {
                Attribute srcAttr=(Attribute)attrs.get(i);
                arrAtt.add(new AttributeConfig(srcAttr.getName(),srcAttr.getValue()));
            }
        }
        return arrAtt;
    }
    /**
     * 根据一个基础文件路径和相对路径拼装其绝对路径
     * @param confFile File
     * @param relativeFile String
     * @return File
     */
    public static File getAbsoluteFile(File confFile,String relativeFile)
    {
        String genFilePath=confFile.getParent();
        if (!relativeFile.startsWith(FILE_SEP))
        {//相对路径没有以/开头，则加上，因为genFilePath没有/结尾
            relativeFile=FILE_SEP+relativeFile;
        }
        String absModFile=genFilePath+relativeFile;
        return new File(absModFile);

    }
    /**
     * 替换文本中的特殊字符，目前只是用于替换文本中的&为&amp;
     * 被GeneralConfig和ModuleConfig的save方法调用
     * @param src String
     * @param token String
     * @param replaced String
     * @return String
     */
    public static String replaceToken(String src,String token,String replaced)
    {
        if (src!=null)
        {
            return src.replaceAll(token,replaced);
        }
        return null;
    }
}
