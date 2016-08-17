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
 * �µ�ϵͳ����ģ����ʹ�õ��ĸ�������
 * @author yanfeng
 * @version 1.4.1.004
 */

public class ConfigUtil implements Constant
{
    public ConfigUtil()
    {
    }
    /**
     * �������ֺ�ֵ����XML��ʽ���ַ�������<Name>username</Name>
     * ��β���ϻ��з�
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
        {//û�����ַ��ؿ�
            return "";
        }
        StringBuffer sb=new StringBuffer();
        sb.append(indent);
        sb.append("<");
        sb.append(name);
        if (attrs!=null)
        {
            for(int i=0;i<attrs.size();i++)
            {//��������
                AttributeConfig attr=(AttributeConfig)attrs.get(i);
                sb.append(LINE_INDENT);
                sb.append(attr.getAttrName());
                sb.append("=\"");
                sb.append(attr.getAttrValue());
                sb.append("\"");
            }
        }
        if ((value!=null)&&!value.equals(""))
        {//�ڵ���ֵ
            sb.append(">");
            if (!oneLine)
            { //��Ҫ����
                sb.append(LINE_SEP);
            }
            sb.append(value);
            if (!oneLine)
            { //������Ҫ����
                sb.append(indent);
//            sb.append(LINE_SEP);
            }
            sb.append("</");
            sb.append(name);
            sb.append(">");
        }
        else
        {//�ڵ���ֵ��ֱ�Ӽ�/
            sb.append(" />");
        }
        sb.append(LINE_SEP);
        return sb.toString();
    }
    /**
     * ���һ��XML�ڵ�
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
     * ���һ��XML�ڵ�
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
     * �������ֺ�ֵ����XML��ʽ��һ���ڵ��ַ�������
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
     * ���Element�е������б�
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
     * ����һ�������ļ�·�������·��ƴװ�����·��
     * @param confFile File
     * @param relativeFile String
     * @return File
     */
    public static File getAbsoluteFile(File confFile,String relativeFile)
    {
        String genFilePath=confFile.getParent();
        if (!relativeFile.startsWith(FILE_SEP))
        {//���·��û����/��ͷ������ϣ���ΪgenFilePathû��/��β
            relativeFile=FILE_SEP+relativeFile;
        }
        String absModFile=genFilePath+relativeFile;
        return new File(absModFile);

    }
    /**
     * �滻�ı��е������ַ���Ŀǰֻ�������滻�ı��е�&Ϊ&amp;
     * ��GeneralConfig��ModuleConfig��save��������
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
