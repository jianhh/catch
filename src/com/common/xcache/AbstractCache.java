
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

    // ��Ҫ���������
    protected T cacheObj;

    // key����ʶ
    protected String sign;

    // �Ƿ�Keyֵ����
    boolean isSaveKeyToCache = false;

    protected String memcacheName="";
    
    // ����ֵ
    protected String key;
    //��ȡ�б��������������
    protected String countkey;

	protected static XCacheManager manager = XCacheManager.getInstants();

    private static XCacheConfig xconfig = XCacheConfig.getInstants();

 // ����ʧЧʱ��
    protected long timeValue;

    // ʧЧʱ���ʶ0:���ʧЧʱ����1������ʧЧʱ��
    protected int trategy;
    
    public AbstractCache(){
    	//do nothing
    }
    
    public AbstractCache(String memcacheServiceName)
    {
        this.memcacheName=memcacheServiceName;
    }
    
    /**
     * ����KEYֵ���д浽����
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
        // ����keyֵ������
        String keySeqStr = Constants.KEYSIGN + sign;
        // ��ȡ������
        long keySeq = client.incr(keySeqStr, 1);
        String primaryKey = sign + "_" + (keySeq);
        // ��keyֵ���浽������
        Object keyObj = client.get(primaryKey);
        if (logger.isDebugEnabled())
        {
            logger.debug("keyֵ������Ϊ: " + primaryKey);
        }
        if (keyObj == null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("keyֵ�ڻ����в����ڣ���KEYֵ��ӵ�������.");
            }
            return client.add(primaryKey, 0, key);
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("keyֵ�ڻ������Ѵ��ڣ��������е�keyֵ�����滻.");
            }
            return client.replace(primaryKey, 0, key);
        }
    }

    
    /**
     * ����ʧЧʱ��
     */
    public void setExpired(long value, int strategy)
    {

        trategy = strategy;
        timeValue = value;
    }
    
    /**
     * �����ݿ��л�ȡ����
     * 
     * @throws Exception
     */
    protected void getDataFromDB() throws Exception
    {

        long startDate = 0;
        long endDate = 0;
        startDate = System.currentTimeMillis();
        // ִ��ҵ�����ݿ��ѯ����
        select();
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("�����ݿ��ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate) + " ����");
        }
    }

    /**
     * �ӻ����л�ȡ����
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
            logger.debug("��ʼ�ڻ����в��һ�������");
        startDate = System.currentTimeMillis();
        if (logger.isDebugEnabled())
            logger.debug("��ʼȡkeyΪ: " + key + "������");
        // �ӻ����в�ѯ������Ϣ

//        long timeout = this.xconfig.getTimeout(); // /�������ӳ�ʱʱ��
        T obj = ( T ) client.get(key);
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("�ӻ����ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate) + "����");
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
                logger.debug("����������ر�:�����ݿ��ѯ" );
        }
        if (logger.isDebugEnabled())
            logger.debug("��ȡ�Ļ������ݶ���Ϊ:" + cacheObj);
        // ��������в����ڴ�����
        if (cacheObj == null)
        {
            if (logger.isDebugEnabled())
                logger.debug("�����в����ڴ����ݣ���ʼ�����ݿ��в�ѯ");
            getDataFromDB();
//            logger.debug("�����ݿ��л�ȡ���ݣ�"+(System.currentTimeMillis()-startDate));
            if (cacheObj instanceof List)
                if ((( List ) cacheObj).size() == 0)
                    return;
            // ��������ӵ�����
            try
            {
            	addToCache(manager.getMemcachedClient());
//            	logger.debug("��������ӵ������� ��"+(System.currentTimeMillis()-startDate));
            }
            catch (Exception e)
            {
                if (logger.isDebugEnabled())
                    logger.debug("����������ر�:����ʧ��" );
            }
        }
    }

    /**
     * ��������ӵ�������
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
            logger.debug("��ʼ�����ݱ��浽������...");

        if (obj == null)
        {

            logger.debug("�������Ϊnull,ֱ��return");
            return;
        }
        long startDate = 0;
        long endDate = 0;
        boolean isOk = false;
        if (isSaveKeyToCache)
        {
            if (logger.isDebugEnabled())
                logger.debug("��ʼ����Keyֵ"+key);
            isOk = saveKey(key, sign);
            if (!isOk)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("����Keyֵʧ��"+key);
                    logger.debug("�������ݶ���ʧ��"+obj);
                    return;
                }
            }
        }

        int expire = xconfig.getExpire();
        if (logger.isDebugEnabled())
        {
            logger.debug("������󱣴�ʱ��Ϊ: " + expire);
            logger.debug("����Ķ���Ϊ: " + obj + "keyֵΪ:" + key);
        }
        startDate = System.currentTimeMillis();
        isOk = client.add(key, expire, obj);

        if (logger.isDebugEnabled())
        {
            if (isOk)
                logger.debug("�������ݳɹ�");
            else
                logger.debug("��������ʧ��");
        }
    }
    
    
    protected void addCountToCache(MemcachedClient client, Object obj)
			throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("��ʼ�����ݱ��浽������...");

		if (obj == null) {

			logger.debug("�������Ϊnull,ֱ��return");
			return;
		}
		long startDate = 0;
		long endDate = 0;
		boolean isOk = false;
		if (isSaveKeyToCache) {
			if (logger.isDebugEnabled())
				logger.debug("��ʼ����Keyֵ"+countkey);
			isOk = saveKey(countkey, sign);
			if (!isOk) {
				if (logger.isDebugEnabled()) {
					logger.debug("����Keyֵʧ��"+ countkey);
					logger.debug("�������ݶ���ʧ��" +obj);
					return;
				}
			}
		}

		int expire = xconfig.getExpire();
		if (logger.isDebugEnabled()) {
			logger.debug("������󱣴�ʱ��Ϊ: " + expire);
			logger.debug("����Ķ���Ϊ: " + obj + "keyֵΪ:" + countkey);
		}
		startDate = System.currentTimeMillis();
		isOk = client.add(countkey, expire, obj);

		if (logger.isDebugEnabled()) {
			if (isOk)
				logger.debug("�������ݳɹ�");
			else
				logger.debug("��������ʧ��");
		}
	}

    /**
     * @param key
     */
    public void setKey(String key) throws Exception
    {

        if (key == null)
            throw new Exception("key ֵ����Ϊ��");
        this.key = key;
    }

    public String getKey()
    {

        return key;
    }

    /**
     * ���û���Keyֵ������Keyֵ��ӵ�����
     * 
     * @param key keyֵ
     * @param sign keyֵ������ʶ
     * @throws Exception
     */
    public void buildKeyBySaveKey(String key, String sign) throws Exception
    {

        this.sign = sign;
        this.isSaveKeyToCache = true;
        setKey(key);
    }

    /**
     * ���û���Keyֵ������Keyֵ���浽������(����Keyֵ���з�����ʶ)
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
     * ��Memcached�л�ȡĳ�����������
     * @return
     * @throws Exception
     */
    protected BaseType getTotalCountNum() throws Exception{
    	BaseType vo = null;
    	
    	long startDate = 0;
        long endDate = 0;
        MemcachedClient client = manager.getMemcachedClient();
        if (logger.isDebugEnabled())
            logger.debug("��ʼ�ڻ����в���count������������");
        startDate = System.currentTimeMillis();
        
        if (logger.isDebugEnabled())
            logger.debug("��ʼȡkeyΪ: " + countkey + "������");
        // �ӻ����в�ѯ������Ϣ
        vo = ( BaseType ) client.get(countkey);
        if (logger.isDebugEnabled())
        {
            endDate = System.currentTimeMillis();
            logger.debug("��ȡ count�������ӻ����ж�ȡ��������ʱ��Ϊ: " + (endDate - startDate) + "����");
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
