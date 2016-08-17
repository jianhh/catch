//
//package com.aspire.common.cache;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.List;
//
//import com.aspire.common.cache.constants.Constants;
//import com.aspire.common.cache.memchached.MemchachedManager;
//import com.aspire.common.cache.util.CachedConfig;
//import com.aspire.common.cache.util.DateUtil;
//import com.aspire.common.log.proxy.JLogger;
//import com.aspire.common.log.proxy.LoggerFactory;
//import com.danga.MemCached.MemCachedClient;
///**
// * ���洦�����
// * @author x_nijiangli
// *
// * @param <T>
// */
//public abstract class AbstractCache<T extends Serializable> implements
//                CacheDataInterface
//{
//
//    private static final JLogger logger = LoggerFactory.getLogger(AbstractCache.class);
//
//    protected static CachedConfig config = CachedConfig.getInstants();
//
//    protected static MemchachedManager manager = MemchachedManager.getInstants();
//    
//    protected String memcacheName=Constants.DEFAULTMEMCACHENAME;
//    // ����ֵ
//    protected String key;
//
//    // ����ʧЧʱ��
//    protected long timeValue;
//
//    // ʧЧʱ���ʶ0:���ʧЧʱ����1������ʧЧʱ��
//    protected int trategy;
//
//    // ��Ҫ���������
//    protected T cacheObj;
//
//    // key����ʶ
//    protected String sign;
//
//    // �Ƿ�Keyֵ����
//    boolean isSaveKeyToCache = false;
//
//    public AbstractCache()
//    {
//
//        try
//        {
//            setExpireTimeFromConfig();
//        }
//        catch (Exception e)
//        {
//            logger.error("�����ʹ��ʧ�����ݽ������л���", e);
//        }
//    }
//    public AbstractCache(String memcacheServiceName)
//    {
//        this.memcacheName=memcacheServiceName;
//    }
//
//    /**
//     * ����ϵͳĬ�ϵĻ���ʧЧ���ԣ��˲������Ի��������ļ�
//     * 
//     * @throws Exception
//     */
//    protected void setExpireTimeFromConfig() throws Exception
//    {
//
//        try
//        {
//            trategy = Integer.valueOf(config.getStrategy());
//            // �������Ϊ���ʧЧʱ��
//            if (Constants.XDSTRATEGY == trategy)
//                timeValue = Long.valueOf(config.getTimeValue());
//            // �������Ϊ����ʧЧʱ��
//            else if (Constants.JDSTRATEGY == trategy)
//                timeValue = DateUtil.addNowYearMToDatePotint(config.getTimeValue())
//                                    .getTime();
//        }
//        catch (Exception e)
//        {
//            logger.error("�����ʹ��ʧ�����ݽ������л���", e);
//        }
//    }
//
//    /**
//     * ����KEYֵ���д浽����
//     * 
//     * @param key
//     * @param sign
//     */
//    protected boolean saveKey(String key, String sign)
//    {
//        if (sign == null)
//            sign = "";
//        else
//            sign=sign.toLowerCase();
//        MemCachedClient client = manager.getMemCachedClient(memcacheName);
//        // ����keyֵ������
//        String keySeqStr = Constants.KEYSIGN + sign;
//        //��ȡ������
//        long keySeq =client.addOrIncr(keySeqStr,1);
//        String primaryKey = sign + "_" + (keySeq);
//        // ��keyֵ���浽������
//        Object keyObj = client.get(primaryKey);
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("keyֵ������Ϊ: " + primaryKey);
//        }
//        if (keyObj == null)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("keyֵ�ڻ����в����ڣ���KEYֵ��ӵ�������.");
//            }
//            return client.add(primaryKey, key);
//        }
//        else
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("keyֵ�ڻ������Ѵ��ڣ��������е�keyֵ�����滻.");
//            }
//            return client.replace(primaryKey, key);
//        }
//    }
//
//    /**
//     * �����ݿ��л�ȡ����
//     * 
//     * @throws Exception
//     */
//    protected void getDataFromDB() throws Exception
//    {
//        long startDate = 0;
//        long endDate = 0;
//        startDate = System.currentTimeMillis();
//        // ִ��ҵ�����ݿ��ѯ����
//        select();
//        if (logger.isDebugEnabled())
//        {
//            endDate = System.currentTimeMillis();
//            logger.debug("�����ݿ��ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate) + " ����");
//        }
//    }
//
//    /**
//     * �ӻ����л�ȡ����
//     * 
//     * @return
//     * @throws Exception
//     */
//    protected T getDataFromCache() throws Exception
//    {
//
//        long startDate = 0;
//        long endDate = 0;
//        MemCachedClient client = manager.getMemCachedClient(memcacheName);
//        if (logger.isDebugEnabled())
//        logger.debug("��ʼ�ڻ����в��һ�������");
//        startDate = System.currentTimeMillis();
//        if (logger.isDebugEnabled())
//        logger.debug("��ʼȡkeyΪ: " + key + "������");
//        // �ӻ����в�ѯ������Ϣ
//        T obj = ( T ) client.get(key);
//        if (logger.isDebugEnabled())
//        {
//            endDate = System.currentTimeMillis();
//            logger.debug("�ӻ����ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate) + "����");
//        }
//        return obj;
//    }
//
//    public void loadData() throws Exception
//    {
//
//        try
//        {
//            // ������濪�ؿ���
//            if (Boolean.valueOf(config.getIsOpen()))
//            {
//                cacheObj = getDataFromCache();
//                if (logger.isDebugEnabled())
//                logger.debug("��ȡ�Ļ������ݶ���Ϊ:" + cacheObj);
//                // ��������в����ڴ�����
//                if (cacheObj == null)
//                {
//                    if (logger.isDebugEnabled())
//                    logger.debug("�����в����ڴ����ݣ���ʼ�����ݿ��в�ѯ");
//                    getDataFromDB();
//                    if (cacheObj instanceof List)
//						if (((List) cacheObj).size() == 0)
//							return;
//                    // ��������ӵ�����
//                    addToCache(manager.getMemCachedClient(memcacheName));
//                }
//            }
//            // ������湦�ܹر�
//            else
//            {
//                if (logger.isDebugEnabled())
//                logger.debug("���ݻ����ѹرգ�ֱ�Ӷ�ȡ���ݿ�����");
//                getDataFromDB();
//            }
//        }
//        catch (Exception ex)
//        {
//            logger.error(ex);
//            throw ex;
//        }
//    }
//
//    /**
//     * ��������ӵ�������
//     * 
//     * @param client
//     */
//    protected void addToCache(MemCachedClient client) throws Exception
//    {
//
//        addToCache(client, cacheObj);
//    }
//
//    protected void addToCache(MemCachedClient client, Object obj)
//    {
//        if (logger.isDebugEnabled())
//         logger.debug("��ʼ�����ݱ��浽������...");
//        
//        if(obj==null){
//            
//            logger.info("�������Ϊnull,ֱ��return");
//            return ;
//        } 
//         
//        long startDate = 0;
//        long endDate = 0;
//        boolean isOk = false;
//
//        if (isSaveKeyToCache)
//        {
//            if (logger.isDebugEnabled())
//            logger.debug("��ʼ����Keyֵ");
//            isOk = saveKey(key, sign);
//            if (!isOk)
//            {
//                if (logger.isDebugEnabled())
//                {
//                    logger.debug("����Keyֵʧ��");
//                    logger.debug("�������ݶ���ʧ��");
//                    return;
//                }
//            }
//        }
//        // ��������ʧЧʱ��
//        if (Constants.XDSTRATEGY == trategy)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("������Ϊ:" + trategy + "������������ʱ����ʧЧ���ʱ��Ϊ: "
//                             + timeValue);
//                logger.debug("����Ķ���Ϊ: " + obj + "keyֵΪ:" + key);
//            }
//            startDate = System.currentTimeMillis();
//            isOk = client.add(key, obj, new Date(System.currentTimeMillis()
//                                                 + timeValue));
//            if (logger.isDebugEnabled())
//            {
//                endDate = System.currentTimeMillis();
//                logger.debug("�������ݵ�����������ʱ��Ϊ: " + (endDate - startDate) + "����");
//                if (isOk)
//                    logger.debug("�������ݳɹ�");
//                else
//                    logger.debug("��������ʧ��");
//            }
//            return;
//        }
//        // ����Ǿ���ʧЧʱ��
//        else if (Constants.JDSTRATEGY == trategy)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("������Ϊ:" + trategy + "��������ھ���ʱ����ʧЧʱ��Ϊ:" + timeValue);
//                logger.debug("����Ķ���Ϊ: " + obj + "keyֵΪ:" + key);
//            }
//            isOk = client.add(key, obj, new Date(timeValue));
//            if (logger.isDebugEnabled())
//            {
//                if (isOk)
//                    logger.debug("�������ݳɹ�");
//                else
//                    logger.debug("��������ʧ��");
//            }
//            return;
//        }
//        if (logger.isDebugEnabled())
//         logger.debug("ʧЧʱ���������󣬽�����������...");
//    }
//
//    /**
//     * @param key
//     */
//    public void setKey(String key) throws Exception
//    {
//
//        if (key == null)
//            throw new Exception("key ֵ����Ϊ��");
//        this.key = key;
//    }
//
//    public String getKey()
//    {
//
//        return key;
//    }
//
//    /**
//     * ����ʧЧʱ��
//     */
//    public void setExpired(long value, int strategy)
//    {
//
//        trategy = strategy;
//        timeValue = value;
//    }
//
//    /**
//     * ���û���Keyֵ������Keyֵ��ӵ�����
//     * 
//     * @param key keyֵ
//     * @param sign keyֵ������ʶ
//     * @throws Exception
//     */
//    public void buildKeyBySaveKey(String key, String sign) throws Exception
//    {
//        this.sign=sign;
//        this.isSaveKeyToCache = true;
//        setKey(key);
//    }
//
//    /**
//     * ���û���Keyֵ������Keyֵ���浽������(����Keyֵ���з�����ʶ)
//     * 
//     * @param key
//     * @throws Exception
//     */
//    public void buildKeyBySaveKey(String key) throws Exception
//    {
//        this.isSaveKeyToCache = true;
//        setKey(key);
//    }
//
//    public abstract void delete(String key);
//
//    public abstract void select() throws Exception;
//
//    public abstract void update(String key, CachedVO cachedVO);
//
//}
