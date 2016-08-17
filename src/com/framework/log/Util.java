package com.framework.log;

import com.framework.util.StringUtils;

/**
 * 日志记录工具类
 * 
 * @author x_maosuling
 * 
 */
public class Util {

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

		// 采用定长记录日志
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
		if (str_leng < len) {// 需要填充
		// for (int i = 0; i < len - str_leng; i++)
		// {
		// resultStr.append(" ");
		// }
		} else {// 需要截取
			String result = StringUtils.formatByLen(str, len, "");
			return result;
		}
		return resultStr.toString();
	}

	/**
	 * 空字符串处理
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
	 * 获取请求头参数
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
