package com.common.config ;

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
// @CheckItem@ OPT-huangbigui-20070123 CQ:13110 ��SystemConfig��ί�з�����GeneralConfig֮ǰ�ж�GeneralConfig��·���Ƿ��ѱ�����

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
     * �µ�ȫ�����ö���,���е����ò���ͨ��������תʵ��
     */
    private GeneralConfig generalConfig ;

    /**
     * Ĭ�Ϲ�����
     */
    public SystemConfig ()
    {
        generalConfig = GeneralConfig.getInstance() ;
    }

    /**
     * ���������ļ���·�����ƣ�JMX�ӿڡ�
     * @param file �����ļ���·������
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
     * ���������ļ�
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
     * ���ڴ�������Ϣ����֮�����ļ�������Ա�ļ�
     */
    //@CheckLine-20031022-oam-hecs:save����������synchronized�����е��õģ����Լ������߳�ͬ����
    public void save ()
    {
        try
        {
            generalConfig.save2DefaultFile() ;
            //֪ͨ�����Ķ���
            doRefresh() ;
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out) ;
        }
    }

    /**
     * ���첢����һ����ϵͳ�����࣬��������ϵͳ�����������
     * @param moduleName ��ϵͳ����
     * @return ��ϵͳ������
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
     * ȡ�����е���ϵͳ�������б�
     * @return ��ϵͳ�����ࣺModuleConfig����
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
     * ���ص�ǰ�ĺ궨����ʵ��
     * @return
     */
    public MarcoDef getMarcoConfig ()
    {
        return marco ;
    }

    /**
     * ����һ����ϵͳ��ϼ���
     * @param module ModuleConfig��
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
     * ɾ��һ����ϵͳ����
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
     * �޸���ϵͳ���õ�����
     * @param module ��ϵͳ����
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
     * �޸��ض��������ֵ
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
     * �޸�ָ���Ķ�ֵ������
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
     *  ����һ��������
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
     * ɾ��һ��������
     * @param moduleName ��ϵͳ����
     * @param itemName ����������
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
     * ע��һ���������ݼ�����
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
     * ȡ��һ���������ݼ�����
     * @param listener
     */
    public synchronized void removeConfigDataListener (ConfigDataListener
                                                       listener)
    {
        this.dataListeners.remove(listener) ;
    }

    /**
     * ����ÿ����ע���ConfigDataListenerʵ����ˢ�º�����ˢ�����á�
     */
// @CheckLine@ REQOAM-hecs-20030905 ���ﲻ���߳�ͬ����
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
     * ��������ϵͳ����
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
     * ����һ���궨��
     * @param name  ������
     * @param value ��ֵ
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
     * �޸�һ���궨��
     * @param name ������
     * @param value ��ֵ
     */
    public void modifyMarco (String name, String value, String desc)
    {
        marco.modifyMarco(name, value, desc) ;
        //this.save();
    }

    /**
     * ɾ��һ���궨��
     * @param name ������
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
     * ��������ļ��Ƿ����ָ�����ַ���
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
