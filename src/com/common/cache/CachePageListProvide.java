
package com.common.cache;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.cache.impl.CachePageList;
import com.common.cache.util.ClassUtil;

public class CachePageListProvide extends CachePageList
{

    // ��ִ�еķ�����������
    private Class[] methodParameterType;

    // DAO����
    private Object daoObject;

    // ��������
    private String methodName;
    protected Object[] paramterObjx;
    
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
        this.paramterObjx = paramterObj;
        this.methodName = methodName;
        setParamterObj(paramterObjx);
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
        setKey(key);
//        this.key = key;
    }
    
 
    
    @Override
    public void select() throws Exception
    {

        try
        {
             ClassUtil.excuteMethod(daoObject, ClassUtil.getMethodByName(daoObject.getClass(), methodParameterType, methodName),paramterObj);
        }
        catch (Exception e)
        {
            logger.error(e);
            throw e;
        }
    }

    
}
