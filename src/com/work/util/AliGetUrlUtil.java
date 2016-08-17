package com.work.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StringUtils;

/**
 * 
 * ���湤����
 * 
 * @author tangbiao
 * 
 * Sep 2, 2014 4:30:01 PM
 */
public class AliGetUrlUtil {

	static JLogger logger = LoggerFactory.getLogger(AliGetUrlUtil.class);

	public static List<String> listIP = null;// ip��
	static String proxyUrl = "";// �����ַ
	public static String host = "";// ����ip
	public static int port = 0;// ����˿�

	/**
	 * �������ݻ��Jsoup.Document
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
			if (StringUtils.isNotEmpty(host)) {// ����ip��Ϊ�վ��ô���ip����
				xml = JsoupUtil.conUrl(url, false, "GBK", host, port);
			}
			if (StringUtils.isEmpty(xml)) {// ��һ��ûִ�л������ӳ�ʱ������Ϊ��һ��Ĵ���ip��������
				xml = JsoupUtil.conUrl(url, false, "GBK", "", 0);// ���Լ��ķ�����������
				// ���Լ��ķ�����������
				if (StringUtils.isNotEmpty(xml) && listIP != null) {// ���������������һ��Ĵ���ip����
					listIP = null;// �ƿ�ip��
					host = "";// �ƿ�ip
					port = 0;// �ƿն˿�
				}
			}
			int num = 0;// �ڶ��λ�ȡlistIP���±�
			Document doc = Jsoup.parse(xml);
			String companyName = JsoupUtil.commodityUrl(doc, url);// ��˾��

			if (JsoupUtil.aliCompanyName(companyName, url)) {// xmlΪ�գ�������ǰ��Ľӿ�ȫ������ʱ����������Ϊ����Ͱͣ�����Ƶ���������
				if (listIP == null) {// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					// proxyUrl = "http://pachong.org/anonymous.html";// ���ô����ַ
					// listIP = JsoupUtil.ip(proxyUrl);// ����ip��
					//
					// if (listIP == null || listIP.size() == 0) {//
					// ip���ڴ���Ϊ�վ�ȥ������ҳ��ȡip
					proxyUrl = "http://revx.daili666.com";
					listIP = JsoupUtil.daliIp();// �������ô���ip
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
					if (JsoupUtil.aliCompanyName(companyName, url)) {
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
						xml = JsoupUtil.conUrl(url, false, "GBK", host, port);
						doc = Jsoup.parse(xml);
						companyName = JsoupUtil.commodityUrl(doc, url);// ��˾��
						if ("1688.com,����Ͱʹ����ȫ�����Ĳɹ�����ƽ̨".equals(companyName)) {
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
							companyName = JsoupUtil.commodityUrl(doc, url);// ��˾��
						}

						if (JsoupUtil.aliCompanyName(companyName, url)) {
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
								listIP = JsoupUtil.daliIp();// �������ô���ip
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
			logger.error("�������ݻ��Jsoup.Documentʧ��" + url, e);
			// �����ʼ�
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(),
			// "�������ݻ��Jsoup.Documentʧ��"
			// + url, LogUtil.getExceptionError(e));
			return getDocumentUrl(url);
		}
	}
}
