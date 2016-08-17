
package com.framework.base;

import com.common.cache.vo.CachedVOSingle;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * 基本类型数据存储VO
 * 
 */

public class BaseType extends CachedVOSingle
{

    private static final JLogger logger = LoggerFactory.getLogger(BaseType.class);

    private String s;

    private int i;

    private long l;

    private double d;

    public double getD()
    {

        return d;
    }

    public void setD(double d)
    {

        this.d = d;
    }

    public int getI()
    {

        return i;
    }

    public void setI(int i)
    {

        this.i = i;
    }

    public long getL()
    {

        return l;
    }

    public void setL(long l)
    {

        this.l = l;
    }

    public String getS()
    {

        return s;
    }

    public void setS(String s)
    {

        this.s = s;
    }

}
