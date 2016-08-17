package com.work.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.httpsend.HttpParsVo;
import com.framework.httpsend.HttpReturnVo;
import com.framework.httpsend.HttpSend;
import com.framework.log.LogUtil;
import com.framework.util.StartContent;
import com.framework.util.StringUtils;
import com.work.admin.dao.AdminDAO;
import com.work.commodity.content.CommodityContent;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;

/**
 * 
 * 爬虫工具类
 * 
 * @author tangbiao
 * 
 * Sep 2, 2014 4:30:01 PM
 */
public class JsoupUtil {

	static JLogger logger = LoggerFactory.getLogger(JsoupUtil.class);

	public static List<String> listCookie = new ArrayList<String>();// Cookie集
	static List<String> listIP = null;// ip集
	static String proxyUrl = "";// 代理地址
	public static String host = "";// 代理ip
	public static int port = 0;// 代理端口
	static int cookieNum = 1;

	/**
	 * 获取商品ID
	 * 
	 * @return
	 */
	public static String goodsId() {
		String xml = "";
		String goodsId = "";
		try {
			// String sendUrl = "http://"
			// + StartContent.getInstance().getDomainIp()
			// + "/service_drp/getid.jsp?type=goods";
			// xml = conUrl(sendUrl, true, "utf-8", "", 0);
			// JSONObject json = new JSONObject(xml);
			// goodsId = json.getString("goods_id");

			// IdService service = RPCProxy.getProxy(IdService.class,
			// "112.95.227.4", 20382);
			// goodsId = service.getId(IdType.TYPE_GOODS) + "";

			boolean isTruedian = false;
			if (StartContent.getInstance().getDomainUrl().equals(
					"www.truedian.com")) {// 现网
				isTruedian = true;
			}
			AdminDAO adminDao = new AdminDAO();
			goodsId = adminDao.getGoodsId(isTruedian);
		} catch (Exception e) {
			logger.error("获取商品ID失败", e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取商品ID失败", xml
					+ LogUtil.getExceptionError(e));
		}
		return goodsId;
	}

	public static void main(String[] args) throws Exception {
		// System.out
		// .println(urlDecodeByUtf("\u6ca1\u6709\u627e\u5230\u7b26\u5408\u6761\u4ef6\u7684\u4ee3\u7406\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5\u3002"));
		// System.out.println(urlEcodeByUtf("中国"));
		// daili();

		// String goodsId = "";
		// IdService service = RPCProxy.getProxy(IdService.class,
		// "112.95.227.4",
		// 20382);
		// goodsId = service.getId(IdType.TYPE_GOODS) + "";
		// System.out.println(goodsId);
		String sendUrl = "http://" + "truedian.com"
				+ "/service_drp/getid.jsp?type=goods";
		String xml = JsoupUtil.conUrl(sendUrl, true, "utf-8", "", 0);
		System.out.println(xml);
		JSONObject json = new JSONObject(xml);
		System.out.println(json.getString("goods_id"));
	}

	public static void daili() {
		// &area=美国
		// 117.177.243.101:8083
		String url = "http://www.kuaidaili.com/api/getproxy?orderid=933653755698386&num=30&area_ex="
				+ urlEcodeByUtf("中国")
				+ "&quality=1&protocol=1&method=1&browser=1&sort=1&format=json";
		String xml = conUrl(url, false, "utf-8", "", 0);
		System.out.println(xml);
	}

