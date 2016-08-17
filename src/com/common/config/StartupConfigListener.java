package com.common.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StartupInterface;

/**
 * 根据系统启动项配置startup.properties，来决定启动那些进程或者配置
 * @author chenqishi
 *
 */

public class StartupConfigListener {
	private static JLogger logger = LoggerFactory.getLogger(StartupConfigListener.class);
	
	private static Properties startup = new Properties();
	
	StartupConfigListener(){}
	
	void init(){
		
		String configpath = ServerInfo.appRootPath+System.getProperty("file.separator")+"conf"+System.getProperty("file.separator")+"startup.properties";
		logger.debug("configpath = "+configpath);
		try{
            startup.load( new FileInputStream(configpath));
            this.doStart(this.getStartupList());
        }catch(Exception e)
        {
            logger.error("load Properties file error",e);
        }
        
	}
	
	private List getStartupList(){
		List ret = new ArrayList();
		Iterator it = startup.keySet().iterator();
		while(it.hasNext()){
			ret.add(it.next());
		}		
		return ret;
	}
	

	private void doStart(List l){
		for(int i=0;i<l.size();i++){
			String classname = "";
			try{
				classname = (String)l.get(i);
			}catch(Exception e){
				logger.error(e);
				logger.debug("系统启动项第"+i+"行配置出错");
			}
			
			StartupInterface si = null;
			Class obj = null;
			try{
				obj = Class.forName(classname); 
				
				//必须是StartupInterface扩展
				si = (StartupInterface)obj.newInstance();
			}catch(Exception ex){
				logger.error(ex);
				logger.debug(classname+"读取错误");
			}
			try{
				if(si != null){
					si.doStart();
					logger.debug("启动"+classname);
				}
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}
	
}
