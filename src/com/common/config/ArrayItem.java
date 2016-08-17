package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: This class contains all ArrayConfigItem attribute and
 *    all the operations.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 ���Ӻ궨�幦��
// @CheckItem@ OPT-hecs-20040212 �����޸�:������޸ĺ�,���������ļ��е�λ�ú�webҳ���е�λ�ñ��ֲ���
// @CheckItem@ SELFBUG-huangbigui-20060603 �� modify ����֮ǰ���Ƚ���������ɾ������ԭ���ڸ�������ֵʱ���ܼ��٣�ֻ�����Ӻ��޸ģ�
import java.util.Iterator;
import java.util.*;

import com.common.config.model.*;
import org.jdom.Element;

public class ArrayItem extends AbstractItem implements java.io.Serializable {
    public ArrayItem() {
        //this.element = new Element("ArrayConfigItem");
        newItem=new MultiItemConfig();
    }
    public ArrayItem(MultiItemConfig newItem)
    {
        super(newItem);
    }

    /**
     * �����������ڴ����е�������ϵͳ�еõ�һ����ֵ������
     * @param element jdom��Element
     */
    public ArrayItem(Element element) {
        //this.element = element;
        newItem=new MultiItemConfig();
    }

    /**
     * ������������һ���µĶ�ֵ�����
     * @param itemName ���������ơ�
     */
    public ArrayItem(String name) {
        //this.element = new Element("ArrayConfigItem");
        //this.element.addContent(new Element("Name"));
        //this.element.getChild("Name").setText(name);
        newItem=new MultiItemConfig();
        newItem.setName(name);
    }
    /**
     * ����AbstractItem�ж���ķ���
     * @param attributeName String
     * @return String
     */
    public String getAttributeValue(String attributeName)
    {
        return newItem.getAttributeValue(attributeName);
    }
    public void addArrayValue(ArrayValue av) {
        //this.element.addContent(av.getElement());
        com.common.config.model.ArrayValue newAv=
            new com.common.config.model.ArrayValue();
        try
        {
            newAv.setID(Integer.parseInt(av.getId()));
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace(System.out);
        }
        newAv.setValue(av.getOrigValue());
        newAv.setReserved(av.getOrigReserved());
        ((MultiItemConfig)newItem).addValue(newAv);

    }

    /**
     * ���ر�֮�б������������ֵ����
     * @return ArrayValue��������
     */
    public ArrayValue[] getArrayValues() {
        /*List list = this.element.getChildren("ArrayValue");
        ArrayValue[] avs = new ArrayValue[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            Element ele = (Element) itrts.next();
            ArrayValue av = new ArrayValue(ele);
            avs[i] = av;
            i++;
        }
        return avs;*/
        ArrayList arrVal=((MultiItemConfig)newItem).getArrValue();
        ArrayValue[] avs=new  ArrayValue[arrVal.size()];
        for(int i=0;i<arrVal.size();i++)
        {
            com.common.config.model.ArrayValue
                newAv=(com.common.config.model.ArrayValue)arrVal.get(i);
            avs[i]=new ArrayValue(newAv);
        }
        return avs;
    }

    /**
     * ���ر�֮�б������������ֵ
     * @return String����
     */
    public String[] getValueList() {
        /*List list = this.element.getChildren("ArrayValue");
        String[] avs = new String[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            Element ele = (Element) itrts.next();
            String s = new ArrayValue(ele).getValue();
            avs[i] = s;
            i++;
        }
        return avs;*/
    return ((MultiItemConfig)newItem).getValues();
    }

    /**
     * �޸ĵ�ǰ��ֵ�б�������
     * @param aItem ArrayItem Ŀ��ֵ
     */
    public void modify(ArrayItem aItem) {
        /*this.setDescription(aItem.getDescription());
        this.setEffectTime(aItem.getEffectTime());
        this.setPriority(aItem.getPriority());
        //----add by huangbigui on 20060603
        com.aspire.common.config.Attribute[] attsOld = this.getAttributes();
        if(attsOld != null && attsOld.length > 0)
        {
            for(int i=0; i<attsOld.length; i++)
            {
                this.removeAttribute(attsOld[i].getName());
            }
        }
        //----add by huangbigui on 20060303
        //set attributes
        com.aspire.common.config.Attribute[] atts = aItem.getAttributes();
        if (atts != null && atts.length > 0) {
            for (int i = 0; i < atts.length; i++) {
                this.addAttribute(atts[i].getName(), atts[i].getOrigValue());
            }
        }
        //Remove all old array values
        this.element.removeChildren("ArrayValue");
        //Add new array values
        ArrayValue[] arrValues = aItem.getArrayValues();
        ArrayValue tmpValue = null;
        if (arrValues != null && arrValues.length > 0) {
            for (int i = 0; i < arrValues.length; i++) {
                tmpValue = new ArrayValue();
                tmpValue.setId(arrValues[i].getId());
                tmpValue.setValue(arrValues[i].getOrigValue());
                if (arrValues[i].getOrigReserved() != null) {
                    tmpValue.setReserved(arrValues[i].getOrigReserved());
                }
                try {
                    this.addArrayValue(tmpValue);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }*/
        super.modify(aItem);
        ArrayValue[] avs=aItem.getArrayValues();
        ((MultiItemConfig)newItem).resetArrValue();
        for(int i=0;i<avs.length;i++)
        {
            addArrayValue(avs[i]);
        }
    }
    public String getName()
    {
        return newItem.getName();
    }
    public String getDescription()
    {
        return newItem.getDesc();
    }
    public String getId()
    {
        return String.valueOf(newItem.getID());
    }

}
