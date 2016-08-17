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
//            logger.debug("����ģ�鿪ʼ��ʼ��........");
//            Map<String,List<String>> serviceMap=config.getServiceMap();
//            Map<String,List<Integer>> weightMap=config.getWeightsMap();
//            Object[] services=serviceMap.entrySet().toArray();
//            for(Object obj:services)
//            {
//          
//            Entry<String,List<String>> entry=(Entry<String,List<String>>)obj;
////            System.out.println("key: "+entry.getKey()+" value: "+entry.getValue());
//            //�����������ַ
//            SockIOPool pool = SockIOPool.getInstance(entry.getKey());
//            pool.setServers(entry.getValue().toArray(new String[entry.getValue().size()]));
//            List<Integer> weightList=weightMap.get(entry.getKey());
////            System.out.println("weightkey: "+entry.getKey());
////            System.out.println("weightList: "+weightList);
//            if (weightList != null && weightList.size() > 0)
//                {
//                    Integer[] weights = weightList.toArray(new Integer[weightList.size()]);
//                    // �����������������
//                    pool.setWeights(weights);
//                }
//            //��ʹ���߽���
//            pool.setInitConn(Integer.valueOf(config.getInitConnet()));
//            //����������
//            pool.setMinConn(Integer.valueOf(config.getMinConnet()));
//            //���������
//            pool.setMaxConn(Integer.valueOf(config.getMaxConnet()));
//            pool.setMaxIdle(Integer.valueOf(config.getMaxIdle()));
//            //���߳�����ʱ��
//            pool.setMaintSleep(Integer.valueOf(config.getMainSleepSize()));
//            // Tcp�Ĺ�������ڷ���һ����֮ǰ�����ػ�����ȴ�Զ������
//            // ����һ�η��͵İ���ȷ����Ϣ��������������Ϳ��Թر��׽��ֵĻ��棬
//            // ���������׼�����˾ͷ���
//            pool.setNagle(Boolean.valueOf(config.getNagle()));
//            // ���ӽ�����Գ�ʱ�Ŀ���
//            pool.setSocketTO(Integer.valueOf(config.getSocketTo()));
//            // ���ӽ���ʱ�Գ�ʱ�Ŀ���
//            pool.setSocketConnectTO(Integer.valueOf(config.getSocketConnectTO()));
//            // initialize the connection pool
//            // ��ʼ��һЩֵ����MemcachedServer�ν�������
//            pool.initialize();
//            }
//            if (logger.isDebugEnabled())
//            logger.debug("����ģ���ʼ�����....");
//        }
//        catch (Exception ex)
//        {
//            logger.debug("����ģ���ʼ��ʧ��!");
//            logger.error("�����ʼ��ʧ��",ex);
//        }
//    }
//
//    /**
//     * ��ȡmemcached �ͻ��˶���
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
