
package com.common.cache.impl;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.AbstractCache;
import com.common.xcache.manage.XCacheManager;
import com.common.cache.CachedVO;
import com.common.cache.vo.CachePageResultVO;
import net.rubyeye.xmemcached.MemcachedClient;

public abstract class CachePageList extends AbstractCache
{

    /**
     * 记录日志的实例对象
     */
    private static JLogger logger = LoggerFactory.getLogger(CachePageList.class);

    // 需执行的方法的参数信息
    protected Object[] paramterObj;
    
    
    public void delete(String key)
    {

    }

    public CachePageList()
    {
        super();
    }

    public CachePageList(String memcachedServiceName)
    {
        super(memcachedServiceName);
    }
    
    public void loadData() throws Exception
    {

        long startDate = 0;
        long endDate = 0;
        try
        {
            
            // 数据= 根据key获取缓存中的数据
            
                if (logger.isDebugEnabled())
                    logger.debug("数据缓存已打开,开始获取缓存数据信息.....");

                cacheObj = getDataFromCache();
                if (cacheObj != null)
                {
                    CachePageResultVO pageObj=(CachePageResultVO)cacheObj;
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("PageNo:" + pageObj.getCurrentPageNo());
                        logger.debug("TotalRows:" + pageObj.getTotalRows());
                    }
                    // 将缓存中查询到的数据赋值给传入的page对象
                    setParamterValue(pageObj);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("从缓存中读取数据消耗时间为: " + (endDate - startDate)
                                     + "毫秒");
                        logger.debug("获取的缓存数据对象为:" + pageObj);
                    }
                    return;
                }
                if (logger.isDebugEnabled())
                    logger.debug("缓存中不存在此数据，开始从数据库中查询");
                getDataFromDB();
                
                // 将数据添加到缓存中
                addPageInfoToCache(manager.getMemcachedClient());
               

        }
        catch (Exception ex)
        {
            logger.error(ex);
            throw ex;
        }
    }

    /**
     * 将数据添加到缓存中
     * 
     * @param client
     */
    private void addPageInfoToCache(MemcachedClient client) throws Exception
    {
        CachePageResultVO obj = ( CachePageResultVO ) getParamterValue();
        if (obj==null||obj.getPageInfo().size()==0)
        	return;
         super.addToCache(client, obj);
    }

    /**
     * 为注入的page对象赋值
     * 
     * @param value
     */
    public void setParamterValue(CachePageResultVO value)
    {
        if (logger.isDebugEnabled())
            logger.debug("开始设置值.....");
        for (int i = 0; i < paramterObj.length; i++)

            if (paramterObj[i] instanceof CachePageResultVO)
            {
                CachePageResultVO vo = ( CachePageResultVO ) paramterObj[i];
                vo.setCurrentPageNo(value.getCurrentPageNo());
                vo.setPageInfo(value.getPageInfo());
                vo.setPageSize(value.getPageSize());
                vo.setTotalPages(value.getTotalPages());
                vo.setTotalRows(value.getTotalRows());
                return;
            }
    }

    /**
     * 获取注入方法中传入的page对象
     * 
     * @return
     */
    public Object getParamterValue()
    {

        for (int i = 0; i < paramterObj.length; i++)
            if (paramterObj[i] instanceof CachePageResultVO)
            {
                return paramterObj[i];
            }
        return null;
    }

    public abstract void select() throws Exception;

    public void update(String key, CachedVO cachedVO)
    {

    }
    
    public void setParamterObj(Object[] paramterObj)
    {

        this.paramterObj = paramterObj;
    }

}
