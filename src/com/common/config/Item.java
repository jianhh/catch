package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: Portal OAM Program file</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 ���Ӻ궨�幦��
// @CheckItem@ OPT-hecs-20040212 �����޸�:������޸ĺ�,���������ļ��е�λ�ú�webҳ���е�λ�ñ��ֲ���
// @CheckItem@ REQ-huangbigui-20060424 �� id ����ת�Ƶ� AbstractItem ��
// @CheckItem@ SELFBUG-huangbigui-20060603 �� modify ����֮ǰ���Ƚ���������ɾ������ԭ���ڸ�������ֵʱ���ܼ��٣�ֻ�����Ӻ��޸ģ���ɾ�� setAttributeValue ������ʹ�ø���� addAttribute ����
import org.jdom.Element;
import org.jdom.*;

import com.common.config.model.*;

public class Item extends AbstractItem implements java.io.Serializable {

    // ��ֵ�������value
    private String value = null;

    // ������ı���ֵ
    private String reservedValue = null;
    /**
     * ��ģ��
     */
    //private SingleItemConfig sItem;
    public Item() {
        //this.element = new Element("ConfigItem");
        super(new SingleItemConfig());
    }

    /**
     * �����������ڴ����е�������ϵͳ�еõ�һ��������
     * @param element jdom��Element
     */
    public Item(org.jdom.Element element) {
        //this.element = element;
        super(new SingleItemConfig());
    }
    /**
     * �µĹ��췽��
     * @param newItem SingleItemConfig
     */
    public Item(SingleItemConfig newItem)
    {
        super(newItem);
    }

    /**
     * ������������һ���µ������
     * @param itemName ���������ơ�
     */
    public Item(String itemName) {
        super(new SingleItemConfig());
        setName(itemName);
    }

    /**
     * ��ȡ�������ֵ��ԭʼֵ
     * @return ������ֵ
     */
    public String getOrigValue() {
        return ((SingleItemConfig)newItem).getValue();
    }

//    /**
//     * ��ȡ�������ֵ,���к��滻��
//     * @return ������ֵ
//     */
//    public String getValue() {
//        MarcoDef marco = MarcoDef.getInstance();
//        String value = element.getChild("Value").getText();
//        if (value == null) {
//            return "";
//        }
//        return marco.replaceByMarco(value);
//    }
//
//    /**
//     * �����������ֵ�����û�д˱�ǩ��������һ����
//     * @param value
//     */
//    public void setValue(String value) {
//        this.addElement("Value", value);
//    }

//    /**
//     * �õ�����������
//     * @return ���
//     */
//    public String getId() {
//        String id = element.getChild("Id").getText();
//        if (id == null) {
//            return "";
//        }
//        return id;
//    }
//
//    /**
//     * ������������ţ����û�д˱�ǩ��������һ����
//     * @param value
//     */
//    public void setId(String id) {
//        this.addElement("Id", id);
//    }

    /**
     * �õ���������ı���ֵ��
     * @return
     */
    public String getOrigReserved() {

        return ((SingleItemConfig)newItem).getReserved();
    }

    /**
     * �õ���������ı���ֵ�����滻��
     * @return ���滻��ı���ֵ
     */
//    public String getReserved() {
//        MarcoDef marco = MarcoDef.getInstance();
//        String value = this.element.getChild("Reserved").getText();
//        if (value == null) {
//            return "";
//        }
//        return marco.replaceByMarco(value);
//    }
//
//    /**
//     * �����������ֵ�����û�д˱�ǩ��������һ����
//     * @param value
//     */
//    public void setReserved(String value) {
//        this.addElement("Reserved", value);
//    }



    /**
     * ��ȡ�������ֵ
     *
     * @return ������ֵ
     */

    public String getValue()
    {
        /*value = element.getChild("Value").getText();
        if (value == null) {
            return "";
        }
        return value;*/
        return ((SingleItemConfig)newItem).getValue();
    }

    /**
     * �����������ֵ
     *
     * @param value
     */
    public void setValue(String value) {
        ((SingleItemConfig)newItem).setValue(value);
    }


    /**
     * �õ�������ı���ֵ
     *
     * @return ����ֵ
     */
    public String getReserved() {

        return ((SingleItemConfig)newItem).getReserved();
    }

    /**
     * ����������ı���ֵ
     *
     * @param value
     */
    public void setReserved(String value) {
        ((SingleItemConfig)newItem).setReserved(value);
    }

    /**
     * �޸ĵ�ǰ������������
     * @param item Item �����������
     */
    public void modify(Item item) {
        /*
        this.setId(item.getId());
        this.setDescription(item.getDescription());
        this.setValue(item.getOrigValue());
        this.setEffectTime(item.getEffectTime());
        this.setPriority(item.getPriority());
        this.setReserved(item.getOrigReserved());
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
        com.aspire.common.config.Attribute[] atts = item.getAttributes();
        if (atts != null && atts.length > 0) {
            for (int i = 0; i < atts.length; i++) {
                this.addAttribute(atts[i].getName(), atts[i].getOrigValue());
            }
        }*/
        super.modify(item);
        ((SingleItemConfig)newItem).setReserved(item.getReserved());
        ((SingleItemConfig)newItem).setValue(item.getValue());
    }


}
