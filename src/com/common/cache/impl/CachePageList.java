
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
     * ��¼��־��ʵ������
     */
    private static JLogger logger = LoggerFactory.getLogger(CachePageList.class);

    // ��ִ�еķ����Ĳ�����Ϣ
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
            
            // ����= ����key��ȡ�����е�����
            
                if (logger.isDebugEnabled())
                    logger.debug("���ݻ����Ѵ�,��ʼ��ȡ����������Ϣ.....");

                cacheObj = getDataFromCache();
                if (cacheObj != null)
                {
                    CachePageResultVO pageObj=(CachePageResultVO)cacheObj;
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("PageNo:" + pageObj.getCurrentPageNo());
                        logger.debug("TotalRows:" + pageObj.getTotalRows());
                    }
                    // �������в�ѯ�������ݸ�ֵ�������page����
                    setParamterValue(pageObj);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("�ӻ����ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate)
                                     + "����");
                        logger.debug("��ȡ�Ļ������ݶ���Ϊ:" + pageObj);
                    }
                    return;
                }
                if (logger.isDebugEnabled())
                    logger.debug("�����в����ڴ����ݣ���ʼ�����ݿ��в�ѯ");
                getDataFromDB();
                
                // ��������ӵ�������
                addPageInfoToCache(manager.getMemcachedClient());
               

        }
        catch (Exception ex)
        {
            logger.error(ex);
            throw ex;
        }
    }

    /**
     * ��������ӵ�������
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
     * Ϊע���page����ֵ
     * 
     * @param value
     */
    public void setParamterValue(CachePageResultVO value)
    {
        if (logger.isDebugEnabled())
            logger.debug("��ʼ����ֵ.....");
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
     * ��ȡע�뷽���д����page����
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
