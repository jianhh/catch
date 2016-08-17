package com.common.db ;

/**
 * <p>DAO异常类</p>
 * <p>DAO异常类，为所有的DAO类使用</p>
 * <p>Copyright (c) 2003-2005 ASPire TECHNOLOGIES (SHENZHEN) LTD All Rights Reserved</p>
 * @author shidr
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
public class DAOException extends Exception
{

    /**
     * 构造方法
     */
    public DAOException()
    {
        super();
    }

    /**
     * 构造方法
     * @param errorMsg 错误信息
     */
    public DAOException(String errorMsg)
    {
        super(errorMsg);
    }

    /**
     * 构造方法
     * @param cause 引起DAO异常的根源异常
     */
    public DAOException(Throwable cause)
    {
        super(cause);
    }

    /**
     * 构造方法
     * @param errorMsg 错误信息
     * @param cause 引起DAO异常的根源异常
     */
    public DAOException(String errorMsg, Throwable cause)
    {
        super(errorMsg, cause);
    }
}