	/**
	 * 根据url获得响应数据
	 * 
	 * @param url
	 * @return
	 */
	public static String conUrl(String url, boolean post, String charset,
			String proxyHost, int proxyPort) {
		String proxyip = "非代理请求";// 记录代理ip
		if (StringUtils.isNotEmpty(proxyHost)) {
			proxyip = "代理ip " + proxyHost + ":" + proxyPort;
		}
		LogUtil
				.writeCommodityLog("JsoupUtil.conUrl:" + proxyip + " url="
						+ url);
		if (url.indexOf(":///") > -1) {
			return "<html></html>";
		}
		try {
			if (StringUtils.isNotEmpty(url)) {
				url = url.trim();
			}
			HttpParsVo httpVo = new HttpParsVo();
			httpVo.setPost(post);
			httpVo.setCharset(charset);
			httpVo.setProxyHost(proxyHost);
			httpVo.setProxyPort(proxyPort);
			// Map<String, String> headMap = httpVo.getHeadMap();
			// headMap
			// .put(
			// "Cookie",
			// "JSESSIONID=8L78CIqv1-JixUwqEYzVFZhKeCP9-PiNpSZP-8eJ4;
			// __cn_logon__=false;
			// _tmp_ck_0=\"iE%2BU27qP%2F8Ui4xCFSmf%2BYJJZ%2FRctMXKLSs7Cb70bbUWIdlIzvQTf786fVhc8gPMh%2FCHauSCFKmLN447th6BAyNdp6NlLUVV2mCj7ib9AL369dnF3Kjj6qsE4H5YwR86ZN7MqwsyqB5aE72Le765F0mA1S%2BDkZNAOnb8Rwrw09mjT9WdVZwtCu8aJ2ev2kVjQiNJnQQOdQgQAKE82Y8M9dlWGy86kWAs4bqhaTZ5EuRyYkcLaFlv%2B4ATWSjy%2BkSUxjZ135kcuZaZPBPEnQbZDWDfebnofu6ekpy7l%2BdwC%2BDVyYivFKUMsF%2FSbT8GyrZEAqoLZcWkvsmF2zcOOMzxCOr%2FVIdypIFPnFaI45SffZ5ehtuWTeMea36YKPaVtG9NzBN5aV6tx%2Fn4%3D\";
			// ali_beacon_id=115.174.81.245.1452496525136.379151.3;
			// cna=jUQaD5R8AmMCAXOuUfUsQmX5");
			// httpVo.setHeadMap(headMap);
			HttpReturnVo returnVo = HttpSend.senderReq(null, url, httpVo);
			String xml = returnVo.getRespString();
			return xml;
		} catch (Exception e) {
			logger.error("根据url获得响应数据失败", e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "根据url获得响应数据失败", LogUtil
					.getExceptionError(e));
		}
		return "";
	}

	/**
	 * 根据url获得响应数据
	 * 
	 * @param url
	 * @return
	 */
	public static String conUrl(String url, boolean post, String charset,
			String proxyHost, int proxyPort, String cookie) {
		String proxyip = cookieNum + " cookie：" + cookie + " 非代理请求";// 记录代理ip
		if (StringUtils.isNotEmpty(proxyHost)) {
			proxyip = cookieNum + " cookie：" + cookie + " 代理ip " + proxyHost
					+ ":" + proxyPort;
		}
		LogUtil
				.writeCommodityLog("JsoupUtil.conUrl:" + proxyip + " url="
						+ url);
		try {
			if (StringUtils.isNotEmpty(url)) {
				url = url.trim();
			}
			HttpParsVo httpVo = new HttpParsVo();
			httpVo.setPost(post);
			httpVo.setCharset(charset);
			httpVo.setProxyHost(proxyHost);
			httpVo.setProxyPort(proxyPort);
			if (StringUtils.isNotEmpty(cookie)) {
				Map<String, String> headMap = httpVo.getHeadMap();
				headMap.put("Cookie", cookie);
				httpVo.setHeadMap(headMap);
			}
			HttpReturnVo returnVo = HttpSend.senderReq(null, url, httpVo);
			String xml = returnVo.getRespString();
			return xml;
		} catch (Exception e) {
			logger.error("根据url获得响应数据失败", e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "根据url获得响应数据失败", LogUtil
					.getExceptionError(e));
		}
		return "";
	}

