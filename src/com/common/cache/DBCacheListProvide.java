package com.common.cache;

import java.util.ArrayList;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.cache.impl.CachedList;
import com.common.cache.util.ClassUtil;
import com.common.cache.vo.CachedVOList;

public class DBCacheListProvide extends CachedList
{

    // ��ִ�еķ����Ĳ�����Ϣ
    protected Object[] paramterObj;

    // ��ִ�еķ�����������
    protected Class[] methodParameterType;

    // DAO����
    protected Object daoObject;

    // ��������
    protected String methodName;
    private static final JLogger logger = LoggerFactory.getLogger(DBCacheSingleProvide.class);


    /**
     * ����DAOִ�еķ�����Ϣ
     * 
     * @param methodName
     * @param methodParameterType
     * @param paramterObj
     */
    public void buildMethodInfo(String methodName, Class[] methodParameterType,
                                Object[] paramterObj)
    {

        this.methodParameterType = methodParameterType;
        this.paramterObj = paramterObj;
        this.methodName = methodName;
    }

    /**
     * ����DAO������Ϣ
     * 
     * @param obj
     */
    public void buildObjectInfo(Object obj)
    {

        this.daoObject = obj;
    }


    /**
     * �������ݶ����ڻ����б���ļ�ֵ
     * 
     * @param key
     */
    public void buildKey(String key) throws Exception
    {

        this.setKey(key);
    }

    @Override
    public void select() throws Exception
    {

        try
        {
            cacheObj =new CachedVOList();
           ((CachedVOList) cacheObj).setVoList((ArrayList) ClassUtil.excuteMethod(daoObject,
                                                                       ClassUtil.getMethodByName(daoObject.getClass(),
                                                                                                 methodParameterType,
                                                                                                 methodName),
                                                                       paramterObj));
        }
        catch (Exception e)
        {
            logger.error(e);
            throw e;
        }
    }

}
