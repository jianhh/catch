
package com.common.cache;

public interface CacheDataInterface
{

//    void setKey(String key);

    void loadData() throws Exception;

    void update(String key, CachedVO cachedVO);

    void delete(String key);

    void setExpired(long value, int strategy);

    /**
     * ִ�����ݿ��ѯ
     * 
     */
    void select() throws Exception;
}
