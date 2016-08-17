package com.common.config;

/**
 * <p>Title: Poralt OAM</p>
 * <p>Description: Portal OAM Program file</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aspire Technologies</p>
 * @author He Chengshou
 * @version 1.0
 */
// @CheckItem@ OPT-hecs-20040202 增加宏定义功能
// @CheckItem@ OPT-hecs-20040212 功能修改:配置项被修改后,它在配置文件中的位置和web页面中的位置保持不变
// @CheckItem@ REQ-huangbigui-20060424 将 id 属性转移到 AbstractItem 中
// @CheckItem@ SELFBUG-huangbigui-20060603 在 modify 属性之前，先将所有属性删除（因原先在更改属性值时不能减少，只能增加和修改）；删除 setAttributeValue 方法，使用父类的 addAttribute 方法
import org.jdom.Element;
import org.jdom.*;

import com.common.config.model.*;

public class Item extends AbstractItem implements java.io.Serializable {

    // 单值配置项的value
    private String value = null;

    // 配置项的保留值
    private String reservedValue = null;
    /**
     * 新模型
     */
    //private SingleItemConfig sItem;
    public Item() {
        //this.element = new Element("ConfigItem");
        super(new SingleItemConfig());
    }

    /**
     * 构造器：用于从现有的配置子系统中得到一个配置项
     * @param element jdom的Element
     */
    public Item(org.jdom.Element element) {
        //this.element = element;
        super(new SingleItemConfig());
    }
    /**
     * 新的构造方法
     * @param newItem SingleItemConfig
     */
    public Item(SingleItemConfig newItem)
    {
        super(newItem);
    }

    /**
     * 构造器：构造一个新的配置项。
     * @param itemName 配置项名称。
     */
    public Item(String itemName) {
        super(new SingleItemConfig());
        setName(itemName);
    }

    /**
     * 获取配置项的值，原始值
     * @return 配置项值
     */
    public String getOrigValue() {
        return ((SingleItemConfig)newItem).getValue();
    }

//    /**
//     * 获取配置项的值,进行宏替换后
//     * @return 配置项值
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
//     * 设置配置项的值，如果没有此标签，便增加一个。
//     * @param value
//     */
//    public void setValue(String value) {
//        this.addElement("Value", value);
//    }

//    /**
//     * 得到配置项的序号
//     * @return 序号
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
//     * 设置配置项序号，如果没有此标签，便增加一个。
//     * @param value
//     */
//    public void setId(String id) {
//        this.addElement("Id", id);
//    }

    /**
     * 得到本配置项的备用值。
     * @return
     */
    public String getOrigReserved() {

        return ((SingleItemConfig)newItem).getReserved();
    }

    /**
     * 得到本配置项的备用值。宏替换后
     * @return 宏替换后的保留值
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
//     * 设置配置项备用值，如果没有此标签，便增加一个。
//     * @param value
//     */
//    public void setReserved(String value) {
//        this.addElement("Reserved", value);
//    }



    /**
     * 获取配置项的值
     *
     * @return 配置项值
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
     * 设置配置项的值
     *
     * @param value
     */
    public void setValue(String value) {
        ((SingleItemConfig)newItem).setValue(value);
    }


    /**
     * 得到配置项的保留值
     *
     * @return 保留值
     */
    public String getReserved() {

        return ((SingleItemConfig)newItem).getReserved();
    }

    /**
     * 设置配置项的保留值
     *
     * @param value
     */
    public void setReserved(String value) {
        ((SingleItemConfig)newItem).setReserved(value);
    }

    /**
     * 修改当前的配置向内容
     * @param item Item 输入的新数据
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
