package com.work.commodity.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StartContent;
import com.framework.util.StringUtils;
import com.work.admin.dao.AdminDAO;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.schema.ShopSchema;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.AliGetUrlUtil;
import com.work.util.JsoupUtil;

/**
 * 商家商品信息爬取
 * 
 * @author tangbiao
 * 
 */
public class CommodityUtil {

	static JLogger logger = LoggerFactory.getLogger(CommodityUtil.class);

	/**
	 * 获取商家所有商品信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            店铺地址
	 */
	public static String commodityInfo(String shopId, String url) {
		LogUtil
				.writeCommodityLog("获取商家所有商品信息CommodityUtil.commodityInfo()...........start "
						+ shopId + " " + url);

		String state = commodityShop(shopId, url);// 保存店铺信息
		LogUtil
				.writeCommodityLog("获取商家所有商品信息CommodityUtil.commodityInfo()...........end "
						+ shopId + " " + url + " " + state);
		return state;
	}

	/**
	 * 获取店鋪用戶信息
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> memberinfo(Document doc, String url)
			throws Exception {
		LogUtil
				.writeCommodityLog("获取店鋪用戶信息CommodityUtil.memberinfo()...........start "
						+ url);

		Map<String, String> map = new HashMap<String, String>();
		Elements infoEle = doc.select("[id=memberinfo]");
		Elements keyEle = infoEle.select("dd");
		if (keyEle != null && keyEle.size() > 0) {
			for (int i = 0; i < keyEle.size(); i++) {
				if (i == 0) {
					String str = keyEle.attr("title");
					map.put("contactname", str);
				} else {
					String str = keyEle.get(i).text().replaceAll("&nbsp;", " ");
					if (StringUtils.isNotEmpty(str) && str.indexOf("联系电话") > -1) {
						str = str.substring(5);
						map.put("contactmobile", str);
					} else if (StringUtils.isNotEmpty(str)
							&& str.indexOf("固定电话") > -1) {
						str = str.substring(5);
						map.put("contactphone", str);
					}
				}
			}
		}
		Elements aEle = infoEle.select("a");
		if (aEle != null && aEle.size() > 0) {
			String data = aEle.get(aEle.size() - 1).attr("data-alitalk");
			JSONObject json = new JSONObject(data);
			String id = json.getString("id");
			map.put("wwnumber", id);
		}
		LogUtil
				.writeCommodityLog("获取店鋪用戶信息CommodityUtil.memberinfo()...........end "
						+ map.toString());
		return map;
	}

	/**
	 * 获取店铺json信息
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static JSONObject commodityJson(Document doc, String url)
			throws Exception {
		LogUtil
				.writeCommodityLog("获取店铺json信息CommodityUtil.commodityJson()...........start "
						+ url);

		JSONObject json = null;
		Elements infoEle = doc.select("[class=company-info fd-clr]");
		if (infoEle.size() > 0) {
			Elements liEle = infoEle.select("li");
			Elements aEle = liEle.get(liEle.size() - 1).select("a");
			String jsonString = aEle.attr("map-mod-config");
			if (StringUtils.isNotEmpty(jsonString)) {
				json = new JSONObject(jsonString);
			}
		}
		LogUtil
				.writeCommodityLog("获取店铺json信息CommodityUtil.commodityJson()...........end "
						+ json);
		return json;
	}

	public static void main(String[] args) throws JSONException {
		// "72.162.36.14:8080", "210.101.131.231:8080", "64.213.148.50:8080",
		// "185.28.193.95:8080", "199.27.135.76:80", "173.245.60.102:80",
		// "31.170.179.35:8080", "194.154.128.65:8080", "182.93.224.38:8080",
		// "173.245.60.97:80", "46.245.59.111:8080", "210.57.208.14:80"]}}
		// List<String> listIP = new ArrayList<String>();// ip集
		// listIP.add("219.85.160.55:8088");
		// AliGetUrlUtil.listIP = listIP;
		// AliGetUrlUtil.host = "219.85.160.55";
		// AliGetUrlUtil.port = 8088;
		// System.out.println("host:" + AliGetUrlUtil.host);
		// System.out.println("port:" + AliGetUrlUtil.port);
		// System.out
		// .println(aliLoginUserName(
		// aliLoginDoc("http://charsu.1688.com"),
		// "http://charsu.1688.com"));
		// System.out.println("host:" + AliGetUrlUtil.host);
		// System.out.println("port:" + AliGetUrlUtil.port);

		String doc = JsoupUtil
				.conUrl(
						"http://revx.daili666.com/ip/?tid=558460242745998&num=10&delay=1&foreign=all&filter=on",
						false, "UTF8", "", 0);
		List<String> listIP = new ArrayList<String>();// ip集
		String[] str = doc.split("\n");
		for (String s : str) {
			System.out.println("s:" + s.trim());
			listIP.add(s.trim());
		}
		AliGetUrlUtil.listIP = listIP;
		int all = listIP.size();
		int i = 0;
		int j = 0;
		for (String x : listIP) {
			AliGetUrlUtil.host = x.substring(0, x.indexOf(":"));
			String port = x.substring(x.indexOf(":") + 1, x.length());
			System.out.println("ip:" + x);
			AliGetUrlUtil.port = Integer.parseInt(port);
			// System.out.println("host:" + AliGetUrlUtil.host);
			// System.out.println("port:" + AliGetUrlUtil.port);
			String loginUserName = aliLoginUserName(
					aliLoginDoc("http://charsu.1688.com"),
					"http://charsu.1688.com");
			if (StringUtils.isNotEmpty(loginUserName)) {
				i++;
			} else {
				j++;
			}
			System.out.println("loginUserName:" + loginUserName);
		}
		System.out.println("ip数量：" + all + " 成功数：" + i + " 失败数：" + j);
	}

	/**
	 * 获取阿里用户登录名
	 * 
	 * @param url
	 *            阿里域名地址
	 * @return
	 * @throws JSONException
	 */
	public static String aliLoginUserName(Document doc, String url)
			throws JSONException {
		LogUtil
				.writeCommodityLog("获取阿里用户登录名CommodityUtil.aliLoginUserName()...........start "
						+ url);
		String loginUserName = "";// 公司名
		// Elements aListEle = doc.select("a");
		// String data = "";
		// for (int i = 0; i < aListEle.size(); i++) {
		// data = aListEle.get(i).attr("data-alitalk");
		// if (StringUtils.isNotEmpty(data))
		// break;
		// }
		// if (StringUtils.isNotEmpty(data)) {
		// JSONObject json = new JSONObject(data);
		// loginUserName = json.getString("id");
		// }

		Elements aListEle = doc.select("[class=config]");
		String data = "";
		for (int i = 0; i < aListEle.size(); i++) {
			data = aListEle.get(i).attr("data-config");
			if (StringUtils.isNotEmpty(data)) {
				data = JsoupUtil.getReplaceBy(data);
				break;
			}
		}
		LogUtil
				.writeCommodityLog("获取阿里用户登录名CommodityUtil.aliLoginUserName()...........end "
						+ data);
		if (StringUtils.isNotEmpty(data)) {
			JSONObject json = new JSONObject(data);
			loginUserName = json.getString("memberLoginId");
		}
		LogUtil
				.writeCommodityLog("获取阿里用户登录名CommodityUtil.aliLoginUserName()...........end "
						+ url + ";爬取名称为：" + loginUserName);
		return loginUserName;
	}

