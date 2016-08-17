package com.work.businesses.util;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.framework.util.StringUtils;
import com.work.businesses.bo.BusinessesInfoBO;
import com.work.businesses.schema.BusinessesInfoSchema;
import com.work.commodity.util.CommodityUtil;
import com.work.util.JsoupUtil;

public class BusinessesInfoUtil {

	static JLogger logger = LoggerFactory.getLogger(BusinessesInfoUtil.class);

	public static void main(String[] args) {
		infoQiyeUrl("http://futian.1688.com/qiye.htm?keywords=",
				"福田" + ";所有商家", 1);
	}

	public static void infoQiyeUrl(String url, String keyword, int num) {
		LogUtil.writeBusinessesLog(url + "&page=" + num);
		Document doc = JsoupUtil.getDocument(url + "&page=" + num);
		Elements listEle = doc.select("[class=mod-company-list]");
		Elements liEle = listEle.select("[class=sign-pic img-vertical]");
		LogUtil.writeBusinessesLog("liEle.size():" + liEle.size());
		BusinessesInfoBO bo = new BusinessesInfoBO();
		for (int i = 0; i < liEle.size(); i++) {
			Elements aEle = liEle.get(i).select("[class=img-wrap]");
			String href = aEle.attr("href");
			try {
				BusinessesInfoSchema schema = userQiyeInfo(href);
				LogUtil.writeBusinessesLog("phone:" + schema.getPhone()
						+ " number:" + schema.getNumber() + " address:"
						+ schema.getAddress() + " url:" + href);
				if (StringUtils.isNotEmpty(href)) {
					schema.setUid(href);
					schema.setKeyword(keyword);// 搜索关键字
					schema.setCompanyUrl(href);// 店铺域名
					schema.setCreate_time(DateUtil.format8.format(new Date()));
					schema.setGetUrl(url + "&page=" + num);
					boolean success = bo.addBusinessesInfo(schema);
					LogUtil.writeBusinessesLog(keyword + " " + schema.getUid()
							+ " 爬取结果：" + success);
				}
			} catch (Exception e) {
				logger.error(href, e);
			}
		}
		LogUtil.writeBusinessesLog(keyword + " 第" + num + "页 ok");
		Elements pageEle = doc.select("[id=jumpto]");
		String page = pageEle.attr("data-max");
		int count = 0;
		if (StringUtils.isNotEmpty(page)) {
			count = Integer.parseInt(page);
		}
		if (num < count) {
			int i = num + 1;
			infoQiyeUrl(url, keyword, i);
		}
	}

