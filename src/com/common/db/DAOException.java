package com.common.db ;

/**
 * <p>DAO�쳣��</p>
 * <p>DAO�쳣�࣬Ϊ���е�DAO��ʹ��</p>
 * <p>Copyright (c) 2003-2005 ASPire TECHNOLOGIES (SHENZHEN) LTD All Rights Reserved</p>
 * @author shidr
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
public class DAOException extends Exception
{

    /**
     * ���췽��
     */
    public DAOException()
    {
        super();
    }

    /**
     * ���췽��
     * @param errorMsg ������Ϣ
     */
    public DAOException(String errorMsg)
    {
        super(errorMsg);
    }

    /**
     * ���췽��
     * @param cause ����DAO�쳣�ĸ�Դ�쳣
     */
    public DAOException(Throwable cause)
    {
        super(cause);
    }

    /**
     * ���췽��
     * @param errorMsg ������Ϣ
     * @param cause ����DAO�쳣�ĸ�Դ�쳣
     */
    public DAOException(String errorMsg, Throwable cause)
    {
        super(errorMsg, cause);
    }
}