	/**
	 * 判断阿里地址访问是否正确
	 * 
	 * @param companyName
	 * @return true有问题，false正确
	 */
	public static boolean aliCompanyName(String companyName, String url) {
		logger.debug("aliCompanyName():companyName=" + companyName);
		boolean isName = false;
		// xml为空（可能是前面的接口全部请求超时）||名称为阿里巴巴（访问频繁的情况）||连接等待||出现(404||302)(有的代理ip可能存在问题)
		if (StringUtils.isEmpty(companyName)
				|| "阿里巴巴".equals(companyName)
				|| "正在连接中，请稍后..".equals(companyName)
				|| "亲，访问受限了".equals(companyName)
				|| "对不起，您访问的页面不存在！".equals(companyName)
				|| "1688.com,阿里巴巴打造的全球最大的采购批发平台".equals(companyName)
				|| (StringUtils.isNotEmpty(companyName)
						&& companyName.length() > 3 && (companyName.substring(
						0, 3).equals("404") || companyName.substring(0, 3)
						.equals("302")))) {
			if (url.indexOf("detail.1688.com/offer") > 0
					&& (companyName.length() > 3 && companyName.substring(0, 3)
							.equals("404"))) {
				isName = false;
			} else {
				isName = true;
			}
		}
		logger.debug("aliCompanyName():isName=" + isName);
		return isName;
	}

	/**
	 * 根据数据获得String
	 * 
	 * @param url
	 * @return
	 */
	public static String getUrl(String url) {
		return getUrl(url, 2000, "GBK");
	}

	public static boolean isRetryConnect(String xml, int minStrNum) {
		if (StringUtils.isEmpty(xml)) {
			return true;
		}
		if (xml.indexOf("宝贝不存在") != -1) {
			return false;
		}
		if (xml.length() < minStrNum) {
			return true;
		}
		if (xml.length() < 2000 && xml.indexOf("redirectUrl") != -1) {
			// 获取的数据为重向， 则再用代理IP访问
			return true;
		}
		return false;
	}

	public static String getUrl(String url, int minStrNum, String xcode) {
		String code = xcode;
		if (code == null) {
			code = "GBK";
		}
		try {
			if (url.indexOf(":///") > -1) {
				return "";
			}
			String xml = conUrl(url, false, code, "", 0);// 用自己的服务器做请求
			if (isRetryConnect(xml, minStrNum) && StringUtils.isNotEmpty(host)) {// 代理ip不为空就用代理ip请求
				xml = conUrl(url, false, code, host, port);
			}
			int num = 0;// 第二次获取listIP的下标

			// xml为空（可能是前面的接口全部请求超时）或者名称为阿里巴巴（访问频繁的情况），需要拿新的代理ip去请求阿里||出现404(有的代理ip可能存在问题)
			if (isRetryConnect(xml, minStrNum)) {
				if (listIP == null || listIP.size() == 0) {// ip在内存中为空就去代理网页获取ip
					// proxyUrl = "http://pachong.org/anonymous.html";// 设置代理地址
					// listIP = ip(proxyUrl);// 设置ip集
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip在内存中为空就去代理网页获取ip
					proxyUrl = "http://revx.daili666.com";
					listIP = JsoupUtil.daliIp();// 重新设置代理ip
					if (listIP.size() > 1) {
						String ip = listIP.get(0);
						host = ip.substring(0, ip.indexOf(":"));// 设置代理ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// 设置代理端口
						logger.debug("ip=" + ip);
					} else {
						getUrl(url);
					}
					// }
				}

				if (StringUtils.isNotEmpty(host)) {// 代理ip在内存中存在，就用存在的去请求
					for (int i = 0; i < listIP.size(); i++) {// 循环ip集，取内存中的ip在ip集中的下标位置
						String ip = listIP.get(i);
						if (ip.equals(host + ":" + port)) {
							num = i;
						}
					}
					num += 1;// 当前ip已经请求一次了，所以从下一个开始
				}

				for (int i = num; i < listIP.size(); i++) {// 循环ip集，直到xml不为空（可能是前面的接口全部请求超时）且名称不为阿里巴巴（访问频繁的情况）
					// xml为空（可能是前面的接口全部请求超时）或者名称为阿里巴巴（访问频繁的情况），需要拿新的代理ip去请求阿里||出现404(有的代理ip可能存在问题)
					if (isRetryConnect(xml, minStrNum)) {
						String ip = listIP.get(i);
						if (StringUtils.isEmpty(ip)) {
							continue;
						}
						host = ip.substring(0, ip.indexOf(":"));// 设置代理ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// 设置代理端口
						xml = conUrl(url, false, code, host, port);

						if (isRetryConnect(xml, minStrNum)) {
							if ((i + 1) == listIP.size()) {// 到最后一个代理ip，那就重新从代理网页获取ip（ip每天都会变化）
								num = 0;
								listIP = null;// 制空ip集
								host = "";// 制空ip
								port = 0;// 制空端口
								// if
								// (proxyUrl.equals("http://pachong.org/anonymous.html"))
								// {
								// proxyUrl =
								// "http://pachong.org/transparent.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// 重新设置ip集，下面要把内存中存在的ip和端口制空，保证下次访问的时候不用内存中的ip和端口
								// } else {
								// proxyUrl =
								// "http://pachong.org/anonymous.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// 重新设置ip集，下面要把内存中存在的ip和端口制空，保证下次访问的时候不用内存中的ip和端口
								// }
								// if (listIP == null || listIP.size() == 0) {//
								// ip在内存中为空就去代理网页获取ip
								proxyUrl = "http://revx.daili666.com";
								listIP = daliIp();// 重新设置代理ip
								// }
								getUrl(url);
							}
						}
					} else {
						break;
					}
				}
			}
			return xml;
		} catch (Exception e) {
			logger.error("根据数据获得Jsoup.Document失败" + url, e);
			return "";
		}
	}

