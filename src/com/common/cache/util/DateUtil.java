package com.common.cache.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil
{   
    /**
     * 为时间点加上当前年月日
     * @return
     */
    public static Date addNowYearMToDatePotint(String dateTime) throws Exception
    {
        Date newDate=null;
        Calendar ca=Calendar.getInstance();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat yearDateFormat=new SimpleDateFormat("yyyy-MM-dd");
         newDate=format.parse(yearDateFormat.format(ca.getTime())+" "+dateTime);
        if (newDate.after(ca.getTime()))
        {
           ca.set(ca.DATE,ca.get(ca.DATE)+1);
           newDate=format.parse(yearDateFormat.format(ca.getTime())+" "+dateTime);
        }
        return newDate;
    }
}
