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
 * ����ϵͳ����������startup.properties��������������Щ���̻�������
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
				logger.debug("ϵͳ�������"+i+"�����ó���");
			}
			
			StartupInterface si = null;
			Class obj = null;
			try{
				obj = Class.forName(classname); 
				
				//������StartupInterface��չ
				si = (StartupInterface)obj.newInstance();
			}catch(Exception ex){
				logger.error(ex);
				logger.debug(classname+"��ȡ����");
			}
			try{
				if(si != null){
					si.doStart();
					logger.debug("����"+classname);
				}
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}
	
}