	/**
	 * 根据数据获得Jsoup.Document
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDocument(String url) {
		try {
			if (url.indexOf(":///") > -1) {
				return Jsoup.parse("");
			}
			String xml = "";
			if (StringUtils.isNotEmpty(host)) {// 代理ip不为空就用代理ip请求
				xml = conUrl(url, false, "GBK", host, port);
			}
			if (StringUtils.isEmpty(xml)) {// 上一步没执行或者连接超时（可能为上一天的代理ip做的请求）
				xml = conUrl(url, false, "GBK", "", 0);// 用自己的服务器做请求
				if (StringUtils.isNotEmpty(xml) && listIP != null) {// 这种情况是属于上一天的代理ip数据
					listIP = null;// 制空ip集
					host = "";// 制空ip
					port = 0;// 制空端口
				}
			}
			int num = 0;// 第二次获取listIP的下标

			// 处理&nbsp;变成问号的问题
			Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));

			String companyName = commodityUrl(doc, url);// 公司名

			// xml为空（可能是前面的接口全部请求超时）或者名称为阿里巴巴（访问频繁的情况），需要拿新的代理ip去请求阿里||出现404(有的代理ip可能存在问题)
			if (aliCompanyName(companyName, url)) {
				if (listIP == null || listIP.size() == 0) {// ip在内存中为空就去代理网页获取ip
					// proxyUrl = "http://pachong.org/anonymous.html";// 设置代理地址
					// listIP = ip(proxyUrl);// 设置ip集
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip在内存中为空就去代理网页获取ip
					proxyUrl = "http://revx.daili666.com/";
					listIP = daliIp();// 重新设置代理ip
					String ip = listIP.get(0);
					host = ip.substring(0, ip.indexOf(":"));// 设置代理ip
					port = Integer.parseInt(ip.substring(ip.indexOf(":") + 1));// 设置代理端口
					// }
				}

				if (StringUtils.isNotEmpty(host)) {// 代理ip在内存中存在，就用存在的去请求
					for (int i = 0; i < listIP.size(); i++) {// 循环ip集，取内存中的ip在ip集中的下标位置
						String ip = listIP.get(i);
						if (ip.equals(host + ":" + port)) {
							num = i;
						}
					}
					num += 1;// 当前ip已经请求一次了，所以从下一个开始
				}

				for (int i = num; i < listIP.size(); i++) {// 循环ip集，直到xml不为空（可能是前面的接口全部请求超时）且名称不为阿里巴巴（访问频繁的情况）
					// xml为空（可能是前面的接口全部请求超时）或者名称为阿里巴巴（访问频繁的情况），需要拿新的代理ip去请求阿里||出现404(有的代理ip可能存在问题)
					if (aliCompanyName(companyName, url)) {
						logger
								.debug("is404="
										+ (StringUtils.isNotEmpty(companyName)
												&& companyName.length() > 3 && companyName
												.substring(0, 3).equals("404")));
						// 404的情况，如果使用代理ip请求的，就用本地再请求一次，把代理ip制空(有的代理ip可能存在问题)
						if (StringUtils.isNotEmpty(companyName)
								&& companyName.length() > 3
								&& companyName.substring(0, 3).equals("404")) {
							String url1 = "http://szoycp.1688.com";
							xml = JsoupUtil.conUrl(url1, false, "GBK", host,
									port);
							Document doc1 = Jsoup.parse(xml);
							companyName = JsoupUtil.commodityUrl(doc1, url1);// 公司名
							if (StringUtils.isNotEmpty(companyName)
									&& companyName.length() > 3
									&& !companyName.substring(0, 3).equals(
											"404")) {
								return doc;
							}
						}

						String ip = listIP.get(i);
						if (StringUtils.isEmpty(ip)) {
							continue;
						}
						host = ip.substring(0, ip.indexOf(":"));// 设置代理ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// 设置代理端口
						xml = conUrl(url, false, "GBK", host, port);
						doc = Jsoup.parse(xml);
						companyName = commodityUrl(doc, url);// 公司名
						if ("1688.com,阿里巴巴打造的全球最大的采购批发平台".equals(companyName)) {
							if (cookieNum > listCookie.size()) {
								cookieNum = 1;
							}
							String cookie = listCookie.get(cookieNum);
							cookieNum++;
							xml = conUrl(url, false, "GBK", host, port, cookie);
							doc = Jsoup.parse(xml);
							companyName = commodityUrl(doc, url);// 公司名
						}

						if (aliCompanyName(companyName, url)) {
							if ((i + 1) == listIP.size()) {// 到最后一个代理ip，那就重新从代理网页获取ip（ip每天都会变化）
								num = 0;
								listIP = null;// 制空ip集
								host = "";// 制空ip
								port = 0;// 制空端口
								// if
								// (proxyUrl.equals("http://pachong.org/anonymous.html"))
								// {
								// proxyUrl =
								// "http://pachong.org/transparent.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// 重新设置ip集，下面要把内存中存在的ip和端口制空，保证下次访问的时候不用内存中的ip和端口
								// } else {
								// proxyUrl =
								// "http://pachong.org/anonymous.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// 重新设置ip集，下面要把内存中存在的ip和端口制空，保证下次访问的时候不用内存中的ip和端口
								// }
								// if (listIP == null || listIP.size() == 0) {//
								// ip在内存中为空就去代理网页获取ip
								proxyUrl = "http://revx.daili666.com";
								listIP = daliIp();// 重新设置代理ip
								// }
								getDocument(url);
							}
						}
					} else {
						break;
					}
				}
			}
			return doc;
		} catch (Exception e) {
			logger.error("根据数据获得Jsoup.Document失败" + url, e);
			// 发送邮件
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(),
			// "根据数据获得Jsoup.Document失败"
			// + url, LogUtil.getExceptionError(e));
			return getDocument(url);
		}
	}

	/**
	 * 验证地址
	 * 
	 * @param doc
	 * @return
	 */
	public static String commodityUrl(Document doc, String url) {
		LogUtil
				.writeCommodityLog("验证地址JsoupUtil.commodityUrl()...........start "
						+ url);
		String companyName = "";// 公司名
		Elements companyEle = doc.select("[class=company-name]");
		companyName = companyEle.text();// 公司名
		if (StringUtils.isEmpty(companyName)) {
			Elements titleEle = doc.select("title");
			companyName = titleEle.text();// 公司名
		}
		boolean isMessy = isMessyCode(companyName);
		if (isMessy) {// 乱码
			companyName = "";
		}
		LogUtil.writeCommodityLog("验证地址JsoupUtil.commodityUrl()...........end "
				+ url + " " + companyName + " " + isMessy);
		return companyName;
	}

