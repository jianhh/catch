package com.common.config ;

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
// @CheckItem@ OPT-huangbigui-20070123 CQ:13110 将SystemConfig中委托方法给GeneralConfig之前判断GeneralConfig的路径是否已被设置

import org.jdom.Document ;
import org.jdom.Element ;
import java.util.* ;
import com.common.config.model.* ;

public class SystemConfig
{
    private static String configFileName = null ;

    private static Document sysDocument = null ;

    private static Element sysElement = null ;

    private static Element marcoElement = null ;

    private static MarcoDef marco = null ;

    private Vector dataListeners = new Vector() ;

    /**
     * 新的全局配置对象,所有的配置操作通过它来中转实现
     */
    private GeneralConfig generalConfig ;

    /**
     * 默认构造器
     */
    public SystemConfig ()
    {
        generalConfig = GeneralConfig.getInstance() ;
    }

    /**
     * 设置配置文件的路径名称，JMX接口。
     * @param file 配置文件的路径名称
     */
    void setConfigFile (String file)
    {
        configFileName = file ;
    }

    public String getConfigFileName ()
    {
        return configFileName ;
    }

    /**
     * 加载配置文件
     */
    public synchronized void load ()
    {
        try
        {
            if (generalConfig.getFullPath() == null)
            {
                generalConfig.setFullPath(SystemConfig.configFileName) ;
            }
            generalConfig.loadFromDefaultFile() ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out) ;
        }
    }

    /**
     * 将内存配置信息保存之配置文件，覆盖员文件
     */
    //@CheckLine-20031022-oam-hecs:save方法都是在synchronized方法中调用的，它自己不需线程同步。
    public void save ()
    {
        try
        {
            generalConfig.save2DefaultFile() ;
            //通知监听的对象
            doRefresh() ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out) ;
        }
    }

    /**
     * 构造并返回一个子系统配置类，包含该子系统的所有配置项。
     * @param moduleName 子系统名称
     * @return 子系统配置类
     */
    public ModuleConfig getModuleConfig (String moduleName)
    {
        com.common.config.model.ModuleConfig mc =
            generalConfig.getModuleByName(moduleName) ;
        if (mc != null)
        {
            ModuleConfig mConfig = new ModuleConfig(mc) ;
            return mConfig ;
        }
        return null ;
    }

    /**
     * 取得所有的子系统配置类列表
     * @return 子系统配置类：ModuleConfig数组
     */
    public ModuleConfig[] getModuleConfigs ()
    {
        ArrayList modules = generalConfig.getModules() ;
        ModuleConfig[] mConfigs = new ModuleConfig[modules.size()] ;
        for (int i = 0 ; i < modules.size() ; i++)
        {
            com.common.config.model.ModuleConfig mc =
                (com.common.config.model.ModuleConfig) modules.get(i) ;
            mConfigs[i] = new ModuleConfig(mc) ;
        }
        return mConfigs ;
    }

    /**
     * 返回当前的宏定义类实例
     * @return
     */
    public MarcoDef getMarcoConfig ()
    {
        return marco ;
    }

    /**
     * 新增一个子系统配合集合
     * @param module ModuleConfig类
     */
    public synchronized void addModuleConfig (ModuleConfig module)
        throws
        Exception
    {
        if (getModuleConfig(module.getModuleName()) != null)
        {
            throw new Exception("Duplicated name tag:" +
                                      module.getModuleName()) ;
        }
        else
        {
            com.common.config.model.ModuleConfig newMc =
                module.getNewModule() ;
            try
            {
                generalConfig.addModule(newMc) ;
                save() ;
            }
            catch (Exception ex)
            {
                throw new Exception(ex) ;
            }
        }

    }

    /**
     * 删除一个子系统配置
     * @param moduleName
     */
    public synchronized void removeModuleConfig (String moduleName)
    {
        /*ModuleConfig mcfg = this.getModuleConfig(moduleName);
                 if (mcfg == null) {
            return;
                 }
                 sysElement.removeContent(mcfg.getElement());*/
        generalConfig.removeModule(moduleName) ;
        save() ;
    }

    /**
     * 修改子系统配置的属性
     * @param module 子系统名称
     */
    public synchronized void modifyModuleConfig (ModuleConfig module)
    {
        ModuleConfig mcfg = getModuleConfig(module.getModuleName()) ;
//        mcfg.setDescription(module.getDescription());
        try
        {
            mcfg.modify(module) ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out) ;
        }
    }

    /**
     * 修改特定配置项的值
     * @param moduleName
     * @param item
     */
    public synchronized void modifyItem (String moduleName, Item item)
    {
        ModuleConfig mcfg = getModuleConfig(moduleName) ;
        mcfg.getItem(item.getName()).modify(item) ;
//        try {
//            mcfg.addItem(item);
//        }
//        catch (ConfigException ex) {
//            logger.info("Error: AddItem failed." + ex.getMessage());
//            logger.error(OAMErrorCode._EC_OAM_ADD_ITEM_ERR, ex);
//        }
        this.save() ;
    }

    /**
     * 修改指定的多值配置项
     * @param moduleName
     * @param aItem
     */
    public synchronized void modifyArrayItem (String moduleName,
                                              ArrayItem aItem)
    {
        ModuleConfig mcfg = getModuleConfig(moduleName) ;
        mcfg.getArrayItem(aItem.getName()).modify(aItem) ;
        save() ;
    }

    /**
     *  增加一个配置项
     * @param moduleName
     * @param item
     * @throws ConfigException
     */
    public synchronized void addItem (String moduleName, Item item)
        throws
        Exception
    {
        ModuleConfig mcfg = this.getModuleConfig(moduleName) ;
        if (mcfg.getItem(item.getName()) != null)
        {
            throw new Exception("Duplicated name tag:" + item.getName()) ;
        }
        else
        {
            try
            {
                mcfg.addItem(item) ;
                save() ;
            }
            catch (Exception ex)
            {
                throw new Exception(ex) ;
            }
        }

    }

    public synchronized void addArrayItem (String moduleName, ArrayItem aitem)
        throws Exception
    {
        /*
                 ModuleConfig mcfg = this.getModuleConfig(moduleName);
                 if (mcfg.getItem(aitem.getName()) != null) {
         throw new ConfigException("Duplicated name tag:" + aitem.getName());
                 }
                 else {
            mcfg.getElement().addContent(aitem.getElement());
            this.save();
                 }*/
        ModuleConfig mc = getModuleConfig(moduleName) ;
        if (mc == null)
        {
            throw new Exception("No module:" + moduleName + " exist!") ;
        }
        try
        {
            mc.addArrayItem(aitem) ;
            save() ;
        }
        catch (Exception ex)
        {
            throw new Exception(ex) ;
        }
    }

    /**
     * 删除一个配置项
     * @param moduleName 子系统名称
     * @param itemName 配置项名称
     */
    public synchronized void removeItem (String moduleName, String itemName)
    {
        ModuleConfig mcfg = this.getModuleConfig(moduleName) ;
        /*Item item = mcfg.getItem(itemName);
                 if (item != null) {
            mcfg.removeItem(itemName);
                 }
                 else {
            mcfg.removeArrayItem(itemName);
                 }*/
        mcfg.removeItem(itemName) ;
        save() ;
    }

    /**
     * 注册一个配置数据监听器
     * @param listener
     */
    public synchronized void addConfigDataListener (ConfigDataListener listener)
    {
        Object obj = getConfigObj(listener.getListenerName()) ;
        if (obj != null)
        {
            this.removeListener(listener.getListenerName()) ;
        }
        dataListeners.add(listener) ;
    }

    /**
     * get a registered config ConfigListener in the dataListeners
     * @param listenerName
     * @return Object: if found .null: not found
     */
    public synchronized Object getConfigListener (String listenerName)
    {
        return getConfigObj(listenerName) ;
    }

    /**
     * 取消一个配置数据监听器
     * @param listener
     */
    public synchronized void removeConfigDataListener (ConfigDataListener
                                                       listener)
    {
        this.dataListeners.remove(listener) ;
    }

    /**
     * 调用每个已注册的ConfigDataListener实例的刷新函数，刷新配置。
     */
