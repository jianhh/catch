package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description:Item,��ArrayItem��Ĺ�������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 ���Ӻ궨�幦��
// @CheckItem@ OPT-penglq-20050301 ��������element�ķ���
// @CheckItem@ REQ-huangbigui-20060424 ΪPortalConfig�������������޸���֧�� id
import org.jdom.CDATA;
import org.jdom.Element;

import java.io.IOException;
import java.util.*;
import com.common.config.model.*;

public class AbstractItem implements java.io.Serializable
{
    protected Element element = null;

    // ���ȼ���0,�ظģ�1���߼���-1:����
    protected String priority;

    // ��Ч���ڣ�0,������1��������-1���¸��������ڡ�
    protected String effectTime;

    //�������Id
    protected String Id;
    /**
     * �µ�������ģ��
     */
    protected ItemConfig newItem;

    public AbstractItem()
    {

    }

    public AbstractItem(ItemConfig newItem)
    {
        this.newItem=newItem;
    }
    /**
     * ��ȡ��ֵ���������Ч����
     *
     * @return
     */
    public String getEffectTime() {

        return String.valueOf(newItem.getEffectMode());
    }

    /**
     * ���õ�ֵ���������Ч����
     *
     * @return
     */
    public void setEffectTime(String effectTime) {
        newItem.setEffectMode(Integer.parseInt(effectTime));
    }

    /**
     * �õ�����������
     *
     * @return ���
     */
    public String getId() {

        return String.valueOf(newItem.getID());
    }

    /**
     * �������������
     *
     * @param value
     */
    public void setId(String id) {
        try
        {
            newItem.setID(Integer.parseInt(id));
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * ��ȡ��ֵ������ļ���
     *
     * @return
     */
    public String getPriority() {

        return String.valueOf(newItem.getPriority());
    }

    /**
     * ���õ�ֵ������ļ���
     *
     * @return
     */
    public void setPriority(String priority) {
        newItem.setPriority(Integer.parseInt(priority));
    }


    /**
     * �õ��������������
     * @return �����������ִ�
     */
    public String getDescription() {

        return newItem.getDesc();
    }

    /**
     * �������������������û�д˱�ǩ��������һ����
     * @param value
     */
    public void setDescription(String desc) {
        newItem.setDesc(desc);
    }

    /**
     * ����һ������
     * @param name ��������
     * @param value ����ֵ
     */
    public void addAttribute(String name, String value) {
        newItem.addAttribute(name,value);
    }

    /**
     * ɾ��һ������
     * @param attribute ��������
     */
    public void removeAttribute(String attName) {
        newItem.removeAttribute(attName);
    }

    /**
     * �õ�����������
     * @return
     */
    public String getName() {
        return newItem.getName();
    }

    /**
     * �����������ƣ����û�д˱�ǩ��������һ����
     * @param itemName
     */
    public void setName(String itemName) {
        newItem.setName(itemName);
    }

    /**
     * �õ��������һ�������б�
     * @param attribName
     * @return
     */
    public com.common.config.Attribute getAttribute(String attribName) {
        AttributeConfig ac=newItem.getAttribute(attribName);
        return new Attribute(ac);
    }

    /**
     * �õ��������һ������ֵ
     * @param attributeName
     * @return
     */
    public String getOrigAttributeValue(String attributeName) {
        return newItem.getAttributeValue(attributeName);
    }

    /**
     * �õ��������һ������ֵ,�����к��滻
     * @param attributeName
     * @return
     */
    public String getAttributeValue(String attributeName) {
        return newItem.getAttributeValue(attributeName);
    }
    /**
     * �õ�������������б�
     * @return
     */
    public com.common.config.Attribute[] getAttributes() {
        /*List list = this.element.getAttributes();
        com.aspire.common.config.Attribute[] attriubutes = new com.aspire.
                common.
                config.Attribute[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            org.jdom.Attribute tempAattribute = (org.jdom.Attribute) itrts.next();
            com.aspire.common.config.Attribute myAttr =
                    new com.aspire.common.config.Attribute(tempAattribute.
                    getName(),
                    tempAattribute.getValue());
            attriubutes[i] = myAttr;
            i++;
        }
        return attriubutes;*/
        ArrayList arr=newItem.getAttrConfigs();
        com.common.config.Attribute[] attriubutes =
            new com.common.config.Attribute[arr.size()];
        for(int i=0;i<arr.size();i++)
        {
            AttributeConfig attr=(AttributeConfig)arr.get(i);
            attriubutes[i]=new Attribute(attr);
        }
        return attriubutes;
    }

    /**
     * For Testing use
     */
    public void showData() {/*
        try {
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent("   ");
            out.setNewlines(true);
            out.output(this.element, System.out);
        } catch (IOException ex) {
        }*/
    }

    /**
     * ��������һ�����ò�������
     * @param name ��ǩ����
     * @param value ��Ӧֵ
     */
    protected void addElement(String name, String value) {
        this.element.removeChild(name);
        Element ele = new Element(name);
        if (value.indexOf("&") != -1 || value.indexOf("<") != -1 ||
            value.indexOf(">") != -1) {
            CDATA cdata = new CDATA(value);
            ele.addContent(cdata);
        } else {
            ele.setText(value);
        }
        this.element.addContent(ele);

    }

    /**
     * �����������xml����
     * @param ele xml Element
     */
    public void setElement(Element ele) {
        this.element = ele;
    }

    /**
     * ���ر�Item��Ӧ��JDOM Element
     * @return
     */
    protected Element getElement() {
        return this.element.detach();
    }

    public Element getItemElement() {
        return this.element;
    }

    public String toString() {
        String xml = null;
        java.io.StringWriter sw = null;
        try {
        	sw = new java.io.StringWriter(1000);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent("   ");
            //out.setNewlines(true);
            out.output(this.element, sw);
            xml = sw.toString();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
        	try {
				if(null!=sw){
					sw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return xml;

    }
    public void modify(AbstractItem item)
    {
        try
        {
            newItem.setID(Integer.parseInt(item.getId()));
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace(System.out);
        }
        newItem.setDesc(item.getDescription());
        newItem.setEffectMode(Integer.parseInt(item.getEffectTime()));
        newItem.setName(item.getName());
        newItem.setPriority(Integer.parseInt(item.getPriority()));
        Attribute[] attrs=item.getAttributes();
        newItem.resetAttrConfigs();
        for(int i=0;i<attrs.length;i++)
        {
            newItem.addAttribute(attrs[i].getName(),attrs[i].getValue());
        }
    }
    public ItemConfig getNewItem()
    {
        return newItem;
    }
}
