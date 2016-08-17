
package com.common.log.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils
{

    /**
     * trim the string even when it's null
     *
     * @param str the string need to be trimmed
     * @return the trimmed string
     */
    public static String trim(String str)
    {

        if (str == null)
        {
            return "";
        }
        return str.trim();
    }

    /**
     * convert the ISO char encoding to GBK
     *
     * @param str the ISO encoding string
     * @return the GBK encoding string created by yanfeng at 13/5/2003 modified
     *         by yanfeng at14/7/2003 for recursive invoke of this function in
     *         log.error()
     */
    public static String ISOtoGBK(String str)
    {

        byte[] by = null;
        try
        {
            by = str.getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            return str;
        }
        try
        {
            String a = new String(by, "GBK");
            return a;
        }
        catch (UnsupportedEncodingException ex1)
        {
            ex1.printStackTrace();
            return str;
        }
    }

    /**
     * convert a date to string according to the format pattern
     *
     * @param date input date
     * @param pattern format pattern
     * @return the formated string
     */
    public static String toString(Date date, String pattern)
    {

        SimpleDateFormat fo = new SimpleDateFormat(pattern);
        return fo.format(date);
    }

    /**
     * replace a old substring with rep in str
     *
     * @param str the string need to be replaced
     * @param old the string need to be removed
     * @param rep the string to be inserted
     * @return string replaced
     */
    public static String replaceOnlyOnce(String str, String old, String rep)
    {

        if ((old == null) || old.equals(""))
        {// if old is null or blank return the original string
            return str;
        }
        if ((str == null) || str.equals(""))
        {// if str is null or blank return the original string
            return str;
        }
        int leftIndex = str.indexOf(old);
        if (leftIndex < 0)
        { // if no old string found so nothing to replace,return the origin
            return str;
        }
        String leftStr = str.substring(0, leftIndex);
        String rightStr = str.substring(leftIndex + old.length());
        return leftStr + rep + rightStr;
    }

    /**
     * Copy a file.
     *
     * @param source Source file to copy.
     * @param target Destination target file.
     * @throws IOException Failed to copy file.
     */
    public static void fileCopy(final File source, final File target)
                    throws IOException
    {

        /** The default size of the copy buffer. */
        int DEFAULT_BUFFER_SIZE = 8192; // 8k
        byte buff[] = new byte[DEFAULT_BUFFER_SIZE];

        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(source)));

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(target)));

        int read;

        try
        {
            while ((read = in.read(buff)) != -1)
            {
                out.write(buff, 0, read);
            }
        }
        finally
        {
            out.flush();
            in.close();
            out.close();
        }
    }
}