// @CheckLine@ REQOAM-hecs-20030905 这里不需线程同步。
    public void doRefresh ()
    {
        ConfigDataListener listener = null ;
        int size = dataListeners.size() ;
        for (int i = 0 ; i < size ; i++)
        {
            try
            {
                listener = (ConfigDataListener) dataListeners.get(i) ;
                listener.doConfigRefresh() ;
            }
            catch (Throwable ex)
            {
                ex.printStackTrace(System.out) ;
            }
        }
    }

    /**
     * 返回整个系统配置
     * @return Jdom root Element
     */
    protected org.jdom.Document getDocument ()
    {
        return sysDocument ;
    }

    /**
     * Remove the specified listener
     * @param listenerName
     */
    private void removeListener (String listenerName)
    {
        ConfigDataListener listener = null ;
        Vector tmpListeners = (Vector) dataListeners.clone() ;
        int size = tmpListeners.size() ;
        for (int i = 0 ; i < size ; i++)
        {
            listener = (ConfigDataListener) tmpListeners.get(i) ;
            if (listener.getListenerName().equals(listenerName))
            {
                dataListeners.remove(listener) ;
                break ;
            }
        }

    }

    /**
     * get a registered config object in the dataListeners
     * @param listenerName
     * @return Object: if found .null: not found
     */
    private Object getConfigObj (String listenerName)
    {
        Object obj = null ;
        ConfigDataListener listener = null ;
        int size = dataListeners.size() ;
        for (int i = 0 ; i < size ; i++)
        {
            listener = (ConfigDataListener) dataListeners.get(i) ;
            if (listener.getListenerName().equals(listenerName))
            {
                obj = dataListeners.get(i) ;
                break ;
            }
        }
        return obj ;
    }

    /**
     * get all the config listener name array
     * @return String array.
     */
    public synchronized ConfigDataListener[] getListenerNames ()
    {
        int size = dataListeners.size() ;
        ConfigDataListener[] names = new ConfigDataListener[size] ;
        ConfigDataListener listener = null ;
        for (int i = 0 ; i < size ; i++)
        {
            listener = (ConfigDataListener) dataListeners.get(i) ;
            names[i] = listener ;
        }
        return names ;
    }

    /**
     * Clear all config listeners from memory. This method only be call when this application
     * been undeployed.
     */
    public void clearListeners ()
    {
        dataListeners.clear() ;
    }

    /**
     * Return total count of ConfigDataListeners registered in the OAM
     * @return count of listeners
     */
    public int getListenerCount ()
    {
        return dataListeners.size() ;
    }

    /**
     * For debug use
     */
    protected void finalize ()
    {
    }

    /**
     * 增加一个宏定义
     * @param name  宏名称
     * @param value 宏值
     * @deprecated
     * @throws ConfigException
     */
    public void addMarco (String name, String value, String desc)
        throws Exception
    {
        if (marcoElement == null)
        {
            marcoElement = new Element("MarcoDefine") ;
            sysElement.addContent(marcoElement) ;
            marco.setElement(marcoElement) ;
        }
        marco.addMarco(name, value, desc) ;
        //this.save();
    }

    /**
     * 修改一个宏定义
     * @param name 宏名称
     * @param value 宏值
     */
    public void modifyMarco (String name, String value, String desc)
    {
        marco.modifyMarco(name, value, desc) ;
        //this.save();
    }

    /**
     * 删除一个宏定义
     * @param name 宏名称
     */
    public void removeMarco (String name)
        throws Exception
    {
        /*if(this.isFileContains("${"+name+"}") ){
         throw new ConfigException("Marco:"+name+" currently in use, can't be removed!");
                 }else{
            marco.removeMarco(name);
            this.save();
                 }*/
    }

    /**
     * 检查配置文件是否包含指定的字符串
     * @param ss
     * @return

         private boolean isFileContains(String ss){
        boolean res = false;
        java.io.FileReader fr = null;
        try {
            fr = new FileReader(this.getConfigFileName());
            char[] cs = new char[1024];
            String tmp = null;
            while((fr.read(cs)) != -1){
                tmp = new String(cs);
                if(tmp.indexOf(ss) != -1){
                    res = true;
                    break;
                }
            }
        }
        catch (Exception ex) { }
        try {
            fr.close();
        }
        catch (IOException ex1) {
        }
        return res;
         }
     */
    /**
     * For testing use
     * @param s Not used
     */
    public static void main (String[] s)
    {
        SystemConfig sc = new SystemConfig() ;
        sc.setConfigFile(
            "D:/xPortal16/sysmonitor_deploy/conf/system-config_tmp.xml") ;
        sc.load() ;
        sc.removeItem("OAM", "test") ;
        Item item = new Item("test") ;
        item.setId("111") ;
        item.setValue("http://asldfha.com:9&009/asldkfjh") ;
        try
        {
            sc.addItem("OAM", item) ;
            sc.save() ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace() ;
        }
        sc.removeItem("OAM", "arraTest") ;
        ArrayItem ai = new ArrayItem("arraTest") ;
        ai.setDescription("description *************") ;
        ArrayValue av = new ArrayValue() ;
        av.setId("333") ;
        av.setValue("&KKKKKKKKKKKKKKKKK") ;
        av.setReserved("") ;
        ai.addArrayValue(av) ;
        ArrayValue av1 = new ArrayValue() ;
        av1.setId("333") ;
        av1.setValue("&KKKKKKKKKKKKKKKKK") ;
        av1.setReserved("") ;
        ai.addArrayValue(av1) ;

        try
        {
            sc.addArrayItem("OAM", ai) ;
            sc.save() ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace() ;
        }
    }
}