	/**
	 * 
	 * 获取商家信息地址
	 * 
	 * @param url
	 *            店铺域名地址
	 * @return
	 */
	public static BusinessesInfoSchema userQiyeInfo(String url)
			throws Exception {
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		String userurl = url + "/page/creditdetail.htm";
		Document doc = JsoupUtil.getDocument(userurl);
		if (doc == null) {
			return schema;
		}
		Map<String, String> map = CommodityUtil.memberinfo(doc, userurl);// 获取店鋪用戶信息

		// 获取公司黄页地址(地址里面加上tab是连接到联系方式页面)
		Elements comment_link = doc.select("[class=comment-link]");
		String infoUrl = comment_link.attr("href");
		if (StringUtils.isNotEmpty(infoUrl)) {
			infoUrl = infoUrl + "&tab=companyWeb_contact";
			schema = setUserInfo(infoUrl, map);
		}
		Elements infoEle = doc.select("[class=company-info fd-clr]");
		Elements liEle = infoEle.select("li");
		String companyName = CommodityUtil.commodityName(doc, userurl);// 公司名
		schema.setCompany_name(companyName);
		Elements aEle = liEle.select("a");
		String jsonStr = aEle.attr("map-mod-config");
		if (StringUtils.isNotEmpty(jsonStr)) {// 获取公司经营地址
			JSONObject json = new JSONObject(jsonStr);
			String shopAddress = json.getString("address");
			schema.setShop_address(shopAddress);
		}

		String userurl1 = url + "/page/creditdetail_remark.htm";
		Document doc1 = JsoupUtil.getDocument(userurl1);
		if (doc1 != null) {// 获取交易数
			Elements bodyEle = doc1.select("[class=body-col2]");
			Elements emEle = bodyEle.get(0).select("em");
			String num = emEle.text();
			schema.setBusiness_number(num);
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
	public static BusinessesInfoSchema setUserInfo(String url,
			Map<String, String> map) {
		BusinessesInfoSchema schema = new BusinessesInfoSchema();
		String xml = JsoupUtil.conUrl(url, false, "GBK", "", 0);
		Document docUrl = Jsoup.parse(xml);
		if (docUrl != null) {
			// 获取联系人
			Elements contentEle = docUrl.select("[class=content]");
			if (contentEle.size() > 0) {
				Elements pEle = contentEle.get(1).select("p");
				int k = 0;
				if (pEle.size() > 0) {
					for (int i = 0; i < pEle.size(); i++) {
						k = i;
						String phoneName = pEle.get(i).text();
						if (phoneName.indexOf("地址") > -1) {
							String address = pEle.get(k + 1).text();
							schema.setAddress(address);// 获取地址
						} else if (phoneName.indexOf("邮编") > -1) {
							String zipcode = pEle.get(k + 1).text();
							if (zipcode.length() == 6
									&& StringUtils.isNum(zipcode)) {
								schema.setZipcode(zipcode);// 获取邮编
							}
						}
					}
				}
			}
		}

		schema.setName(map.get("contactname")); // 联系人
		schema.setPhone(map.get("contactmobile"));// 手机号
		schema.setNumber(map.get("contactphone"));// 电话号码

		// 获取公司主页URL
		// Elements outsiteEle = contentEle.select("[class=outsite]");
		// String companyUrl = outsiteEle.attr("href");
		// schema.setCompanyUrl(companyUrl);// 获取公司主页URL
		return schema;
	}

	/**
	 * 根据商品爬取商家数据（预留）
	 * 
	 * @param url
	 * @param keyword
	 * @param num
	 */
	public static void infoUrl(String url, String keyword, int num) {
		LogUtil.writeBusinessesLog(url + "&page=" + num);
		Document doc = JsoupUtil.getDocument(url + "&page=" + num);
		Elements listEle = doc.select("[class=mod-offer-list]");
		Elements liEle = listEle.select("li");
		BusinessesInfoBO bo = new BusinessesInfoBO();
		for (int i = 0; i < liEle.size(); i++) {
			Elements aEle = liEle.get(i).select("[class=img-wrap]");
			String href = aEle.attr("href");
			try {
				BusinessesInfoSchema schema = userInfo(href, keyword);
				LogUtil.writeBusinessesLog("phone:" + schema.getPhone()
						+ " number:" + schema.getNumber() + " address:"
						+ schema.getAddress() + " url:" + href);
				boolean success = false;
				if (StringUtils.isNotEmpty(schema.getUid())
						&& StringUtils.isNotEmpty(schema.getAddress())) {
					schema.setKeyword(keyword);// 搜索关键字
					schema.setCompanyUrl(href);// 店铺域名
					success = bo.addBusinessesInfo(schema);
				}
				LogUtil.writeBusinessesLog(keyword + " " + schema.getUid()
						+ " 爬取结果：" + success);
			} catch (Exception e) {
				logger.error(href, e);
			}
		}
		LogUtil.writeBusinessesLog(keyword + " 第" + num + "页 ok");
		if (num < 100) {
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
	public static BusinessesInfoSchema userInfo(String url, String keyword)
			throws Exception {
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
			Document doc1 = JsoupUtil.getDocument(userurl);
			if (doc1 == null) {
				return schema;
			}
			Map<String, String> map = CommodityUtil.memberinfo(doc1, userurl);// 获取店鋪用戶信息

			// 获取公司黄页地址(地址里面加上tab是连接到联系方式页面)
			Elements comment_link = doc1.select("[class=comment-link]");
			String infoUrl = comment_link.attr("href");
			if (StringUtils.isNotEmpty(infoUrl)) {
				infoUrl = infoUrl + "&tab=companyWeb_contact";
				schema = setUserInfo(infoUrl, map);

				String uid = "";
				if (StringUtils.isNotEmpty(schema.getPhone())) {
					uid = schema.getPhone();
				} else if (StringUtils.isNotEmpty(schema.getNumber())) {
					uid = schema.getNumber();
				}
				schema.setUid(uid);// 商家ID
			}
		}

		return schema;
	}

}
