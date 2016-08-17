
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
     * ��ʼ��Memcached����
     */
    private void init(){
    	String services = xconfig.getServies(); // ///��ȡ��Ⱥ����
        logger.debug("services:" + services);
        // host1:port1
        // host2:port2
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(services));
        builder.setCommandFactory(new BinaryCommandFactory());// //�洢����java���л�����
        builder.setTranscoder(new SerializingTranscoder(xconfig.getCompressionSize()*1024)); //���洢10M��С�Ķ���
        
        int poolsize = xconfig.getConnectionPoolSize();
        builder.setConnectionPoolSize(poolsize); ////���ó�������
        
        builder.setSocketOption(StandardSocketOption.SO_RCVBUF, xconfig.getRecBuffer() * 1024); // ���ý��ջ�����Ϊ32K��Ĭ��16K
        builder.setSocketOption(StandardSocketOption.SO_SNDBUF, xconfig.getSetBuffer() * 1024); // ���÷��ͻ�����Ϊ16K��Ĭ��Ϊ8K
        
        // ѡ��ɢ��:net.rubyeye.xmemcached.impl.ElectionMemcachedSessionLocator
        // һ����ɢ��:net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator
         ///String sessionLocator = xconfig.getSessionLocator();
       // Class sessionLocatorObj = Class.forName(sessionLocator);
        builder.setSessionLocator(new KetamaMemcachedSessionLocator()); // ///����hash
        try{
        	client = initClient(builder.build());
        	
        }catch(IOException ioe){
        	logger.error("��ʼ��MEMCACHED Clientʧ��"+ioe);
        }
        //����ȫ�ֲ���timeoutʱ�䣬Ĭ��Ϊ1��
        client.setOpTimeout(xconfig.getTimeout());
        
    }
    
    /***************************************************************************
     * ��ȡMemcached�ͻ���
     * 
     * @throws Exception
     */
    public MemcachedClient getMemcachedClient() throws Exception
    {

       if(client != null){ 
    	   return client;
       }else{
    	   throw new Exception("Clientδ��ʼ���ɹ��������ڡ�");
       }
    }
    
    /**
     * ��ȡMemcached�ͻ���
     * ������memcachedClientName����
     */
    public MemcachedClient getMemcachedClient(String memcachedClientName) throws Exception
    {
    	
       if(client != null){ 
    	   
    	   return client;
       }else{
    	   throw new Exception("Clientδ��ʼ���ɹ��������ڡ�");
       }
    }

    /***************************************************************************
     * �ͻ��˳�ʼ��
     * 
     * @param memcachedClient
     */
    private MemcachedClient initClient(MemcachedClient memcachedClient)
    {

        int compressionSize = xconfig.getCompressionSize(); // ///��������ѹ��
        
        ////builder.setTranscoder(new SerializingTranscoder());�����Ҫƥ��
        (( SerializingTranscoder ) memcachedClient.getTranscoder()).setCompressionThreshold(compressionSize);
        
        
        return memcachedClient;
    }

}
