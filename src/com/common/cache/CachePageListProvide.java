
package com.common.cache;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.cache.impl.CachePageList;
import com.common.cache.util.ClassUtil;

public class CachePageListProvide extends CachePageList
{

    // 需执行的方法参数类型
    private Class[] methodParameterType;

    // DAO对象
    private Object daoObject;

    // 方法名称
    private String methodName;
    protected Object[] paramterObjx;
    
    private static final JLogger logger = LoggerFactory.getLogger(DBCacheSingleProvide.class);

    /**
     * 构建DAO执行的方法信息
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
     * 构建DAO对象信息
     * 
     * @param obj
     */
    public void buildObjectInfo(Object obj)
    {

        this.daoObject = obj;
    }

    /**
     * 构建数据对象在缓存中保存的键值
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
