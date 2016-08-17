
package com.common.cache.impl;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.AbstractCache;
import com.common.cache.CachedVO;

import com.common.cache.vo.CachedVOList;

public abstract class CachedList extends AbstractCache
{

    private static final JLogger logger = LoggerFactory.getLogger(CachedList.class);

    public void delete(String key)
    {

    }

    public CachedList()
    {
       super();
    }
    
    public CachedList(String memcachedServiceName)
    {
        super(memcachedServiceName);
    }
    
    public CachedVOList getValue()
    {
        if (cacheObj==null)
            return null;
        return (CachedVOList)cacheObj;
    }

    public abstract void select()throws Exception;

    public void update(String key, CachedVO cachedVO)
    {

    }

}
