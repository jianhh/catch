package com.framework.log;

import com.framework.util.StringUtils;

/**
 * ��־��¼������
 * 
 * @author x_maosuling
 * 
 */
public class Util {

	/**
	 * ���ݳ��������/��ȡ�ַ���
	 * 
	 * @param str
	 *            ��Ҫ������ַ���
	 * @param len
	 *            �̶�����
	 * @return String- �����Ľ��
	 */
	public static String formatStrByLen(String str, int len) {

		// ���ö�����¼��־
		if (str == null)
			str = "";
		if (len == 0)
			return "";
		StringBuffer resultStr = new StringBuffer();
		resultStr.append(str);
		int str_leng = StringUtils.lengthOfHZ(str);
		if (str_leng == len) {
			return resultStr.toString();
		}
		if (str_leng < len) {// ��Ҫ���
		// for (int i = 0; i < len - str_leng; i++)
		// {
		// resultStr.append(" ");
		// }
		} else {// ��Ҫ��ȡ
			String result = StringUtils.formatByLen(str, len, "");
			return result;
		}
		return resultStr.toString();
	}

	/**
	 * ���ַ�������
	 */
	public static String formatStr(String str) {

		if (str == null)
			return "";
		return str;
	}

//	public static String getSearchType(String searchType) {
//
//		if (Xtype.APPLICATION_CHANNEL_XTYPE_APPGAME.equals(searchType)) {
//			return "appgame";
//		} else if (Xtype.APPLICATION_CHANNEL_XTYPE_APPSOFTWARE
//				.equals(searchType)) {
//			return "appsoftware";
//		} else if (Xtype.APPLICATION_CHANNEL_XTYPE_APPTHEME.equals(searchType)) {
//			return "apptheme";
//		} else if (Xtype.ALL_CHANNEL_XTYPE.equals(searchType)) {
//			return "all";
//		}
//
//		else if (Xtype.CARVEOUT_CHANNEL_XTYPE.equals(searchType)) {
//			return "business";
//		} else if (Xtype.MUSIC_CHANNEL_XTYPE.equals(searchType)) {
//			return "music";
//		} else if (Xtype.READ_CHANNEL_XTYPE.equals(searchType)) {
//			return "read";
//		} else if (Xtype.BREAD_CHANNEL_XTYPE.equals(searchType)) {
//			return "bread";
//		} else if (Xtype.VIDEO_CHANNEL_XTYPE.equals(searchType)) {
//			return "video";
//		} else if (Xtype.COMIC_CHANNEL_XTYPE.equals(searchType)) {
//			return "comic";
//		}
//
//		else {
//			return "";
//		}
//	}

	/**
	 * ��ȡ����ͷ����
	 */
//	public static LogHeaderVO getHeader(Req request, Resp response) {
//		LogHeaderVO logvo = new LogHeaderVO();
//		logvo.setMmSource(request.getHeader("MM-Source"));
//		logvo.setChannelId(request.getHeader("channel-id"));
//		logvo.setMmInstallId(request.getHeader("MM-Install-Id"));
//		logvo.setMmVisitorId(request.getHeader("MM-Visitor-Id"));
//		logvo.setMmDeviceInfo(request.getHeader("MM_DEVICE_INFO"));
//		logvo.setReferrer(request.getHeader("Referrer"));
//		if (response != null)
//			logvo.setFrom(ParamterUtil.getParamterValue(request, response,
//					"from"));
//		return logvo;
//	}
}