	public static boolean isMessyCode(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			// 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
			// 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
			// System.out.println("--- " + (int) c);
			if ((int) c == 0xfffd) {
				// 存在乱码
				// System.out.println("存在乱码 " + (int) c);
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据数据获得Jsoup.Document
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDocumentByProxy(String url) {
		try {
			String xml = conUrl(url, false, "utf-8", "", 0);
			for (int i = 1; i <= 3; i++) {
				if (StringUtils.isEmpty(xml)) {
					String flag = "成功";
					xml = conUrl(url, false, "utf-8", "", 0);
					if (StringUtils.isEmpty(xml)) {
						flag = "失败";
					}
					// 发送邮件
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), flag + " 接口超时，第"
							+ i + "次尝试请求：" + url, flag);
				} else {
					break;
				}
			}
			Document doc = Jsoup.parse(xml);
			return doc;
		} catch (Exception e) {
			logger.error("根据数据获得Jsoup.Document失败,重复发送" + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(),
					"根据数据获得Jsoup.Document失败,重复发送" + url, LogUtil
							.getExceptionError(e));
		}
		return null;
	}

	public Document getDocumentPost(String url) {
		try {
			Connection con = Jsoup.connect(url);
			con.header("Cache-Control", "max-age=0");
			con.header("Content-Length", "1253");
			con.header("Content-Type", "application/x-www-form-urlencoded");
			con.header("Origin", "http://s.1688.com");
			// con.header("Cookie", "");
			return con.post();
		} catch (Exception e) {
			return getDocument(url);
		}
	}

