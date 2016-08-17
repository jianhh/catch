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

		searchList(null, "女装");
		// String city = "深圳市";
		// String url = "http://m.1688.com/gongsi_search/-"
		// + JsoupUtil.urlEcode("女装").replaceAll("%", "") + ".html?city="
		// + JsoupUtil.urlEcodeByUtf(city);
		// System.out.println(url);
	}

	/**
	 * 搜索列表
	 * 
	 * @throws Exception
	 */
	public static void searchList(String city, String keyword) {
		if (StringUtils.isEmpty(city)) {
			city = "深圳市";
		}
		String url = "http://m.1688.com/gongsi_search/-"
				+ JsoupUtil.urlEcode(keyword).replaceAll("%", "")
				+ ".html?city=" + JsoupUtil.urlEcodeByUtf(city);// 搜索列表页地址转换
		infoUrl(url, keyword, 1);
	}

	public static void infoUrl(String url, String keyword, int num) {
		LogUtil.writeBusinessesLog(url + "&beginPage=" + num);
		Document doc = JsoupUtil.getDocument(url);
		Elements listEle = doc.select("[class=item]");
		LogUtil.writeBusinessesLog("获取到的列表页地址数量：" + listEle.size());
		BusinessesInfoBO bo = new BusinessesInfoBO();
		for (int i = 0; i < 1; i++) {
			// 获取a标签
			Elements aEle = listEle.get(i).select("a");
			// 获取商品详情页地址
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
		int count = 0;// 总页数
		Elements pageCountEle = doc.select("[id=page_info_msg]");
		String pageCount = pageCountEle.attr("value");// 总页数
		if (StringUtils.isNotEmpty(pageCount)) {
			count = Integer.parseInt(pageCount);
		}
		count = 0;// 测试用
		if (num < count) {
			int i = num + 1;
			infoUrl(url, keyword, i);
		}
	}

	/**
	 * 
	 * 获取商家信息地址
	 * 
	 * @param url
	 *            商品详情页地址
	 * @return
	 */
	public static BusinessesInfoSchema userUrl(String url) {
		// logger.debug("setUserInfo。。。。。。。。。。。。。。。。。。。。。。");
		System.out.println("商品详情页地址url:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document doc = JsoupUtil.getDocument(url);
		if (doc == null) {
			return schema;
		}
		// 获取公司详细信息地址
		Elements userUrlEle = doc.select("[data-mtrace=wp_page_index_company]");
		String userurl = userUrlEle.attr("href");
		Document doc1 = JsoupUtil.getDocument(userurl);
		if (doc1 == null) {
			return schema;
		}
		System.out.println("公司信息页:" + userurl);
		// 获取公司黄页地址(地址里面加上tab是连接到联系方式页面)
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
	 * 获取商家联系信息
	 * 
	 * @param url
	 *            商家联系方式地址
	 * @return
	 */
	public static BusinessesInfoSchema setUserInfo(String url) {
		// logger.debug("userinfoByUrl。。。。。。。。。。。。。。。。。。。。。。");
		// logger.debug("商家联系方式地址url:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document docUrl = JsoupUtil.getDocument(url);
		if (docUrl == null) {
			return schema;
		}
		// 获取联系人
		Elements contentEle = docUrl.select("[class=content]");
		Elements nameEle = contentEle.select("[class=contact-name]");
		if (nameEle.size() > 0) {
			String name = nameEle.get(0).text();
			if (name.indexOf("先生") > 0) {
				name = name.substring(0, name.indexOf("先生") - 1);
			} else if (name.indexOf("女士") > 0) {
				name = name.substring(0, name.indexOf("女士") - 1);
			}
			schema.setName(name);
			// logger.debug("联系人:" + name);
		} else {
			return schema;
		}
		Elements pEle = contentEle.get(1).select("p");
		int k = 0;
		if (pEle.size() > 0) {
			for (int i = 0; i < pEle.size(); i++) {
				k = i;
				String phoneName = pEle.get(i).text();
				// logger.debug("p标签里的名称:" + phoneName);
				if (phoneName.indexOf("移动电话") > -1) {
					String phone = pEle.get(k + 1).text();
					if (StringUtils.isNum(phone)) {
						schema.setPhone(phone);
						// logger.debug("手机号:" + phone);
					}
				} else if (phoneName.indexOf("电话 :") > -1) {
					String number = pEle.get(k + 1).text();
					schema.setNumber(number);
					// logger.debug("电话号码:" + number);
				} else if (phoneName.indexOf("地址") > -1) {
					String address = pEle.get(k + 1).text();
					schema.setAddress(address);
					// logger.debug("获取地址:" + address);
				} else if (phoneName.indexOf("邮编") > -1) {
					String zipcode = pEle.get(k + 1).text();
					if (zipcode.length() == 6 && StringUtils.isNum(zipcode)) {
						schema.setZipcode(zipcode);
						// logger.debug("获取邮编:" + zipcode);
					}
				}
			}
		}

		// 获取公司主页URL
		Elements outsiteEle = contentEle.select("[class=outsite]");
		String companyUrl = outsiteEle.attr("href");
		schema.setCompanyUrl(companyUrl);
		// logger.debug("获取公司主页URL:" + companyUrl);
		return schema;
	}

}
