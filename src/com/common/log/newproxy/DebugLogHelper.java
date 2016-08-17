/* 文 件 名:  DebugLogHelper.java
 * 版    权:  Copyright: Copyright (c) 2009-2011 ASPire TECHNOLOGIES (SHENZHEN) LTD All Rights Reserved
 * 描    述:  debug日志包装类。
 * 修 改 人:  YangChuang
 * 修改时间:  [V20120223,2012-02-29]
 * 修改内容:  新增
 */
package com.common.log.newproxy;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.Serializer;
import com.sun.org.apache.xml.internal.serialize.SerializerFactory;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * debug日志包装类。
 * @author YangChuang
 * @version [V20120223,2012-02-29]
 */
public class DebugLogHelper
{
    /**写日志的类*/
    private Class cls = null;
    
    /**系统配置的日志对象*/
    private Logger debugLogger = null;
    
    /**方法开始与结束的标识符*/
    //private static String debugcore = "[Core]";
    
    /**系统换行分隔符*/
    private static String LINESEPARATOR = System.getProperty("line.separator");
    
    /**
     * 构造方法。
     * @param cls 写日志的类。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public DebugLogHelper(Class cls)
    {
        try
        {
            this.cls = cls;
            debugLogger = Logger.getLogger(cls);
            
        }
        catch (Exception e)
        {
            System.out.println("Open DebugLog is Error!");
        }
    }
    
    /**
     * 进行方法的日志入口。
     * @param objects 要打印的参数（N）。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void enterFuncDebugLog(Object... objects)
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            Integer num = eles[index].getLineNumber();
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[进入方法]" + methodName + "." + num + "[参数]");
            if (objects == null || objects.length == 0)
            {
                str.append("(null)");
            }
            
            else
            {
                str.append(LINESEPARATOR + "(");
                str.append(LINESEPARATOR + "<Param>");
                for (int i = 0; i < objects.length; i++)
                {
                    str.append(LINESEPARATOR);
                    str.append("<Param" + (i + 1) + ">").append(LINESEPARATOR);
                    
                    String foramtParam = appendParam(null, objects[i]);
                    if (null != foramtParam)
                    {
                        str.append(foramtParam);
                        str.deleteCharAt(str.length() - 1);
                    }
                    else
                    {
                        str.append(convertObjectToXml(objects[i]));
                    }
                    
                    str.append(LINESEPARATOR).append("</Param" + (i + 1) + ">");
                }
                str.append(LINESEPARATOR).append("<Param>");
                str.append(LINESEPARATOR + ")");
                
            }
            
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            
            debugLogger.debug(e.toString());
        }
    }
    
    /**
     * 在方法的内打日志入口。
     * @param objects 要打印的参数（N）。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void atFuncDebugLog(Object... objects)
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[在方法]" + methodName + "[中的参数]");
            
            if (objects == null || objects.length == 0)
            {
                str.append("(null)");
            }
            else
            {
                str.append("( ");
                for (int i = 0; i < objects.length; i++)
                {
                    if (objects[i] != null)
                    {
                        str.append(appendParam(null, objects[i]));
                        
                    }
                }
                str.deleteCharAt(str.length() - 1);
                str.append(" )");
                
            }
            
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]atFuncDebugLog[Description]" + e.toString());
        }
    }
    
    /**
     * 在方法的内打日志入口。
     * @param desc 描述
     * @param object 要打印的参数。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void atFuncDebugLog(String desc, Object object)
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[在方法]" + methodName + "[中的参数]");
            
            str.append("( ");
            str.append(appendParam(desc, object));
            
            str.deleteCharAt(str.length() - 1);
            str.append(" )");
            
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]atFuncDebugLog[Description]" + e.toString());
        }
        
    }
    
    /**
     * 退出方法异常的日志入口。
     * @param object 要打印的参数。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void exitFuncDebugLog(Object obj)
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[退出方法]" + methodName + "[结果]");
            
            if (obj == null)
            {
                str.append("null");
            }
            else
            {
                str.append("( ");
                str.append(appendParam(null, obj));
                str.deleteCharAt(str.length() - 1);
                str.append(" )");
                
            }
            
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]exitFuncDebugLog[Description]" + e.toString());
        }
    }
    
    /**
     * 退出方法的日志入口。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void exitFuncDebugLog()
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[退出方法]" + methodName + "[结果]");
            str.append("void");
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]exitFuncDebugLog[Description]" + e.toString());
        }
    }
    
    /**
     * 方法异常的日志入口。
     * @param desc 描述
     * @param object 要打印的参数。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void excepFuncDebugLog(Object obj)
    {
        
        excepFuncDebugLog("", obj);
    }
    
    /**
     * 方法异常的日志入口。
     * @param desc 描述
     * @param object 要打印的参数。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void excepFuncDebugLog(String message, Object obj)
    {
        
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[异常]:");
            
            str.append(message);
            
            str.append(LINESEPARATOR);
            
            if (obj == null)
            {
                str.append("null");
            }
            else if (obj instanceof String)
            {
                str.append(obj);
            }
            else if (obj instanceof Exception)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                ((Exception)obj).printStackTrace(ps);
                String result = baos.toString();
                
                try
                {
                    ps.close();
                    baos.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                str.append("( ");
                str.append(result);
                str.append(" )");
            }
            else
            {
                str.append("( ");
                str.append(appendParam(null, obj));
                str.deleteCharAt(str.length() - 1);
                str.append(" )");
            }
            
            debugLogger.error(str.toString());
            if (isDebugEnable())
            {
                debugLogger.debug(str.toString());
            }
            
        }
        catch (Throwable e)
        {
            debugLogger.error("[异常][Function]excepFuncDebugLog[Description]" + e.toString());
            if (isDebugEnable())
            {
                debugLogger.debug("[异常][Function]excepFuncDebugLog[Description]" + e.toString());
            }
        }
    }
    
    /**
     * 获取当前类线程信息的方法。
     * @param eles 栈信息
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    private int getMethodIndex(StackTraceElement[] eles)
    {
        int index = 0;
        for (StackTraceElement ele : eles)
        {
            if (index > 5)
            {
                index = 3;
                break;
            }
            if (ele.getClassName().equals(cls.getName()))
            {
                break;
            }
            ++index;
        }
        
        return index;
    }
    
    /**
     * 获取当前类线程信息的方法。
     * @param name 名称
     * @param obj 要打印的参数。
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    private String appendParam(String name, Object obj)
    {
        StringBuffer strBuf = null;
        try
        {
            strBuf = new StringBuffer();
            
            if (obj == null)
            {
                strBuf.append(name);
                strBuf.append("=");
                strBuf.append("null,");
                return String.valueOf(strBuf);
            }
            
            boolean isClassType =
                Integer.class.isAssignableFrom(obj.getClass()) || Long.class.isAssignableFrom(obj.getClass())
                    || Character.class.isAssignableFrom(obj.getClass()) || Byte.class.isAssignableFrom(obj.getClass())
                    || Boolean.class.isAssignableFrom(obj.getClass());
            if (isClassType || String.class.isAssignableFrom(obj.getClass())
                || StringBuffer.class.isAssignableFrom(obj.getClass()) || Double.class.isAssignableFrom(obj.getClass())
                || Float.class.isAssignableFrom(obj.getClass()) || Date.class.isAssignableFrom(obj.getClass())
                || Number.class.isAssignableFrom(obj.getClass()))
            {
                
                strBuf.append(obj.getClass().getSimpleName());
                strBuf.append(name == null ? "" : (":" + name));
                strBuf.append(" = " + obj.toString() + ",");
            }
            
            else if (Map.class.isAssignableFrom(obj.getClass()))
            {
                Map map = (Map)obj;
                
                if (map.isEmpty())
                {
                    strBuf.append(map.getClass().getSimpleName() + "[],");
                    return String.valueOf(strBuf);
                }
                
                strBuf.append("\r" + map.getClass().getSimpleName());
                strBuf.append(name == null ? "" : (":" + name));
                strBuf.append("( size = " + map.size() + " ) = { ");
                Set keySet = map.entrySet();
                
                for (Iterator iter = keySet.iterator(); iter.hasNext();)
                {
                    Map.Entry entry = (Map.Entry)iter.next();
                    Object listObj = entry.getKey();
                    
                    if (listObj != null)
                    {
                        strBuf.append("\r\t[ " + appendParam(null, listObj));
                        strBuf.append(appendParam(null, entry.getValue()));
                        strBuf.deleteCharAt(strBuf.length() - 1);
                        strBuf.append(" ]");
                    }
                    
                }
                strBuf.append("\r},");
            }
            else if (Collection.class.isAssignableFrom(obj.getClass()))
            {
                Collection coll = (Collection)obj;
                
                if (coll.isEmpty())
                {
                    strBuf.append(coll.getClass().getSimpleName() + "[],");
                    return String.valueOf(strBuf);
                }
                
                strBuf.append("\r" + coll.getClass().getSimpleName());
                strBuf.append(name == null ? "" : (":" + name));
                strBuf.append("( size = " + coll.size() + ") = { ");
                
                for (Iterator iter = coll.iterator(); iter.hasNext();)
                {
                    Object listObj = iter.next();
                    
                    if (listObj != null)
                    {
                        strBuf.append("\r\t[ " + appendParam(null, listObj));
                        strBuf.deleteCharAt(strBuf.length() - 1);
                        strBuf.append(" ]");
                    }
                    
                }
                strBuf.append("\r},");
            }
            
            else if (Node.class.isAssignableFrom(obj.getClass()))
            {
                if (Document.class.isAssignableFrom(obj.getClass()))
                {
                    Document document = (Document)obj;
                    strBuf.append(DOM2String(document));
                }
                else
                {
                    Node node = (Node)obj;
                    strBuf.append(node2String(node, true));
                }
                strBuf.append(",");
            }
            else
            {
                BeanInfo info = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] props = info.getPropertyDescriptors();
                
                strBuf.append(obj.getClass().getName());
                strBuf.append(name == null ? "" : (":" + name));
                strBuf.append(" = ( ");
                
                for (PropertyDescriptor prop : props)
                {
                    String propName = prop.getName();
                    if (prop.getWriteMethod() == null || prop.getReadMethod() == null)
                    {
                        continue;
                    }
                    
                    Method readMethod = prop.getReadMethod();
                    Object paramObj = invokeMethod(readMethod, obj);
                    if (paramObj != null)
                    {
                        strBuf.append(appendParam(propName, paramObj));
                    }
                }
                strBuf.deleteCharAt(strBuf.length() - 1);
                strBuf.append(" ),");
            }
            
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]appendParam[Description]" + e.toString());
        }
        
        return String.valueOf(strBuf);
        
    }
    
    /**
     *  将对象过XMLEncode转换为String
     * @param requestObject 请求对象
     * @return String 对象转换后的String
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public String convertObjectToXml(Object requestObject)
    {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        
        BufferedOutputStream bos = new BufferedOutputStream(bo);
        
        XMLEncoder xmlEncoder = new XMLEncoder(bos);
        
        PersistenceDelegate pd = xmlEncoder.getPersistenceDelegate(Date.class);
        
        xmlEncoder.setPersistenceDelegate(Timestamp.class, pd);
        
        String content = null;
        
        try
        {
            // 使用XML编码器写对象
            xmlEncoder.writeObject(requestObject);
            
            xmlEncoder.close();
            
            content = bo.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bos.close();
                
                bo.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return content;
    }
    
    /**
     *  将DOM对象转换为String
     * @param doc DOM对象
     * @return String 对象转换后的String
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public String DOM2String(Document doc)
    {
        String docStr;
        ByteArrayOutputStream outPut;
        if (doc == null)
            return null;
        docStr = null;
        outPut = null;
        try
        {
            outPut = new ByteArrayOutputStream();
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            format.setEncoding("UTF-8");
            Serializer serializer = SerializerFactory.getSerializerFactory("xml").makeSerializer(outPut, format);
            serializer.asDOMSerializer().serialize(doc);
            docStr = new String(outPut.toByteArray(), "UTF-8");
        }
        catch (Exception ex)
        {
            
        }
        finally
        {
            
            IOUtils.closeQuietly(outPut);
        }
        
        return docStr;
    }
    
    /**
     * 将Node对象转换为String
     * @param doc DOM对象
     * @return String 对象转换后的String
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public final String node2String(Node node, boolean isPreserveSpace)
    {
        if (node == null)
            return null;
        if (node.getNodeType() == 9)
            node = ((Document)node).getDocumentElement();
        OutputFormat format = new OutputFormat(node.getOwnerDocument());
        format.setEncoding("UTF-8");
        format.setIndenting(false);
        format.setPreserveSpace(isPreserveSpace);
        StringWriter stringOut = new StringWriter();
        XMLSerializer serial = new XMLSerializer(stringOut, format);
        try
        {
            serial.asDOMSerializer();
            serial.serialize((Element)node);
        }
        catch (IOException ex)
        {
            
        }
        return stringOut.toString();
    }
    
    /**
     * 通过反射获取内部属性对象。
     * @param method 方法
     * @param target 类
     * @return Object 参数值
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public Object invokeMethod(Method method, Object target)
    {
        return invokeMethod(method, target, null);
    }
    
    /**
     * 通过反射获取内部属性对象。
     * @param method 方法
     * @param target 类
     * @return Object 参数值
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public Object invokeMethod(Method method, Object target, Object args[])
    {
        try
        {
            return method.invoke(target, args);
        }
        catch (IllegalAccessException ex)
        {
            handleReflectionException(ex);
            throw new IllegalStateException(
                (new StringBuilder("Unexpected reflection exception - ")).append(ex.getClass().getName())
                    .append(": ")
                    .append(ex.getMessage())
                    .toString());
        }
        catch (InvocationTargetException ex)
        {
            handleReflectionException(ex);
            throw new IllegalStateException(
                (new StringBuilder("Unexpected reflection exception - ")).append(ex.getClass().getName())
                    .append(": ")
                    .append(ex.getMessage())
                    .toString());
        }
    }
    
    /**
     * 处理异常
     * @param ex 异常
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void handleReflectionException(Exception ex)
    {
        if (ex instanceof NoSuchMethodException)
            throw new IllegalStateException((new StringBuilder("Method not found: ")).append(ex.getMessage())
                .toString());
        if (ex instanceof IllegalAccessException)
            throw new IllegalStateException((new StringBuilder("Could not access method: ")).append(ex.getMessage())
                .toString());
        if (ex instanceof InvocationTargetException)
            handleInvocationTargetException((InvocationTargetException)ex);
        throw new IllegalStateException((new StringBuilder("Unexpected reflection exception - ")).append(ex.getClass()
            .getName()).append(": ").append(ex.getMessage()).toString());
    }
    
    /**
     * 处理异常
     * @param ex 异常
     * @author YangChuang
     * @version [V20120223,2012-02-29]
     */
    public void handleInvocationTargetException(InvocationTargetException ex)
    {
        if (ex.getTargetException() instanceof RuntimeException)
            throw (RuntimeException)ex.getTargetException();
        if (ex.getTargetException() instanceof Error)
            throw (Error)ex.getTargetException();
        else
            throw new IllegalStateException(
                (new StringBuilder("Unexpected exception thrown by method - ")).append(ex.getTargetException()
                    .getClass()
                    .getName()).append(": ").append(ex.getTargetException().getMessage()).toString());
    }
    
    public boolean isDebugEnable()
    {
        return debugLogger.isDebugEnabled();
    }
    
    public void atCollectionSize(String desc, Collection collection)
    {
        if (!isDebugEnable())
        {
            return;
        }
        try
        {
            StackTraceElement[] eles = Thread.currentThread().getStackTrace();
            
            int index = getMethodIndex(eles);
            String methodName = eles[index].getMethodName();
            
            StringBuffer str = new StringBuffer();
            str.append("[");
            str.append(Thread.currentThread().hashCode());
            str.append("]");
            str.append("[在方法]" + methodName + "[中的参数]");
            
            str.append("( ");
            str.append(desc);
            str.append("[size]");
            str.append("=");
            if (null == collection)
            {
                str.append("null");
                
            }
            else
            {
                str.append(collection.size());
            }
            
            str.append(" )");
            
            debugLogger.debug(str.toString());
        }
        catch (Throwable e)
        {
            debugLogger.debug("[异常][Function]atFuncDebugLog[Description]" + e.toString());
        }
    }
}
