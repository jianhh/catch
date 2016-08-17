package com.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

public class DateUtil {

	private static final JLogger logger = LoggerFactory
			.getLogger(DateUtil.class);

	public static SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

	public static SimpleDateFormat format3 = new SimpleDateFormat("yyyyMM");

	public static SimpleDateFormat format4 = new SimpleDateFormat("yyyy年MM月dd日");

	public static SimpleDateFormat format5 = new SimpleDateFormat("yyyyMMdd");

	public static SimpleDateFormat format6 = new SimpleDateFormat("yyyy.MM.dd");

	public static SimpleDateFormat format7 = new SimpleDateFormat("yyyy/MM/dd");

	public static SimpleDateFormat format8 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws Exception {

		System.out.println(getDataStr("beforeYesterday"));
		System.out.println(getDataStr("yesterday"));
		System.out.println(getDataStr("today"));
		System.out.println(getDataStr("tomorrow"));
		System.out.println(getTimeNowStr());
		String date = getAddMinute(10);
		System.out.println(date);
		System.out.println(compareDateByString(date, getTimeNowStr()));
	}

	/**
	 * 判断 date 1 和date2 哪个日期大 如果date1>date2 1 如果date1 =date2 0 如果date1<date2 -1
	 * 要求date1,date2 格式为 yyyyMMddHHmmss
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public static int compareDateByString(String date1, String date2) {
		int result = 0;
		if (date1.equals(date2)) {
			return result;
		}
		try {
			Date date1Str = format.parse(date1);
			Date date2Str = format.parse(date2);
			if (date1Str.after(date2Str)) {
				result = 1;
			} else {
				result = -1;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	/**
	 * 根据类别获取指定日期字符串yyyyMMdd
	 * 
	 * @param type
	 *            前天：beforeYesterday; 昨天：yesterday ;今天：today ;明天：tomorrow
	 * 
	 * @return
	 */
	public static String getDataStr(String type) {
		Calendar cal = Calendar.getInstance();
		if ("today".equals(type)) {

		} else if ("yesterday".equals(type)) {
			cal.add(Calendar.DATE, -1);
		} else if ("beforeYesterday".equals(type)) {
			cal.add(Calendar.DATE, -2);
		} else if ("tomorrow".equals(type)) {
			cal.add(Calendar.DATE, +1);
		}

		Date d = cal.getTime();
		return format5.format(d);
	}

	/**
	 * 获取当前时间字符串
	 * 
	 * @return
	 */
	public static String getTimeNowStr() {
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		return format.format(d);
	}

	/**
	 * 获取当前时间字符串（加上多少分钟）
	 * 
	 * @param minute
	 *            分钟
	 * @return
	 */
	public static String getAddMinute(int minute) {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, minute);
		return format.format(nowTime.getTime());
	}

	/**
	 * 获取缓存失效时间
	 * 
	 * @return
	 */
	public static long getCacheDate() {

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		gc.add(GregorianCalendar.DATE, 1);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 5);
		gc.set(GregorianCalendar.SECOND, 0);
		return gc.getTimeInMillis();
	}

	/**
	 * 日期格式转化
	 */
	public static String changeDateToString(Date date, String format) {

		logger.debug("format:" + format);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String dataStr = dateFormat.format(date.getTime());
		logger.debug("dataStr:" + dataStr);
		return dataStr;
	}

	/**
	 * 日期格式转换
	 */
	public static String getFormatExpireDate(String date) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date dat = sd.parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			logger.debug("getFormatExpireDate is return");
			return sdf.format(dat);
		} catch (Exception e) {

			logger.error(e);
		}

		return "";
	}
	/**
	 * String 转Date
	 */
	public static Date getStringToDate(String date,String format) {

		SimpleDateFormat sd = new SimpleDateFormat(format);
		Date dat=new Date();
		try {
			 dat = sd.parse(date);
			logger.debug("getFormatExpireDate is return");
			return dat;
		} catch (Exception e) {

			logger.error(e);
		}
		return dat;
	}

	/**
	 * 日期格式转换
	 */
	public static String getFormatExpireDate(String date, String startFormat,
			String endFormat) {

		SimpleDateFormat sd = new SimpleDateFormat(startFormat);
		try {
			Date dat = sd.parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat(endFormat);
			logger.debug("getFormatExpireDate is return");
			return sdf.format(dat);
		} catch (Exception e) {

			logger.error(e);
		}

		return "";
	}
}
