package com.framework.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.DateUtil;

/**
 * ��־��¼������
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
	 * ���ӳ���־��¼
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
	 * �̼���־��¼
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
	 * ��Ʒ��־��¼
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
	 * ��Ʒ��־��¼
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
	 * �û���¼��־��¼
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
	 * ��¼�ӿ������־
	 * 
	 * @param type
	 *            �ӿ�����
	 * @param date
	 *            ��Ӧʱ��
	 * @param isSuccess
	 *            �ɹ���ʶ
	 * @param msisdn
	 *            �ֻ���
	 * @param url
	 *            �ӿ�url
	 * @param reqStr
	 *            �ӿ�����ͷ����
	 * @param xml
	 *            �ӿڷ��ص�xml
	 */
	public static void writeInterfaceLog(String type, String date,
			String isSuccess, String msisdn, String url, String reqStr,
			String xml) {

		StringBuilder sb = new StringBuilder();
		sb.append("�ӿ����ͣ�").append(type).append("\n");
		sb.append(CHAR_31);
		sb.append("��Ӧʱ�䣺").append(date).append(" ms\n");
		sb.append(CHAR_31);
		sb.append("�ɹ���ʶ��").append(isSuccess).append("\n");
		sb.append(CHAR_31);
		sb.append("�ֻ��ţ�").append(msisdn).append("\n");
		sb.append(CHAR_31);
		sb.append("����ʱ�䣺").append(
				Util.formatStrByLen(DateUtil.format.format(new Date()), 14))
				.append("\n");
		sb.append(CHAR_31);
		sb.append("�ӿ�url��").append(url).append("\n");
		sb.append(CHAR_31);
		sb.append("�ӿ�����ͷ���ݣ�\n").append(reqStr).append("\n");
		sb.append(CHAR_31);
		sb.append("�ӿڷ��ص�xml��\n").append(xml).append("\n");
		logger.debug("��¼�ӿ������־��\n" + sb.toString());
	}

	/**
	 * ���ض�ջ��Ϣ
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