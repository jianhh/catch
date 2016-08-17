package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: Portal OAM Program file</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-penglq-20050301 ��������element�ķ���
import java.io.IOException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import com.common.config.model.ItemConfig;
import com.common.config.model.MultiItemConfig;
import com.common.config.model.SingleItemConfig;

public class ModuleConfig implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -742851597811078690L;

    private Item[] items = null;

    // ��Ӧ�ļ��е�ModuleConfig�ڵ�
    private Element moduleConfigElement = null;

    // ģ��ID��Ψһ��ʶ
    private String moduleId = null;

    // ģ����
    private String moduleName = null;

    // ģ�������ļ������·��
    private String ref = null;

    // �޸�ģ�������ļ�ǰ�ı����ļ����·��
    private String lastRef = null;

    // ��Ԫ��
    private Element rootElement = null;

    // �ĵ�
    private Document systemDocument = null;
    /**
     * �µ�ģ��
     */
    private com.common.config.model.ModuleConfig mc;
    /**
     * ������������һ���µ���ϵͳ�����
     *
     * @param moduleName
     */
    public ModuleConfig(String moduleName) {
        Element element = new Element("ModuleConfig");
        element.setAttribute(new org.jdom.Attribute("Name", moduleName));
        this.moduleName = moduleName;
        this.moduleConfigElement = element;
        mc=new com.common.config.model.ModuleConfig();
        mc.setName(moduleName);
    }
    /**
     * �µĹ��췽����ֻ�ڰ��ڵ���
     * @param mc ModuleConfig
     */
    ModuleConfig(com.common.config.model.ModuleConfig mc)
    {
        this.mc=mc;
        this.moduleConfigElement = mc.getModuleElement();
    }


    /**
     * ���������������ļ�����һ����ϵͳ�����ࡣ
     *
     * @param element
     */
    public ModuleConfig(Element element) {
        this.moduleConfigElement = element;
        this.moduleName = element.getAttribute("Name").getValue();
        mc=new com.common.config.model.ModuleConfig();
        mc.setName(moduleName);
    }
    /**
     * ��ģ�͸ı�ʱ�����¼���
     * @param eleSubConfig Element
     */
    public void setElement(Element eleSubConfig)
    {
        this.moduleConfigElement = eleSubConfig;
    }
    /**
     * ���ModuleConfig������
     *
     * @return ModuleConfig������
     */
    public String getModuleName() {
        moduleName = mc.getName();
        return moduleName;
    }

    /**
     * ����ModuleConfig��Name
     *
     * @param name
     */
    public void setModuleName(String name) {
        mc.setName(name);
    }

    /**
     * ���ModuleConfig��Id
     *
     * @return
     */
    public String getModuleId() {
        moduleId = String.valueOf(mc.getID());
        return moduleId;
    }

    /**
     * ����ModuleConfig��Id
     *
     * @param Id
     */
    public void setModuleId(String moduleId) {
        mc.setID(Integer.parseInt(moduleId));
    }

    /**
     * ���ModuleConfig��������Ϣ
     *
     * @return
     */
    public String getDescription() {
        return mc.getDesc();

    }

    /**
     * ����ModuleConfig��������Ϣ
     *
     * @param desc
     */
    public void setDescription(String desc) {
        mc.setDesc(desc);
    }


    /**
     * ������ϵͳ���õ�һ������
     *
     * @param attName ��������
     * @return ϵͳ���õ�һ������
     */
    public String getModuleAttributeValue(String attName) {
        String attributeValue = moduleConfigElement.getAttributeValue(attName);
        return attributeValue;
    }


    /**
     * @return Returns the ref.
     */
    public String getRef() {
        ref = mc.getRef();
        return ref;
    }

    /**
     * @param ref
     *            The ref to set.
     */
    public void setRef(String ref) {
        mc.setRef(ref);
    }


    /**
     * �õ�ָ������������
     *
     * @param itemName ����������
     * @return ��������
     */
    public Item getItem(String itemName)
    {
        /*Item item = null;
        List list = moduleConfigElement.getChildren("ConfigItem");
        Iterator itrts = list.iterator();
        while (itrts.hasNext()) {
            Element element = (Element) itrts.next();
            if ((element.getChild("Name").getText()).equalsIgnoreCase(itemName)) {
                item = new Item(element);
            }
        }
        return item;*/
        com.common.config.model.ItemConfig item=
            mc.getItemByName(itemName);
        if ((item!=null)&&
            (item instanceof com.common.config.model.SingleItemConfig))
        {
            SingleItemConfig sItem=(SingleItemConfig)item;
            Item oldItem=new Item(sItem);
            return oldItem;
        }
        return null;
    }

    /**
     * �õ�����ϵͳ������������
     *
     * @return ����������
     */
    public Item[] getItems() {
        /*List list = moduleConfigElement.getChildren("ConfigItem");
        Item[] items = new Item[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            Element element = (Element) itrts.next();
            Item item = new Item(element);
            items[i] = item;
            i++;
        }*/
        ArrayList arrItems=mc.getItems();
        ArrayList singles=new ArrayList();
        for(int i=0;i<arrItems.size();i++)
        {
            ItemConfig item=(ItemConfig)arrItems.get(i);
            if (item instanceof SingleItemConfig)
            {//��ֵ����
                singles.add(item);
            }
        }
        Item[] items = new Item[singles.size()];
        for(int i=0;i<singles.size();i++)
        {
            SingleItemConfig sItem=(SingleItemConfig)singles.get(i);
            items[i]=new Item(sItem);
        }
        return items;

    }

    /**
     * �õ�ָ������������
     *
     * @param itemName ����������
     * @return ��������
     */
    public ArrayItem getArrayItem(String itemName)
    {
        /*ArrayItem arrayItem = null;
        try {
            List list = moduleConfigElement.getChildren("ArrayConfigItem");
            Iterator itrts = list.iterator();
            while (itrts.hasNext()) {
                Element element = (Element) itrts.next();
                if ((element.getChild("Name").getText())
                    .equalsIgnoreCase(itemName)) {
                    arrayItem = new ArrayItem(element);
                }
            }
        } catch (Exception ex) {
        }
        return arrayItem;*/
        com.common.config.model.ItemConfig item=
            mc.getItemByName(itemName);
        if ((item!=null)&&
            (item instanceof com.common.config.model.MultiItemConfig))
        {
            MultiItemConfig mItem=(MultiItemConfig)item;
            ArrayItem oldItem=new ArrayItem(mItem);
            return oldItem;
        }
        return null;

    }

    /**
     * �õ�����ϵͳ������������
     *
     * @return ����������
     */
    public ArrayItem[] getArrayItems() {
/*

        List list = moduleConfigElement.getChildren("ArrayConfigItem");
        ArrayItem[] arrayItems = new ArrayItem[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            Element element = (Element) itrts.next();
            ArrayItem arrayItem = new ArrayItem(element);
            arrayItems[i] = arrayItem;
            i++;
        }
        return arrayItems;*/
        ArrayList arrItems = mc.getItems();
        ArrayList arr = new ArrayList();
        for (int i = 0; i < arrItems.size(); i++)
        {
            ItemConfig item = (ItemConfig) arrItems.get(i);
            if (item instanceof MultiItemConfig)
            { //��ֵ����
                arr.add(item);
            }
        }
        ArrayItem[] items = new ArrayItem[arr.size()];
        for (int i = 0; i < arr.size(); i++)
        {
            MultiItemConfig mItem = (MultiItemConfig) arr.get(i);
            items[i] = new ArrayItem(mItem);
        }
        return items;
    }

    /**
     * �򵥵�ȡֵ������Ϊ�˷�����ã�
     *
     * @param itemName ����������
     * @return ����ֵ
     */
    public String getItemValue(String itemName)
    {
        Item item = this.getItem(itemName);
        return item.getValue();

    }

    /**
     * �򵥵�ȡ����ֵ������Ϊ�˷�����ã�
     *
     * @param itemName ��������
     * @return ����ֵ
     */
    public String getItemAttributeValue(String itemName, String attributeName) {
//        Item item = getItem(itemName);
//        return item.getAttributeValue(attributeName);

        Item item = this.getItem(itemName);
        return item.getAttributeValue(attributeName);
    }

    /**
     * �򵥵�ȡֵ�б�����Ϊ�˷�����ã�
     *
     * @param itemName ����������
     * @return ����ֵ����
     */
    public String[] getItemValueList(String itemName) {
//        ArrayItem arrayItem = this.getArrayItem(itemName);
//        return arrayItem.getValueList();

        ArrayItem arrayItem = this.getArrayItem(itemName);
        return arrayItem.getValueList();
    }

    /**
     * ����һ��������
     *
     * @param item ���������
     * @throws ConfigException
     */
    public void addItem(Item item)
    throws Exception
    {
//        if (this.getItem(item.getName()) != null) {
//            throw new ConfigException("Duplicated name tag:" + item.getName());
//        }
//        moduleConfigElement.addContent(item.getElement());

        if (this.getItem(item.getName()) != null) {
            // ��SingleItem�Ѿ�����
            return;
        } else {
            //moduleConfigElement.addContent(item.getElement());
            SingleItemConfig sic=(SingleItemConfig)item.getNewItem();
            mc.addItem(sic);
        }

    }

    /**
     * ����һ����ֵ������
     *
     * @param arrayItem ���������
     * @throws ConfigException
     */
    public void addArrayItem(ArrayItem arrayItem)
    throws Exception
    {
//        if (this.getArrayItem(arrayItem.getName()) != null) {
//            throw new ConfigException("Duplicated name tag:" +
//                                      arrayItem.getName());
//        }
//        moduleConfigElement.addContent(arrayItem.getElement());

        if (this.getArrayItem(arrayItem.getName()) != null) {
            // ��ArrayItem�Ѿ�����
            return;
        } else {
            //moduleConfigElement.addContent(arrayItem.getElement());
            MultiItemConfig mic=(MultiItemConfig)arrayItem.getNewItem();
            //mic.loadFromElement(arrayItem.getElement());
            mc.addItem(mic);
        }

    }

    /**
     * ɾ��һ��������
     *
     * @param itemName
     */
    public void removeItem(String itemName) {
        /*Item item = this.getItem(itemName);
        if (item != null) {
            moduleConfigElement.removeContent(item.getElement());
        }*/
        mc.removeItem(itemName);

    }

    /**
     * ɾ��һ����ֵ������
     *
     * @param itemName
     */
    public void removeArrayItem(String itemName) {
        /*ArrayItem arrayItem = this.getArrayItem(itemName);
        if (arrayItem != null) {
            moduleConfigElement.removeContent(arrayItem.getElement());
        }*/
        mc.removeItem(itemName);

    }

    protected Element getElement() {
        return this.moduleConfigElement;
    }

    public String toString() {
        String xml = null;
        try {
            java.io.StringWriter sw = new java.io.StringWriter(1000);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent("   ");
            out.setNewlines(true);
            out.output(this.moduleConfigElement, sw);
            xml = sw.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return xml;
    }

    public void showData() {
        try {
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent("   ");
            //out.setNewlines(true);
            out.output(this.moduleConfigElement, System.out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ���Module�������б�,module����Name,Desc������������
     * #author Chen Yang 2004-08-19
     */
    public com.common.config.Attribute[] getAttributes() {

        /*List list = this.moduleConfigElement.getAttributes();
        com.common.config.Attribute[] attriubutes = new com.
                common.
                config.Attribute[list.size()];
        Iterator itrts = list.iterator();
        int i = 0;
        while (itrts.hasNext()) {
            org.jdom.Attribute tempAattribute = (org.jdom.Attribute) itrts.next();
            com.common.config.Attribute myAttr =
                    new com.common.config.Attribute(tempAattribute.
                    getName(), tempAattribute.getValue());
            attriubutes[i] = myAttr;
            i++;
        }
        return attriubutes;*/
        return null;
    }

    /*
     * ����ģ�����
     */
    public Element getModuleElement() {
        return moduleConfigElement;
    }
    public void modify(ModuleConfig oldMc)
    throws Exception
    {
        //mc.loadFromElement(oldMc.getElement());
        setModuleName(oldMc.getModuleName());
        setDescription(oldMc.getDescription());
        setRef(oldMc.getRef());
    }
    public com.common.config.model.ModuleConfig getNewModule()
    {
        return mc;
    }
}