	/**
	 * 获取项目服务器地址
	 * 
	 * @return
	 */
	public static String SystemPath() {
		String path = System.getProperty("user.dir");
		path = path.replace("bin", "webapps");
		path = path.replace("\\", "/");
		return path;
	}

	/**
	 * 获取图片地址
	 * 
	 * @return
	 */
	public static String SystemPathImage(String shopId) {
		String filePath = JsoupUtil.SystemPath() + "/image/" + shopId + "/";// 设置图片下载的根目录
		return filePath;
	}

	/**
	 * 价格转换（元转为分）
	 * 
	 * @param price
	 * @return
	 */
	public static String priceFen(String price) {
		if (StringUtils.isNotEmpty(price)) {
			double pr = Double.parseDouble(price);
			int p = Integer.parseInt(new java.text.DecimalFormat("0")
					.format(pr * 100));
			return p + "";
		}
		return "0";
	}

	/**
	 * 价格转换（分转为元）
	 * 
	 * @param price
	 * @return
	 */
	public static String priceYuan(String price) {
		String yuan = "0";
		if (StringUtils.isNotEmpty(price)) {
			final int MULTIPLIER = 100;
			Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
			Matcher matcher = pattern.matcher(price);
			if (matcher.matches()) {
				yuan = new BigDecimal(price).divide(new BigDecimal(MULTIPLIER))
						.setScale(2).toString();
			}
		}
		return yuan;
	}

	// public static void main(String[] args) {
	// System.out.println(priceYuan("12900"));
	// }

