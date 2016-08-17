package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: 多值配置项中一个值对象</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 增加宏定义功能
import org.jdom.Element;
import org.jdom.*;
import com.common.config.model.*;

public class ArrayValue
    implements java.io.Serializable
{
    private Element arrayElement = null;
    private com.common.config.model.ArrayValue newAv;
    /**
     * 构造器：用于从现有的配置子系统中得到一个多值配置项值
     * @param element jdom的Element
     */
    public ArrayValue(Element element)
    {
        //this.arrayElement = element;
        newAv=new com.common.config.model.ArrayValue();
    }
    ArrayValue(com.common.config.model.ArrayValue newAv)
    {
        this.newAv=newAv;
    }

    /**
     * 构造器：创建一个多值配置项值
     * @param element jdom的Element
     */
    public ArrayValue()
    {
        //this.arrayElement = new Element("ArrayValue");
        newAv=new com.common.config.model.ArrayValue();
    }

    public String getId()
    {
        //return this.arrayElement.getChildText("Id");
        int ID=newAv.getID();
        return String.valueOf(ID);
    }

    public void setId(String id)
    {
//        setValueItem("Id", id);
        try
        {
            newAv.setID(Integer.parseInt(id));
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace(System.out);
        }
    }

    /**
     * 取得多值配置项值。
     * @return 宏替换前的值
     */
    public String getOrigValue()
    {
        //return this.arrayElement.getChildText("Value");
        return newAv.getValue();
    }

    /**
     * 取得多值配置项值，进行宏替换
     * @return 宏替换后的值
     */
    public String getValue()
    {
        /*MarcoDef marco = MarcoDef.getInstance();
        String value = this.arrayElement.getChildText("Value");
        return marco.replaceByMarco(value);*/
        return newAv.getValue();
    }

    public void setValue(String value)
    {
//        this.setValueItem("Value", value);
        newAv.setValue(value);
    }

    /**
     * 返回宏替换前的保留值
     * @return
     */
    public String getOrigReserved()
    {
        //return this.arrayElement.getChildText("Reserved");
        return newAv.getReserved();
    }

    /**
     * 返回宏替换后的保留值
     * @return
     */
    public String getReserved()
    {
        /*MarcoDef marco = MarcoDef.getInstance();
        String value = this.arrayElement.getChildText("Reserved");
        return marco.replaceByMarco(value);*/
        return newAv.getReserved();
    }

    public void setReserved(String resv)
    {
        //this.setValueItem("Reserved", resv);
        newAv.setReserved(resv);
    }

    /**
     * 返回本ArrayItem对应的JDOM Element
     * @deprecated
     */
    protected Element getElement()
    {
        return this.arrayElement;
    }

    /**
     * 通用方法：修改或创建一个xml元素
     * @param name 标签名称
     * @param value 标签的值
     */
    private void setValueItem(String name, String value)
    {
        arrayElement.removeChild(name);
        Element element = new Element(name);
        if (value.indexOf("&") != -1 || value.indexOf("<") != -1 || value.indexOf(">") != -1) {
            CDATA cdata = new CDATA(value);
            element.addContent(cdata);
        }
        else {
            element.setText(value);
        }
        this.arrayElement.addContent(element);

    }
}
