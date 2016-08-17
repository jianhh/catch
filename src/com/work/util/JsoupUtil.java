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
 * ���湤����
 * 
 * @author tangbiao
 * 
 * Sep 2, 2014 4:30:01 PM
 */
public class JsoupUtil {

	static JLogger logger = LoggerFactory.getLogger(JsoupUtil.class);

	public static List<String> listCookie = new ArrayList<String>();// Cookie��
	static List<String> listIP = null;// ip��
	static String proxyUrl = "";// �����ַ
	public static String host = "";// ����ip
	public static int port = 0;// ����˿�
	static int cookieNum = 1;

	/**
	 * ��ȡ��ƷID
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
					"www.truedian.com")) {// ����
				isTruedian = true;
			}
			AdminDAO adminDao = new AdminDAO();
			goodsId = adminDao.getGoodsId(isTruedian);
		} catch (Exception e) {
			logger.error("��ȡ��ƷIDʧ��", e);
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "��ȡ��ƷIDʧ��", xml
					+ LogUtil.getExceptionError(e));
		}
		return goodsId;
	}

	public static void main(String[] args) throws Exception {
		// System.out
		// .println(urlDecodeByUtf("\u6ca1\u6709\u627e\u5230\u7b26\u5408\u6761\u4ef6\u7684\u4ee3\u7406\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5\u3002"));
		// System.out.println(urlEcodeByUtf("�й�"));
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
		// &area=����
		// 117.177.243.101:8083
		String url = "http://www.kuaidaili.com/api/getproxy?orderid=933653755698386&num=30&area_ex="
				+ urlEcodeByUtf("�й�")
				+ "&quality=1&protocol=1&method=1&browser=1&sort=1&format=json";
		String xml = conUrl(url, false, "utf-8", "", 0);
		System.out.println(xml);
	}

	/**
	 * ����url�����Ӧ����
	 * 
	 * @param url
	 * @return
	 */
	public static String conUrl(String url, boolean post, String charset,
			String proxyHost, int proxyPort) {
		String proxyip = "�Ǵ�������";// ��¼����ip
		if (StringUtils.isNotEmpty(proxyHost)) {
			proxyip = "����ip " + proxyHost + ":" + proxyPort;
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
			logger.error("����url�����Ӧ����ʧ��", e);
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "����url�����Ӧ����ʧ��", LogUtil
					.getExceptionError(e));
		}
		return "";
	}

	/**
	 * ����url�����Ӧ����
	 * 
	 * @param url
	 * @return
	 */
	public static String conUrl(String url, boolean post, String charset,
			String proxyHost, int proxyPort, String cookie) {
		String proxyip = cookieNum + " cookie��" + cookie + " �Ǵ�������";// ��¼����ip
		if (StringUtils.isNotEmpty(proxyHost)) {
			proxyip = cookieNum + " cookie��" + cookie + " ����ip " + proxyHost
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
			logger.error("����url�����Ӧ����ʧ��", e);
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "����url�����Ӧ����ʧ��", LogUtil
					.getExceptionError(e));
		}
		return "";
	}

	/**
	 * �жϰ����ַ�����Ƿ���ȷ
	 * 
	 * @param companyName
	 * @return true�����⣬false��ȷ
	 */
	public static boolean aliCompanyName(String companyName, String url) {
		logger.debug("aliCompanyName():companyName=" + companyName);
		boolean isName = false;
		// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ��||����Ϊ����Ͱͣ�����Ƶ���������||���ӵȴ�||����(404||302)(�еĴ���ip���ܴ�������)
		if (StringUtils.isEmpty(companyName)
				|| "����Ͱ�".equals(companyName)
				|| "���������У����Ժ�..".equals(companyName)
				|| "�ף�����������".equals(companyName)
				|| "�Բ��������ʵ�ҳ�治���ڣ�".equals(companyName)
				|| "1688.com,����Ͱʹ����ȫ�����Ĳɹ�����ƽ̨".equals(companyName)
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
	 * �������ݻ��String
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
		if (xml.indexOf("����������") != -1) {
			return false;
		}
		if (xml.length() < minStrNum) {
			return true;
		}
		if (xml.length() < 2000 && xml.indexOf("redirectUrl") != -1) {
			// ��ȡ������Ϊ���� �����ô���IP����
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
			String xml = conUrl(url, false, code, "", 0);// ���Լ��ķ�����������
			if (isRetryConnect(xml, minStrNum) && StringUtils.isNotEmpty(host)) {// ����ip��Ϊ�վ��ô���ip����
				xml = conUrl(url, false, code, host, port);
			}
			int num = 0;// �ڶ��λ�ȡlistIP���±�

			// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ����������Ϊ����Ͱͣ�����Ƶ�������������Ҫ���µĴ���ipȥ������||����404(�еĴ���ip���ܴ�������)
			if (isRetryConnect(xml, minStrNum)) {
				if (listIP == null || listIP.size() == 0) {// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					// proxyUrl = "http://pachong.org/anonymous.html";// ���ô����ַ
					// listIP = ip(proxyUrl);// ����ip��
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					proxyUrl = "http://revx.daili666.com";
					listIP = JsoupUtil.daliIp();// �������ô���ip
					if (listIP.size() > 1) {
						String ip = listIP.get(0);
						host = ip.substring(0, ip.indexOf(":"));// ���ô���ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// ���ô���˿�
						logger.debug("ip=" + ip);
					} else {
						getUrl(url);
					}
					// }
				}

				if (StringUtils.isNotEmpty(host)) {// ����ip���ڴ��д��ڣ����ô��ڵ�ȥ����
					for (int i = 0; i < listIP.size(); i++) {// ѭ��ip����ȡ�ڴ��е�ip��ip���е��±�λ��
						String ip = listIP.get(i);
						if (ip.equals(host + ":" + port)) {
							num = i;
						}
					}
					num += 1;// ��ǰip�Ѿ�����һ���ˣ����Դ���һ����ʼ
				}

				for (int i = num; i < listIP.size(); i++) {// ѭ��ip����ֱ��xml��Ϊ�գ�������ǰ��Ľӿ�ȫ������ʱ�������Ʋ�Ϊ����Ͱͣ�����Ƶ���������
					// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ����������Ϊ����Ͱͣ�����Ƶ�������������Ҫ���µĴ���ipȥ������||����404(�еĴ���ip���ܴ�������)
					if (isRetryConnect(xml, minStrNum)) {
						String ip = listIP.get(i);
						if (StringUtils.isEmpty(ip)) {
							continue;
						}
						host = ip.substring(0, ip.indexOf(":"));// ���ô���ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// ���ô���˿�
						xml = conUrl(url, false, code, host, port);

						if (isRetryConnect(xml, minStrNum)) {
							if ((i + 1) == listIP.size()) {// �����һ������ip���Ǿ����´Ӵ�����ҳ��ȡip��ipÿ�춼��仯��
								num = 0;
								listIP = null;// �ƿ�ip��
								host = "";// �ƿ�ip
								port = 0;// �ƿն˿�
								// if
								// (proxyUrl.equals("http://pachong.org/anonymous.html"))
								// {
								// proxyUrl =
								// "http://pachong.org/transparent.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// ��������ip��������Ҫ���ڴ��д��ڵ�ip�Ͷ˿��ƿգ���֤�´η��ʵ�ʱ�����ڴ��е�ip�Ͷ˿�
								// } else {
								// proxyUrl =
								// "http://pachong.org/anonymous.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// ��������ip��������Ҫ���ڴ��д��ڵ�ip�Ͷ˿��ƿգ���֤�´η��ʵ�ʱ�����ڴ��е�ip�Ͷ˿�
								// }
								// if (listIP == null || listIP.size() == 0) {//
								// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
								proxyUrl = "http://revx.daili666.com";
								listIP = daliIp();// �������ô���ip
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
			logger.error("�������ݻ��Jsoup.Documentʧ��" + url, e);
			return "";
		}
	}

	/**
	 * �������ݻ��Jsoup.Document
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
			if (StringUtils.isNotEmpty(host)) {// ����ip��Ϊ�վ��ô���ip����
				xml = conUrl(url, false, "GBK", host, port);
			}
			if (StringUtils.isEmpty(xml)) {// ��һ��ûִ�л������ӳ�ʱ������Ϊ��һ��Ĵ���ip��������
				xml = conUrl(url, false, "GBK", "", 0);// ���Լ��ķ�����������
				if (StringUtils.isNotEmpty(xml) && listIP != null) {// ���������������һ��Ĵ���ip����
					listIP = null;// �ƿ�ip��
					host = "";// �ƿ�ip
					port = 0;// �ƿն˿�
				}
			}
			int num = 0;// �ڶ��λ�ȡlistIP���±�

			// ����&nbsp;����ʺŵ�����
			Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));

			String companyName = commodityUrl(doc, url);// ��˾��

			// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ����������Ϊ����Ͱͣ�����Ƶ�������������Ҫ���µĴ���ipȥ������||����404(�еĴ���ip���ܴ�������)
			if (aliCompanyName(companyName, url)) {
				if (listIP == null || listIP.size() == 0) {// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					// proxyUrl = "http://pachong.org/anonymous.html";// ���ô����ַ
					// listIP = ip(proxyUrl);// ����ip��
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					proxyUrl = "http://revx.daili666.com/";
					listIP = daliIp();// �������ô���ip
					String ip = listIP.get(0);
					host = ip.substring(0, ip.indexOf(":"));// ���ô���ip
					port = Integer.parseInt(ip.substring(ip.indexOf(":") + 1));// ���ô���˿�
					// }
				}

				if (StringUtils.isNotEmpty(host)) {// ����ip���ڴ��д��ڣ����ô��ڵ�ȥ����
					for (int i = 0; i < listIP.size(); i++) {// ѭ��ip����ȡ�ڴ��е�ip��ip���е��±�λ��
						String ip = listIP.get(i);
						if (ip.equals(host + ":" + port)) {
							num = i;
						}
					}
					num += 1;// ��ǰip�Ѿ�����һ���ˣ����Դ���һ����ʼ
				}

				for (int i = num; i < listIP.size(); i++) {// ѭ��ip����ֱ��xml��Ϊ�գ�������ǰ��Ľӿ�ȫ������ʱ�������Ʋ�Ϊ����Ͱͣ�����Ƶ���������
					// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ����������Ϊ����Ͱͣ�����Ƶ�������������Ҫ���µĴ���ipȥ������||����404(�еĴ���ip���ܴ�������)
					if (aliCompanyName(companyName, url)) {
						logger
								.debug("is404="
										+ (StringUtils.isNotEmpty(companyName)
												&& companyName.length() > 3 && companyName
												.substring(0, 3).equals("404")));
						// 404����������ʹ�ô���ip����ģ����ñ���������һ�Σ��Ѵ���ip�ƿ�(�еĴ���ip���ܴ�������)
						if (StringUtils.isNotEmpty(companyName)
								&& companyName.length() > 3
								&& companyName.substring(0, 3).equals("404")) {
							String url1 = "http://szoycp.1688.com";
							xml = JsoupUtil.conUrl(url1, false, "GBK", host,
									port);
							Document doc1 = Jsoup.parse(xml);
							companyName = JsoupUtil.commodityUrl(doc1, url1);// ��˾��
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
						host = ip.substring(0, ip.indexOf(":"));// ���ô���ip
						port = Integer.parseInt(ip
								.substring(ip.indexOf(":") + 1));// ���ô���˿�
						xml = conUrl(url, false, "GBK", host, port);
						doc = Jsoup.parse(xml);
						companyName = commodityUrl(doc, url);// ��˾��
						if ("1688.com,����Ͱʹ����ȫ�����Ĳɹ�����ƽ̨".equals(companyName)) {
							if (cookieNum > listCookie.size()) {
								cookieNum = 1;
							}
							String cookie = listCookie.get(cookieNum);
							cookieNum++;
							xml = conUrl(url, false, "GBK", host, port, cookie);
							doc = Jsoup.parse(xml);
							companyName = commodityUrl(doc, url);// ��˾��
						}

						if (aliCompanyName(companyName, url)) {
							if ((i + 1) == listIP.size()) {// �����һ������ip���Ǿ����´Ӵ�����ҳ��ȡip��ipÿ�춼��仯��
								num = 0;
								listIP = null;// �ƿ�ip��
								host = "";// �ƿ�ip
								port = 0;// �ƿն˿�
								// if
								// (proxyUrl.equals("http://pachong.org/anonymous.html"))
								// {
								// proxyUrl =
								// "http://pachong.org/transparent.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// ��������ip��������Ҫ���ڴ��д��ڵ�ip�Ͷ˿��ƿգ���֤�´η��ʵ�ʱ�����ڴ��е�ip�Ͷ˿�
								// } else {
								// proxyUrl =
								// "http://pachong.org/anonymous.html";
								// listIP = JsoupUtil.ip(proxyUrl);//
								// ��������ip��������Ҫ���ڴ��д��ڵ�ip�Ͷ˿��ƿգ���֤�´η��ʵ�ʱ�����ڴ��е�ip�Ͷ˿�
								// }
								// if (listIP == null || listIP.size() == 0) {//
								// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
								proxyUrl = "http://revx.daili666.com";
								listIP = daliIp();// �������ô���ip
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
			logger.error("�������ݻ��Jsoup.Documentʧ��" + url, e);
			// �����ʼ�
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(),
			// "�������ݻ��Jsoup.Documentʧ��"
			// + url, LogUtil.getExceptionError(e));
			return getDocument(url);
		}
	}

	/**
	 * ��֤��ַ
	 * 
	 * @param doc
	 * @return
	 */
	public static String commodityUrl(Document doc, String url) {
		LogUtil
				.writeCommodityLog("��֤��ַJsoupUtil.commodityUrl()...........start "
						+ url);
		String companyName = "";// ��˾��
		Elements companyEle = doc.select("[class=company-name]");
		companyName = companyEle.text();// ��˾��
		if (StringUtils.isEmpty(companyName)) {
			Elements titleEle = doc.select("title");
			companyName = titleEle.text();// ��˾��
		}
		boolean isMessy = isMessyCode(companyName);
		if (isMessy) {// ����
			companyName = "";
		}
		LogUtil.writeCommodityLog("��֤��ַJsoupUtil.commodityUrl()...........end "
				+ url + " " + companyName + " " + isMessy);
		return companyName;
	}

	public static boolean isMessyCode(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			// ����Unicode������ĳ���ַ���ת��ʱ������ڸ��ַ�����û�ж�Ӧ�ı��룬��õ�0x3f�����ʺ��ַ�?��
			// �������ַ�����Unicode����ת��ʱ�����������������ڸ��ַ�����û�б�ʶ�κε��ַ�����õ��Ľ����0xfffd
			// System.out.println("--- " + (int) c);
			if ((int) c == 0xfffd) {
				// ��������
				// System.out.println("�������� " + (int) c);
				return true;
			}
		}
		return false;
	}

	/**
	 * �������ݻ��Jsoup.Document
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDocumentByProxy(String url) {
		try {
			String xml = conUrl(url, false, "utf-8", "", 0);
			for (int i = 1; i <= 3; i++) {
				if (StringUtils.isEmpty(xml)) {
					String flag = "�ɹ�";
					xml = conUrl(url, false, "utf-8", "", 0);
					if (StringUtils.isEmpty(xml)) {
						flag = "ʧ��";
					}
					// �����ʼ�
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), flag + " �ӿڳ�ʱ����"
							+ i + "�γ�������" + url, flag);
				} else {
					break;
				}
			}
			Document doc = Jsoup.parse(xml);
			return doc;
		} catch (Exception e) {
			logger.error("�������ݻ��Jsoup.Documentʧ��,�ظ�����" + url, e);
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(),
					"�������ݻ��Jsoup.Documentʧ��,�ظ�����" + url, LogUtil
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
	 * ��ȡ��Ŀ��������ַ
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
	 * ��ȡͼƬ��ַ
	 * 
	 * @return
	 */
	public static String SystemPathImage(String shopId) {
		String filePath = JsoupUtil.SystemPath() + "/image/" + shopId + "/";// ����ͼƬ���صĸ�Ŀ¼
		return filePath;
	}

	/**
	 * �۸�ת����ԪתΪ�֣�
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
	 * �۸�ת������תΪԪ��
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
	 * ��λת����ǧ��תΪ�ˣ�
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
	 * URL���루gb2312����ת���ģ�
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL���루gb2312����ת���ģ�ʧ��", e);
		}
		return "";
	}

	/**
	 * URL���루����תgb2312���룩
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlEcode(String str) {
		try {
			return URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL���루����תgb2312���룩ʧ��", e);
		}
		return "";
	}

	/**
	 * URL���루utf-8����ת���ģ�
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlDecodeByUtf(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL���루utf-8����ת���ģ�ʧ��", e);
		}
		return "";
	}

	/**
	 * URL���루����תutf-8���룩
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String urlEcodeByUtf(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL���루����תutf-8���룩ʧ��", e);
		}
		return "";
	}

	// ���ص�Json
	public static String getJson(String code, String info) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\"");
		sb.append("}");
		return sb.toString();
	}

	// ���ص�Json
	public static String getCompanyUrlJson(String code, String info, String url) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"url\":").append("\"" + url + "\"");
		sb.append("}");
		return sb.toString();
	}

	// ���ص�Json
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

	// ���ص�Json
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

	// ���ص�Json
	public static String getUptokenJson(String code, String info, String uptoken) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":").append("\"" + info + "\",");
		sb.append("\"uptoken\":").append("\"" + uptoken + "\"");
		sb.append("}");
		return sb.toString();
	}

	// ͼƬ�ϴ����ص�Json
	public static String getGoodsImgJson(String code, String url, String width,
			String height) {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"errcode\":").append(code).append(",");
		sb.append("\"errmsg\":\"�ɹ�\",");
		sb.append("\"result\":[{\"url\":\"").append(url + "\"");
		sb.append(",\"width\":").append(width);
		sb.append(",\"height\":").append(height).append("}]");
		sb.append("}");
		return sb.toString();
	}

	// �û���¼���ص�Json
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

	// �û���¼���ص�Json
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
	 * ��ȡ����ip
	 * 
	 * @param doc
	 * @return
	 */
	public static List<String> ip(String url) {
		LogUtil.writeCommodityLog("��ȡ����ip...........start ");

		List<String> list = new ArrayList<String>();
		// http://pachong.org 29ʧ��
		// http://pachong.org/anonymous.html 24ʧ��
		// http://pachong.org/transparent.html 25ʧ��
		// String url = "http://pachong.org/transparent.html";
		Document doc = JsoupUtil.getDocumentByProxy(url);
		// System.out.println(doc.html());
		Elements scriptEle = doc.select("[type=text/javascript]");
		// var hen=1376+6577;var dog=4752+817^hen;var cock=1475+561^dog;var
		// chick=3421+6078^cock;var cat=4547+4981^chick;
		String script = scriptEle.get(2).html();
		String[] str = script.split(";");
		Map<String, String> map = new HashMap<String, String>();// ����ֵ
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
			// �й�ͼƬ��ַ&&״̬Ϊ�����С�
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

		LogUtil.writeCommodityLog("��ȡ����ip...........end " + map);
		return list;
	}

	/**
	 * ��ȡ����ip
	 * 
	 * @param doc
	 * @return
	 */
	public static List<String> daliIp() {
		LogUtil.writeCommodityLog("��ȡ����daliIp...........start ");
		// 555281263105790 558460242745998
		String doc = JsoupUtil
				.conUrl(
						"http://revx.daili666.com/ip/?tid=558460242745998&num=500&delay=1&foreign=all",
						false, "UTF8", "", 0);
		List<String> listIP = new ArrayList<String>();// ip��
		String[] str = doc.split("\n");
		for (String s : str) {
			listIP.add(s.trim());
		}
		LogUtil.writeCommodityLog("��ȡ����daliIp...........end " + listIP.size());
		return listIP;
	}

	// url�������ַ�ת��
	public static String getReplace(String url) {

		if (StringUtils.isEmpty(url)) {
			return "";
		}
		return url.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"",
				"&quot;");
	}

	// url�������ַ�ת��
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