	/**
	 * 单位转换（千克转为克）
	 * 
	 * @param price
	 * @return
	 */
	public static String unit(String unit) {
		if (StringUtils.isNotEmpty(unit)) {
			double pr = Double.parseDouble(unit);
			int p = Integer.parseInt(new java.text.DecimalFormat("0")
					.format(pr * 1000));
			return p + "";
		}
		return "0";
	}

	/**
	 * URL编码（gb2312编码转中文）
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL编码（gb2312编码转中文）失败", e);
		}
		return "";
	}

	/**
	 * URL编码（中文转gb2312编码）
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlEcode(String str) {
		try {
			return URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL编码（中文转gb2312编码）失败", e);
		}
		return "";
	}

	/**
	 * URL编码（utf-8编码转中文）
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlDecodeByUtf(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL编码（utf-8编码转中文）失败", e);
		}
		return "";
	}

	/**
	 * URL编码（中文转utf-8编码）
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlEcodeByUtf(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL编码（中文转utf-8编码）失败", e);
		}
		return "";
	}

	// 返回的Json
	public static String getJson(String code, String info) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\"");
		sb.append("}");
		return sb.toString();
	}

	// 返回的Json
	public static String getCompanyUrlJson(String code, String info, String url) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"url\":").append("\"" + url + "\"");
		sb.append("}");
		return sb.toString();
	}

	// 返回的Json
	public static String getCompanyNameJson(String code, String info,
			String name) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"name\":").append("\"" + name + "\"");
		sb.append("}");
		return sb.toString();
	}

	// 返回的Json
	public static String getShopCheckJson(String code, String info,
			String name, String url) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"name\":").append("\"" + name + "\",");
		sb.append("\"url\":").append("\"" + url + "\"");
		sb.append("}");
		return sb.toString();
	}

	// 返回的Json
	public static String getUptokenJson(String code, String info, String uptoken) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"uptoken\":").append("\"" + uptoken + "\"");
		sb.append("}");
		return sb.toString();
	}

	// 图片上传返回的Json
	public static String getGoodsImgJson(String code, String url, String width,
			String height) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":\"成功\",");
		sb.append("\"result\":[{\"url\":\"").append(url + "\"");
		sb.append(",\"width\":").append(width);
		sb.append(",\"height\":").append(height).append("}]");
		sb.append("}");
		return sb.toString();
	}

	// 用户登录返回的Json
	public static String getLoginUrlJson(String code, String info, String url,
			String name) {

		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n");
		sb.append("\"errcode\":").append(code).append(",\r\n");
		sb.append("\"errmsg\":\"").append(info).append("\",\r\n");
		sb.append("\"url\":\"").append(url).append("\",\r\n");
		sb.append("\"name\":\"").append(name).append("\"");
		sb.append("\r\n}");
		return sb.toString();
	}

	// 用户登录返回的Json
	public static String getLoginJson(String code, String info, String url,
			String target, String username, String userpw) {

		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n");
		sb.append("\"errcode\":").append(code).append(",\r\n");
		sb.append("\"errmsg\":\"").append(info).append("\",\r\n");
		sb.append("\"url\":\"").append(url).append("\",\r\n");
		sb.append("\"target\":\"").append(target).append("\",\r\n");
		sb.append("\"username\":\"").append(username).append("\",\r\n");
		sb.append("\"userpw\":\"").append(userpw).append("\"");
		sb.append("\r\n}");
		return sb.toString();
	}

	/**
	 * 获取代理ip
	 * 
	 * @param doc
	 * @return
	 */
	public static List<String> ip(String url) {
		LogUtil.writeCommodityLog("获取代理ip...........start ");

		List<String> list = new ArrayList<String>();
		// http://pachong.org 29失败
		// http://pachong.org/anonymous.html 24失败
		// http://pachong.org/transparent.html 25失败
		// String url = "http://pachong.org/transparent.html";
		Document doc = JsoupUtil.getDocumentByProxy(url);
		// System.out.println(doc.html());
		Elements scriptEle = doc.select("[type=text/javascript]");
		// var hen=1376+6577;var dog=4752+817^hen;var cock=1475+561^dog;var
		// chick=3421+6078^cock;var cat=4547+4981^chick;
		String script = scriptEle.get(2).html();
		String[] str = script.split(";");
		Map<String, String> map = new HashMap<String, String>();// 变量值
		for (int i = 0; i < str.length; i++) {
			String s = str[i];
			String var = s.substring(4, s.indexOf("="));
			String calfS = s.substring(s.indexOf("=") + 1);
			String calfS1 = calfS.substring(0, calfS.indexOf("+"));
			String calfS2 = calfS.substring(calfS.indexOf("+") + 1);
			if (calfS2.indexOf("^") > -1) {
				String calfS3 = calfS2.substring(0, calfS2.indexOf("^"));
				String calfS4 = calfS2.substring(calfS2.indexOf("^") + 1);
				String calfS5 = map.get(calfS4);
				int number = Integer.parseInt(calfS1)
						+ Integer.parseInt(calfS3) ^ Integer.parseInt(calfS5);
				map.put(var, number + "");
			} else {
				int number = Integer.parseInt(calfS1)
						+ Integer.parseInt(calfS2);
				map.put(var, number + "");
			}
		}
		Elements tbEle = doc.select("[class=tb]");
		// System.out.println(tbEle.html());
		Elements trEle = tbEle.select("tr");
		for (int i = 1; i < trEle.size(); i++) {
			Elements imgEle = trEle.get(i).select("img");
			String img = imgEle.attr("src");
			Elements spanEle = trEle.get(i).select("span");
			String span = spanEle.attr("class");
			// if (img.equals("/view/ensign/cn.png") && span.equals("zt1")) {//
			// 中国图片地址&&状态为“空闲”
			Elements tdEle = trEle.get(i).select("td");
			String host = tdEle.get(1).text();
			String port = tdEle.get(2).html();
			String str1 = port.substring(port.indexOf("write(") + 7, port
					.indexOf(");"));
			String str2 = str1.substring(0, str1.indexOf("^"));
			String str3 = str1.substring(str1.indexOf("^") + 1, str1
					.indexOf(")+"));
			String str4 = str1.substring(str1.indexOf(")+") + 2);
			int number = (Integer.parseInt(str2) ^ Integer.parseInt(map
					.get(str3)))
					+ Integer.parseInt(str4);

			list.add(host + ":" + number);
			// }
		}

		LogUtil.writeCommodityLog("获取代理ip...........end " + map);
		return list;
	}

