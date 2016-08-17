
package com.common.cache;
import java.io.Serializable;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.common.cache.impl.CachedSingle;
import com.common.cache.util.ClassUtil;

public class DBCacheSingleProvide extends CachedSingle
{

    
    // 需执行的方法的参数信息
    protected Object[] paramterObj;

    // 需执行的方法参数类型
    protected Class[] methodParameterType;

    // DAO对象
    protected Object daoObject;

    // 方法名称
    protected String methodName;
    
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
        this.paramterObj = paramterObj;
        this.methodName = methodName;
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
        this.setKey(key);
    }

    @Override
    public void select() throws Exception
    {

        try
        {
            cacheObj =(Serializable)ClassUtil.excuteMethod(daoObject,  ClassUtil.getMethodByName(daoObject.getClass(), methodParameterType, methodName),
                                                                       paramterObj);
        }
        catch (Exception e)
        {
            logger.error(e);
            throw e;
        }
    }

}
