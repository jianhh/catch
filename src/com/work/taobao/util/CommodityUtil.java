package com.work.taobao.util;

import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.util.JsoupUtil;
import com.work.util.TaobaoGetUrlUtil;

/**
 * 商家商品信息爬取
 * 
 * @author tangbiao
 * 
 */
public class CommodityUtil {
	private static String TAG = CommodityUtil.class.getSimpleName();
	static JLogger logger = LoggerFactory.getLogger(CommodityUtil.class);

	/**
	 * 获取淘宝用户登录名
	 * 
	 * @param url
	 *            淘宝域名地址
	 * @return
	 * @throws JSONException
	 */
	public static String tbLoginUserName(Document doc, String url) throws JSONException {
		LogUtil.writeCommodityLog(TAG + "获取淘宝用户登录名tbLoginUserName()...........start " + url);
		String loginUserName = "";// 公司名
		Elements contactEle = doc.select("[class=service-content wws]");
		System.out.println(contactEle);
		String data = "";
		for (int i = 0; i < contactEle.size(); i++) {
			data = contactEle.get(i).attr("data-nick");
			if (StringUtils.isNotEmpty(data)) {
				data = JsoupUtil.getReplaceBy(data);
				break;
			}
		}

		loginUserName = JsoupUtil.urlDecodeByUtf(data);
		LogUtil.writeCommodityLog(TAG + "获取淘宝用户登录名CommodityUtil.tbLoginUserName()...........end " + loginUserName);
		return loginUserName;
	}
	
	
	public static String getRateUrl(Document doc, String url) throws JSONException {
		LogUtil.writeCommodityLog(TAG + "获取淘宝RateUrl...........start " + url);
		Elements contactEleEle = doc.select("[class=rank-icon-v2]");
		System.out.println(contactEleEle);
		String rateUrl = "";
		Elements aEle = contactEleEle.select("a");
		if (aEle.size() > 0) {
			rateUrl = aEle.attr("href");
		}
		LogUtil.writeCommodityLog(TAG + "获取淘宝RateUrl...........end " + url);
		return rateUrl;

	}
	
	public static boolean openShopSuccess(String rateUrl){
		//TODO:保证金
		Document doc = TaobaoGetUrlUtil.getDocumentUrl(rateUrl);
		Elements contactEleEle = doc.select("[class=charge]");
		return false;
	}

	/**
	 * 获取阿里用户登录名的Doc
	 * 
	 * @param url
	 *            阿里域名地址
	 * @return
	 * @throws JSONException
	 */
	public static Document tbLoginDoc(String url) throws JSONException {
		LogUtil.writeCommodityLog("获取淘宝用户登录名的DocCommodityUtil.tbLoginDoc()...........start " + url);
		Document doc = TaobaoGetUrlUtil.getDocumentUrl(url);
		LogUtil.writeCommodityLog("获取淘宝用户登录名的DocCommodityUtil.tbLoginDoc()...........end " + url);
		return doc;
	}

	/**
	 * 获取公司名
	 * 
	 * @param doc
	 * @return
	 */
	public static String commodityName(Document doc, String url) {
		LogUtil.writeCommodityLog(TAG + "获取公司名CommodityUtil.commodityName()...........start " + url);
		String companyName = "";// 公司名
		// Elements offerlistEle = doc.select("[data-page-name=offerlist]");//
		// 获取所有商品连接
		// if (StringUtils.isNotEmpty(offerlistEle.text())) {//
		// 有所有商品连接，证明店铺地址没问题，可以查询店铺名称
		Elements companyEle = doc.select("[class=company-name]");
		companyName = companyEle.text();// 公司名
		if (StringUtils.isEmpty(companyName)) {
			// Elements titleEle = doc.select("title");
			// companyName = titleEle.text();// 公司名
			companyName = shopName(doc); // 公司名不存在则取店铺名
		}
		// }
		LogUtil.writeCommodityLog(TAG + "获取公司名CommodityUtil.commodityName()...........end " + companyName);
		return companyName;
	}

	/**
	 * 获取店铺名
	 * 
	 * @param doc
	 * @return
	 */
	public static String shopName(Document doc) {
		LogUtil.writeCommodityLog(TAG + "获取店铺名CommodityUtil.shopName()...........start ");
		String shopName = "";// 公司名
		Elements companyEle = doc.select("[class=shop-name]");
		for (int i = 0; i < companyEle.size(); i++) {
			String text = companyEle.get(i).text();
			if (StringUtils.isEmpty(text)) {
				continue;
			}
			if (StringUtils.isNotEmpty(shopName)) {
				if (text.length() < shopName.length()) {
					shopName = text;
				}
			} else {
				shopName = text;
			}

		}

		if (StringUtils.isEmpty(shopName)) {
			Elements titleEle = doc.select("title");
			shopName = titleEle.text();// 公司名
		}
		LogUtil.writeCommodityLog(TAG + "获取公司名CommodityUtil.commodityName()...........end " + shopName);
		return shopName;

	}

}
