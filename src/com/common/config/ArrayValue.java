package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: ��ֵ��������һ��ֵ����</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 ���Ӻ궨�幦��
import org.jdom.Element;
import org.jdom.*;
import com.common.config.model.*;

public class ArrayValue
    implements java.io.Serializable
{
    private Element arrayElement = null;
    private com.common.config.model.ArrayValue newAv;
    /**
     * �����������ڴ����е�������ϵͳ�еõ�һ����ֵ������ֵ
     * @param element jdom��Element
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
     * ������������һ����ֵ������ֵ
     * @param element jdom��Element
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
     * ȡ�ö�ֵ������ֵ��
     * @return ���滻ǰ��ֵ
     */
    public String getOrigValue()
    {
        //return this.arrayElement.getChildText("Value");
        return newAv.getValue();
    }

    /**
     * ȡ�ö�ֵ������ֵ�����к��滻
     * @return ���滻���ֵ
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
     * ���غ��滻ǰ�ı���ֵ
     * @return
     */
    public String getOrigReserved()
    {
        //return this.arrayElement.getChildText("Reserved");
        return newAv.getReserved();
    }

    /**
     * ���غ��滻��ı���ֵ
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
     * ���ر�ArrayItem��Ӧ��JDOM Element
     * @deprecated
     */
    protected Element getElement()
    {
        return this.arrayElement;
    }

    /**
     * ͨ�÷������޸Ļ򴴽�һ��xmlԪ��
     * @param name ��ǩ����
     * @param value ��ǩ��ֵ
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
