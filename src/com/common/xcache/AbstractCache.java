
package com.common.xcache;

import java.io.Serializable;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;

import com.common.cache.CacheDataInterface;
import com.common.cache.CachedVO;
import com.common.cache.constants.Constants;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.config.XCacheConfig;
import com.common.xcache.manage.XCacheManager;
import com.framework.base.BaseType;

public abstract class AbstractCache<T extends Serializable> implements
                CacheDataInterface
{

    private static final JLogger logger = LoggerFactory.getLogger(AbstractCache.class);

    // 需要缓存的数据
    protected T cacheObj;

    // key键标识
    protected String sign;

    // 是否将Key值缓存
    boolean isSaveKeyToCache = false;

    protected String memcacheName="";
    
    // 主键值
    protected String key;
    //获取列表对象总数的主键
    protected String countkey;

	protected static XCacheManager manager = XCacheManager.getInstants();

    private static XCacheConfig xconfig = XCacheConfig.getInstants();

 // 数据失效时间
    protected long timeValue;

    // 失效时间标识0:相对失效时长，1：绝对失效时长
    protected int trategy;
    
    public AbstractCache(){
    	//do nothing
    }
    
    public AbstractCache(String memcacheServiceName)
    {
        this.memcacheName=memcacheServiceName;
    }
    
    /**
     * 保存KEY值保有存到缓存
     * 
     * @param key
     * @param sign
     * @throws Exception
     */
    protected boolean saveKey(String key, String sign) throws Exception
    {

        if (sign == null)
            sign = "";
        else
            sign = sign.toLowerCase();
        
        MemcachedClient client = manager.getMemcachedClient();
        // 保存key值的主键
        String keySeqStr = Constants.KEYSIGN + sign;
        // 获取计数器
        long keySeq = client.incr(keySeqStr, 1);
        String primaryKey = sign + "_" + (keySeq);
        // 将key值保存到缓存中
        Object keyObj = client.get(primaryKey);
        if (logger.isDebugEnabled())
        {
            logger.debug("key值的主键为: " + primaryKey);
        }
        if (keyObj == null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("key值在缓存中不存在，将KEY值添加到缓存中.");
            }
            return client.add(primaryKey, 0, key);
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("key值在缓存中已存在，将缓存中的key值进行替换.");
            }
            return client.replace(primaryKey, 0, key);
        }
    }

    
    /**
     * 设置失效时间
     */
    public void setExpired(long value, int strategy)
    {

        trategy = strategy;
        timeValue = value;
    }
    
    /**
     * 从数据库中获取数据
     * 
     * @throws Exception
     */
    protected void getDataFromDB() throws Exception
    {

        long startDate = 0;
        long endDate = 0;
        startDate = System.currentTimeMillis();
        // 执行业务数据库查询方法
        select();
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("从数据库中读取数据消耗时间为: " + (endDate - startDate) + " 毫秒");
        }
    }

    /**
     * 从缓存中获取数据
     * 
     * @return
     * @throws Exception
     */
    protected T getDataFromCache() throws Exception
    {

        long startDate = 0;
        long endDate = 0;
        MemcachedClient client = manager.getMemcachedClient();
        if (logger.isDebugEnabled())
            logger.debug("开始在缓存中查找缓存数据");
        startDate = System.currentTimeMillis();
        if (logger.isDebugEnabled())
            logger.debug("开始取key为: " + key + "的数据");
        // 从缓存中查询数据信息

//        long timeout = this.xconfig.getTimeout(); // /设置连接超时时间
        T obj = ( T ) client.get(key);
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("从缓存中读取数据消耗时间为: " + (endDate - startDate) + "毫秒");
        }
        return obj;
    }

    public void loadData() throws Exception
    {
    	long startDate = System.currentTimeMillis();
        
        try
        {
            cacheObj = getDataFromCache();
            
        }
        catch (Exception e)
        {
            if (logger.isDebugEnabled())
                logger.debug("缓存服务器关闭:从数据库查询" );
        }
        if (logger.isDebugEnabled())
            logger.debug("获取的缓存数据对象为:" + cacheObj);
        // 如果缓存中不存在此数据
        if (cacheObj == null)
        {
            if (logger.isDebugEnabled())
                logger.debug("缓存中不存在此数据，开始从数据库中查询");
            getDataFromDB();
//            logger.debug("从数据库中获取数据："+(System.currentTimeMillis()-startDate));
            if (cacheObj instanceof List)
                if ((( List ) cacheObj).size() == 0)
                    return;
            // 将数据添加到缓存
            try
            {
            	addToCache(manager.getMemcachedClient());
//            	logger.debug("将数据添加到缓存中 ："+(System.currentTimeMillis()-startDate));
            }
            catch (Exception e)
            {
                if (logger.isDebugEnabled())
                    logger.debug("缓存服务器关闭:插入失败" );
            }
        }
    }

    /**
     * 将数据添加到缓存中
     * 
     * @param client
     */
    protected void addToCache(MemcachedClient client) throws Exception
    {

        addToCache(client, cacheObj);
    }

    protected void addToCache(MemcachedClient client, Object obj)
                    throws Exception
    {

        if (logger.isDebugEnabled())
            logger.debug("开始将数据保存到缓存中...");

        if (obj == null)
        {

            logger.debug("缓存对象为null,直接return");
            return;
        }
        long startDate = 0;
        long endDate = 0;
        boolean isOk = false;
        if (isSaveKeyToCache)
        {
            if (logger.isDebugEnabled())
                logger.debug("开始缓存Key值"+key);
            isOk = saveKey(key, sign);
            if (!isOk)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("缓存Key值失败"+key);
                    logger.debug("缓存数据对象失败"+obj);
                    return;
                }
            }
        }

        int expire = xconfig.getExpire();
        if (logger.isDebugEnabled())
        {
            logger.debug("缓存对象保存时间为: " + expire);
            logger.debug("缓存的对象为: " + obj + "key值为:" + key);
        }
        startDate = System.currentTimeMillis();
        isOk = client.add(key, expire, obj);

        if (logger.isDebugEnabled())
        {
            if (isOk)
                logger.debug("缓存数据成功");
            else
                logger.debug("缓存数据失败");
        }
    }
    
    
    protected void addCountToCache(MemcachedClient client, Object obj)
			throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("开始将数据保存到缓存中...");

		if (obj == null) {

			logger.debug("缓存对象为null,直接return");
			return;
		}
		long startDate = 0;
		long endDate = 0;
		boolean isOk = false;
		if (isSaveKeyToCache) {
			if (logger.isDebugEnabled())
				logger.debug("开始缓存Key值"+countkey);
			isOk = saveKey(countkey, sign);
			if (!isOk) {
				if (logger.isDebugEnabled()) {
					logger.debug("缓存Key值失败"+ countkey);
					logger.debug("缓存数据对象失败" +obj);
					return;
				}
			}
		}

		int expire = xconfig.getExpire();
		if (logger.isDebugEnabled()) {
			logger.debug("缓存对象保存时间为: " + expire);
			logger.debug("缓存的对象为: " + obj + "key值为:" + countkey);
		}
		startDate = System.currentTimeMillis();
		isOk = client.add(countkey, expire, obj);

		if (logger.isDebugEnabled()) {
			if (isOk)
				logger.debug("缓存数据成功");
			else
				logger.debug("缓存数据失败");
		}
	}

    /**
     * @param key
     */
    public void setKey(String key) throws Exception
    {

        if (key == null)
            throw new Exception("key 值不能为空");
        this.key = key;
    }

    public String getKey()
    {

        return key;
    }

    /**
     * 设置缓存Key值，并将Key值添加到缓存
     * 
     * @param key key值
     * @param sign key值分区标识
     * @throws Exception
     */
    public void buildKeyBySaveKey(String key, String sign) throws Exception
    {

        this.sign = sign;
        this.isSaveKeyToCache = true;
        setKey(key);
    }

    /**
     * 设置缓存Key值，并将Key值保存到缓存中(不将Key值进行分区标识)
     * 
     * @param key
     * @throws Exception
     */
    public void buildKeyBySaveKey(String key) throws Exception
    {

        this.isSaveKeyToCache = true;
        setKey(key);
    }
    
    /**
     * 从Memcached中获取某结果集的总数
     * @return
     * @throws Exception
     */
    protected BaseType getTotalCountNum() throws Exception{
    	BaseType vo = null;
    	
    	long startDate = 0;
        long endDate = 0;
        MemcachedClient client = manager.getMemcachedClient();
        if (logger.isDebugEnabled())
            logger.debug("开始在缓存中查找count总数缓存数据");
        startDate = System.currentTimeMillis();
        
        if (logger.isDebugEnabled())
            logger.debug("开始取key为: " + countkey + "的数据");
        // 从缓存中查询数据信息
        vo = ( BaseType ) client.get(countkey);
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("获取 count总数，从缓存中读取数据消耗时间为: " + (endDate - startDate) + "毫秒");
        }
        
        
        return vo;
    }

    public String getCountkey() {
		return countkey;
	}

	public void setCountkey(String countkey) {
		this.countkey = countkey;
	}

	
    public abstract void delete(String key);

    public abstract void select() throws Exception;

    public abstract void update(String key, CachedVO cachedVO);

}