	/**
	 * 获取阿里用户登录名的Doc
	 * 
	 * @param url
	 *            阿里域名地址
	 * @return
	 * @throws JSONException
	 */
	public static Document aliLoginDoc(String url) throws JSONException {
		LogUtil
				.writeCommodityLog("获取阿里用户登录名的DocCommodityUtil.aliLoginDoc()...........start "
						+ url);
		Document doc = AliGetUrlUtil.getDocumentUrl(url);
		// String loginUserName = aliLoginUserName(doc, url);
		// if (StringUtils.isEmpty(loginUserName)) {
		// doc = AliGetUrlUtil.getDocumentUrl(url + "/page/creditdetail.htm");
		// }
		LogUtil
				.writeCommodityLog("获取阿里用户登录名的DocCommodityUtil.aliLoginDoc()...........end "
						+ url);
		return doc;
	}

	/**
	 * 获取公司名
	 * 
	 * @param doc
	 * @return
	 */
	public static String commodityName(Document doc, String url) {
		LogUtil
				.writeCommodityLog("获取公司名CommodityUtil.commodityName()...........start "
						+ url);
		String companyName = "";// 公司名
		// Elements offerlistEle = doc.select("[data-page-name=offerlist]");//
		// 获取所有商品连接
		// if (StringUtils.isNotEmpty(offerlistEle.text())) {//
		// 有所有商品连接，证明店铺地址没问题，可以查询店铺名称
		Elements companyEle = doc.select("[class=company-name]");
		if (companyEle != null && companyEle.size() > 0) {
			companyName = companyEle.get(0).text();// 公司名
		}
		if (StringUtils.isEmpty(companyName)) {
			Elements titleEle = doc.select("title");
			companyName = titleEle.text();// 公司名
		}
		if ("404-阿里巴巴".equals(companyName)) {
			companyName = "";
		}
		// }
		LogUtil
				.writeCommodityLog("获取公司名CommodityUtil.commodityName()...........end "
						+ companyName);
		return companyName;
	}

