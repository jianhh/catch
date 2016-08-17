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
// * 缓存处理基类
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
//    // 主键值
//    protected String key;
//
//    // 数据失效时间
//    protected long timeValue;
//
//    // 失效时间标识0:相对失效时长，1：绝对失效时长
//    protected int trategy;
//
//    // 需要缓存的数据
//    protected T cacheObj;
//
//    // key键标识
//    protected String sign;
//
//    // 是否将Key值缓存
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
//            logger.error("对象初使化失败数据将不进行缓存", e);
//        }
//    }
//    public AbstractCache(String memcacheServiceName)
//    {
//        this.memcacheName=memcacheServiceName;
//    }
//
//    /**
//     * 设置系统默认的缓存失效策略，此策略来自缓存配置文件
//     * 
//     * @throws Exception
//     */
//    protected void setExpireTimeFromConfig() throws Exception
//    {
//
//        try
//        {
//            trategy = Integer.valueOf(config.getStrategy());
//            // 如果设置为相对失效时间
//            if (Constants.XDSTRATEGY == trategy)
//                timeValue = Long.valueOf(config.getTimeValue());
//            // 如果设置为绝对失效时间
//            else if (Constants.JDSTRATEGY == trategy)
//                timeValue = DateUtil.addNowYearMToDatePotint(config.getTimeValue())
//                                    .getTime();
//        }
//        catch (Exception e)
//        {
//            logger.error("对象初使化失败数据将不进行缓存", e);
//        }
//    }
//
//    /**
//     * 保存KEY值保有存到缓存
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
//        // 保存key值的主键
//        String keySeqStr = Constants.KEYSIGN + sign;
//        //获取计数器
//        long keySeq =client.addOrIncr(keySeqStr,1);
//        String primaryKey = sign + "_" + (keySeq);
//        // 将key值保存到缓存中
//        Object keyObj = client.get(primaryKey);
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("key值的主键为: " + primaryKey);
//        }
//        if (keyObj == null)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("key值在缓存中不存在，将KEY值添加到缓存中.");
//            }
//            return client.add(primaryKey, key);
//        }
//        else
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("key值在缓存中已存在，将缓存中的key值进行替换.");
//            }
//            return client.replace(primaryKey, key);
//        }
//    }
//
//    /**
//     * 从数据库中获取数据
//     * 
//     * @throws Exception
//     */
//    protected void getDataFromDB() throws Exception
//    {
//        long startDate = 0;
//        long endDate = 0;
//        startDate = System.currentTimeMillis();
//        // 执行业务数据库查询方法
//        select();
//        if (logger.isDebugEnabled())
//        {
//            endDate = System.currentTimeMillis();
//            logger.debug("从数据库中读取数据消耗时间为: " + (endDate - startDate) + " 毫秒");
//        }
//    }
//
//    /**
//     * 从缓存中获取数据
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
//        logger.debug("开始在缓存中查找缓存数据");
//        startDate = System.currentTimeMillis();
//        if (logger.isDebugEnabled())
//        logger.debug("开始取key为: " + key + "的数据");
//        // 从缓存中查询数据信息
//        T obj = ( T ) client.get(key);
//        if (logger.isDebugEnabled())
//        {
//            endDate = System.currentTimeMillis();
//            logger.debug("从缓存中读取数据消耗时间为: " + (endDate - startDate) + "毫秒");
//        }
//        return obj;
//    }
//
//    public void loadData() throws Exception
//    {
//
//        try
//        {
//            // 如果缓存开关开启
//            if (Boolean.valueOf(config.getIsOpen()))
//            {
//                cacheObj = getDataFromCache();
//                if (logger.isDebugEnabled())
//                logger.debug("获取的缓存数据对象为:" + cacheObj);
//                // 如果缓存中不存在此数据
//                if (cacheObj == null)
//                {
//                    if (logger.isDebugEnabled())
//                    logger.debug("缓存中不存在此数据，开始从数据库中查询");
//                    getDataFromDB();
//                    if (cacheObj instanceof List)
//						if (((List) cacheObj).size() == 0)
//							return;
//                    // 将数据添加到缓存
//                    addToCache(manager.getMemCachedClient(memcacheName));
//                }
//            }
//            // 如果缓存功能关闭
//            else
//            {
//                if (logger.isDebugEnabled())
//                logger.debug("数据缓存已关闭，直接读取数据库数据");
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
//     * 将数据添加到缓存中
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
//         logger.debug("开始将数据保存到缓存中...");
//        
//        if(obj==null){
//            
//            logger.info("缓存对象为null,直接return");
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
//            logger.debug("开始缓存Key值");
//            isOk = saveKey(key, sign);
//            if (!isOk)
//            {
//                if (logger.isDebugEnabled())
//                {
//                    logger.debug("缓存Key值失败");
//                    logger.debug("缓存数据对象失败");
//                    return;
//                }
//            }
//        }
//        // 如果是相对失效时间
//        if (Constants.XDSTRATEGY == trategy)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("配置项为:" + trategy + "保存对象在相对时间内失效相对时间为: "
//                             + timeValue);
//                logger.debug("缓存的对象为: " + obj + "key值为:" + key);
//            }
//            startDate = System.currentTimeMillis();
//            isOk = client.add(key, obj, new Date(System.currentTimeMillis()
//                                                 + timeValue));
//            if (logger.isDebugEnabled())
//            {
//                endDate = System.currentTimeMillis();
//                logger.debug("保存数据到缓存中消耗时间为: " + (endDate - startDate) + "毫秒");
//                if (isOk)
//                    logger.debug("缓存数据成功");
//                else
//                    logger.debug("缓存数据失败");
//            }
//            return;
//        }
//        // 如果是绝对失效时间
//        else if (Constants.JDSTRATEGY == trategy)
//        {
//            if (logger.isDebugEnabled())
//            {
//                logger.debug("配置项为:" + trategy + "保存对象在绝对时间内失效时间为:" + timeValue);
//                logger.debug("缓存的对象为: " + obj + "key值为:" + key);
//            }
//            isOk = client.add(key, obj, new Date(timeValue));
//            if (logger.isDebugEnabled())
//            {
//                if (isOk)
//                    logger.debug("缓存数据成功");
//                else
//                    logger.debug("缓存数据失败");
//            }
//            return;
//        }
//        if (logger.isDebugEnabled())
//         logger.debug("失效时间设置有误，将不缓存数据...");
//    }
//
//    /**
//     * @param key
//     */
//    public void setKey(String key) throws Exception
//    {
//
//        if (key == null)
//            throw new Exception("key 值不能为空");
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
//     * 设置失效时间
//     */
//    public void setExpired(long value, int strategy)
//    {
//
//        trategy = strategy;
//        timeValue = value;
//    }
//
//    /**
//     * 设置缓存Key值，并将Key值添加到缓存
//     * 
//     * @param key key值
//     * @param sign key值分区标识
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
//     * 设置缓存Key值，并将Key值保存到缓存中(不将Key值进行分区标识)
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
