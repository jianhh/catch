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
		searchList(null, "女装");
	}

	/**
	 * 搜索列表
	 * 
	 * @throws Exception
	 */
	public static void searchList(String city, String keyword) {
		// logger.debug("searchList。。。。。。。。。。。。。。。。。。。。。。");
		// logger.debug("搜索列表keyword:" + keyword);
		String keyCode = JsoupUtil.urlEcode(keyword);
		if (StringUtils.isEmpty(city)) {
			city = "深圳市";
		}
		String url = "http://s.1688.com/selloffer/offer_search.htm?city="
				+ JsoupUtil.urlEcode(city) + "&keywords=" + keyCode;
		// logger.debug("url:" + url);

		Document doc = JsoupUtil.getDocument(url);
		Elements listEle = doc
				.select("[class=sm-floorOfferResult-itemcountcontain]");
		LogUtil.writeBusinessesLog("获取到的列表页地址数量：" + listEle.size());
		if (listEle.size() > 0) {
			for (int i = 1; i < listEle.size(); i++) {
				// 获取a标签
				Elements aEle = listEle.get(i).select("a");
				// 获取商品列表页地址
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
			// 获取a标签
			Elements aEle = userUrlEle.get(i).select("a");
			// 获取商品详情页地址
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
		LogUtil.writeBusinessesLog("第" + num + "页 ok");
		// }

		String href = url;
		Elements pageNodeEle = docHref.select("[trace=pageNode]");
		if (pageNodeEle.size() > 0) {
			// 获取a标签
			Elements aEle = pageNodeEle.get(0).select("a");
			// 获取下一页地址
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
	 * 获取商家信息地址
	 * 
	 * @param url
	 *            商品详情页地址
	 * @return
	 */
	public static BusinessesInfoSchema userUrl(String url) throws Exception {
		// logger.debug("setUserInfo。。。。。。。。。。。。。。。。。。。。。。");
		// logger.debug("商品详情页地址url:" + url);
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		Document doc = JsoupUtil.getDocument(url);
		if (doc == null) {
			return schema;
		}
		// 获取公司档案地址
		Elements userUrlEle = doc.select("[data-page-name=creditdetail]");
		Elements aEle = userUrlEle.select("a");
		if (aEle.size() > 0) {// 公司档案
			String userurl = aEle.get(0).attr("href");
			// logger.debug("公司档案地址:" + userurl);
			Document doc1 = JsoupUtil.getDocument(userurl);
			if (doc1 == null) {
				return schema;
			}
			// 获取公司黄页地址(地址里面加上tab是连接到联系方式页面)
			Elements comment_link = doc1.select("[class=comment-link]");
			String infoUrl = comment_link.attr("href");
			if (StringUtils.isNotEmpty(infoUrl)) {
				infoUrl = infoUrl + "&tab=companyWeb_contact";
				schema = setUserInfo(infoUrl);
				// logger.debug("获取商家信息成功：" + infoUrl);

				String uid = infoUrl.substring(infoUrl.lastIndexOf("/") + 1,
						infoUrl.indexOf(".html"));
				schema.setUid(uid);
				// logger.debug("商家ID:" + uid);
			}
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
