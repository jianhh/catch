package com.framework.util;

/**
 * <p>Title: aspire xPortal project</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: www.aspire-tech.com</p>
 * @author achang
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

public class StringUtils {

	static JLogger log = LoggerFactory.getLogger(StringUtils.class);

	public static SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmSSS");

	public static Random rand = new Random();

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {

		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {

		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * @param str
	 *            the string need to be parsed
	 * @param delim
	 *            the delimiter to seperate created by YanFeng at 14/5/2003
	 */
	public static String[] parseToArray(String str, String delim) {

		ArrayList arr = new ArrayList();
		StringTokenizer st = new StringTokenizer(str, delim);
		while (st.hasMoreTokens()) {
			arr.add(st.nextToken());
		}
		String[] ret = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			ret[i] = (String) arr.get(i);
		}
		return ret;
	}

	/**
	 * replace a old substring with rep in str
	 * 
	 * @param str
	 *            the string need to be replaced
	 * @param old
	 *            the string need to be removed
	 * @param rep
	 *            the string to be inserted
	 * @return string replaced
	 */
	public static String replace(String str, String old, String rep) {

		if ((str == null) || (old == null) || (rep == null)) {// if one is
			// null return
			// ""
			return "";
		}
		int index = str.indexOf(old);
		if ((index < 0) || old.equals("")) { // if no old string found or
			// nothing to replace,return the
			// origin
			return str;
		}
		StringBuffer strBuf = new StringBuffer(str);
		while (index >= 0) { // found old part
			strBuf.delete(index, index + old.length());
			strBuf.insert(index, rep);
			index = strBuf.toString().indexOf(old);
		}
		return strBuf.toString();
	}

	/**
	 * replace a old substring with rep in str
	 * 
	 * @param str
	 *            the string need to be replaced
	 * @param old
	 *            the string need to be removed
	 * @param rep
	 *            the string to be inserted
	 * @return string replaced
	 * @CheckItem@ selfbug-yanfeng-20031023 only replace once ocurrence of old
	 */
	public static String replaceOnlyOnce(String str, String old, String rep) {

		if ((old == null) || old.equals("")) {// if old is null or blank
			// return the original string
			return str;
		}
		if ((str == null) || str.equals("")) {// if str is null or blank
			// return the original string
			return str;
		}
		int leftIndex = str.indexOf(old);
		if (leftIndex < 0) { // if no old string found so nothing to
			// replace,return the origin
			return str;
		}
		String leftStr = str.substring(0, leftIndex);
		String rightStr = str.substring(leftIndex + old.length());
		return leftStr + rep + rightStr;
	}

	/**
	 * get the string format of a date precise to millisecond
	 * 
	 * @param date
	 *            the input date
	 * @return the string created by yanfeng at13/5/2003
	 */
	public static String getTimeString(Date date) {

		String timePattren = "yyyyMMddHHmmssSSS";
		return toString(date, timePattren);
	}

	/**
	 * convert a date to string according to the format pattern
	 * 
	 * @param date
	 *            input date
	 * @param pattern
	 *            format pattern
	 * @return the formated string
	 */
	public static String toString(Date date, String pattern) {

		SimpleDateFormat fo = new SimpleDateFormat(pattern);
		return fo.format(date);
	}

	/**
	 * Deal with null strings converting them to "" instead. It also invokes
	 * String.trim() on the output.
	 * 
	 * @param foo
	 *            A String.
	 * @return A String.
	 */
	public static final String makeString(String foo) {

		return (foo == null ? "" : foo.trim());
	}

	/**
	 * Validates that the supplied string is neither <code>null</code> nor the
	 * empty string.
	 * 
	 * @param foo
	 *            The text to check.
	 * @return Whether valid.
	 */
	public static final boolean isValid(String foo) {

		return (foo != null && foo.length() > 0);
	}

	/**
	 * Determine whether a (trimmed) string is empty
	 * 
	 * @param foo
	 *            The text to check.
	 * @return Whether empty.
	 */
	public static final boolean isEmpty(String foo) {

		return (foo == null || foo.trim().length() == 0);
	}

	/**
	 * Determine whether a (trimmed) string is not empty
	 * 
	 * @param foo
	 * @return
	 */
	public static final boolean isNotEmpty(String foo) {

		return (null != foo && foo.trim().length() > 0);
	}

	/**
	 * Returns the output of printStackTrace as a String.
	 * 
	 * @param e
	 *            A Throwable.
	 * @return A String.
	 */
	public static final String stackTrace(Throwable e) {

		String foo = null;
		try {
			// And show the Error Screen.
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(buf, true));
			foo = buf.toString();
		} catch (Exception f) {
			// Do nothing.
		}
		return foo;
	}

	/**
	 * Returns the output of printStackTrace as a String.
	 * 
	 * @param e
	 *            A Throwable.
	 * @param addPre
	 *            a boolean to add HTML
	 * 
	 * <pre>
	 *  tags around the stacktrace
	 * &#064;return A String.
	 * 
	 */
	public static final String stackTrace(Throwable e, boolean addPre) {

		if (addPre) {
			return "<pre>" + stackTrace(e) + "</pre>";
		} else {
			return stackTrace(e);
		}
	}

	/**
	 * Compares two Strings, returns true if their values are the same.
	 * 
	 * @param s1
	 *            The first string.
	 * @param s2
	 *            The second string.
	 * @return True if the values of both strings are the same.
	 */
	public static boolean equals(String s1, String s2) {

		if (s1 == null) {
			return (s2 == null);
		} else if (s2 == null) {
			// s1 is not null
			return false;
		} else {
			return s1.equals(s2);
		}
	}

	public static final int PPKEY_CLASSNAME = 0;

	public static final int PPKEY_ID = 1;

	public static final int PPKEY_PROPERTY = 2;

	/**
	 * Takes a String of the form substring[substring]subtring and returns the 3
	 * substrings
	 * 
	 * @return a three element String array
	 */
	public static String[] parseObjectKey(String s) {

		String[] p = new String[3];
		StringTokenizer st = new StringTokenizer(s, "[]");
		int count = st.countTokens();
		if (count > 1) {
			p[0] = st.nextToken();
			p[1] = st.nextToken();
			if (count == 3) {
				p[2] = st.nextToken();
			}
		}
		return p;
	}

	/**
	 * Remove Underscores from a string and replaces first Letters with
	 * Capitals. foo_bar becomes FooBar
	 */
	public static String removeUnderScores(String data) {

		String temp = null;
		StringBuffer out = new StringBuffer();
		temp = data;

		StringTokenizer st = new StringTokenizer(temp, "_");
		while (st.hasMoreTokens()) {
			String element = (String) st.nextElement();
			out.append(firstLetterCaps(element));
		}
		return out.toString();
	}

	/**
	 * Makes the first letter caps and leaves the rest as is.
	 */
	public static String firstLetterCaps(String data) {

		StringBuffer sbuf = new StringBuffer(data.length());
		sbuf.append(data.substring(0, 1).toUpperCase()).append(
				data.substring(1));
		return sbuf.toString();
	}

	/**
	 * Splits the provided CSV text into a list.
	 * 
	 * @param text
	 *            The CSV list of values to split apart.
	 * @param separator
	 *            The separator character.
	 * @return The list of values.
	 */
	public static String[] split(String text, String separator) {

		StringTokenizer st = new StringTokenizer(text, separator);
		String[] values = new String[st.countTokens()];
		int pos = 0;
		while (st.hasMoreTokens()) {
			values[pos++] = st.nextToken();
		}
		return values;
	}

	/**
	 * Joins the elements of the provided array into a single string containing
	 * a list of CSV elements.
	 * 
	 * @param list
	 *            The list of values to join together.
	 * @param separator
	 *            The separator character.
	 * @return The CSV text.
	 */
	public static String join(String[] list, String separator) {

		StringBuffer csv = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				csv.append(separator);
			}
			csv.append(list[i]);
		}
		return csv.toString();
	}

	/**
	 * Takes a block of text which might have long lines in it and wraps the
	 * long lines based on the supplied wrapColumn parameter. It was initially
	 * implemented for use by VelocityEmail. If there are tabs in inString, you
	 * are going to get results that are a bit strange, since tabs are a single
	 * character but are displayed as 4 or 8 spaces. Remove the tabs.
	 * 
	 * @param inString
	 *            Text which is in need of word-wrapping.
	 * @param newline
	 *            The characters that define a newline.
	 * @param wrapColumn
	 *            The column to wrap the words at.
	 * @return The text with all the long lines word-wrapped.
	 */

	public static String wrapText(String inString, String newline,
			int wrapColumn) {

		StringTokenizer lineTokenizer = new StringTokenizer(inString, newline,
				true);
		StringBuffer stringBuffer = new StringBuffer();

		while (lineTokenizer.hasMoreTokens()) {
			try {
				String nextLine = lineTokenizer.nextToken();

				if (nextLine.length() > wrapColumn) {
					// This line is long enough to be wrapped.
					nextLine = wrapLine(nextLine, newline, wrapColumn);
				}

				stringBuffer.append(nextLine);
			} catch (NoSuchElementException nsee) {
				// thrown by nextToken(), but I don't know why it would
				break;
			}
		}

		return (stringBuffer.toString());
	}

	/**
	 * Wraps a single line of text. Called by wrapText(). I can't think of any
	 * good reason for exposing this to the public, since wrapText should always
	 * be used AFAIK.
	 * 
	 * @param line
	 *            A line which is in need of word-wrapping.
	 * @param newline
	 *            The characters that define a newline.
	 * @param wrapColumn
	 *            The column to wrap the words at.
	 * @return A line with newlines inserted.
	 */

	protected static String wrapLine(String line, String newline, int wrapColumn) {

		StringBuffer wrappedLine = new StringBuffer();

		while (line.length() > wrapColumn) {
			int spaceToWrapAt = line.lastIndexOf(' ', wrapColumn);

			if (spaceToWrapAt >= 0) {
				wrappedLine.append(line.substring(0, spaceToWrapAt));
				wrappedLine.append(newline);
				line = line.substring(spaceToWrapAt + 1);
			}

			// This must be a really long word or URL. Pass it
			// through unchanged even though it's longer than the
			// wrapColumn would allow. This behavior could be
			// dependent on a parameter for those situations when
			// someone wants long words broken at line length.
			else {
				spaceToWrapAt = line.indexOf(' ', wrapColumn);

				if (spaceToWrapAt >= 0) {
					wrappedLine.append(line.substring(0, spaceToWrapAt));
					wrappedLine.append(newline);
					line = line.substring(spaceToWrapAt + 1);
				} else {
					wrappedLine.append(line);
					line = "";
				}
			}
		}

		// Whatever is left in line is short enough to just pass through,
		// just like a small small kidney stone
		wrappedLine.append(line);

		return wrappedLine.toString();
	}

	/**
	 * convert the ISO char encoding to GBK
	 * 
	 * @param str
	 *            the ISO encoding string
	 * @return the GBK encoding string created by yanfeng at 13/5/2003 modified
	 *         by yanfeng at14/7/2003 for recursive invoke of this function in
	 *         log.error()
	 * @CheckItem@ SELFBUG-yanfeng-20030714 可能产生循环调用
	 */
	public static String ISOtoGBK(String str) {

		byte[] by = null;
		try {
			by = str.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return str;
		}
		try {
			String a = new String(by, "GBK");
			return a;
		} catch (UnsupportedEncodingException ex1) {
			ex1.printStackTrace();
			return str;
		}
	}

	/**
	 * trim the string even when it's null
	 * 
	 * @param str
	 *            the string need to be trimmed
	 * @return the trimmed string
	 */
	public static String trim(String str) {

		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 判断str是否在strArr中
	 * 
	 * @param str
	 * @param strArr
	 * @return true:str在strArr中出现,;false otherwise.
	 */
	public static boolean stringInArray(String str, String[] strArr) {

		for (int i = 0; i < strArr.length; i++) {
			if (str.equals(strArr[i]))
				return true;
		}
		return false;
	}

	/**
	 * 将带有格式的字符串转换成HTML方式
	 * 
	 * @param origine
	 * @return
	 */
	public static String convert2Html(String origine) {

		String outStr = null;
		if (origine != null) {
			String tmp = StringUtils.replace(origine, ">", "&gt;");
			String tmp2 = StringUtils.replace(tmp, "<", "&lt;");
			String tmp3 = StringUtils.replace(tmp2, " ", "&nbsp;");

			String tmp4 = StringUtils.replace(tmp3, "\r\n", "<br>");

			outStr = tmp4;
		} else {
			outStr = "";
		}
		return outStr;
	}

	/**
	 * 根据长度来填充/截取字符串
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @param len
	 *            固定长度
	 * @return String- 处理后的结果
	 */
	public static String formatStrByLen(String str, int len) {

		if (str == null)
			str = "";
		if (len == 0)
			return "";
		StringBuffer resultStr = new StringBuffer();
		resultStr.append(str);
		int str_leng = lengthOfHZ(str);
		if (str_leng == len) {
			return resultStr.toString();
		}
		if (str_leng < len) {// 需要填充
			for (int i = 0; i < len - str_leng; i++) {
				resultStr.append(" ");
			}
		} else {// 需要截取
			String result = formatByLen(str, len, "");
			return result;
		}
		return resultStr.toString();
	}

	/**
	 * 把一个字符串的根据长度len来截取，汉字当成两个字符计算，ascii英文字符当成一个。
	 * 
	 * @param aStr
	 *            目标字符串
	 * @param len
	 *            长度
	 * @param endStr
	 *            如果超出长度，在截取后的字符串尾巴上添加的字符
	 * @return 截取后的长度
	 */
	public static String formatByLen(String aStr, int len, String endStr) {

		char c;
		int length = len;
		int aStrLen = aStr.length();
		StringBuffer resultStr = new StringBuffer();
		int i;
		for (i = 0; i < aStrLen; i++) {
			c = aStr.charAt(i);
			if (c >= 127) {
				length -= 2;
			} else {
				length -= 1;
			}
			if (length >= 0) {
				resultStr.append(c);
			} else {
				break;
			}
		}
		if (i < aStrLen) {
			resultStr.append(endStr);
		}
		return resultStr.toString();
	}

	/**
	 * 计算一个字符串的长度，汉字当成两个字符计算，ascii英文字符当成一个。
	 * 
	 * @param aStr
	 *            要计算长度的目标字符串
	 * @return 计算出的长度
	 */
	public static int lengthOfHZ(String aStr) {

		char c;
		int length = 0;
		for (int i = 0; i < aStr.length(); i++) {
			c = aStr.charAt(i);
			if (c >= 127) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length;
	}

	/**
	 * 把时间转成指定格式的字符串
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static final String getDateToString(String format, Date date) {

		SimpleDateFormat df = null;
		String returnValue = "";

		if (date == null) {
			// logger.error("date is null!");
			return null;
		} else {
			df = new SimpleDateFormat(format);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 生成交易流水号 18位
	 * 
	 * @return
	 */
	public static String getTransactionID() {

		int k = rand.nextInt(900) + 100;
		String transactionId = fm.format(new Date());
		transactionId = transactionId.concat(String.valueOf(k));
		return transactionId;
	}

	public static void main(String[] args) {

		String a = "?";
		System.out.println(replace(a, "", "oo"));
	}

	/**
	 * 第一个字母小写
	 * 
	 * @return
	 */
	public static String getStringFirstLowerCase(String str) {

		if (StringUtils.isNotEmpty(str)) {
			if (str.length() == 1) {
				return str.toLowerCase();
			} else {
				String first = str.substring(0, 1).toLowerCase();
				return (first + str.substring(1));
			}

		}
		return str;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}