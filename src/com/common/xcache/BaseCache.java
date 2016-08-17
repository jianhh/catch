
package com.common.xcache;

import com.common.xcache.AbstractCache;
import com.common.cache.CachedVO;
import com.framework.dc.util.DCUtil;
import com.framework.util.CommonUtil;

public abstract class BaseCache extends AbstractCache
{

    @Override
    public void delete(String key)
    {

        // TODO Auto-generated method stub

    }

    @Override
    public void select() throws Exception
    {

        // TODO Auto-generated method stub

    }

    @Override
    public void update(String key, CachedVO cachedVO)
    {

        // TODO Auto-generated method stub

    }

    /**
     * 获取分页数据缓存
     * 
     * @param userKey用户自定义KEY
     * @param sql 查询SQL
     * @param dateVersionId 数据版本ID
     * @param parameters 参数信息
     * @param currentPage 当前页
     * @param pageSize 每页数据大小
     * @return
     */
    public String getPageQueryKey(String userKey, String sql,
                                  String dateVersionId, String[] parameters,
                                  int currentPage, int pageSize)
    {

        StringBuffer buffer = new StringBuffer(getKey(userKey,
                                                      sql,
                                                      dateVersionId,
                                                      parameters));
        buffer.append("{size:" + pageSize);
        buffer.append("}");
        buffer.append("{cur:" + currentPage);
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * 获取key值
     * 
     * @param userKey 用户自定义key
     * @param sql 查询sql
     * @param dateVersionId 数据版本号ID
     * @param parameters 查询参数
     * @return
     */
    public String getKey(String userKey, String sql, String dataVersionId,
                         String[] parameters)
    {

        String _key = userKey;
        if (_key == null || "".equals(_key))
        {
            _key = "{"
                   + sql.hashCode()
                   + "}{"
                   + DCUtil.getInstance()
                                           .getDataVersion(dataVersionId) + "}"
                   + CommonUtil.getMemcachedKey(parameters);
        }
        return _key;
    }


    public abstract Object query() throws Exception;
}