	/**
	 * 获取代理ip
	 * 
	 * @param doc
	 * @return
	 */
	public static List<String> daliIp() {
		LogUtil.writeCommodityLog("获取代理daliIp...........start ");
		// 555281263105790 558460242745998
		String doc = JsoupUtil
				.conUrl(
						"http://revx.daili666.com/ip/?tid=558460242745998&num=500&delay=1&foreign=all",
						false, "UTF8", "", 0);
		List<String> listIP = new ArrayList<String>();// ip集
		String[] str = doc.split("\n");
		for (String s : str) {
			listIP.add(s.trim());
		}
		LogUtil.writeCommodityLog("获取代理daliIp...........end " + listIP.size());
		return listIP;
	}

	// url对特殊字符转义
	public static String getReplace(String url) {

		if (StringUtils.isEmpty(url)) {
			return "";
		}
		return url.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"",
				"&quot;");
	}

	// url对特殊字符转义
	public static String getReplaceBy(String url) {

		if (StringUtils.isEmpty(url)) {
			return "";
		}
		return url.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll(
				"&gt;", ">").replaceAll("&apos;", "'").replaceAll("&quot;",
				"\"");
	}

	public static String getReplaceHtml(String text) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}
		return text.replaceAll("&nbsp;", " ");

	}
}
