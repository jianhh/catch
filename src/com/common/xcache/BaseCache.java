
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
     * ��ȡ��ҳ���ݻ���
     * 
     * @param userKey�û��Զ���KEY
     * @param sql ��ѯSQL
     * @param dateVersionId ���ݰ汾ID
     * @param parameters ������Ϣ
     * @param currentPage ��ǰҳ
     * @param pageSize ÿҳ���ݴ�С
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
     * ��ȡkeyֵ
     * 
     * @param userKey �û��Զ���key
     * @param sql ��ѯsql
     * @param dateVersionId ���ݰ汾��ID
     * @param parameters ��ѯ����
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
