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

public class BusinessesMUtil {

	static JLogger logger = LoggerFactory.getLogger(BusinessesMUtil.class);

	public static void main(String[] args) throws Exception {
		// String img =
		// "http://i01.c.aliimg.com/img/ibank/2014/271/792/1654297172_1463428846.32x32.jpg";
		// img = img.substring(0, img.indexOf("32x32")) + "jpg";
		// System.out.println(img);
		// File picture = new File("C:/pic/1.jpg");
		// System.out.println(picture);
		// BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
		// System.out.println(sourceImg.getWidth());
		// System.out.println(sourceImg.getHeight());

		searchList(null, "Ůװ");
		// String city = "������";
		// String url = "http://m.1688.com/gongsi_search/-"
		// + JsoupUtil.urlEcode("Ůװ").replaceAll("%", "") + ".html?city="
		// + JsoupUtil.urlEcodeByUtf(city);
		// System.out.println(url);
	}

	/**
	 * �����б�
	 * 
	 * @throws Exception
	 */
	public static void searchList(String city, String keyword) {
		if (StringUtils.isEmpty(city)) {
			city = "������";
		}
		String url = "http://m.1688.com/gongsi_search/-"
				+ JsoupUtil.urlEcode(keyword).replaceAll("%", "")
				+ ".html?city=" + JsoupUtil.urlEcodeByUtf(city);// �����б�ҳ��ַת��
		infoUrl(url, keyword, 1);
	}

	public static void infoUrl(String url, String keyword, int num) {
		LogUtil.writeBusinessesLog(url + "&beginPage=" + num);
		Document doc = JsoupUtil.getDocument(url);
		Elements listEle = doc.select("[class=item]");
		LogUtil.writeBusinessesLog("��ȡ�����б�ҳ��ַ������" + listEle.size());
		BusinessesInfoBO bo = new BusinessesInfoBO();
		for (int i = 0; i < 1; i++) {
			// ��ȡa��ǩ
			Elements aEle = listEle.get(i).select("a");
			// ��ȡ��Ʒ����ҳ��ַ
			String href = aEle.attr("href");

			BusinessesInfoSchema schema = userUrl(href);
			LogUtil.writeBusinessesLog("phone:" + schema.getPhone());
			// if (StringUtils.isNotEmpty(schema.getPhone())
			// && StringUtils.isNotEmpty(schema.getAddress())) {
			// schema.setKeyword(keyword);
			// boolean success = false;
			// try {
			// success = bo.addBusinessesInfo(schema);
			// } catch (Exception e) {
			// logger.error(e);
			// }
			// LogUtil.writeBusinessesLog(success);
			// }
		}
		int count = 0;// ��ҳ��
		Elements pageCountEle = doc.select("[id=page_info_msg]");
		String pageCount = pageCountEle.attr("value");// ��ҳ��
		if (StringUtils.isNotEmpty(pageCount)) {
			count = Integer.parseInt(pageCount);
		}
		count = 0;// ������
		if (num < count) {
			int i = num + 1;
			infoUrl(url, keyword, i);
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
	public static BusinessesInfoSchema userUrl(String url) {
		// logger.debug("setUserInfo��������������������������������������������");
		System.out.println("��Ʒ����ҳ��ַurl:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document doc = JsoupUtil.getDocument(url);
		if (doc == null) {
			return schema;
		}
		// ��ȡ��˾��ϸ��Ϣ��ַ
		Elements userUrlEle = doc.select("[data-mtrace=wp_page_index_company]");
		String userurl = userUrlEle.attr("href");
		Document doc1 = JsoupUtil.getDocument(userurl);
		if (doc1 == null) {
			return schema;
		}
		System.out.println("��˾��Ϣҳ:" + userurl);
		// ��ȡ��˾��ҳ��ַ(��ַ�������tab�����ӵ���ϵ��ʽҳ��)
		Elements basicEle = doc1.select("[class=archive-list]");
		Elements liEle = basicEle.select("li");
		System.out.println(liEle.size());
		for (int i = 0; i < liEle.size(); i++) {
			Elements emEle = liEle.get(i).select("em");
			Elements spanEle = liEle.get(i).select("span");
			String em = emEle.text();
			String span = spanEle.text();
			logger.debug("em:" + em + " span:" + span);
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
