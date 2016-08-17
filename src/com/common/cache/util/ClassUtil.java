package com.common.cache.util;

import java.lang.reflect.Method;


public class ClassUtil
{
    /**
     * ���ݷ������ƻ�ȡ����
     * @param cla ������class
     * @param methodType ������������
     * @param className ��������
     * @return ����
     * @throws Exception
     */
    public static Method getMethodByName(Class cla,Class[] methodType,String className) throws Exception
    {
        return cla.getMethod(className,methodType);
    }
    
    /**
     * ִ�з���
     * @param obj ������Ӧ�Ķ���
     * @param method ����
     * @param methodParamter ��������
     * @return
     * @throws Exception
     */
    public static Object excuteMethod(Object obj,Method method,Object[] methodParamter) throws Exception
    {
       return method.invoke(obj,methodParamter);
    }
    
}