	/**
	 * 获取阿里首页地址
	 * 
	 * @param doc
	 * @return
	 */
	public static String aliIndexUrl(Document doc, String url) {
		LogUtil
				.writeCommodityLog("获取阿里首页地址CommodityUtil.aliIndexUrl()...........start "
						+ url);
		String href = "";// 阿里首页地址
		try {
			Elements nameEle = doc.select("[data-page-name=index]");
			if (nameEle != null && nameEle.size() > 0) {
				Elements aEle = nameEle.select("a");
				href = aEle.attr("href");
			}
		} catch (Exception e) {
			logger.error("获取阿里首页地址失败：", e);
		}
		LogUtil
				.writeCommodityLog("获取阿里首页地址CommodityUtil.aliIndexUrl()...........end "
						+ href);
		return href;
	}

	/**
	 * 阿里诚信通校验
	 * 
	 * @param doc
	 * @return
	 */
	public static boolean aliCXT(Document doc, String url) {
		LogUtil
				.writeCommodityLog("阿里诚信通校验CommodityUtil.aliCXTImg()...........start "
						+ url);
		boolean cxt = false;// 阿里诚信通校验
		try {
			Elements cxtEle = doc.select("[data-tracelog=wp_head_wp_cxt]");
			if (cxtEle != null && cxtEle.size() > 0) {
				cxt = true;// 阿里诚信通校验
			}
			Elements cxtEle1 = doc.select("[class=year-number]");
			if (cxtEle1 != null && cxtEle1.size() > 0) {
				cxt = true;// 阿里诚信通校验
			}
		} catch (Exception e) {
			logger.error("阿里诚信通校验失败：", e);
		}
		LogUtil
				.writeCommodityLog("阿里诚信通校验CommodityUtil.aliCXTImg()...........end "
						+ cxt);
		return cxt;
	}

	/**
	 * 获取店铺名
	 * 
	 * @param doc
	 * @return
	 */
	public static String shopName(String companyName) {
		LogUtil
				.writeCommodityLog("获取店铺名CommodityUtil.shopName()...........start "
						+ companyName);

		String shopName = companyName;// 店铺名
		if (shopName.indexOf("市") > 0) {
			shopName = shopName.substring(shopName.indexOf("市") + 1);
		}
		if (shopName.indexOf("区") > 0) {
			shopName = shopName.substring(shopName.indexOf("区") + 1);
		}
		if (shopName.indexOf("县") > 0) {
			shopName = shopName.substring(shopName.indexOf("县") + 1);
		}
		LogUtil
				.writeCommodityLog("获取店铺名CommodityUtil.shopName()...........end "
						+ shopName);
		return shopName;
	}

