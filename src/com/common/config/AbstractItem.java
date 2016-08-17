package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description:Item,和ArrayItem类的公共方法</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 增加宏定义功能
// @CheckItem@ OPT-penglq-20050301 新增返回element的方法
// @CheckItem@ REQ-huangbigui-20060424 为PortalConfig的配置项排序修改以支持 id
import org.jdom.CDATA;
import org.jdom.Element;

import java.io.IOException;
import java.util.*;
import com.common.config.model.*;

public class AbstractItem implements java.io.Serializable
{
    protected Element element = null;

    // 优先级：0,必改，1：高级；-1:保留
    protected String priority;

    // 生效周期：0,重启，1，立即，-1：下个运行周期。
    protected String effectTime;

    //配置项的Id
    protected String Id;
    /**
     * 新的配置项模型
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
     * 获取单值配置项的生效周期
     *
     * @return
     */
    public String getEffectTime() {

        return String.valueOf(newItem.getEffectMode());
    }

    /**
     * 设置单值配置项的生效周期
     *
     * @return
     */
    public void setEffectTime(String effectTime) {
        newItem.setEffectMode(Integer.parseInt(effectTime));
    }

    /**
     * 得到配置项的序号
     *
     * @return 序号
     */
    public String getId() {

        return String.valueOf(newItem.getID());
    }

    /**
     * 设置配置项序号
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
     * 获取单值配置项的级别
     *
     * @return
     */
    public String getPriority() {

        return String.valueOf(newItem.getPriority());
    }

    /**
     * 设置单值配置项的级别
     *
     * @return
     */
    public void setPriority(String priority) {
        newItem.setPriority(Integer.parseInt(priority));
    }


    /**
     * 得到此配置项的描述
     * @return 配置项描述字串
     */
    public String getDescription() {

        return newItem.getDesc();
    }

    /**
     * 设置配置项描述，如果没有此标签，便增加一个。
     * @param value
     */
    public void setDescription(String desc) {
        newItem.setDesc(desc);
    }

    /**
     * 增加一个属性
     * @param name 属性名称
     * @param value 属性值
     */
    public void addAttribute(String name, String value) {
        newItem.addAttribute(name,value);
    }

    /**
     * 删除一个属性
     * @param attribute 属性名称
     */
    public void removeAttribute(String attName) {
        newItem.removeAttribute(attName);
    }

    /**
     * 得到参数的名称
     * @return
     */
    public String getName() {
        return newItem.getName();
    }

    /**
     * 设置属性名称，如果没有此标签，便增加一个。
     * @param itemName
     */
    public void setName(String itemName) {
        newItem.setName(itemName);
    }

    /**
     * 得到配置项的一个属性列表
     * @param attribName
     * @return
     */
    public com.common.config.Attribute getAttribute(String attribName) {
        AttributeConfig ac=newItem.getAttribute(attribName);
        return new Attribute(ac);
    }

    /**
     * 得到配置项的一个属性值
     * @param attributeName
     * @return
     */
    public String getOrigAttributeValue(String attributeName) {
        return newItem.getAttributeValue(attributeName);
    }

    /**
     * 得到配置项的一个属性值,并进行宏替换
     * @param attributeName
     * @return
     */
    public String getAttributeValue(String attributeName) {
        return newItem.getAttributeValue(attributeName);
    }
    /**
     * 得到配置项的属性列表
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
     * 增加配置一个配置参数子项
     * @param name 标签名称
     * @param value 对应值
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
     * 更新配置项的xml内容
     * @param ele xml Element
     */
    public void setElement(Element ele) {
        this.element = ele;
    }

    /**
     * 返回本Item对应的JDOM Element
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
