package com.work.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StringUtils;

/**
 * 
 * 爬虫工具类
 * 
 * @author tangbiao
 * 
 * Sep 2, 2014 4:30:01 PM
 */
public class AliGetUrlUtil {

	static JLogger logger = LoggerFactory.getLogger(AliGetUrlUtil.class);

	public static List<String> listIP = null;// ip集
	static String proxyUrl = "";// 代理地址
	public static String host = "";// 代理ip
	public static int port = 0;// 代理端口

	/**
	 * 根据数据获得Jsoup.Document
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDocumentUrl(String url) {
		try {
			if (url.indexOf(":///") > -1) {
				return Jsoup.parse("");
			}
			String xml = "";
			if (StringUtils.isNotEmpty(host)) {// 代理ip不为空就用代理ip请求
				xml = JsoupUtil.conUrl(url, false, "GBK", host, port);
			}
			if (StringUtils.isEmpty(xml)) {// 上一步没执行或者连接超时（可能为上一天的代理ip做的请求）
				xml = JsoupUtil.conUrl(url, false, "GBK", "", 0);// 用自己的服务器做请求
				// 用自己的服务器做请求
				if (StringUtils.isNotEmpty(xml) && listIP != null) {// 这种情况是属于上一天的代理ip数据
					listIP = null;// 制空ip集
					host = "";// 制空ip
					port = 0;// 制空端口
				}
			}
			int num = 0;// 第二次获取listIP的下标
			Document doc = Jsoup.parse(xml);
			String companyName = JsoupUtil.commodityUrl(doc, url);// 公司名

			if (JsoupUtil.aliCompanyName(companyName, url)) {// xml为空（可能是前面的接口全部请求超时）或者名称为阿里巴巴（访问频繁的情况）
				if (listIP == null) {// ip在内存中为空就去代理网页获取ip
					// proxyUrl = "http://pachong.org/anonymous.html";// 设置代理地址
					// listIP = JsoupUtil.ip(proxyUrl);// 设置ip集
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip在内存中为空就去代理网页获取ip
					proxyUrl = "http://revx.daili666.com";
					listIP = JsoupUtil.daliIp();// 重新设置代理ip
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
					if (JsoupUtil.aliCompanyName(companyName, url)) {
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
						xml = JsoupUtil.conUrl(url, false, "GBK", host, port);
						doc = Jsoup.parse(xml);
						companyName = JsoupUtil.commodityUrl(doc, url);// 公司名
						if ("1688.com,阿里巴巴打造的全球最大的采购批发平台".equals(companyName)) {
							if (JsoupUtil.cookieNum > JsoupUtil.listCookie
									.size()) {
								JsoupUtil.cookieNum = 1;
							}
							String cookie = JsoupUtil.listCookie
									.get(JsoupUtil.cookieNum);
							JsoupUtil.cookieNum++;
							xml = JsoupUtil.conUrl(url, false, "GBK", host,
									port, cookie);
							doc = Jsoup.parse(xml);
							companyName = JsoupUtil.commodityUrl(doc, url);// 公司名
						}

						if (JsoupUtil.aliCompanyName(companyName, url)) {
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
								listIP = JsoupUtil.daliIp();// 重新设置代理ip
								// }
								getDocumentUrl(url);
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
			return getDocumentUrl(url);
		}
	}
}
