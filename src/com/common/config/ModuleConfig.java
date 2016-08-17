package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: Portal OAM Program file</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-penglq-20050301 新增返回element的方法
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

    // 对应文件中的ModuleConfig节点
    private Element moduleConfigElement = null;

    // 模块ID，唯一标识
    private String moduleId = null;

    // 模块名
    private String moduleName = null;

    // 模块配置文件的相对路径
    private String ref = null;

    // 修改模块配置文件前的备份文件相对路径
    private String lastRef = null;

    // 根元素
    private Element rootElement = null;

    // 文档
    private Document systemDocument = null;
    /**
     * 新的模型
     */
    private com.common.config.model.ModuleConfig mc;
    /**
     * 构造器：创建一个新的子系统配置项。
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
     * 新的构造方法，只在包内调用
     * @param mc ModuleConfig
     */
    ModuleConfig(com.common.config.model.ModuleConfig mc)
    {
        this.mc=mc;
        this.moduleConfigElement = mc.getModuleElement();
    }


    /**
     * 构造器：从配置文件构造一个子系统配置类。
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
     * 当模型改变时，重新加载
     * @param eleSubConfig Element
     */
    public void setElement(Element eleSubConfig)
    {
        this.moduleConfigElement = eleSubConfig;
    }
    /**
     * 获得ModuleConfig的名称
     *
     * @return ModuleConfig的名称
     */
    public String getModuleName() {
        moduleName = mc.getName();
        return moduleName;
    }

    /**
     * 设置ModuleConfig的Name
     *
     * @param name
     */
    public void setModuleName(String name) {
        mc.setName(name);
    }

    /**
     * 获得ModuleConfig的Id
     *
     * @return
     */
    public String getModuleId() {
        moduleId = String.valueOf(mc.getID());
        return moduleId;
    }

    /**
     * 设置ModuleConfig的Id
     *
     * @param Id
     */
    public void setModuleId(String moduleId) {
        mc.setID(Integer.parseInt(moduleId));
    }

    /**
     * 获得ModuleConfig的描述信息
     *
     * @return
     */
    public String getDescription() {
        return mc.getDesc();

    }

    /**
     * 设置ModuleConfig的描述信息
     *
     * @param desc
     */
    public void setDescription(String desc) {
        mc.setDesc(desc);
    }


    /**
     * 返回子系统配置的一个属性
     *
     * @param attName 属性名称
     * @return 系统配置的一个属性
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
     * 得到指定的配置项类
     *
     * @param itemName 配置项名称
     * @return 配置项类
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
     * 得到本子系统的所有配置项
     *
     * @return 配置项数组
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
            {//单值配置
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
     * 得到指定的配置项类
     *
     * @param itemName 配置项名称
     * @return 配置项类
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
     * 得到本子系统的所有配置项
     *
     * @return 配置项数组
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
            { //多值配置
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
     * 简单的取值函数。为了方便调用，
     *
     * @param itemName 配置项名称
     * @return 配置值
     */
    public String getItemValue(String itemName)
    {
        Item item = this.getItem(itemName);
        return item.getValue();

    }

    /**
     * 简单的取属性值函数。为了方便调用，
     *
     * @param itemName 属性名称
     * @return 属性值
     */
    public String getItemAttributeValue(String itemName, String attributeName) {
//        Item item = getItem(itemName);
//        return item.getAttributeValue(attributeName);

        Item item = this.getItem(itemName);
        return item.getAttributeValue(attributeName);
    }

    /**
     * 简单的取值列表函数。为了方便调用，
     *
     * @param itemName 配置项名称
     * @return 配置值数组
     */
    public String[] getItemValueList(String itemName) {
//        ArrayItem arrayItem = this.getArrayItem(itemName);
//        return arrayItem.getValueList();

        ArrayItem arrayItem = this.getArrayItem(itemName);
        return arrayItem.getValueList();
    }

    /**
     * 增加一个配置项
     *
     * @param item 配置项对象
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
            // 该SingleItem已经存在
            return;
        } else {
            //moduleConfigElement.addContent(item.getElement());
            SingleItemConfig sic=(SingleItemConfig)item.getNewItem();
            mc.addItem(sic);
        }

    }

    /**
     * 增加一个多值配置项
     *
     * @param arrayItem 配置项对象
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
            // 该ArrayItem已经存在
            return;
        } else {
            //moduleConfigElement.addContent(arrayItem.getElement());
            MultiItemConfig mic=(MultiItemConfig)arrayItem.getNewItem();
            //mic.loadFromElement(arrayItem.getElement());
            mc.addItem(mic);
        }

    }

    /**
     * 删除一个配置项
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
     * 删除一个多值配置项
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
     * 获得Module的属性列表,module除了Name,Desc外无其它属性
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
     * 返回模块对象
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
