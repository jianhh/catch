package com.common.log.newproxy;


public class LogHelper
{
    /***   log文件根路径     */
    private static String LOG_ROOT_PATH = ".";
     
    
    public static void setLogRootPath(String logRootPath) {
        
        LOG_ROOT_PATH = logRootPath;
        
    } 
    
    
    public static String getLogRootPath() {
        
         return LOG_ROOT_PATH ;
         
         
    }    
}
