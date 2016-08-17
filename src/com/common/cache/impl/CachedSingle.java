
package com.common.cache.impl;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.xcache.AbstractCache;
import com.common.cache.CachedVO;

import com.common.cache.vo.CachedVOSingle;


public abstract class CachedSingle extends AbstractCache
{

    private static final JLogger logger = LoggerFactory.getLogger(CachedSingle.class);

    public void delete(String key)
    {

    }

    public CachedSingle()
    {
       super();
    }
    public CachedSingle(String memcachedServiceName)
    {
        super(memcachedServiceName);
    }
    
    public CachedVOSingle getValue()
    {
        if (cacheObj==null)
            return null;
        return (CachedVOSingle)cacheObj;
    }

    public void update(String key, CachedVO cachedVO)
    {

    }

    public abstract void select()throws Exception;

}
