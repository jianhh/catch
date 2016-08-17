package com.common.cache.util;

import java.lang.reflect.Method;


public class ClassUtil
{
    /**
     * 根据方法名称获取方法
     * @param cla 方法类class
     * @param methodType 方法参数类型
     * @param className 方法名称
     * @return 方法
     * @throws Exception
     */
    public static Method getMethodByName(Class cla,Class[] methodType,String className) throws Exception
    {
        return cla.getMethod(className,methodType);
    }
    
    /**
     * 执行方法
     * @param obj 方法对应的对象
     * @param method 方法
     * @param methodParamter 方法参数
     * @return
     * @throws Exception
     */
    public static Object excuteMethod(Object obj,Method method,Object[] methodParamter) throws Exception
    {
       return method.invoke(obj,methodParamter);
    }
    
}
