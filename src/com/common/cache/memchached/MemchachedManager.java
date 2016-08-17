//
//package com.aspire.common.cache.memchached;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.aspire.common.log.proxy.JLogger;
//import com.aspire.common.log.proxy.LoggerFactory;
//import com.aspire.common.cache.constants.Constants;
//import com.aspire.common.cache.util.CachedConfig;
//import com.danga.MemCached.MemCachedClient;
//import com.danga.MemCached.SockIOPool;
//
//public class MemchachedManager
//{
//
//    private static final JLogger logger = LoggerFactory.getLogger(MemchachedManager.class);
//
//    private static CachedConfig config = CachedConfig.getInstants();
//    
//    private static MemchachedManager manager=new MemchachedManager();
//    
//    private MemchachedManager(){};
//    
//    public static synchronized  MemchachedManager getInstants()
//    {
//        if (manager==null)
//            manager=new MemchachedManager();
//        return manager;
//    }
//    
//    public  void init()
//    {
//
//        try
//        {
//            if (logger.isDebugEnabled())
//            logger.debug("缓存模块开始初始化........");
//            Map<String,List<String>> serviceMap=config.getServiceMap();
//            Map<String,List<Integer>> weightMap=config.getWeightsMap();
//            Object[] services=serviceMap.entrySet().toArray();
//            for(Object obj:services)
//            {
//          
//            Entry<String,List<String>> entry=(Entry<String,List<String>>)obj;
////            System.out.println("key: "+entry.getKey()+" value: "+entry.getValue());
//            //缓存服务器地址
//            SockIOPool pool = SockIOPool.getInstance(entry.getKey());
//            pool.setServers(entry.getValue().toArray(new String[entry.getValue().size()]));
//            List<Integer> weightList=weightMap.get(entry.getKey());
////            System.out.println("weightkey: "+entry.getKey());
////            System.out.println("weightList: "+weightList);
//            if (weightList != null && weightList.size() > 0)
//                {
//                    Integer[] weights = weightList.toArray(new Integer[weightList.size()]);
//                    // 缓存各服务器负载量
//                    pool.setWeights(weights);
//                }
//            //初使化边接数
//            pool.setInitConn(Integer.valueOf(config.getInitConnet()));
//            //最少连接数
//            pool.setMinConn(Integer.valueOf(config.getMinConnet()));
//            //最大连接数
//            pool.setMaxConn(Integer.valueOf(config.getMaxConnet()));
//            pool.setMaxIdle(Integer.valueOf(config.getMaxIdle()));
//            //主线程休眠时间
//            pool.setMaintSleep(Integer.valueOf(config.getMainSleepSize()));
//            // Tcp的规则就是在发送一个包之前，本地机器会等待远程主机
//            // 对上一次发送的包的确认信息到来；这个方法就可以关闭套接字的缓存，
//            // 以至这个包准备好了就发；
//            pool.setNagle(Boolean.valueOf(config.getNagle()));
//            // 连接建立后对超时的控制
//            pool.setSocketTO(Integer.valueOf(config.getSocketTo()));
//            // 连接建立时对超时的控制
//            pool.setSocketConnectTO(Integer.valueOf(config.getSocketConnectTO()));
//            // initialize the connection pool
//            // 初始化一些值并与MemcachedServer段建立连接
//            pool.initialize();
//            }
//            if (logger.isDebugEnabled())
//            logger.debug("缓存模块初始化完成....");
//        }
//        catch (Exception ex)
//        {
//            logger.debug("缓存模块初始化失败!");
//            logger.error("缓存初始化失败",ex);
//        }
//    }
//
//    /**
//     * 获取memcached 客户端对象
//     * 
//     * @return
//     */
//    public MemCachedClient getMemCachedClient()
//    {
//       return getMemCachedClient(Constants.DEFAULTMEMCACHENAME);
//    }
//    
//    public MemCachedClient getMemCachedClient(String name)
//    {
//        MemCachedClient client = new MemCachedClient(name);
//        client.setCompressEnable(Boolean.valueOf(config.getCompressEnable()));
//        client.setCompressThreshold(Long.valueOf(config.getCompressThreshold()));
//        return client;
//    }
//}
