
package com.common.cache;

public interface CacheDataInterface
{

//    void setKey(String key);

    void loadData() throws Exception;

    void update(String key, CachedVO cachedVO);

    void delete(String key);

    void setExpired(long value, int strategy);

    /**
     * 执行数据库查询
     * 
     */
    void select() throws Exception;
}
