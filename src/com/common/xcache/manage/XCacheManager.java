
package com.common.xcache.manage;

import java.io.IOException;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.config.XCacheConfig;
import com.google.code.yanf4j.core.impl.StandardSocketOption;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.KestrelCommandFactory;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class XCacheManager
{

    private static final JLogger logger = LoggerFactory.getLogger(XCacheManager.class);

    public long MAX = Integer.MAX_VALUE;

    private static XCacheConfig xconfig = XCacheConfig.getInstants();

    private static XCacheManager manager = new XCacheManager();
    
    private MemcachedClient client = null;
//    private static List<MemcachedClient> clientList;
    
    private XCacheManager(){
    	init();
    }

    public static XCacheManager getInstants()
    {
        if (manager==null){
            manager=new XCacheManager();
//            clientList = new ArrayList<MemcachedClient>();
        }
        return manager;
    }
    
    /**
     * 初始化Memcached链接
     */
    private void init(){
    	String services = xconfig.getServies(); // ///获取集群配置
        logger.debug("services:" + services);
        // host1:port1
        // host2:port2
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(services));
        builder.setCommandFactory(new BinaryCommandFactory());// //存储任意java序列化类型
        builder.setTranscoder(new SerializingTranscoder(xconfig.getCompressionSize()*1024)); //最大存储10M大小的对象
        
        int poolsize = xconfig.getConnectionPoolSize();
        builder.setConnectionPoolSize(poolsize); ////设置池连接数
        
        builder.setSocketOption(StandardSocketOption.SO_RCVBUF, xconfig.getRecBuffer() * 1024); // 设置接收缓存区为32K，默认16K
        builder.setSocketOption(StandardSocketOption.SO_SNDBUF, xconfig.getSetBuffer() * 1024); // 设置发送缓冲区为16K，默认为8K
        
        // 选举散列:net.rubyeye.xmemcached.impl.ElectionMemcachedSessionLocator
        // 一致性散列:net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator
         ///String sessionLocator = xconfig.getSessionLocator();
       // Class sessionLocatorObj = Class.forName(sessionLocator);
        builder.setSessionLocator(new KetamaMemcachedSessionLocator()); // ///进行hash
        try{
        	client = initClient(builder.build());
        	
        }catch(IOException ioe){
        	logger.error("初始化MEMCACHED Client失败"+ioe);
        }
        //设置全局操作timeout时间，默认为1秒
        client.setOpTimeout(xconfig.getTimeout());
        
    }
    
    /***************************************************************************
     * 获取Memcached客户端
     * 
     * @throws Exception
     */
    public MemcachedClient getMemcachedClient() throws Exception
    {

       if(client != null){ 
    	   return client;
       }else{
    	   throw new Exception("Client未初始化成功。不存在。");
       }
    }
    
    /**
     * 获取Memcached客户端
     * 不处理memcachedClientName参数
     */
    public MemcachedClient getMemcachedClient(String memcachedClientName) throws Exception
    {
    	
       if(client != null){ 
    	   
    	   return client;
       }else{
    	   throw new Exception("Client未初始化成功。不存在。");
       }
    }

    /***************************************************************************
     * 客户端初始化
     * 
     * @param memcachedClient
     */
    private MemcachedClient initClient(MemcachedClient memcachedClient)
    {

        int compressionSize = xconfig.getCompressionSize(); // ///大对象进行压缩
        
        ////builder.setTranscoder(new SerializingTranscoder());和这个要匹配
        (( SerializingTranscoder ) memcachedClient.getTranscoder()).setCompressionThreshold(compressionSize);
        
        
        return memcachedClient;
    }

}
