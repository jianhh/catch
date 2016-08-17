package com.framework.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.DateUtil;

/**
 * 日志记录工具类
 * 
 * @author tangbiao
 * 
 */
public class LogUtil {

	private static char CHAR_31 = 31;

	static JLogger logger = LoggerFactory.getLogger(LogUtil.class);

	static JLogger businessesLog = LoggerFactory.getLogger("businesses.log");

	static JLogger commodityLog = LoggerFactory.getLogger("commodity.log");

	static JLogger interfaceLog = LoggerFactory.getLogger("interface.log");

	static JLogger userloginLog = LoggerFactory.getLogger("userlogin.log");

	static JLogger dbpoolLog = LoggerFactory.getLogger("dbpool.log");

	/**
	 * 连接池日志记录
	 * 
	 * @param info
	 */
	public static void writeDbpoolLog(Object info) {
		if (logger.isDebugEnabled())
			logger.debug(info.toString());
		dbpoolLog.info(DateUtil.format8.format(new Date()) + " "
				+ info.toString());
	}

	/**
	 * 商家日志记录
	 * 
	 * @param info
	 */
	public static void writeBusinessesLog(Object info) {
		if (logger.isDebugEnabled())
			logger.debug(info.toString());
		businessesLog.info(DateUtil.format8.format(new Date()) + " "
				+ info.toString());
	}

	/**
	 * 商品日志记录
	 * 
	 * @param info
	 */
	public static void writeCommodityLog(Object info) {
		if (logger.isDebugEnabled())
			logger.debug(info.toString());
		commodityLog.info(DateUtil.format8.format(new Date()) + " "
				+ info.toString());
	}

	/**
	 * 商品日志记录
	 * 
	 * @param info
	 */
	public static void writeInterfaceLog(Object info) {
		if (logger.isDebugEnabled())
			logger.debug(info.toString());
		interfaceLog.info(DateUtil.format8.format(new Date()) + " "
				+ info.toString());
	}

	/**
	 * 用户登录日志记录
	 * 
	 * @param info
	 */
	public static void writeUserloginLog(Object info) {
		if (logger.isDebugEnabled())
			logger.debug(info.toString());
		userloginLog.info(DateUtil.format8.format(new Date()) + " "
				+ info.toString());
	}

	/**
	 * 记录接口相关日志
	 * 
	 * @param type
	 *            接口类型
	 * @param date
	 *            响应时间
	 * @param isSuccess
	 *            成功标识
	 * @param msisdn
	 *            手机号
	 * @param url
	 *            接口url
	 * @param reqStr
	 *            接口请求头数据
	 * @param xml
	 *            接口返回的xml
	 */
	public static void writeInterfaceLog(String type, String date,
			String isSuccess, String msisdn, String url, String reqStr,
			String xml) {

		StringBuilder sb = new StringBuilder();
		sb.append("接口类型：").append(type).append("\n");
		sb.append(CHAR_31);
		sb.append("响应时间：").append(date).append(" ms\n");
		sb.append(CHAR_31);
		sb.append("成功标识：").append(isSuccess).append("\n");
		sb.append(CHAR_31);
		sb.append("手机号：").append(msisdn).append("\n");
		sb.append(CHAR_31);
		sb.append("发生时间：").append(
				Util.formatStrByLen(DateUtil.format.format(new Date()), 14))
				.append("\n");
		sb.append(CHAR_31);
		sb.append("接口url：").append(url).append("\n");
		sb.append(CHAR_31);
		sb.append("接口请求头数据：\n").append(reqStr).append("\n");
		sb.append(CHAR_31);
		sb.append("接口返回的xml：\n").append(xml).append("\n");
		logger.debug("记录接口相关日志：\n" + sb.toString());
	}

	/**
	 * 返回堆栈信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionError(Throwable e) {
		if (e != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} else {
			return "No Exception";
		}
	}
}