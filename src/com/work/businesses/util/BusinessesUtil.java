package com.work.businesses.util;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.businesses.bo.BusinessesInfoBO;
import com.work.businesses.schema.BusinessesInfoSchema;
import com.work.util.JsoupUtil;

public class BusinessesUtil {

	static JLogger logger = LoggerFactory.getLogger(BusinessesUtil.class);

	public static void main(String[] args) throws Exception {
		searchList(null, "Ůװ");
	}

	/**
	 * �����б�
	 * 
	 * @throws Exception
	 */
	public static void searchList(String city, String keyword) {
		// logger.debug("searchList��������������������������������������������");
		// logger.debug("�����б�keyword:" + keyword);
		String keyCode = JsoupUtil.urlEcode(keyword);
		if (StringUtils.isEmpty(city)) {
			city = "������";
		}
		String url = "http://s.1688.com/selloffer/offer_search.htm?city="
				+ JsoupUtil.urlEcode(city) + "&keywords=" + keyCode;
		// logger.debug("url:" + url);

		Document doc = JsoupUtil.getDocument(url);
		Elements listEle = doc
				.select("[class=sm-floorOfferResult-itemcountcontain]");
		LogUtil.writeBusinessesLog("��ȡ�����б�ҳ��ַ������" + listEle.size());
		if (listEle.size() > 0) {
			for (int i = 1; i < listEle.size(); i++) {
				// ��ȡa��ǩ
				Elements aEle = listEle.get(i).select("a");
				// ��ȡ��Ʒ�б�ҳ��ַ
				String href = aEle.attr("href");
				LogUtil.writeBusinessesLog(i + " " + href);
				try {
					infoUrl(keyword, href, 1);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		} else {
			try {
				infoUrl(keyword, url, 1);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public static void infoUrl(String keyword, String url, int num)
			throws Exception {
		Document docHref = JsoupUtil.getDocument(url);
		Elements userUrlEle = docHref.select("[class=su-photo220]");
		BusinessesInfoBO bo = new BusinessesInfoBO();
		// if (num != 76) {
		for (int i = 0; i < userUrlEle.size(); i++) {
			// ��ȡa��ǩ
			Elements aEle = userUrlEle.get(i).select("a");
			// ��ȡ��Ʒ����ҳ��ַ
			String href = aEle.attr("href");

			BusinessesInfoSchema schema = userUrl(href);
			LogUtil.writeBusinessesLog("phone:" + schema.getPhone());
			if (StringUtils.isNotEmpty(schema.getPhone())
					&& StringUtils.isNotEmpty(schema.getAddress())) {
				schema.setKeyword(keyword);
				boolean success = false;
				try {
					success = bo.addBusinessesInfo(schema);
				} catch (Exception e) {
					logger.error(e);
				}
				LogUtil.writeBusinessesLog(success);
			}
		}
		LogUtil.writeBusinessesLog("��" + num + "ҳ ok");
		// }

		String href = url;
		Elements pageNodeEle = docHref.select("[trace=pageNode]");
		if (pageNodeEle.size() > 0) {
			// ��ȡa��ǩ
			Elements aEle = pageNodeEle.get(0).select("a");
			// ��ȡ��һҳ��ַ
			href = aEle.attr("href");
		}
		if (num < 100) {
			int i = num + 1;
			LogUtil.writeBusinessesLog(i);
			href = href.substring(0, href.indexOf("beginPage")) + "beginPage=";
			infoUrl(keyword, href + i, i);
		}
	}

	/**
	 * 
	 * ��ȡ�̼���Ϣ��ַ
	 * 
	 * @param url
	 *            ��Ʒ����ҳ��ַ
	 * @return
	 */
	public static BusinessesInfoSchema userUrl(String url) throws Exception {
		// logger.debug("setUserInfo��������������������������������������������");
		// logger.debug("��Ʒ����ҳ��ַurl:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document doc = JsoupUtil.getDocument(url);
		if (doc == null) {
			return schema;
		}
		// ��ȡ��˾������ַ
		Elements userUrlEle = doc.select("[data-page-name=creditdetail]");
		Elements aEle = userUrlEle.select("a");
		if (aEle.size() > 0) {// ��˾����
			String userurl = aEle.get(0).attr("href");
			// logger.debug("��˾������ַ:" + userurl);
			Document doc1 = JsoupUtil.getDocument(userurl);
			if (doc1 == null) {
				return schema;
			}
			// ��ȡ��˾��ҳ��ַ(��ַ�������tab�����ӵ���ϵ��ʽҳ��)
			Elements comment_link = doc1.select("[class=comment-link]");
			String infoUrl = comment_link.attr("href");
			if (StringUtils.isNotEmpty(infoUrl)) {
				infoUrl = infoUrl + "&tab=companyWeb_contact";
				schema = setUserInfo(infoUrl);
				// logger.debug("��ȡ�̼���Ϣ�ɹ���" + infoUrl);

				String uid = infoUrl.substring(infoUrl.lastIndexOf("/") + 1,
						infoUrl.indexOf(".html"));
				schema.setUid(uid);
				// logger.debug("�̼�ID:" + uid);
			}
		}
		return schema;
	}

	/**
	 * ��ȡ�̼���ϵ��Ϣ
	 * 
	 * @param url
	 *            �̼���ϵ��ʽ��ַ
	 * @return
	 */
	public static BusinessesInfoSchema setUserInfo(String url) {
		// logger.debug("userinfoByUrl��������������������������������������������");
		// logger.debug("�̼���ϵ��ʽ��ַurl:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document docUrl = JsoupUtil.getDocument(url);
		if (docUrl == null) {
			return schema;
		}
		// ��ȡ��ϵ��
		Elements contentEle = docUrl.select("[class=content]");
		Elements nameEle = contentEle.select("[class=contact-name]");
		if (nameEle.size() > 0) {
			String name = nameEle.get(0).text();
			if (name.indexOf("����") > 0) {
				name = name.substring(0, name.indexOf("����") - 1);
			} else if (name.indexOf("Ůʿ") > 0) {
				name = name.substring(0, name.indexOf("Ůʿ") - 1);
			}
			schema.setName(name);
			// logger.debug("��ϵ��:" + name);
		} else {
			return schema;
		}
		Elements pEle = contentEle.get(1).select("p");
		int k = 0;
		if (pEle.size() > 0) {
			for (int i = 0; i < pEle.size(); i++) {
				k = i;
				String phoneName = pEle.get(i).text();
				// logger.debug("p��ǩ�������:" + phoneName);
				if (phoneName.indexOf("�ƶ��绰") > -1) {
					String phone = pEle.get(k + 1).text();
					if (StringUtils.isNum(phone)) {
						schema.setPhone(phone);
						// logger.debug("�ֻ���:" + phone);
					}
				} else if (phoneName.indexOf("�绰 :") > -1) {
					String number = pEle.get(k + 1).text();
					schema.setNumber(number);
					// logger.debug("�绰����:" + number);
				} else if (phoneName.indexOf("��ַ") > -1) {
					String address = pEle.get(k + 1).text();
					schema.setAddress(address);
					// logger.debug("��ȡ��ַ:" + address);
				} else if (phoneName.indexOf("�ʱ�") > -1) {
					String zipcode = pEle.get(k + 1).text();
					if (zipcode.length() == 6 && StringUtils.isNum(zipcode)) {
						schema.setZipcode(zipcode);
						// logger.debug("��ȡ�ʱ�:" + zipcode);
					}
				}
			}
		}

		// ��ȡ��˾��ҳURL
		Elements outsiteEle = contentEle.select("[class=outsite]");
		String companyUrl = outsiteEle.attr("href");
		schema.setCompanyUrl(companyUrl);
		// logger.debug("��ȡ��˾��ҳURL:" + companyUrl);
		return schema;
	}

}