	/**
	 * 获取第三方店铺ID
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String platformId(Document doc, String url) throws Exception {
		LogUtil
				.writeCommodityLog("获取第三方店铺ID CommodityUtil.platformId()...........start "
						+ url);

		String platformId = "";
		Elements companyEle = doc.select("[id=content]");
		String jsonString = companyEle.attr("data-user-context");
		if (StringUtils.isNotEmpty(jsonString)) {
			JSONObject json = new JSONObject(jsonString);
			platformId = json.getString("uid");// 公司名
		}
		LogUtil
				.writeCommodityLog("获取第三方店铺ID CommodityUtil.platformId()...........end "
						+ platformId);
		return platformId;
	}

	/**
	 * 保存店铺信息
	 * 
	 * @param url
	 *            店铺首页地址
	 */
	public static String commodityShop(String shopId, String url) {
		LogUtil
				.writeCommodityLog("保存店铺信息CommodityUtil.commodityShop()...........start "
						+ shopId + " " + url);
		String state = "10000";
		try {
			Document doc = JsoupUtil
					.getDocument(url + "/page/creditdetail.htm");// 公司档案地址
			Map<String, String> map = memberinfo(doc, url
					+ "/page/creditdetail.htm");
			JSONObject json = commodityJson(doc, url + "/page/creditdetail.htm");// 获取店铺json信息
			String companyName = "";// 公司名
			String address = "";// 公司地址
			if (json != null) {
				companyName = json.getString("companyName");// 公司名
				address = json.getString("address").substring(7);// 公司地址
			}
			if (StringUtils.isEmpty(companyName)) {
				companyName = commodityName(doc, url);// 公司名
			}
			AdminDAO adminDao = new AdminDAO();
			String sliUrl = adminDao.getShopAliUrl(shopId);// 阿里地址
			String shopName = "";
			if (StringUtils.isEmpty(sliUrl)) {
				shopName = shopName(companyName);// 店铺名
				LogUtil.writeCommodityLog(shopId + " 公司名称：" + companyName
						+ " 店铺名称：" + shopName);
				if (StringUtils.isEmpty(shopName)) {
					state = "2";
				}
			}
			CommodityBO bo = new CommodityBO();
			ShopSchema info = bo.getShopByShopId(shopId);
			if (info != null) {
				state = "0";
				if (info.getN_shop_type_sublevel().equals("1")) {
					info.setN_shop_id(shopId); // 卖家ID:mem_seller表中自己生成的ID
					if (StringUtils.isEmpty(sliUrl)) {
						info.setC_shop_name(shopName);// 店铺名称
					}
					info.setC_company_name(companyName);// 公司名
					info.setN_shop_type("1");// 店铺类型，1阿里，2淘宝，3分销店铺，4微购微铺，5实体店铺
					info.setC_location(address);// 店铺地址
					info.setC_contact_name(map.get("contactname"));// 联系人
					info.setC_contact_mobile(map.get("contactmobile"));// 联系电话
					info.setC_contact_phone(map.get("contactphone"));// 固定电话

					boolean redult = bo.updateShopById(info);// 修改商家信息
					LogUtil.writeCommodityLog("增加商家信息：" + redult);
					if (StringUtils.isEmpty(sliUrl)) {
						try {
							boolean flag = bo.addShopAli(shopId, url);// 增加店铺基本信息
							LogUtil.writeCommodityLog(shopId + "增加店铺基本信息结果:"
									+ flag);
						} catch (Exception e) {
						}
					}
				}
			} else {
				LogUtil.writeCommodityLog("店铺 " + shopId + " 不存在");
				state = "1";
			}
		} catch (Exception e) {
			logger.error("保存店铺信息失败：" + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "保存店铺信息失败：" + url,
					LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("保存店铺信息CommodityUtil.commodityShop()...........end "
						+ state);
		return state;
	}

	/**
	 * 获取阿里店铺基本信息
	 * 
	 * @param shopId
	 *            店铺id
	 * @param url
	 *            店铺地址
	 */
	public static ShopAliSchema memSellerBasicInfo(String shopId, String url) {
		LogUtil
				.writeCommodityLog("获取阿里店铺基本信息CommodityUtil.memSellerBasicInfo()...........start "
						+ shopId + " " + url);
		ShopAliSchema schema = new ShopAliSchema();
		try {
			String platformId = platformId(JsoupUtil.getDocument(url), url);
			if (StringUtils.isEmpty(platformId)) {
				return null;
			}
			// http://www.1688.com/company/lecle.html?fromSite=company_site&tracelog=gsda_huangye
			String xml = JsoupUtil.conUrl("http://www.1688.com/company/"
					+ platformId
					+ ".html?fromSite=company_site&tracelog=gsda_huangye",
					false, "GBK", "", 0);
			Document doc = Jsoup.parse(xml);
			Elements companyEle = doc.select("[class=company-detail-show]");
			if (companyEle.size() > 0) {
				Elements pEle = companyEle.select("p");
				int k = 0;
				if (pEle.size() > 0) {
					for (int i = 0; i < pEle.size(); i++) {
						k = i;
						String name = pEle.get(i).text();
						// System.out.println(name);
						if (name.indexOf("主营产品或服务:") > -1) {
							String c_company_product = pEle.get(k + 1).text();
							schema.setC_company_product(c_company_product);
						} else if (name.indexOf("主营行业:") > -1) {
							String c_company_industry = pEle.get(k + 1).text();
							schema.setC_company_industry(c_company_industry);
						} else if (name.indexOf("经营模式:") > -1) {
							String c_company_model = pEle.get(k + 1).text();
							schema.setC_company_model(c_company_model);
						} else if (name.indexOf("是否提供加工/定制服务:") > -1) {
							String c_company_work = pEle.get(k + 1).text();
							schema.setC_company_work(c_company_work);
						} else if (name.indexOf("注册资本:") > -1) {
							String c_company_capital = pEle.get(k + 1).text();
							schema.setC_company_capital(c_company_capital);
						} else if (name.indexOf("公司成立时间:") > -1) {
							String c_company_founding_time = pEle.get(k + 1)
									.text();
							schema
									.setC_company_founding_time(c_company_founding_time);
						} else if (name.indexOf("公司注册地:") > -1) {
							String c_company_address = pEle.get(k + 1).text();
							schema.setC_company_address(c_company_address);
						} else if (name.indexOf("企业类型:") > -1) {
							String c_company_type = pEle.get(k + 1).text();
							schema.setC_company_type(c_company_type);
						} else if (name.indexOf("法定代表人:") > -1) {
							String c_company_person = pEle.get(k + 1).text();
							schema.setC_company_person(c_company_person);
						} else if (name.indexOf("工商注册号:") > -1) {
							String c_company_no = pEle.get(k + 1).text();
							schema.setC_company_no(c_company_no);
						} else if (name.indexOf("加工方式:") > -1) {
							String c_company_way = pEle.get(k + 1).text();
							schema.setC_company_way(c_company_way);
						} else if (name.indexOf("工艺:") > -1) {
							String c_company_technology = pEle.get(k + 1)
									.text();
							schema
									.setC_company_technology(c_company_technology);
						} else if (name.indexOf("员工人数:") > -1) {
							String c_company_number = pEle.get(k + 1).text();
							schema.setC_company_number(c_company_number);
						} else if (name.indexOf("厂房面积:") > -1) {
							String c_company_area = pEle.get(k + 1).text();
							schema.setC_company_area(c_company_area);
						} else if (name.indexOf("主要销售区域:") > -1) {
							String c_company_region = pEle.get(k + 1).text();
							schema.setC_company_region(c_company_region);
						} else if (name.indexOf("主要客户群体:") > -1) {
							String c_company_custom = pEle.get(k + 1).text();
							schema.setC_company_custom(c_company_custom);
						} else if (name.indexOf("月产量:") > -1) {
							String c_company_output = pEle.get(k + 1).text();
							schema.setC_company_output(c_company_output);
						} else if (name.indexOf("年营业额:") > -1) {
							String c_company_turnover = pEle.get(k + 1).text();
							schema.setC_company_turnover(c_company_turnover);
						} else if (name.indexOf("年出口额:") > -1) {
							String c_company_exports = pEle.get(k + 1).text();
							schema.setC_company_exports(c_company_exports);
						} else if (name.indexOf("品牌名称:") > -1) {
							String c_company_brand = pEle.get(k + 1).text();
							schema.setC_company_brand(c_company_brand);
						} else if (name.indexOf("公司主页:") > -1) {
							String c_company_url = pEle.get(k + 1).text();
							schema.setC_company_url(c_company_url);
						}
					}
				}
				Elements tpiconEle = doc.select("[class=tp-icon]");
				String srcEle = tpiconEle.attr("src");
				schema.setC_qualifications1(srcEle);
				Elements tpinfoEle = doc.select("[class=tp-info]");
				String infoEle = tpinfoEle.text();
				schema.setC_qualifications2(infoEle);
				Elements authiconEle = doc.select("[class=auth-icon]");
				String authsrcEle = authiconEle.attr("src");
				schema.setC_qualifications3(authsrcEle);

				Elements contentEle = doc.select("[class=full-content]");
				Elements spanEle = contentEle.select("span");
				String content = spanEle.html();
				if (StringUtils.isEmpty(content)) {
					contentEle = doc.select("[class=company-content]");
					content = contentEle.html();
				}
				if (StringUtils.isNotEmpty(content)) {
					if (content.length() > 1024) {
						content = content.substring(0, 1024);
					}
					schema.setC_company_desc(content);
				}
			}
			schema.setN_shop_id(shopId);
			schema.setC_platform_id(platformId);// 阿里店铺ID
			String number = getTradeNumber(url);
			if (StringUtils.isEmpty(number)) {
				number = "0";
			}
			schema.setC_trade_number(number);// 交易记录

			Document doc1 = JsoupUtil.getDocument(url
					+ "/page/creditdetail.htm");// 公司档案地址
			Map<String, String> map = memberinfo(doc1, url
					+ "/page/creditdetail.htm");
			schema.setC_ww_number(map.get("wwnumber"));// 阿里旺旺号
		} catch (Exception e) {
			logger.error("获取阿里店铺基本信息：" + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取阿里店铺基本信息：" + shopId
					+ " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取阿里店铺基本信息CommodityUtil.memSellerBasicInfo()...........end ");
		return schema;
	}

	/**
	 * 获取店铺代理加盟介绍并创建html文件
	 * 
	 * @param url
	 *            店铺域名地址
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public static String htmlMerchants(String shopId, String url)
			throws Exception {
		LogUtil
				.writeCommodityLog("获取店铺代理加盟介绍并创建html文件CommodityUtil.htmlMerchants()...........start "
						+ shopId + " " + url);

		String pathUrl = "";
		Document doc = JsoupUtil.getDocument(url + "/page/merchants.htm");
		Elements entityEle = doc.select("[class=entity mod-spm]");
		String entity = entityEle.text();// 实体加盟DIV
		if (StringUtils.isNotEmpty(entity)) {// 先判断div中是不是有内容
			entity = entityEle.toString();// 实体加盟DIV
			entity = "<div><div><h3>实体加盟</h3></div><div>" + entity
					+ "</div></div>";
		}
		Elements netEle = doc.select("[class=net mod-spm]");
		String net = netEle.text();// 网络加盟DIV
		if (StringUtils.isNotEmpty(net)) {// 先判断div中是不是有内容
			net = netEle.toString();// 网络加盟DIV
			net = "<div><div><h3>网络加盟</h3></div><div>" + net + "</div></div>";
		}
		String div = entity + net;
		if (StringUtils.isNotEmpty(entity) || StringUtils.isNotEmpty(net)) {
			String html = bodyStartByMerchants() + div + bodyEnd();
			String htmlUrl = "/html/" + shopId + "/merchants.html";
			boolean flag = file(html, shopId, htmlUrl);// 创建html文件
			if (flag) {
				pathUrl = htmlUrl;
			}
		}
		LogUtil
				.writeCommodityLog("获取店铺代理加盟介绍并创建html文件CommodityUtil.htmlMerchants()...........end "
						+ pathUrl);
		return pathUrl;
	}

	/**
	 * html头部(代理加盟)
	 * 
	 * @return
	 */
	public static String bodyStartByMerchants() {
		LogUtil
				.writeCommodityLog("html头部(代理加盟)CommodityUtil.bodyStartByMerchants()...........start ");

		StringBuffer sb = new StringBuffer();
		sb.append("<!doctype html>\n");
		sb.append("<html lang=\"zh-CN\">\n");
		sb.append("<head>\n");
		sb.append("<meta charset=\"utf-8\">\n");
		sb
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1,user-scalable=no\">\n");
		sb.append("<title>代理加盟</title>\n");
		sb
				.append("<link href=\"http://cd-css.qiniudn.com/pure.min.css\" rel=\"stylesheet\">\n");
		sb
				.append("<style>table,tr{max-width:100% !important;overflow:scroll !important; }img{max-width:100%}.net-apply{display:none}td{max-width: 192px;overflow: scroll;text-overflow: ellipsis;}.pure-table td, .pure-table th{padding-right: 0.275em;	padding-bottom: 0.275em;padding-left: 0.275em;	padding-top: 0.275em;}</style>\n");
		sb.append("</head>\n");
		sb.append("<body cz-shortcut-listen=\"true\">\n");
		sb.append("<div class=\"content\">\n");
		LogUtil
				.writeCommodityLog("html头部(代理加盟)CommodityUtil.bodyStartByMerchants()...........end ");
		return sb.toString();
	}

	/**
	 * html尾部
	 * 
	 * @return
	 */
	public static String bodyEnd() {
		LogUtil
				.writeCommodityLog("html尾部CommodityUtil.bodyEnd()...........start ");

		StringBuffer sb = new StringBuffer();
		sb.append("\n<div class=\"footer\" align=\"center\">\n");
		sb.append("<div class=\"copyright\">\n");
		sb.append("©<a href=\"http://www.truedian.com\">深圳微购科技</a> 提供技术支持\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb
				.append("<script src=\"http://cd-lib.qiniudn.com/jquery.min.js\"></script>\n");
		sb
				.append("<script>$(\"table\").addClass(\"pure-table\");$(\"table\").removeAttr(\"style\");$(\"td\").removeAttr(\"width\");</script>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		LogUtil
				.writeCommodityLog("html尾部CommodityUtil.bodyEnd()...........end ");
		return sb.toString();
	}

	/**
	 * 内容写入到html文件
	 * 
	 * @param html
	 *            存储内容
	 * @param shopId
	 *            店铺ID
	 * @param htmlUrl
	 *            内容存放地址
	 * @throws Exception
	 */
	public static boolean file(String html, String shopId, String htmlUrl)
			throws Exception {
		LogUtil
				.writeCommodityLog("内容写入到html文件CommodityUtil.file()...........start "
						+ shopId + " " + htmlUrl);

		boolean flag = false;
		OutputStreamWriter write = null;
		BufferedWriter writer = null;
		try {
			String filePath = JsoupUtil.SystemPath()
					+ htmlUrl.substring(0, htmlUrl.lastIndexOf("/") + 1);
			File uploadFilePath = new File(filePath);
			if (uploadFilePath.exists() == false) {// 文件夹不存在则创建
				uploadFilePath.mkdirs();
			}
			filePath = JsoupUtil.SystemPath() + htmlUrl;
			File f = new File(filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
			write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			writer = new BufferedWriter(write);
			writer.write(html);
			flag = true;
		} catch (SecurityException e) {
			logger.error("内容写入到html文件失败" + shopId + " " + htmlUrl, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "内容写入到html文件失败" + shopId
					+ " " + htmlUrl, LogUtil.getExceptionError(e));
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (write != null) {
				write.close();
			}
		}
		LogUtil
				.writeCommodityLog("内容写入到html文件CommodityUtil.file()...........start "
						+ flag);
		return flag;
	}

	/**
	 * 生成商家在触店的首页html
	 * 
	 * @param info
	 * @param basicInfo
	 * @return
	 */
	public static String getCommodityIndexHtml(ShopSchema info,
			ShopAliSchema basicInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("<!doctype html>\n");
		sb.append("<html lang=\"zh-CN\">\n");
		sb.append("<head>\n");
		sb.append("<meta charset=\"UTF-8\">\n");
		sb
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1,user-scalable=no\">\n");
		sb.append("<title>" + info.getC_shop_name() + "</title>\n");
		sb
				.append("<link href=\"http://cd-css.qiniudn.com/pure.min.css\" rel=\"stylesheet\">\n");
		sb.append("<style>\n");
		sb
				.append("body{margin:0;font-family:Arial,Helvetica,sans-serif;font-size:13px;line-height:1.5;}\n");
		sb
				.append(".swiper-container{margin:0 auto;position:relative;overflow:hidden;-webkit-backface-visibility:hidden;-moz-backface-visibility:hidden;-ms-backface-visibility:hidden;-o-backface-visibility:hidden;backface-visibility:hidden;z-index:1;}\n");
		sb
				.append(".swiper-wrapper{position:relative;width:100%;-webkit-transition-property:-webkit-transform,left,top;-webkit-transition-duration:0s;-webkit-transform:translate3d(0px,0,0);-webkit-transition-timing-function:ease;-moz-transition-property:-moz-transform,left,top;-moz-transition-duration:0s;-moz-transform:translate3d(0px,0,0);-moz-transition-timing-function:ease;-o-transition-property:-o-transform,left,top;-o-transition-duration:0s;-o-transform:translate3d(0px,0,0);-o-transition-timing-function:ease;-o-transform:translate(0px,0px);-ms-transition-property:-ms-transform,left,top;-ms-transition-duration:0s;-ms-transform:translate3d(0px,0,0);-ms-transition-timing-function:ease;transition-property:transform,left,top;transition-duration:0s;transform:translate3d(0px,0,0);transition-timing-function:ease;}\n");
		sb.append(".swiper-slide{float:left;}\n");
		sb
				.append(".swiper-container{width:100%;height:100%;color:#fff;text-align:center;}\n");
		sb.append(".turquoise-slide{background:#1ABC9C;}\n");
		sb.append(".emerland-slide{background:#40D47E;}\n");
		sb.append(".peter-river-slide{background:#4AA3DF;}\n");
		sb.append(".amethyst-slide{background:#A66BBE;}\n");
		sb.append(".sun-flower-slide{background:#F1C40F;}\n");
		sb.append(".carrot-slide{background:#E67E22;}\n");
		sb.append(".alizarin-slide{background:#E74C3C;}\n");
		sb.append(".concrete-slide{background:#A3B1B2;}\n");
		sb
				.append(".swiper-slide .title{font-style:italic;font-size:30px;margin-top:20px;margin-bottom:0;line-height:45px;}\n");
		sb.append(".swiper-slide span{font-style:italic;font-size:20px;}\n");
		sb
				.append(".pagination{position:absolute;z-index:20;left:10px;top:10px;}\n");
		sb
				.append(".swiper-pagination-switch{display:block;width:8px;height:8px;border-radius:8px;background:#555;margin:0 0px 5px;opacity:0.8;border:1px solid #fff;cursor:pointer;}\n");
		sb.append(".swiper-active-switch{background:#fff;}\n");
		sb.append("</style>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<div class=\"swiper-container\">\n");
		sb.append("<div class=\"swiper-wrapper\">\n");
		if (StringUtils.isNotEmpty(info.getC_shop_name())) {
			sb.append("<div class=\"swiper-slide amethyst-slide\">\n");
			sb.append("<div class=\"title\">店铺名称</div>\n");
			sb.append("<span>" + info.getC_shop_name() + "</span>\n");

			sb.append("<span>\n");
			if (StringUtils.isNotEmpty(basicInfo.getC_qualifications1())) {
				sb.append("<img src=\"" + basicInfo.getC_qualifications1()
						+ "\"/>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_qualifications3())) {
				sb.append("<img src=\"" + basicInfo.getC_qualifications3()
						+ "\"/>\n");
			}
			sb.append("</span>\n");
			sb.append("</div>\n");
		}
		if (StringUtils.isNotEmpty(basicInfo.getC_company_desc())) {
			sb.append("<div class=\"swiper-slide sun-flower-slide\">\n");
			sb.append("<div style=\"font-size:20px\">公司介绍</div>\n");
			sb.append("<span style=\"font-size:10px\">"
					+ basicInfo.getC_company_desc() + "</span>\n");
			sb.append("</div>\n");
		}
		if (StringUtils.isNotEmpty(basicInfo.getC_company_product())
				|| StringUtils.isNotEmpty(basicInfo.getC_company_model())) {
			sb.append("<div class=\"swiper-slide turquoise-slide\">\n");
			if (StringUtils.isNotEmpty(basicInfo.getC_company_product())) {
				sb.append("<div class=\"title\">主营产品</div>\n");
				sb.append("<span>" + basicInfo.getC_company_product()
						+ "</span>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_company_model())) {
				sb.append("<div class=\"title\">经营模式</div>\n");
				sb.append("<span>" + basicInfo.getC_company_model()
						+ "</span>\n");
			}
			sb.append("</div>\n");
		}
		if (StringUtils.isNotEmpty(info.getC_contact_name())
				|| StringUtils.isNotEmpty(basicInfo.getC_ww_number())
				|| StringUtils.isNotEmpty(info.getC_contact_phone())
				|| StringUtils.isNotEmpty(info.getC_contact_mobile())) {
			sb.append("<div class=\"swiper-slide emerland-slide\">\n");
			if (StringUtils.isNotEmpty(info.getC_contact_name())) {
				sb.append("<div class=\"title\">联系人</div>\n");
				sb.append("<span>" + info.getC_contact_name() + "</span>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_ww_number())) {
				sb.append("<div class=\"title\">阿里旺旺号</div>\n");
				sb.append("<span>" + basicInfo.getC_ww_number() + "</span>\n");
			}
			if (StringUtils.isNotEmpty(info.getC_contact_phone())) {
				sb.append("<div class=\"title\">固定电话</div>\n");
				sb.append("<span>" + info.getC_contact_phone() + "</span>\n");
			}
			if (StringUtils.isNotEmpty(info.getC_contact_mobile())) {
				sb.append("<div class=\"title\">移动电话</div>\n");
				sb.append("<span>" + info.getC_contact_mobile() + "</span>\n");
			}
			sb.append("</div>\n");
		}
		if (StringUtils.isNotEmpty(basicInfo.getC_company_founding_time())
				|| StringUtils.isNotEmpty(info.getC_location())
				|| StringUtils.isNotEmpty(basicInfo.getC_company_url())) {
			sb.append("<div class=\"swiper-slide peter-river-slide\">\n");
			if (StringUtils.isNotEmpty(basicInfo.getC_company_founding_time())) {
				sb.append("<div class=\"title\">成立时间</div>\n");
				sb.append("<span>" + basicInfo.getC_company_founding_time()
						+ "</span>\n");
			}
			if (StringUtils.isNotEmpty(info.getC_location())) {
				sb.append("<div class=\"title\">公司地址</div>\n");
				sb.append("<span>" + info.getC_location() + "</span>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_company_url())) {
				sb.append("<div class=\"title\">公司主页</div>\n");
				sb
						.append("<span>" + basicInfo.getC_company_url()
								+ "</span>\n");
			}
			sb.append("</div>\n");
		}
		if (StringUtils.isNotEmpty(basicInfo.getC_company_type())
				|| StringUtils.isNotEmpty(basicInfo.getC_company_person())
				|| StringUtils.isNotEmpty(basicInfo.getC_company_no())) {
			sb.append("<div class=\"swiper-slide sun-flower-slide\">\n");
			if (StringUtils.isNotEmpty(basicInfo.getC_company_type())) {
				sb.append("<div class=\"title\">企业类型</div>\n");
				sb.append("<span>" + basicInfo.getC_company_type()
						+ "</span>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_company_person())) {
				sb.append("<div class=\"title\">法定代表人</div>\n");
				sb.append("<span>" + basicInfo.getC_company_person()
						+ "</span>\n");
			}
			if (StringUtils.isNotEmpty(basicInfo.getC_company_no())) {
				sb.append("<div class=\"title\">工商注册号</div>\n");
				sb.append("<span>" + basicInfo.getC_company_no() + "</span>\n");
			}
			sb.append("</div>\n");
		}
		sb.append("<div class=\"swiper-slide carrot-slide\">\n");
		if (StringUtils.isNotEmpty(basicInfo.getC_shop_domain())) {
			sb.append("<div class=\"title\">触店地址</div>\n");
			String shopUrl = "http://"
					+ StartContent.getInstance().getDomainUrl()
					+ "/static/shop/shop_index.html?is_share=1&shop_id="
					+ basicInfo.getN_shop_id();// 二维码扫描地址
			sb.append("<span>" + shopUrl + "</span>\n");
		}
		sb.append("<div class=\"title\">技术支持</div>\n");
		sb.append("<span>微购科技</span>\n");
		sb.append("<div class=\"title\">客户电话</div>\n");
		sb.append("<span>0755-61980059</span>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("<div class=\"pagination\"></div>\n");
		sb.append("</div>\n");
		sb
				.append("<script src=\"http://cd-lib.qiniudn.com/jquery.min.js\"></script>\n");
		sb
				.append("<script src=\"http://cd-lib.qiniudn.com/swiper.min.js\"></script>\n");
		sb.append("<script>\n");
		sb.append("var mySwiper = new Swiper('.swiper-container',{\n");
		sb.append("pagination: '.pagination',\n");
		sb.append("paginationClickable: true,\n");
		sb.append("mode: 'vertical'\n");
		sb.append("})\n");
		sb.append("</script>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}

	// 获取交易数
	public static String getTradeNumber(String url) {
		String userurl = url + "/page/creditdetail_remark.htm";
		Document doc = JsoupUtil.getDocument(userurl);
		if (doc != null) {// 获取交易数
			String num = "0";
			Elements bodyEle = doc.select("[class=body-col2]");
			if (bodyEle.size() > 0) {
				Elements emEle = bodyEle.get(0).select("em");
				num = emEle.text();
			}
			return num;
		}
		return "0";
	}
}
