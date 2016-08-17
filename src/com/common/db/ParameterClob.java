package com.common.db;

public class ParameterClob
{
    private String text ;

    private int length ;

    public ParameterClob(String text)
    {
        this.text = text;
        this.length = text.length();

    }

    public String getText ()
    {
        return text ;
    }

    public int getLength ()
    {
        return length ;
    }

    public String toString()
    {
        return "ClobString,"+length;
    }
}
