package com.work.commodity.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.framework.util.StartContent;
import com.framework.util.StringUtils;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.Http;
import com.qiniu.api.rs.PutPolicy;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.CfgSysCategorySchema;
import com.work.commodity.schema.GoodsDefaultExpressSchema;
import com.work.commodity.schema.GoodsExtendsPropertySchema;
import com.work.commodity.schema.GoodsImageSchema;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsPriceSectionSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.schema.GoodsSkuInfoSchema;
import com.work.commodity.schema.GoodsSkuListSchema;
import com.work.commodity.schema.GoodsSyncRecordSchema;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 商家商品信息爬取
 * 
 * @author tangbiao
 * 
 */
public class GoodsUtil {

	static JLogger logger = LoggerFactory.getLogger(GoodsUtil.class);

	/**
	 * 
	 * 查询店铺商品是否有上新
	 * 
	 * @param shopId
	 *            店铺id
	 * @param url
	 *            店铺地址
	 * @return 0：有上新;1：没上新 其他：失败
	 */
	public static int goodsNew(String shopId, String url) {
		LogUtil
				.writeCommodityLog("查询店铺商品是否有上新GoodsUtil.goodsNew()...........start "
						+ shopId + " " + url);

		int state = 10000;
		try {
			String href = "";

			Document doc7 = JsoupUtil.getDocument(url
					+ "/page/newOfferList.htm?time=7");
			Elements imgEle7 = doc7.select("[class=img-box]");
			if (imgEle7.size() > 0) {
				Elements aEle = imgEle7.get(0).select("a");
				href = aEle.attr("href");
			}
			if (StringUtils.isEmpty(href)) {
				Document doc15 = JsoupUtil.getDocument(url
						+ "/page/newOfferList.htm?time=15");
				Elements imgEle15 = doc15.select("[class=img-box]");
				if (imgEle15.size() > 0) {
					Elements aEle = imgEle15.get(0).select("a");
					href = aEle.attr("href");
				}
			}
			if (StringUtils.isEmpty(href)) {
				Document doc30 = JsoupUtil.getDocument(url
						+ "/page/newOfferList.htm?time=30");
				Elements imgEle30 = doc30.select("[class=img-box]");
				if (imgEle30.size() > 0) {
					Elements aEle = imgEle30.get(0).select("a");
					href = aEle.attr("href");
				}
			}

			if (StringUtils.isNotEmpty(href)) {
				String cThirdPlatformId = href.substring(
						href.lastIndexOf("/") + 1, href.lastIndexOf("."));

				CommodityBO bo = new CommodityBO();
				GoodsSchema info = bo
						.getGoodsByformId(shopId, cThirdPlatformId);// 根据第三方平台商品ID查询
				if (info != null) {
					state = 1;
				} else {
					state = 0;
				}
			} else {
				state = 1;
			}
		} catch (Exception e) {
			logger.error("查询店铺商品是否有上新异常：" + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "查询店铺商品是否有上新异常："
					+ shopId + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("查询店铺商品是否有上新GoodsUtil.goodsNew()...........end "
						+ state);
		return state;
	}

	/**
	 * 获取近30天的商品
	 * 
	 * @param shopId
	 * @param url
	 */
	public static void goodsInfoUrlByTime(String shopId, String url) {
		LogUtil
				.writeCommodityLog("获取近30天的商品GoodsUtil.goodsInfoUrlByTime()...........start "
						+ shopId + " " + url);

		try {
			Document doc30 = JsoupUtil.getDocument(url
					+ "/page/newOfferList.htm?time=30");
			Elements imgEle30 = doc30.select("[class=img-box]");
			LogUtil.writeCommodityLog("获取近30天的商品:" + imgEle30.size() + " "
					+ shopId + " " + url);
			if (imgEle30.size() > 0) {
				for (int i = imgEle30.size(); i > 0; i--) {
					Elements aEle = imgEle30.get(i - 1).select("a");
					String href = aEle.attr("href");
					goodsInfoCatch(shopId, href, true, false, "", "", "0", url
							+ "/page/newOfferList.htm?time=30");// 获取商品详情信息
				}
			}

			Document doc15 = JsoupUtil.getDocument(url
					+ "/page/newOfferList.htm?time=15");
			Elements imgEle15 = doc15.select("[class=img-box]");
			LogUtil.writeCommodityLog("获取近15天的商品:" + imgEle15.size() + " "
					+ shopId + " " + url);
			if (imgEle15.size() > 0) {
				for (int i = imgEle15.size(); i > 0; i--) {
					Elements aEle = imgEle15.get(i - 1).select("a");
					String href = aEle.attr("href");
					goodsInfoCatch(shopId, href, true, false, "", "", "0", url
							+ "/page/newOfferList.htm?time=15");// 获取商品详情信息
				}
			}

			Document doc7 = JsoupUtil.getDocument(url
					+ "/page/newOfferList.htm?time=7");
			Elements imgEle7 = doc7.select("[class=img-box]");
			LogUtil.writeCommodityLog("获取近7天的商品:" + imgEle7.size() + " "
					+ shopId + " " + url);
			if (imgEle7.size() > 0) {
				for (int i = imgEle7.size(); i > 0; i--) {
					Elements aEle = imgEle7.get(i - 1).select("a");
					String href = aEle.attr("href");
					goodsInfoCatch(shopId, href, true, false, "", "", "0", url
							+ "/page/newOfferList.htm?time=7");// 获取商品详情信息
				}
			}
		} catch (Exception e) {
			logger.error("获取近30天的商品异常：" + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取近30天的商品异常：" + shopId
					+ " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取近30天的商品GoodsUtil.goodsInfoUrlByTime()...........end "
						+ shopId + " " + url);
	}

	/**
	 * 获取近30天的商品(上新)
	 * 
	 * @param shopId
	 * @param url
	 */
	public static List<String> goodsInfoNew(String shopId, String url,
			String taskId, int num) {
		LogUtil
				.writeCommodityLog("获取近30天的商品(上新)GoodsUtil.goodsInfoNew()...........start "
						+ shopId + " " + url + " " + taskId);

		List<String> cThirdPlatformIdList = new ArrayList<String>();// 阿里商品id集合
		try {
			// 15-30天的数据
			Document doc = JsoupUtil.getDocument(url
					+ "/page/newOfferList.htm?time=30");
			int count = 1;// 总页数
			Elements pageCountEle = doc.select("[class=page-count]");
			String pageCount = pageCountEle.text();// 总页数
			if (StringUtils.isNotEmpty(pageCount)) {
				count = Integer.parseInt(pageCount);
			}
			goodsInfoNewByPage(shopId, url + "/page/newOfferList.htm?time=30",
					cThirdPlatformIdList, taskId, count, num);
			// 8-15天的数据
			doc = JsoupUtil.getDocument(url + "/page/newOfferList.htm?time=15");
			count = 1;// 总页数
			pageCountEle = doc.select("[class=page-count]");
			pageCount = pageCountEle.text();// 总页数
			if (StringUtils.isNotEmpty(pageCount)) {
				count = Integer.parseInt(pageCount);
			}
			goodsInfoNewByPage(shopId, url + "/page/newOfferList.htm?time=15",
					cThirdPlatformIdList, taskId, count, num);
			// 1-7天的数据
			doc = JsoupUtil.getDocument(url + "/page/newOfferList.htm?time=7");
			count = 1;// 总页数
			pageCountEle = doc.select("[class=page-count]");
			pageCount = pageCountEle.text();// 总页数
			if (StringUtils.isNotEmpty(pageCount)) {
				count = Integer.parseInt(pageCount);
			}
			goodsInfoNewByPage(shopId, url + "/page/newOfferList.htm?time=7",
					cThirdPlatformIdList, taskId, count, num);
		} catch (Exception e) {
			logger.error("获取近30天的商品(上新)异常：" + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取近30天的商品(上新)异常："
					+ shopId + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取近30天的商品(上新)GoodsUtil.goodsInfoNew()...........end "
						+ shopId + " " + url + " " + taskId);
		return cThirdPlatformIdList;
	}

	/**
	 * 商品爬取(更新)
	 * 
	 * @param url
	 *            商品列表地址
	 */
	public static void goodsInfoNewByPage(String shopId, String url,
			List<String> cThirdPlatformIdList, String taskId, int num,
			int goodsNum) {
		LogUtil
				.writeCommodityLog("商品爬取(更新)GoodsUtil.goodsInfoNewByPage()...........start "
						+ shopId + " " + url + "&pageNum=" + num);

		Document doc = JsoupUtil.getDocument(url + "&pageNum=" + num);
		Elements imgEle = doc.select("[class=img-box]");
		LogUtil.writeCommodityLog("商品(上新):" + imgEle.size() + " " + shopId
				+ " " + url + "&pageNum=" + num);
		CommodityBO bo = new CommodityBO();
		if (imgEle.size() > 0) {
			for (int i = imgEle.size(); i > 0; i--) {
				try {
					Elements aEle = imgEle.get(i - 1).select("a");
					String href = aEle.attr("href");
					String cThirdPlatformId = href.substring(href
							.lastIndexOf("/") + 1, href.lastIndexOf("."));// 阿里商品id
					GoodsSchema info = bo.getGoodsByformId(shopId,
							cThirdPlatformId);// 根据第三方平台商品ID查询
					if (info == null) {
						cThirdPlatformIdList.add(cThirdPlatformId);// 增加阿里商品id
						goodsInfoNewCatch(shopId, href, cThirdPlatformId,
								taskId, url + "&pageNum=" + num, goodsNum);// 获取商品详情信息
					}
				} catch (Exception e) {
					LogUtil.writeCommodityLog(shopId + " " + url + "&pageNum="
							+ num + "增加商品失败");
					// 发送邮件
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), shopId + " "
							+ url + "&pageNum=" + num, "商品(上新)增加商品失败");
				}
			}
		}

		LogUtil
				.writeCommodityLog("商品爬取(更新)GoodsUtil.goodsInfoNewByPage()...........end "
						+ shopId + " " + url + "&pageNum=" + num);
		if (num > 1) {
			int i = num - 1;
			goodsInfoNewByPage(shopId, url, cThirdPlatformIdList, taskId, i,
					goodsNum);
		}
	}

	/**
	 * 商品爬取（为了更新销售量数据）
	 * 
	 * @param url
	 *            商品列表地址
	 */
	public static void goodsInfoUrl(String shopId, String url) {
		LogUtil
				.writeCommodityLog("商品爬取（为了更新销售量数据）GoodsUtil.goodsInfoUrl()...........start "
						+ shopId + " " + url);

		Document doc = JsoupUtil.getDocument(url);

		Elements divEle = doc
				.select("[data-tracelog-exp=wp_widget_offerhand_main_disp]");
		Elements liEle = divEle.select("li");
		LogUtil.writeCommodityLog("商品爬取（为了更新销售量数据）:" + liEle.size());
		for (int i = 0; i < liEle.size(); i++) {
			Elements countsEle = liEle.get(i).select("[class=booked-counts]");
			Elements imageEle = liEle.get(i).select("[class=image]");
			Elements aEle = imageEle.select("a");
			String counts = "0";
			if (countsEle.size() > 0) {
				counts = countsEle.get(0).text();
			}
			if (aEle.size() > 0) {
				String href = aEle.attr("href");
				LogUtil.writeCommodityLog(" 第 " + i + " 条商品详情页地址：" + href
						+ " 成交量：" + counts);
				// 第四个参数传1，是为了抓取数据时，不重新创建n_id
				goodsInfoCatch(shopId, href, false, true, "", "",
						thirdTotalSellByStr(counts), url);// 获取商品详情信息
			}
		}

		LogUtil
				.writeCommodityLog("商品爬取（为了更新销售量数据）GoodsUtil.goodsInfoUrl()...........end "
						+ shopId + " " + url);
	}

	/**
	 * 商品爬取(所有)
	 * 
	 * @param url
	 *            商品列表地址
	 */
	public static void goodsInfoUrlByPage(String shopId, String url, int num,
			int totalCount) {
		LogUtil
				.writeCommodityLog("商品爬取(所有)GoodsUtil.goodsInfoUrlByPage()...........start "
						+ shopId
						+ " "
						+ url
						+ "&pageNum="
						+ num
						+ "&totalCount=" + totalCount);

		Document doc = JsoupUtil.getDocument(url + "&pageNum=" + num);

		Elements divEle = doc
				.select("[data-tracelog-exp=wp_widget_offerhand_main_disp]");
		if (divEle.size() == 0) {
			divEle = doc.select("[class=wp-offerlist-catalogs]");
		}
		int count = totalCount;// 总页数
		if (count == 1) {
			Elements pageCountEle = doc.select("[class=page-count]");
			String pageCount = pageCountEle.text();// 总页数
			if (StringUtils.isNotEmpty(pageCount)) {
				count = Integer.parseInt(pageCount);
			}
		}
		Elements liEle = divEle.select("li");
		LogUtil.writeCommodityLog(url + "&pageNum=" + num + "商品爬取(所有):"
				+ liEle.size());
		for (int i = 0; i < liEle.size(); i++) {
			Elements countsEle = liEle.get(i).select("[class=booked-counts]");
			if (countsEle.size() == 0) {
				countsEle = liEle.get(i).select("[class=booked-count]");
			}
			Elements imageEle = liEle.get(i).select("[class=image]");
			Elements aEle = imageEle.select("a");
			String counts = "0";
			if (countsEle.size() > 0) {
				counts = countsEle.get(0).text();
			}
			if (aEle.size() > 0) {
				String href = aEle.attr("href");
				LogUtil.writeCommodityLog(url + "&pageNum= " + num + " 第 " + i
						+ " 条商品详情页地址：" + href + " 成交量：" + counts);
				goodsInfoCatch(shopId, href, false, false, "", "",
						thirdTotalSellByStr(counts), url + "&pageNum=" + num);// 获取商品详情信息
			}
		}

		LogUtil
				.writeCommodityLog("商品爬取(所有)GoodsUtil.goodsInfoUrlByPage()...........end "
						+ shopId + " " + url + "&pageNum=" + num);
		// int count = 3;
		if (num < count) {
			int i = num + 1;
			goodsInfoUrlByPage(shopId, url, i, count);
		} else {
			Elements offerCountEle = doc.select("[class=offer-count]");
			String offerCount = offerCountEle.text();// 总条数
			if (num == 1) {
				LogUtil.writeCommodityLog(shopId + " 店铺的商品总条数：" + offerCount);
			}
		}
	}

	// 销量的转换
	public static String thirdTotalSellByStr(String n_third_total_sell) {
		if (StringUtils.isNotEmpty(n_third_total_sell)) {
			if (n_third_total_sell.indexOf("万") > 0) {// 销量中包含【万】字，需要转换成数字单位
				n_third_total_sell = n_third_total_sell.substring(0,
						n_third_total_sell.indexOf("万"));
				if (n_third_total_sell.indexOf(".") > 0) {
					String n_third_total_sell1 = n_third_total_sell.substring(
							0, n_third_total_sell.indexOf("."));
					String n_third_total_sell2 = n_third_total_sell.substring(
							n_third_total_sell.indexOf(".") + 1,
							n_third_total_sell.length());
					String str = "000";
					if (n_third_total_sell2.length() == 2) {
						str = "00";
					} else if (n_third_total_sell2.length() == 3) {
						str = "0";
					} else if (n_third_total_sell2.length() == 4) {
						str = "";
					}
					n_third_total_sell = n_third_total_sell1
							+ n_third_total_sell2 + str;
				} else {
					n_third_total_sell = n_third_total_sell + "0000";
				}
			}
		}
		return n_third_total_sell;
	}

	/**
	 * 获取商品详情信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            商品详情页url
	 * @param isGood
	 *            存在的商品是否删除后重新创建
	 * @param isSell
	 *            是否抓取销量
	 */
	public static void goodsInfoCatch(String shopId, String url,
			boolean isGood, boolean isSell, String id, String gid,
			String n_third_total_sell, String tpGoodsUrl) {
		LogUtil
				.writeCommodityLog("获取商品详情信息GoodsUtil.goodsInfo()...........start "
						+ shopId + " " + url);
		try {
			if (url.indexOf("http") == -1) {
				url = "http:" + url;
			}
			String cThirdPlatformId = url.substring(url.lastIndexOf("/") + 1,
					url.lastIndexOf("."));

			CommodityBO bo = new CommodityBO();
			GoodsSchema info = bo.getGoodsByformId(shopId, cThirdPlatformId);// 根据第三方平台商品ID查询
			if (info == null) {
				if (isSell) {// 抓销量
					return;
				}
				Document doc = JsoupUtil.getDocument(url);
				GoodsSchema schema = new GoodsSchema();
				setGoodsCategoryId(doc, url, schema);// 设置商品标准类目ID(用自己的销量临时存了下起批量，防止sku里面不存在)
				if (StringUtils.isNotEmpty(schema.getN_sys_cid())) {// 分类ID不存在证明商品已经下架
					String goodsId = gid;// 获取商品ID
					if (StringUtils.isEmpty(gid)) {
						goodsId = JsoupUtil.goodsId();
					}
					LogUtil.writeCommodityLog(shopId + " 商家的商品ID：" + goodsId);
					List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();

					schema.setN_id(id);
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setC_third_platform_id(cThirdPlatformId);// 第三方平台商品ID
					schema.setC_tp_goods_url(tpGoodsUrl);// 第三方商品地址
					schema.setN_third_platform_type("1");// 商品来源平台类型:0,自己平台;1,阿里;2,淘宝
					schema.setN_shop_id(shopId);// 卖家ID:对应mem_seller表中的主键ID
					schema.setC_goods_name(titleInfo(doc));// 商品名称
					schema.setN_express_pay_type("2");// 运费承担方式：1卖家，2买家，
					schema.setC_goods_state("1");// // 商品状态：1上架，2下架，3删除 4缺货
					// 3自取，4运费到付
					schema.setC_goods_detail_desc(htmlInfo(doc,
							cThirdPlatformId, shopId, goodsId, infoImageList));// 商品信息描述:静态页面链接
					schema.setC_tp_order_url("http://m.1688.com/offer/"
							+ cThirdPlatformId + ".html");// 第三方平台选购URL地址(阿里手机的详情页地址)
					schema
							.setT_create_time(DateUtil.format8
									.format(new Date()));// 添加时间
					schema.setT_last_update_time(DateUtil.format8
							.format(new Date()));// 最后修改时间
					if (StringUtils.isNotEmpty(n_third_total_sell)) {
						schema.setN_third_total_sell(n_third_total_sell);// 第三方平台总销量
					} else {
						schema.setN_third_total_sell("0");// 第三方平台总销量
					}
					String data = getExpressData(cThirdPlatformId, goodsId,
							shopId);// 获取物流和重量的json
					schema.setN_weight(getExpressUnitWeight(data, goodsId,
							shopId));// 单位重量(千克)

					GoodsDefaultExpressSchema express = getExpress(data,
							goodsId, shopId);// 获取默认物流
					List<GoodsExtendsPropertySchema> propertyList = extendsInfo(
							doc, goodsId, shopId, url, schema);// 获取商品的扩展属性
					List<GoodsSkuInfoSchema> skuInfoList = new ArrayList<GoodsSkuInfoSchema>();
					List<GoodsPriceSectionSchema> sectionList = new ArrayList<GoodsPriceSectionSchema>();
					skuAndPrice(doc, goodsId, shopId, schema.getC_goods_name(),
							url, schema.getN_ownner_sell(), schema,
							skuInfoList, sectionList);// 获取SKU和起批量
					List<GoodsImageSchema> imageList = imgInfo(doc, goodsId,
							shopId, url);// 获取图片
					if (skuInfoList.size() > 0 && sectionList.size() > 0
							&& imageList.size() > 0) {
						boolean redult = bo.addGoodsList(shopId, schema, express,
								propertyList, skuInfoList, sectionList,
								imageList, infoImageList);// 增加商家商品信息
						LogUtil.writeCommodityLog("增加商家商品信息：" + redult);
					}
				} else {
					LogUtil.writeCommodityLog(shopId + " 商品已经下架：" + url);
					// 发送邮件
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), shopId
							+ " 商品已经下架：" + url, "商品已经下架");
				}
			} else {
				if (isGood) {// 存在的商品是否删除后重新创建
					bo.deleteGoodsId(info.getN_goods_id(), shopId);// 删除商品信息
					if (StringUtils.isEmpty(id)) {// dao层会根据nid判断，是否要插入原有的nid(获取最新商品时，是需要制空nid，然后重新生成新，为了让最新商品排序在最前面)
						info.setN_id("");
					}

					info.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
					info.setT_last_update_time(DateUtil.format8
							.format(new Date()));// 最后修改时间
					boolean redult = bo.addGoods(shopId, info);// 增加商家商品信息
					LogUtil.writeCommodityLog("存在的商品删除后重新创建：" + redult);
				}
				if (isSell) {
					if (StringUtils.isNotEmpty(n_third_total_sell)
							&& !n_third_total_sell.equals("0")) {// 在更新商品时，需要更新当前页的销售量（业务层会根据销售量排序，按销售量倒序）
						boolean redult = bo.updateGoodsSell(info
								.getN_goods_id(), n_third_total_sell);// 增加商家商品信息
						LogUtil.writeCommodityLog("更新销量：" + redult);
					}
				}
			}
		} catch (Exception e) {
			logger.error(shopId + " 获取商品详情信息失败：" + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), shopId + " 获取商品详情信息失败："
					+ url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品详情信息GoodsUtil.goodsInfo()...........end "
						+ shopId + " " + url);
	}

	/**
	 * 获取商品详情信息(上新)
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            商品详情页url
	 * @param cThirdPlatformId
	 *            阿里商品id
	 * @param taskId
	 *            任务ID
	 */
	public static void goodsInfoNewCatch(String shopId, String url,
			String cThirdPlatformId, String taskId, String tpGoodsUrl, int num) {
		LogUtil
				.writeCommodityLog("获取商品详情信息(上新)GoodsUtil.goodsInfoNew()...........start "
						+ shopId + " " + cThirdPlatformId + " " + url);
		try {
			if (url.indexOf("http") == -1) {
				url = "http:" + url;
			}
			CommodityBO bo = new CommodityBO();
			Document doc = JsoupUtil.getDocument(url);
			GoodsSchema schema = new GoodsSchema();
			setGoodsCategoryId(doc, url, schema);// 设置商品标准类目ID(用自己的销量临时存了下起批量，防止sku里面不存在)
			if (StringUtils.isNotEmpty(schema.getN_sys_cid())) {// 分类ID不存在证明商品已经下架
				String goodsId = JsoupUtil.goodsId();// 获取商品ID
				LogUtil.writeCommodityLog(shopId + " 商家的商品ID：" + goodsId);
				List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();

				schema.setN_goods_id(goodsId);// 商品ID
				schema.setC_third_platform_id(cThirdPlatformId);// 第三方平台商品ID
				schema.setC_tp_goods_url(tpGoodsUrl);// 第三方商品地址
				schema.setN_third_platform_type("1");// 商品来源平台类型:0,自己平台;1,阿里;2,淘宝
				schema.setN_shop_id(shopId);// 卖家ID:对应mem_seller表中的主键ID
				schema.setC_goods_name(titleInfo(doc));// 商品名称
				schema.setN_express_pay_type("2");// 运费承担方式：1卖家，2买家，
				schema.setC_goods_state("1");// // 商品状态：1上架，2下架，3删除 4缺货
				// 3自取，4运费到付
				schema.setC_goods_detail_desc(htmlInfo(doc, cThirdPlatformId,
						shopId, goodsId, infoImageList));// 商品信息描述:静态页面链接
				schema.setC_tp_order_url("http://m.1688.com/offer/"
						+ cThirdPlatformId + ".html");// 第三方平台选购URL地址(阿里手机的详情页地址)
				schema.setT_create_time(DateUtil.format8.format(new Date()));// 添加时间
				schema.setT_last_update_time(DateUtil.format8
						.format(new Date()));// 最后修改时间
				schema.setN_third_total_sell("0");// 第三方平台总销量

				String data = getExpressData(cThirdPlatformId, goodsId, shopId);// 获取物流和重量的json
				schema.setN_weight(getExpressUnitWeight(data, goodsId, shopId));// 单位重量(千克)

				GoodsDefaultExpressSchema express = getExpress(data, goodsId,
						shopId);// 获取默认物流
				List<GoodsExtendsPropertySchema> propertyList = extendsInfo(
						doc, goodsId, shopId, url, schema);// 获取商品的扩展属性
				List<GoodsSkuInfoSchema> skuInfoList = new ArrayList<GoodsSkuInfoSchema>();
				List<GoodsPriceSectionSchema> sectionList = new ArrayList<GoodsPriceSectionSchema>();
				skuAndPrice(doc, goodsId, shopId, schema.getC_goods_name(),
						url, schema.getN_ownner_sell(), schema, skuInfoList,
						sectionList);// 获取SKU和起批量
				List<GoodsImageSchema> imageList = imgInfo(doc, goodsId,
						shopId, url);// 获取图片
				boolean redult = false;
				if (skuInfoList.size() > 0 && sectionList.size() > 0) {
					redult = bo.addGoodsList(shopId, schema, express, propertyList,
							skuInfoList, sectionList, imageList, infoImageList);// 增加商家商品信息
					LogUtil.writeCommodityLog("增加商家商品信息：" + redult);
				}

				if (redult) {
					try {
						GoodsSyncRecordSchema goodsSyncRecordSchema = new GoodsSyncRecordSchema();// 商品上新
						goodsSyncRecordSchema.setN_shop_id(shopId);// 店铺ID
						goodsSyncRecordSchema.setN_goods_id(goodsId);// 商品ID
						goodsSyncRecordSchema.setN_task_id(taskId);// 任务ID
						goodsSyncRecordSchema.setN_num(num + "");// 商品爬取次数
						bo.addGoodsSyncRecord(goodsSyncRecordSchema);// 增加商品上新
					} catch (Exception e) {
						logger.error(shopId + " " + cThirdPlatformId + " "
								+ taskId + " 增加商品上新失败：" + url, e);
					}
				}
			} else {
				LogUtil.writeCommodityLog(shopId + " " + cThirdPlatformId
						+ " 商品已经下架：" + url);
				// 发送邮件
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), shopId + " 商品已经下架："
						+ url, "商品已经下架");
			}
		} catch (Exception e) {
			logger.error(shopId + " " + cThirdPlatformId + " 获取商品详情信息(上新)失败："
					+ url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), shopId
					+ " 获取商品详情信息(上新)失败：" + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品详情信息(上新)GoodsUtil.goodsInfoNew()...........end "
						+ shopId + " " + cThirdPlatformId + " " + url);
	}

	// 检测商品是否已下架（true）
	public static boolean isGoods(Document doc) {
		boolean isGoods = false;
		Elements infoEle = doc
				.select("[data-widget-name=offerdetail_offSale_expire]");
		String str = infoEle.text();
		if ("产品已下架".equals(str)) {
			isGoods = true;
		}
		Elements titleEle = doc.select("[class=mod-detail-offline-title]");
		String title = titleEle.text();
		if ("商品已下架".equals(title)) {
			isGoods = true;
		}
		return isGoods;
	}

	/**
	 * 获取商品详情信息（重新同步商品数据）
	 * 
	 * @param shopId店铺ID
	 * @param cThirdPlatformId第三方ID
	 */
	public static int goodsInfoUpdateCatch(String shopId,
			String cThirdPlatformId) {
		LogUtil
				.writeCommodityLog("获取商品详情信息（重新同步商品数据）GoodsUtil.goodsInfoUpdateCatch()...........start "
						+ shopId + " " + cThirdPlatformId);
		try {
			String url = "http://detail.1688.com/offer/" + cThirdPlatformId
					+ ".html";
			Document doc = JsoupUtil.getDocument(url);
			String companyName = JsoupUtil.commodityUrl(doc, url);
			if (StringUtils.isEmpty(companyName)
					|| (StringUtils.isNotEmpty(companyName)
							&& companyName.length() > 3 && companyName
							.substring(0, 3).equals("404")) || isGoods(doc)) {
				return 1;
			}
			CommodityBO bo = new CommodityBO();
			GoodsSchema info = bo.getGoodsByformId(shopId, cThirdPlatformId);// 根据第三方平台商品ID查询
			if (info == null) {
				return 2;
			}

			String goodsId = info.getN_goods_id();// 商品ID
			String data = getExpressData(cThirdPlatformId, goodsId, shopId);// 获取物流和重量的json
			String weight = getExpressUnitWeight(data, goodsId, shopId);
			if (weight != "0") {
				info.setN_weight(weight);
			}
//			info.setC_goods_name(titleInfo(doc));// 标题
			// bo.updateGoodsName(goodsId, titleInfo(doc),
			// info.getN_weight());// 修改商品名称
			// boolean infoImgRefult = bo
			// .deleteGoodsInfoImageById(goodsId, shopId);
			// LogUtil.writeCommodityLog(goodsId + "删除商品详情图片信息：" +
			// infoImgRefult);
			setGoodsCategoryId(doc, url, info);// 设置商品标准类目ID(用自己的销量临时存了下起批量，防止sku里面不存在)
			List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
			htmlInfo(doc, cThirdPlatformId, shopId, goodsId, infoImageList);// 获取详情页商品介绍并创建html文件

			// boolean priceRefult = bo.deleteGoodsPriceSectionById(goodsId,
			// shopId);
			// LogUtil.writeCommodityLog(goodsId + "删除商品价格区间信息：" + priceRefult);
			List<GoodsSkuInfoSchema> skuInfoList = new ArrayList<GoodsSkuInfoSchema>();
			List<GoodsSkuInfoSchema> skuInfoUpdateList = new ArrayList<GoodsSkuInfoSchema>();
			List<GoodsPriceSectionSchema> sectionList = new ArrayList<GoodsPriceSectionSchema>();
			skuAndPriceUpdate(doc, goodsId, shopId, info.getC_goods_name(),
					url, info, skuInfoList, skuInfoUpdateList, sectionList);// 获取SKU和起批量

			// boolean imgRefult = bo.deleteGoodsImageById(goodsId, shopId);
			// LogUtil.writeCommodityLog(goodsId + "删除商品图片信息：" + imgRefult);
			List<GoodsImageSchema> imageList = imgInfo(doc, goodsId, shopId,
					url);// 获取图片
			// boolean extRefult = bo.deleteGoodsExtendsPropertyById(goodsId,
			// shopId);// 删除商品扩展属性信息
			// LogUtil.writeCommodityLog(goodsId + "删除商品扩展属性信息：" + extRefult);
			List<GoodsExtendsPropertySchema> propertyList = extendsInfo(doc,
					goodsId, shopId, url, info);// 获取商品的扩展属性
			bo.updateGoodsList(shopId, info, propertyList, skuInfoList,
					skuInfoUpdateList, sectionList, imageList, infoImageList);
		} catch (Exception e) {
			logger.error(shopId + " 获取商品详情信息（重新同步商品数据）失败：" + cThirdPlatformId,
					e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), shopId + " 获取商品详情信息失败："
					+ cThirdPlatformId, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品详情信息（重新同步商品数据）GoodsUtil.goodsInfoUpdateCatch()...........end "
						+ shopId + " " + cThirdPlatformId);
		return 0;
	}

	// public static void main(String[] args) {
	// getExpress(getExpressData("1176724546", "1", "1"), "1", "1");
	// }

	public static String getExpressData(String thirdPlatformId, String goodsId,
			String shopId) {
		LogUtil
				.writeCommodityLog("获取物流Json数据GoodsUtil.getExpress()...........start "
						+ thirdPlatformId + " " + goodsId + " " + shopId);
		String url = "http://laputa.1688.com/offer/ajax/widgetList.do?callback=jQuery1720904569074511528_1439344338122&blocks=&data=offerdetail_ditto_title%2Cofferdetail_common_report%2Cofferdetail_ditto_serviceDesc%2Cofferdetail_ditto_preferential%2Cofferdetail_ditto_postage%2Cofferdetail_ditto_offerSatisfaction%2Cofferdetail_ditto_guarantee%2Cofferdetail_ditto_installmentInfo%2Cofferdetail_ditto_tradeWay%2Cofferdetail_ditto_samplePromotion&pageId=laputa20140721212446";
		url += "&offerId=" + thirdPlatformId;
		try {
			String data = JsoupUtil.getUrl(url);
			if (StringUtils.isNotEmpty(data)) {
				data = data.substring(data.indexOf("(") + 1, data.length() - 1);
				return data;
			}
		} catch (Exception e) {
			logger.error("获取物流Json数据异常：" + thirdPlatformId + " " + goodsId
					+ " " + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取物流Json数据异常："
					+ goodsId + " " + shopId + " " + url, LogUtil
					.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取物流Json数据GoodsUtil.getExpress()...........end "
						+ thirdPlatformId + " " + goodsId + " " + shopId);
		return "";
	}

	/**
	 * 获取商品重量
	 * 
	 * @param data
	 * @param goodsId
	 * @param shopId
	 * @return
	 */
	public static String getExpressUnitWeight(String data, String goodsId,
			String shopId) {
		LogUtil
				.writeCommodityLog("获取商品重量GoodsUtil.getExpressUnitWeight()...........start "
						+ " "
						+ goodsId
						+ " "
						+ shopId
						+ " "
						+ (StringUtils.isNotEmpty(data) ? "有值" : "没值"));
		String unitWeight = "0";
		try {
			if (StringUtils.isNotEmpty(data)) {
				JSONObject json = new JSONObject(data);
				JSONObject json1 = json.getJSONObject("data");
				JSONObject json2 = json1.getJSONObject("data");
				JSONObject postage = json2
						.getJSONObject("offerdetail_ditto_postage");
				if (!postage.isNull("unitWeight")) {
					unitWeight = JsoupUtil.priceFen(postage
							.getDouble("unitWeight")
							+ "");// 重量
				}
			}
		} catch (Exception e) {
			logger.error("获取商品重量异常：" + goodsId + " " + shopId, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取默认物流异常：" + goodsId
					+ " " + shopId, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品重量GoodsUtil.getExpressUnitWeight()...........end "
						+ " " + goodsId + " " + shopId + " " + unitWeight);
		return unitWeight;
	}

	public static GoodsDefaultExpressSchema getExpress(String data,
			String goodsId, String shopId) {
		LogUtil
				.writeCommodityLog("获取默认物流GoodsUtil.getExpress()...........start "
						+ goodsId + " " + shopId);
		GoodsDefaultExpressSchema schema = new GoodsDefaultExpressSchema();
		String fee = "0";
		try {
			if (StringUtils.isNotEmpty(data)) {
				JSONObject json = new JSONObject(data);
				JSONObject json1 = json.getJSONObject("data");
				JSONObject json2 = json1.getJSONObject("data");
				JSONObject postage = json2
						.getJSONObject("offerdetail_ditto_postage");
				String freightLocation = "";// 发货地
				if (!postage.isNull("freightLocation")) {
					freightLocation = postage.getString("freightLocation");// 发货地
				}
				if (!postage.isNull("freightCost")) {
					JSONArray freightCostArray = postage
							.getJSONArray("freightCost");
					JSONObject freightCost = freightCostArray.getJSONObject(0);
					JSONArray costItemsArray = freightCost
							.getJSONArray("costItems");
					JSONObject costItems = costItemsArray.getJSONObject(0);
					double express = costItems.getDouble("value");// 默认运费
					fee = JsoupUtil.priceFen(express + "");
				}

				schema.setN_goods_id(goodsId);// 商品ID
				schema.setC_delivery_location(freightLocation);// 发货地
				schema.setC_receive_location("");// 收货地
				schema.setN_price(fee);// 价格
				schema.setN_shop_id(shopId);// 店铺ID

				// CommodityBO bo = new CommodityBO();
				// boolean redult = bo.addGoodsDefaultExpress(schema);// 增加默认物流表
				// LogUtil.writeCommodityLog("增加默认物流表：" + redult);
			}
		} catch (Exception e) {
			logger.error("获取默认物流异常：" + goodsId + " " + shopId, e);
			// 发送邮件
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(), "获取默认物流异常：" + goodsId
			// + " " + shopId, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取默认物流GoodsUtil.getExpress()...........end "
				+ goodsId + " " + shopId + " " + fee);
		return schema;
	}

	/**
	 * 获取默认物流
	 * 
	 * @param doc
	 *            详情页doc
	 * 
	 */
	// public static void expressInfo(Document doc, String goodsId, String
	// shopId,
	// String url) {
	// LogUtil
	// .writeCommodityLog("获取默认物流GoodsUtil.expressInfo()...........start "
	// + goodsId + " " + shopId);
	//
	// try {
	// Elements ele = doc.select("[class=mod mod-detail-postage]");
	// // 发货地
	// Elements detailEle = ele.select("[class=delivery-detail]");
	// String detail = detailEle.text();
	// // 收货地
	// Elements areaEle = ele
	// .select("[class=unit-detail-destination-area]");
	// String location = "";
	// if (areaEle.size() > 0) {
	// String jsonString = areaEle.get(0).attr("data-unit-config");
	// JSONObject json = new JSONObject(jsonString);
	// location = json.getString("visitorProvince");
	// if (StringUtils.isNotEmpty(location)) {
	// location = location.substring(0, location.indexOf("_"));
	// }
	// }
	// // 快递费
	// Elements feeEle = ele.select("[class=cost-entries-type]");
	// Elements emEle = feeEle.select("em");
	// String fee = "800";// 邮费默认800
	// if (emEle.size() > 0) {
	// fee = JsoupUtil.priceFen(emEle.get(0).text());
	// }
	// if (StringUtils.isEmpty(fee)) {
	// fee = "800";
	// }
	//
	// LogUtil.writeCommodityLog("获取默认物流: 发货地：" + detail + " 收货地："
	// + location + " 快递费：" + fee);
	//
	// GoodsDefaultExpressSchema schema = new GoodsDefaultExpressSchema();
	// schema.setN_goods_id(goodsId);// 商品ID
	// schema.setC_delivery_location(detail);// 发货地
	// schema.setC_receive_location(location);// 收货地
	// schema.setN_price(fee);// 价格
	// schema.setN_shop_id(shopId);// 店铺ID
	//
	// CommodityBO bo = new CommodityBO();
	// boolean redult = bo.addGoodsDefaultExpress(schema);// 增加默认物流表
	// LogUtil.writeCommodityLog("增加默认物流表：" + redult);
	// } catch (Exception e) {
	// logger.error("获取默认物流异常：" + goodsId + " " + shopId + " " + url, e);
	// // 发送邮件
	// SimpleMailSender sms = MailSenderFactory.getSender();
	// sms.send(CommodityContent.getRecipients(), "获取默认物流异常：" + goodsId
	// + " " + shopId + " " + url, LogUtil.getExceptionError(e));
	// }
	// LogUtil
	// .writeCommodityLog("获取默认物流GoodsUtil.expressInfo()...........end "
	// + goodsId + " " + shopId);
	// }
	/**
	 * 获取域名首页地址
	 * 
	 * @param doc
	 *            详情页doc
	 * 
	 * @throws Exception
	 */
	public static String indexUrl(Document doc) {
		LogUtil
				.writeCommodityLog("获取域名首页地址GoodsUtil.indexUrl()...........start");

		Elements ele = doc.select("[data-page-name=index]");
		Elements aEle = ele.select("a");
		String href = aEle.attr("href");
		LogUtil.writeCommodityLog("获取域名首页地址GoodsUtil.indexUrl()...........end "
				+ href);
		return href;
	}

	/**
	 * 获取商品的标题
	 * 
	 * @param doc
	 *            详情页doc
	 * 
	 * @throws Exception
	 */
	public static String titleInfo(Document doc) {
		LogUtil
				.writeCommodityLog("获取商品的标题GoodsUtil.titleInfo()...........start");

		Elements ele = doc.select("[class=mod-detail-title]");
		Elements hEle = ele.select("h1");
		String title = hEle.text();
		if (StringUtils.isEmpty(title)) {
			ele = doc.select("[class=mod-detail-hd]");
			hEle = ele.select("h1");
			title = hEle.text();
		}
		LogUtil.writeCommodityLog("商品的标题：" + title);
		LogUtil.writeCommodityLog("获取商品的标题GoodsUtil.titleInfo()...........end "
				+ title);
		return title;
	}

	/**
	 * 获取商品的单件重量
	 * 
	 * @param doc
	 *            详情页doc
	 * 
	 * @throws Exception
	 */
	// public static String parcelInfo(Document doc) {
	// LogUtil
	// .writeCommodityLog("获取商品的单件重量GoodsUtil.parcelInfo()...........start");
	//
	// Elements parcelEle = doc.select("[class=parcel-unit-weight]");
	// Elements spanEle = parcelEle.select("[class=value]");
	// String parcel = spanEle.text();
	// if (StringUtils.isEmpty(parcel)) {// 1278992027
	// parcel = "0";
	// } else {
	// parcel = JsoupUtil.unit(parcel);
	//
	// }
	// LogUtil.writeCommodityLog("商品的单件重量(克)：" + parcel);
	// LogUtil
	// .writeCommodityLog("获取商品的单件重量GoodsUtil.parcelInfo()...........end "
	// + parcel);
	// return parcel;
	// }
	/**
	 * 获取商品的扩展属性
	 * 
	 * @param doc
	 *            详情页doc
	 * @param goodsId
	 *            商品ID
	 */
	public static List<GoodsExtendsPropertySchema> extendsInfo(Document doc,
			String goodsId, String shopId, String url, GoodsSchema goods) {
		LogUtil
				.writeCommodityLog("获取商品的扩展属性GoodsUtil.extendsInfo()...........start "
						+ goodsId + " " + shopId);
		List<GoodsExtendsPropertySchema> propertyList = new ArrayList<GoodsExtendsPropertySchema>();
		try {
			Elements extendsEle = doc
					.select("[data-widget-name=offerdetail_ditto_attributes]");
			// CommodityBO bo = new CommodityBO();
			if (extendsEle.size() > 0) {
				LogUtil.writeCommodityLog("商品的扩展：offerdetail_ditto_attributes");
				Elements tdEle = extendsEle.select("td");
				for (int i = 0; i < tdEle.size(); i++) {
					String key = tdEle.get(i).text();
					if (StringUtils.isNotEmpty(key)) {
						String val = tdEle.get(++i).text();
						LogUtil.writeCommodityLog(i + " 商品的扩展属性：" + key + " "
								+ val);
						GoodsExtendsPropertySchema schema = new GoodsExtendsPropertySchema();
						schema.setN_goods_id(goodsId);// 商品ID
						schema.setC_prop_key_label(key);// 扩展属性键label
						schema.setC_prop_value(val);// 扩展属性值
						schema.setN_shop_id(shopId);// 卖家ID
						if (schema.getC_prop_key_label().equals("货号")) {// 修改商品表的货号
							goods.setC_art_no(schema.getC_prop_value());
						}

						propertyList.add(schema);
						// try {
						// boolean redult =
						// bo.addGoodsExtendsProperty(schema);// 增加商品扩展属性
						// LogUtil.writeCommodityLog("增加商品扩展属性：" + redult);
						// } catch (Exception e) {
						// logger.error("获取商品的扩展属性：" + goodsId + " " + shopId
						// + " " + url, e);
						// }
					}
				}
			} else {
				LogUtil
						.writeCommodityLog("商品的扩展：offerdetail_common_attributes");
				Elements extendsEle1 = doc
						.select("[data-widget-name=offerdetail_common_attributes]");
				Elements tdEle = extendsEle1.select("td");
				for (int i = 0; i < tdEle.size(); i++) {
					String key = tdEle.get(i).text();
					if (StringUtils.isNotEmpty(key)) {
						String[] val = key.split("：");
						LogUtil.writeCommodityLog(i + " 商品的扩展属性：" + val[0]
								+ " " + val[1]);
						GoodsExtendsPropertySchema schema = new GoodsExtendsPropertySchema();
						schema.setN_goods_id(goodsId);// 商品ID
						schema.setC_prop_key_label(val[0]);// 扩展属性键label
						schema.setC_prop_value(val[1]);// 扩展属性值
						schema.setN_shop_id(shopId);// 卖家ID
						if (schema.getC_prop_key_label().equals("货号")) {// 修改商品表的货号
							goods.setC_art_no(schema.getC_prop_value());
						}

						propertyList.add(schema);
						// try {
						// boolean redult =
						// bo.addGoodsExtendsProperty(schema);// 增加商品扩展属性
						// LogUtil.writeCommodityLog("增加商品扩展属性：" + redult);
						// } catch (Exception e) {
						// logger.error("获取商品的扩展属性：" + goodsId + " " + shopId
						// + " " + url, e);
						// }
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取商品的扩展属性：" + goodsId + " " + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取商品的扩展属性：" + goodsId
					+ " " + shopId + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品的扩展属性GoodsUtil.extendsInfo()...........end "
						+ goodsId + " " + shopId);
		return propertyList;
	}

	public static void skuAndPrice(Document doc, String goodsId, String shopId,
			String goodsName, String url, String n_ownner_sell,
			GoodsSchema goods, List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsPriceSectionSchema> sectionList) {

		LogUtil
				.writeCommodityLog("获取SKU和起批量GoodsUtil.skuAndPrice()...........start "
						+ goodsId + " " + shopId + " " + goodsName + " " + url);
		try {
			String jsonString = "";// json数据
			Elements javascript = doc.select("[type=text/javascript]");
			for (int i = 0; i < javascript.size(); i++) {
				Elements ele = javascript.get(i).getAllElements();
				String data = ele.html();
				if (data.indexOf("iDetailData") >= 0) {
					jsonString = data;
					break;
				}
			}
			CommodityBO bo = new CommodityBO();
			jsonString = jsonString
					.substring(jsonString.indexOf("iDetailData"));
			if (jsonString.indexOf(":") > -1) {// 存在sku的情况
				jsonString = jsonString.substring(jsonString.indexOf(":") + 1,
						jsonString.lastIndexOf("}"));

				List<GoodsSkuListSchema> skuLists = new ArrayList<GoodsSkuListSchema>();
				// 解析json获得颜色和尺码
				java.util.List<String> colorList = new ArrayList<String>();// 颜色
				java.util.List<String> sizeList = new ArrayList<String>();// 尺码
				JSONObject json = new JSONObject(jsonString);
				JSONArray skuPropsArray = json.getJSONArray("skuProps");
				for (int i = 0; i < skuPropsArray.length(); i++) {
					JSONObject skuPropsChildJson = skuPropsArray
							.getJSONObject(i);
					String prop = skuPropsChildJson.getString("prop");
					JSONArray valueArray = skuPropsChildJson
							.getJSONArray("value");
					for (int j = 0; j < valueArray.length(); j++) {
						JSONObject childJson1 = valueArray.getJSONObject(j);
						if (childJson1.length() > 0) {
							String name = childJson1.getString("name");

							GoodsSkuListSchema schema = new GoodsSkuListSchema();
							schema.setN_goods_id(goodsId);// 商品ID
							schema.setN_shop_id(shopId);// 店铺ID
							schema.setC_sku_name(name);// sku名称，该节点值的名称，如红色
							schema.setC_sku_prop(prop);// sku属性名，如颜色
							schema.setN_sku_level(i + "");// 层级，如0，1，2……
							schema.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

							skuLists.add(schema);
							// boolean redult = bo.addGoodsSkuList(schema);
							// // 增加商品SKU列表信息
							// LogUtil.writeCommodityLog("增加商品SKU列表信息：" +
							// redult);
							if (i == 0) {
								colorList.add(name);
							} else {
								sizeList.add(name);
							}
						}
					}
				}
				boolean addSkuListRedult = bo.addGoodsSkuList(goodsId, skuLists, null);
				// 增加商品SKU列表信息
				LogUtil.writeCommodityLog("增加商品SKU列表信息：" + addSkuListRedult);
				List<GoodsSkuListSchema> skuList = bo.getGoodsSkuListBygId(
						goodsId, 0);// 获取SKU列表信息
				Map<String, String> skuListMap = new HashMap<String, String>();// SKU列表信息转为map<sku名称,skuId>
				for (GoodsSkuListSchema schema : skuList) {
					skuListMap.put(schema.getC_sku_name(), schema.getN_id());
				}
				int skuId = 0;
				int totalStock = 0;// 总库存
				// 获取sku的库存
				JSONObject skuMapJson = json.getJSONObject("skuMap");
				for (String color : colorList) {
					if (sizeList.size() > 0) {// 尺码和颜色的情况
						for (String size : sizeList) {
							String key = color + "&gt;" + size;
							if (!skuMapJson.isNull(key)) {// sku库存没有就不要
								String js = skuMapJson.getString(key);
								JSONObject json1 = new JSONObject(js);
								String stockNum = json1
										.getString("canBookCount");
								String price = "0";
								if (!json1.isNull("price")) {
									price = json1.getString("price");
								}
								LogUtil.writeCommodityLog(key + " 的库存："
										+ stockNum + " price:" + price);
								totalStock = totalStock
										+ Integer.parseInt(stockNum);

								GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
								schemaInfo.setC_sku_id(goodsId + "_" + skuId);// SKUID
								schemaInfo.setN_goods_id(goodsId);// 商品ID
								schemaInfo.setN_shop_id(shopId);// 店铺ID
								schemaInfo.setN_stock_num(stockNum);// 库存
								schemaInfo.setN_discount_price("0");// 折扣价格
								schemaInfo
										.setN_price(JsoupUtil.priceFen(price));// 价格
								schemaInfo.setC_sku_list_id(skuListMap
										.get(color)
										+ ";" + skuListMap.get(size));// sku列表的ID集合
								schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

								skuId = skuId + 1;
								skuInfoList.add(schemaInfo);
								// boolean redult1 = bo
								// .addGoodsSkuInfo(schemaInfo);// 增加商品SKU详情信息
								// LogUtil.writeCommodityLog("增加商品SKU详情信息："
								// + redult1);
							}
						}
					} else {// 没有尺码的情况（可能是没有颜色）
						if (!skuMapJson.isNull(color)) {// sku库存没有就不要
							String js = skuMapJson.getString(color);
							JSONObject json1 = new JSONObject(js);
							String stockNum = json1.getString("canBookCount");
							String price = "0";
							if (!json1.isNull("price")) {
								price = json1.getString("price");
							}
							LogUtil.writeCommodityLog(color + " 的库存："
									+ stockNum + " price:" + price);
							totalStock = totalStock
									+ Integer.parseInt(stockNum);

							GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
							schemaInfo.setC_sku_id(goodsId + "_" + skuId);// SKUID
							schemaInfo.setN_goods_id(goodsId);// 商品ID
							schemaInfo.setN_shop_id(shopId);// 店铺ID
							schemaInfo.setN_stock_num(stockNum);// 库存
							schemaInfo.setN_discount_price("0");// 折扣价格
							schemaInfo.setN_price(JsoupUtil.priceFen(price));// 价格
							schemaInfo.setC_sku_list_id(skuListMap.get(color));// sku列表的ID集合
							schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

							skuId = skuId + 1;
							skuInfoList.add(schemaInfo);
							// boolean redult1 =
							// bo.addGoodsSkuInfo(schemaInfo);// 增加商品SKU详情信息
							// LogUtil.writeCommodityLog("增加商品SKU详情信息：" +
							// redult1);
						}

					}
				}
				// 获取起批量
				if (json.isNull("priceRangeOriginal")) {// 起批量如果为空
					GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					// 20150509tangbiao 需求要求起批量1件起
					// if (StringUtils.isNotEmpty(n_ownner_sell)
					// && !n_ownner_sell.equals("0")) {
					// schema.setN_num_start(n_ownner_sell);// 开始数量
					// } else {
					// schema.setN_num_start("1");// 开始数量
					// }
					schema.setN_num_start("1");// 开始数量
					schema.setN_num_end("9999999");// 结束数量
					schema.setN_price(goods.getN_sell_price());// 价格
					schema.setN_shop_id(shopId);// 卖家ID

					sectionList.add(schema);
					// try {
					// boolean redult = bo.addGoodsPriceSection(schema);//
					// 增加商品价格区间信息
					// LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
					// } catch (Exception e) {
					// logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId
					// + " " + goodsName + " " + url, e);
					// }
				} else {
					JSONArray priceRangeArray = json
							.getJSONArray("priceRangeOriginal");
					int length = priceRangeArray.length();
					for (int i = 0; i < length; i++) {
						JSONArray objArray = priceRangeArray.getJSONArray(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put(objArray.getString(0), objArray.getString(1));
						LogUtil.writeCommodityLog("获取起批量：" + map);

						GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
						schema.setN_goods_id(goodsId);// 商品ID
						// 20150509tangbiao 需求要求起批量1件起
						if (i == 0) {
							schema.setN_num_start("1");// 开始数量
						} else {
							schema.setN_num_start(objArray.getString(0));// 开始数量
						}
						if (i < length - 1) {
							JSONArray objArray1 = priceRangeArray
									.getJSONArray(i + 1);
							schema.setN_num_end(objArray1.getString(0));// 结束数量
						} else {
							schema.setN_num_end("9999999");// 结束数量
						}
						schema.setN_price(JsoupUtil.priceFen(objArray
								.getString(1)));// 价格
						schema.setN_shop_id(shopId);// 卖家ID

						sectionList.add(schema);
						// try {
						// boolean redult = bo.addGoodsPriceSection(schema);//
						// 增加商品价格区间信息
						// LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
						// } catch (Exception e) {
						// logger.error("获取SKU和起批量异常：" + goodsId + " "
						// + shopId + " " + goodsName + " " + url, e);
						// }
					}
				}
				int state = 1;
				if (totalStock == 0) {
					state = 4;
				}
				goods.setN_total_stock(totalStock + "");
				goods.setC_goods_state(state + "");
				// boolean redult = bo.updateGoods(goodsId, defaultPrice,
				// totalStock + "", priceStr);// 修改商家商品信息
				// LogUtil.writeCommodityLog("修改商家商品信息：" + redult);
			} else {// sku不存在，只有价格区间的情况
				String totalStock = "0";// 总库存

				Elements priceEle = doc.select("[class=original-price]");
				if (priceEle == null || priceEle.size() == 0) {
					priceEle = doc.select("[class=price]");
				}
				if (priceEle != null && priceEle.size() > 0) {// 获得价格区间
					Elements tdEle = priceEle.get(0).select("td");
					int k = 0;
					for (int i = 0; i < tdEle.size(); i++) {
						String jsonStr = tdEle.get(i).attr("data-range");
						if (StringUtils.isNotEmpty(jsonStr)) {
							JSONObject json = new JSONObject(jsonStr);

							GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
							schema.setN_goods_id(goodsId);// 商品ID
							if (k == 0) {
								k = 1;
								schema.setN_num_start("1");// 开始数量
							} else {
								if (StringUtils.isNotEmpty(json
										.getString("begin"))) {
									schema.setN_num_start(json
											.getString("begin"));// 开始数量
								} else {
									schema.setN_num_start("1");// 开始数量
								}
							}
							if (StringUtils.isNotEmpty(json.getString("end"))) {
								schema.setN_num_end(json.getString("end"));// 结束数量
							} else {
								schema.setN_num_end("9999999");// 结束数量
							}

							schema.setN_price(JsoupUtil.priceFen(json
									.getString("price")));// 价格
							schema.setN_shop_id(shopId);// 卖家ID

							sectionList.add(schema);
							// try {
							// boolean redult = bo
							// .addGoodsPriceSection(schema);// 增加商品价格区间信息
							// LogUtil.writeCommodityLog("增加商品价格区间信息："
							// + redult);
							// } catch (Exception e) {
							// logger.error("获取SKU和起批量异常：" + goodsId + " "
							// + shopId + " " + goodsName + " " + url,
							// e);
							// }
						}
					}

					Elements stockEle = doc
							.select("[class=widget-custom offerdetail_ditto_purchasing]");
					Elements divEle = stockEle.select("div");
					String stockJson = divEle.attr("data-mod-config");
					if (StringUtils.isNotEmpty(stockJson)) {// 获得总库存
						JSONObject json = new JSONObject(stockJson);
						totalStock = json.getString("max");
					}

					int state = 1;
					if (totalStock.equals("0")) {
						state = 4;
					}
					goods.setN_total_stock(totalStock);
					goods.setC_goods_state(state + "");
					// boolean redult = bo.updateGoods(goodsId, defaultPrice,
					// totalStock, priceStr);// 修改商家商品信息
					// LogUtil.writeCommodityLog("sku不存在,修改商家商品信息：" + redult);

					GoodsSkuListSchema schema = new GoodsSkuListSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setN_shop_id(shopId);// 店铺ID
					schema.setC_sku_name(goodsName);// sku名称，该节点值的名称，如红色
					schema.setC_sku_prop("商品");// sku属性名，如颜色
					schema.setN_sku_level("0");// 层级，如0，1，2……
					schema.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

					List<GoodsSkuListSchema> skuLists = new ArrayList<GoodsSkuListSchema>();
					skuLists.add(schema);
					boolean addSkuListRedult = bo.addGoodsSkuList(goodsId, skuLists,null);
					// 增加商品SKU列表信息
					LogUtil
							.writeCommodityLog("增加商品SKU列表信息："
									+ addSkuListRedult);

					List<GoodsSkuListSchema> skuList = bo.getGoodsSkuListBygId(
							goodsId, 0);// 获取SKU列表信息
					GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
					schemaInfo.setC_sku_id(goodsId + "_0");// SKUID
					schemaInfo.setN_goods_id(goodsId);// 商品ID
					schemaInfo.setN_shop_id(shopId);// 店铺ID
					schemaInfo.setN_stock_num(totalStock);// 库存
					schemaInfo.setN_discount_price("0");// 折扣价格
					schemaInfo.setN_price("0");// 价格
					if (skuList != null && skuList.size() > 0) {
						schemaInfo.setC_sku_list_id(skuList.get(0).getN_id());// sku列表的ID集合
					}
					schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

					skuInfoList.add(schemaInfo);
					// boolean addSkuInfoRedult =
					// bo.addGoodsSkuInfo(schemaInfo);// 增加商品SKU详情信息
					// LogUtil
					// .writeCommodityLog("增加商品SKU详情信息："
					// + addSkuInfoRedult);
				}
			}
		} catch (Exception e) {
			logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId + " "
					+ goodsName + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取SKU和起批量异常：" + goodsId
					+ " " + shopId + " " + goodsName + " " + url, LogUtil
					.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取SKU和起批量GoodsUtil.skuAndPrice()...........end "
						+ goodsId + " " + shopId + " " + goodsName + " " + url);
	}

	public static void skuAndPriceUpdate(Document doc, String goodsId,
			String shopId, String goodsName, String url, GoodsSchema goods,
			List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsSkuInfoSchema> skuInfoUpdateList,
			List<GoodsPriceSectionSchema> sectionList) {

		LogUtil
				.writeCommodityLog("获取SKU和起批量GoodsUtil.skuAndPrice()...........start "
						+ goodsId + " " + shopId + " " + goodsName + " " + url);
		try {
			String jsonString = "";// json数据
			Elements javascript = doc.select("[type=text/javascript]");
			for (int i = 0; i < javascript.size(); i++) {
				Elements ele = javascript.get(i).getAllElements();
				String data = ele.html();
				if (data.indexOf("iDetailData") >= 0) {
					jsonString = data;
					break;
				}
			}
			CommodityBO bo = new CommodityBO();
			jsonString = jsonString
					.substring(jsonString.indexOf("iDetailData"));
			if (jsonString.indexOf(":") > -1) {// 存在sku的情况
				jsonString = jsonString.substring(jsonString.indexOf(":") + 1,
						jsonString.lastIndexOf("}"));

				Map<String, String> skuListMapBy = bo
						.getGoodsSkuListBygoodsId(goodsId);// map<sku名称,skuId>
				List<GoodsSkuListSchema> skuLists = new ArrayList<GoodsSkuListSchema>();// 新增的skulist
				List<String> oldskuListName = new ArrayList<String>();// 旧的skulistName
				// 解析json获得颜色和尺码
				java.util.List<String> colorList = new ArrayList<String>();// 颜色
				java.util.List<String> sizeList = new ArrayList<String>();// 尺码
				JSONObject json = new JSONObject(jsonString);
				JSONArray skuPropsArray = json.getJSONArray("skuProps");
				for (int i = 0; i < skuPropsArray.length(); i++) {
					JSONObject skuPropsChildJson = skuPropsArray
							.getJSONObject(i);
					String prop = skuPropsChildJson.getString("prop");
					JSONArray valueArray = skuPropsChildJson
							.getJSONArray("value");
					for (int j = 0; j < valueArray.length(); j++) {
						JSONObject childJson1 = valueArray.getJSONObject(j);
						if (childJson1.length() > 0) {
							String name = childJson1.getString("name");
							if (!skuListMapBy.containsKey(name)) {// 数据库中没有就添加
								GoodsSkuListSchema schema = new GoodsSkuListSchema();
								schema.setN_goods_id(goodsId);// 商品ID
								schema.setN_shop_id(shopId);// 店铺ID
								schema.setC_sku_name(name);// sku名称，该节点值的名称，如红色
								schema.setC_sku_prop(prop);// sku属性名，如颜色
								schema.setN_sku_level(i + "");// 层级，如0，1，2……
								schema.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

								skuLists.add(schema);
								// boolean redult = bo.addGoodsSkuList(schema);
								// // 增加商品SKU列表信息
								// LogUtil.writeCommodityLog("增加商品SKU列表信息："
								// + redult);
							} else {
								oldskuListName.add(name);
							}
							if (i == 0) {
								colorList.add(name);
							} else {
								sizeList.add(name);
							}
						}
					}
				}
				for (String str : oldskuListName) {// 过滤掉还存在的skulist(skuListMapBy里面还存在的数据要设置为无效)
					if (skuListMapBy.containsKey(str)) {
						skuListMapBy.remove(str);
					}
				}
				boolean addSkuListRedult = bo.addGoodsSkuList(goodsId, skuLists, skuListMapBy);
				// 增加商品SKU列表信息
				LogUtil.writeCommodityLog("增加商品SKU列表信息：" + addSkuListRedult);
				Map<String, String> skuListMap = bo
						.getGoodsSkuListBygoodsId(goodsId);// map<sku名称,skuId>
				Map<String, GoodsSkuInfoSchema> skuInfoMap = bo
						.getGoodsSkuInfoBygoodsId(goodsId);// Map<sku_list_id,skuInfo对象>
				int skuId = skuInfoMap.size();
				int totalStock = 0;// 总库存
				for (String key : skuInfoMap.keySet()) {
					GoodsSkuInfoSchema info = skuInfoMap.get(key);
					totalStock += Integer.parseInt(info.getN_stock_num());
				}
				// 获取sku的库存
				JSONObject skuMapJson = json.getJSONObject("skuMap");
				for (String color : colorList) {
					if (sizeList.size() > 0) {// 尺码和颜色的情况
						for (String size : sizeList) {
							String key = color + "&gt;" + size;
							if (!skuMapJson.isNull(key)) {// sku库存没有就不要
								String js = skuMapJson.getString(key);
								JSONObject json1 = new JSONObject(js);
								String stockNum = json1
										.getString("canBookCount");// 库存
								String price = "0";// 商品金额
								if (!json1.isNull("price")) {
									price = json1.getString("price");
								}
								LogUtil.writeCommodityLog(key + " 的库存："
										+ stockNum + " price:" + price);
								totalStock = totalStock
										+ Integer.parseInt(stockNum);
								String skuListId = skuListMap.get(color) + ";"
										+ skuListMap.get(size);
								if (!skuInfoMap.containsKey(skuListId)) {// 不存在就新增
									skuId = skuId + 1;
									GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
									schemaInfo.setC_sku_id(goodsId + "_"
											+ skuId);// SKUID
									schemaInfo.setN_goods_id(goodsId);// 商品ID
									schemaInfo.setN_shop_id(shopId);// 店铺ID
									schemaInfo.setN_stock_num(stockNum);// 库存
									schemaInfo.setN_discount_price("0");// 折扣价格
									schemaInfo.setN_price(JsoupUtil
											.priceFen(price));// 价格
									schemaInfo.setC_sku_list_id(skuListId);// sku列表的ID集合
									schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

									skuInfoList.add(schemaInfo);
									// boolean redult1 = bo
									// .addGoodsSkuInfo(schemaInfo);//
									// 增加商品SKU详情信息
									// LogUtil.writeCommodityLog("增加商品SKU详情信息："
									// + redult1);
								} else {// 存在就需要修改库存或价格
									GoodsSkuInfoSchema skuInfo = skuInfoMap
											.get(skuListId);
									totalStock -= Integer.parseInt(skuInfo
											.getN_stock_num());// 上面已经加过了阿里里面的库存
									if (skuInfo.getN_stock_num() != stockNum
											|| skuInfo.getN_price() != JsoupUtil
													.priceFen(price)) {// 库存不相等或者价格不相等的情况
										skuInfo.setN_stock_num(stockNum);
										skuInfo.setN_price(JsoupUtil
												.priceFen(price));
										skuInfoUpdateList.add(skuInfo);
										// boolean redult1 = bo
										// .updateGoodsSkuInfoById(
										// stockNum,
										// JsoupUtil
										// .priceFen(price),
										// skuInfo.getN_id());// 修改商品SKU详情信息
										// LogUtil
										// .writeCommodityLog("修改商品SKU详情信息："
										// + redult1);
									}
								}
							}
						}
					} else {// 没有尺码的情况（可能是没有颜色）
						if (!skuMapJson.isNull(color)) {// sku库存没有就不要
							String js = skuMapJson.getString(color);
							JSONObject json1 = new JSONObject(js);
							String stockNum = json1.getString("canBookCount");
							String price = "0";
							if (!json1.isNull("price")) {
								price = json1.getString("price");
							}
							LogUtil.writeCommodityLog(color + " 的库存："
									+ stockNum + " price:" + price);
							totalStock = totalStock
									+ Integer.parseInt(stockNum);

							String skuListId = skuListMap.get(color);
							if (!skuInfoMap.containsKey(skuListId)) {// 不存在就新增
								skuId = skuId + 1;
								GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
								schemaInfo.setC_sku_id(goodsId + "_" + skuId);// SKUID
								schemaInfo.setN_goods_id(goodsId);// 商品ID
								schemaInfo.setN_shop_id(shopId);// 店铺ID
								schemaInfo.setN_stock_num(stockNum);// 库存
								schemaInfo.setN_discount_price("0");// 折扣价格
								schemaInfo
										.setN_price(JsoupUtil.priceFen(price));// 价格
								schemaInfo.setC_sku_list_id(skuListMap
										.get(color));// sku列表的ID集合
								schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

								skuInfoList.add(schemaInfo);
								// boolean redult1 = bo
								// .addGoodsSkuInfo(schemaInfo);// 增加商品SKU详情信息
								// LogUtil.writeCommodityLog("增加商品SKU详情信息："
								// + redult1);
							} else {// 存在就需要修改库存或价格
								GoodsSkuInfoSchema skuInfo = skuInfoMap
										.get(skuListId);
								totalStock -= Integer.parseInt(skuInfo
										.getN_stock_num());// 上面已经加过了阿里里面的库存
								if (skuInfo.getN_stock_num() != stockNum
										|| skuInfo.getN_price() != JsoupUtil
												.priceFen(price)) {// 库存不相等或者价格不相等的情况
									skuInfo.setN_stock_num(stockNum);
									skuInfo.setN_price(JsoupUtil
											.priceFen(price));
									skuInfoUpdateList.add(skuInfo);
									// boolean redult1 = bo
									// .updateGoodsSkuInfoById(stockNum,
									// JsoupUtil.priceFen(price),
									// skuInfo.getN_id());// 修改商品SKU详情信息
									// LogUtil.writeCommodityLog("修改商品SKU详情信息："
									// + redult1);
								}
							}
						}

					}
				}
				// 获取起批量
				if (json.isNull("priceRangeOriginal")) {// 起批量如果为空
					GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setN_num_start("1");// 开始数量
					schema.setN_num_end("9999999");// 结束数量
					schema.setN_price(goods.getN_sell_price());// 价格
					schema.setN_shop_id(shopId);// 卖家ID

					sectionList.add(schema);
					// try {
					// boolean redult = bo.addGoodsPriceSection(schema);//
					// 增加商品价格区间信息
					// LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
					// } catch (Exception e) {
					// logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId
					// + " " + goodsName + " " + url, e);
					// }
				} else {
					JSONArray priceRangeArray = json
							.getJSONArray("priceRangeOriginal");
					int length = priceRangeArray.length();
					for (int i = 0; i < length; i++) {
						JSONArray objArray = priceRangeArray.getJSONArray(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put(objArray.getString(0), objArray.getString(1));
						LogUtil.writeCommodityLog("获取起批量：" + map);

						GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
						schema.setN_goods_id(goodsId);// 商品ID
						// 20150509tangbiao 需求要求起批量1件起
						if (i == 0) {
							schema.setN_num_start("1");// 开始数量
						} else {
							schema.setN_num_start(objArray.getString(0));// 开始数量
						}
						if (i < length - 1) {
							JSONArray objArray1 = priceRangeArray
									.getJSONArray(i + 1);
							schema.setN_num_end(objArray1.getString(0));// 结束数量
						} else {
							schema.setN_num_end("9999999");// 结束数量
						}
						schema.setN_price(JsoupUtil.priceFen(objArray
								.getString(1)));// 价格
						schema.setN_shop_id(shopId);// 卖家ID
						sectionList.add(schema);
						// try {
						// boolean redult = bo.addGoodsPriceSection(schema);//
						// 增加商品价格区间信息
						// LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
						// } catch (Exception e) {
						// logger.error("获取SKU和起批量异常：" + goodsId + " "
						// + shopId + " " + goodsName + " " + url, e);
						// }
					}
				}
				int state = 1;
				if (totalStock == 0) {
					state = 4;
				}
				goods.setN_total_stock(totalStock + "");
				goods.setC_goods_state(state + "");
				// boolean redult = bo.updateGoods(goodsId, defaultPrice,
				// totalStock + "", priceStr);// 修改商家商品信息
				// LogUtil.writeCommodityLog("修改商家商品信息：" + redult);
			} else {// sku不存在，只有价格区间的情况
				String totalStock = "0";// 总库存

				Elements priceEle = doc.select("[class=original-price]");
				if (priceEle == null || priceEle.size() == 0) {
					priceEle = doc.select("[class=price]");
				}
				if (priceEle != null && priceEle.size() > 0) {// 获得价格区间
					Elements tdEle = priceEle.get(0).select("td");
					int k = 0;
					for (int i = 0; i < tdEle.size(); i++) {
						String jsonStr = tdEle.get(i).attr("data-range");
						if (StringUtils.isNotEmpty(jsonStr)) {
							JSONObject json = new JSONObject(jsonStr);

							GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
							schema.setN_goods_id(goodsId);// 商品ID
							if (k == 0) {
								k = 1;
								schema.setN_num_start("1");// 开始数量
							} else {
								if (StringUtils.isNotEmpty(json
										.getString("begin"))) {
									schema.setN_num_start(json
											.getString("begin"));// 开始数量
								} else {
									schema.setN_num_start("1");// 开始数量
								}
							}
							if (StringUtils.isNotEmpty(json.getString("end"))) {
								schema.setN_num_end(json.getString("end"));// 结束数量
							} else {
								schema.setN_num_end("9999999");// 结束数量
							}
							schema.setN_price(JsoupUtil.priceFen(json
									.getString("price")));// 价格
							schema.setN_shop_id(shopId);// 卖家ID

							sectionList.add(schema);
							// try {
							// boolean redult = bo
							// .addGoodsPriceSection(schema);// 增加商品价格区间信息
							// LogUtil.writeCommodityLog("增加商品价格区间信息："
							// + redult);
							// } catch (Exception e) {
							// logger.error("获取SKU和起批量异常：" + goodsId + " "
							// + shopId + " " + goodsName + " " + url,
							// e);
							// }
						}
					}

					Elements stockEle = doc
							.select("[class=widget-custom offerdetail_ditto_purchasing]");
					Elements divEle = stockEle.select("div");
					String stockJson = divEle.attr("data-mod-config");
					if (StringUtils.isNotEmpty(stockJson)) {// 获得总库存
						JSONObject json = new JSONObject(stockJson);
						totalStock = json.getString("max");
					}

					int state = 1;
					if (totalStock.equals("0")) {
						state = 4;
					}
					goods.setN_total_stock(totalStock);
					goods.setC_goods_state(state + "");
					// boolean redult = bo.updateGoods(goodsId, defaultPrice,
					// totalStock, priceStr);// 修改商家商品信息
					// LogUtil.writeCommodityLog("sku不存在,修改商家商品信息：" + redult);
				}
			}
		} catch (Exception e) {
			logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId + " "
					+ goodsName + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取SKU和起批量异常：" + goodsId
					+ " " + shopId + " " + goodsName + " " + url, LogUtil
					.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取SKU和起批量GoodsUtil.skuAndPrice()...........end "
						+ goodsId + " " + shopId + " " + goodsName + " " + url);
	}

	/**
	 * 设置商品标准类目ID
	 * 
	 * @param doc
	 *            详情页doc
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            商品详情页地址
	 */
	public static void setGoodsCategoryId(Document doc, String url,
			GoodsSchema goods) {
		LogUtil
				.writeCommodityLog("获取商品标准类目ID,GoodsUtil.goodsCategoryId()...........start "
						+ url);
		try {
			String jsonString = "";// json数据
			Elements javascript = doc.select("[type=text/javascript]");
			for (int i = 0; i < javascript.size(); i++) {
				Elements ele = javascript.get(i).getAllElements();
				String data = ele.html();
				if (data.indexOf("iDetailConfig") >= 0) {
					jsonString = data;
					break;
				}
			}
			try {
				jsonString = jsonString.substring(jsonString
						.indexOf("iDetailConfig"), jsonString
						.indexOf("iDetailData"));
			} catch (Exception e) {// 这种情况可能是资源加载失败，再次重新获取
				String companyName = JsoupUtil.commodityUrl(doc, url);// 公司名
				if (!companyName.substring(0, 3).equals("404")) {
					Document doc1 = JsoupUtil.getDocument(url);
					setGoodsCategoryId(doc1, url, goods);
				}
			}
			jsonString = jsonString.substring(jsonString.indexOf("=") + 1,
					jsonString.lastIndexOf(";"));
			JSONObject json = new JSONObject(jsonString);
			String catid = json.getString("catid");// 分类id
			if (StringUtils.isNotEmpty(catid)) {
				String pcatid = json.getString("parentdcatid");// 分类父id
				String unit = json.getString("unit");// 商品单位
				String refPrice = json.getString("refPrice");// 商品金额
				// String n_ownner_sell = json.getString("beginAmount");//
				// SKU起批量

				CommodityBO bo = new CommodityBO();
				if (StringUtils.isNotEmpty(pcatid)) {
					CfgSysCategorySchema cat = bo
							.getCfgSysCategorySchemaByCatId(pcatid);
					if (cat == null) {// 可能在分类表中不存在，需要用catid查出pcatid
						cat = bo.getCfgSysCategorySchemaByCatId(catid);
						if (cat != null) {
							pcatid = cat.getN_parent_id();
						}
					}
				} else {
					CfgSysCategorySchema cat = bo
							.getCfgSysCategorySchemaByCatId(catid);
					if (cat != null) {
						pcatid = cat.getN_parent_id();
					}
				}
				goods.setN_sys_cid(catid);
				goods.setN_sys_parent_cid(StringUtils.isEmpty(pcatid) ? catid
						: pcatid);
				goods.setC_goods_unit(unit);
				goods.setC_price(refPrice);
				goods.setN_sell_price(JsoupUtil.priceFen(refPrice));
				// goods.setN_ownner_sell(n_ownner_sell);//
				// 用自己的销量存一下起批量，只是在业务中临时用一下
			}
		} catch (Exception e) {
			logger.error("获取商品标准类目ID异常：" + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取商品标准类目ID异常：" + url,
					LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("获取商品标准类目ID,GoodsUtil.goodsCategoryId()...........end "
						+ url);
	}

	/**
	 * 商品爬取图文详情(所有)
	 * 
	 * @param url
	 *            商品列表地址
	 * @throws Exception
	 */
	public static void goodsInfoHtml(String shopId, String url, int num,
			int totalCount) throws Exception {
		LogUtil
				.writeCommodityLog("商品爬取图文详情(所有)GoodsUtil.goodsInfoHtml()...........start "
						+ shopId
						+ " "
						+ url
						+ "&pageNum="
						+ num
						+ "&totalCount=" + totalCount);

		Document doc = JsoupUtil.getDocument(url + "&pageNum=" + num);

		Elements divEle = doc
				.select("[data-tracelog-exp=wp_widget_offerhand_main_disp]");
		if (divEle.size() == 0) {
			divEle = doc.select("[class=wp-offerlist-catalogs]");
		}
		int count = totalCount;// 总页数
		if (count == 1) {
			Elements pageCountEle = doc.select("[class=page-count]");
			String pageCount = pageCountEle.text();// 总页数
			if (StringUtils.isNotEmpty(pageCount)) {
				count = Integer.parseInt(pageCount);
			}
		}
		Elements liEle = divEle.select("li");
		LogUtil.writeCommodityLog(url + "&pageNum=" + num + "商品爬取(所有):"
				+ liEle.size());
		CommodityBO bo = new CommodityBO();
		for (int i = 0; i < liEle.size(); i++) {
			Elements countsEle = liEle.get(i).select("[class=booked-counts]");
			if (countsEle.size() == 0) {
				countsEle = liEle.get(i).select("[class=booked-count]");
			}
			Elements imageEle = liEle.get(i).select("[class=image]");
			Elements aEle = imageEle.select("a");
			String counts = "0";
			if (countsEle.size() > 0) {
				counts = countsEle.get(0).text();
			}
			if (aEle.size() > 0) {
				try {
					String href = aEle.attr("href");
					LogUtil.writeCommodityLog(url + "&pageNum= " + num + " 第 "
							+ i + " 条商品详情页地址：" + href + " 成交量：" + counts);
					String cThirdPlatformId = href.substring(href
							.lastIndexOf("/") + 1, href.lastIndexOf("."));
					GoodsSchema info = bo.getGoodsByformId(shopId,
							cThirdPlatformId);// 根据第三方平台商品ID查询
					if (info != null) {
						List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
						Document doc1 = JsoupUtil.getDocument(href);
						htmlInfo(doc1, cThirdPlatformId, shopId, info
								.getN_goods_id(), infoImageList);
						if (infoImageList.size() > 0) {
							bo.updateGoodsInfoImageList(info, infoImageList);
						}
					}
				} catch (Exception e) {
					logger.error("商品爬取图文详情(所有)异常", e);
				}
			}
		}

		LogUtil
				.writeCommodityLog("商品爬取图文详情(所有)GoodsUtil.goodsInfoHtml()...........end "
						+ shopId + " " + url + "&pageNum=" + num);
		// int count = 3;
		if (num < count) {
			int i = num + 1;
			goodsInfoHtml(shopId, url, i, count);
		} else {
			Elements offerCountEle = doc.select("[class=offer-count]");
			String offerCount = offerCountEle.text();// 总条数
			if (num == 1) {
				LogUtil.writeCommodityLog(shopId + " 店铺的商品总条数：" + offerCount);
			}
		}
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * goodsInfoHtml("16440315",
	 * "http://qisiduo.1688.com/page/offerlist.htm?sortType=timeup", 1, 1); //
	 * String url = "http://detail.1688.com/offer/524219026482.html"; //
	 * Document doc = JsoupUtil.getDocument(url); // htmlInfo(doc,
	 * "524219026482", "1", "1", // new ArrayList<GoodsInfoImageSchema>()); }
	 */

	/**
	 * 获取详情页商品介绍并创建html文件
	 * 
	 * @param doc
	 *            详情页doc
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public static String htmlInfo(Document doc, String cThirdPlatformId,
			String shopId, String goodsId,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		LogUtil
				.writeCommodityLog("获取详情页商品介绍并创建html文件GoodsUtil.htmlInfo()...........start "
						+ cThirdPlatformId + " " + shopId + " " + goodsId);

		long begin = java.lang.System.currentTimeMillis(); // 开始时间
		GoodsUtil.goodsInfoImg(doc, cThirdPlatformId, shopId, goodsId, infoImageList);//获取图片
		String pathUrl = "";
		Elements htmlEle = doc.select("[id=de-description-detail]");
		Elements imgEle = htmlEle.select("img");
		Elements htmlEle1 = doc
				.select("[data-widget-name=offerdetail_easyoffer_dsc]");
		String div = htmlEle.toString() + htmlEle1.toString();// 获取商品介绍信息
		long end = java.lang.System.currentTimeMillis(); // 结束时间
		String html = bodyStart() + div + bodyEnd();
		String htmlUrl = "/html/" + shopId + "/" + cThirdPlatformId + ".html";
		boolean flag = file(html, shopId, htmlUrl);// 创建html文件
		if (flag) {
			pathUrl = htmlUrl;
		} else {
			pathUrl = "";
		}
		LogUtil
				.writeCommodityLog("获取详情页商品介绍并创建html文件GoodsUtil.htmlInfo()...........end "
						+ cThirdPlatformId
						+ " "
						+ shopId
						+ " "
						+ goodsId
						+ " 图片数量："
						+ imgEle.size()
						+ " 耗时："
						+ (end - begin)
						+ " " + pathUrl);
		return pathUrl;
	}
	public static void goodsInfoImg(Document doc, String cThirdPlatformId,
			String shopId, String goodsId,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		LogUtil
				.writeCommodityLog("获取详情页图片GoodsUtil.goodsInfoImg()...........start "
						+ cThirdPlatformId + " " + shopId + " " + goodsId);

		Elements htmlEle = doc.select("[id=de-description-detail]");
		Elements imgEle = htmlEle.select("img");
		// CommodityBO bo = new CommodityBO();
		for (int i = 0; i < imgEle.size(); i++) {// 存储详情页中的图片
			try {
				String src = imgEle.get(i).attr("src");// 图片地址
				if (StringUtils.isNotEmpty(src)) {
					GoodsInfoImageSchema schema = new GoodsInfoImageSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setN_shop_id(shopId);// 卖家ID
					schema.setC_img_url(src);// 阿里的绝对地址
					schema.setN_img_type("1");// 图片类型：1,普通图片;2,二维码图片

					infoImageList.add(schema);
					// bo.addGoodsInfoImage(schema);// 增加商品详情图片信息
				}
			} catch (Exception e) {
			}
		}
		Elements htmlEle1 = doc
				.select("[data-widget-name=offerdetail_easyoffer_dsc]");
		Elements imgEle1 = htmlEle1.select("img");
		for (int i = 0; i < imgEle1.size(); i++) {// 存储详情页中的图片
			try {
				String src = imgEle1.get(i).attr("src");// 图片地址
				if (StringUtils.isNotEmpty(src)) {
					GoodsInfoImageSchema schema = new GoodsInfoImageSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setN_shop_id(shopId);// 卖家ID
					schema.setC_img_url(src);// 阿里的绝对地址
					schema.setN_img_type("1");// 图片类型：1,普通图片;2,二维码图片

					infoImageList.add(schema);
					// bo.addGoodsInfoImage(schema);// 增加商品详情图片信息
				}
			} catch (Exception e) {
			}
		}
	}
	public static void tbGoodsInfoImg(Document doc, String cThirdPlatformId,
			String shopId, String goodsId) throws Exception {
		LogUtil
				.writeCommodityLog("获取淘宝详情页图片GoodsUtil.tbGoodsInfoImg()...........start "
						+ cThirdPlatformId + " " + shopId + " " + goodsId);

		Elements imgEle = doc.select("img");
		CommodityBO bo = new CommodityBO();
		bo.deleteGoodsInfoImageById(goodsId, shopId);
		for (int i = 0; i < imgEle.size(); i++) {// 存储详情页中的图片
			try {
				String src = imgEle.get(i).attr("src");// 图片地址
				if (StringUtils.isNotEmpty(src)) {
					GoodsInfoImageSchema schema = new GoodsInfoImageSchema();
					schema.setN_goods_id(goodsId);// 商品ID
					schema.setN_shop_id(shopId);// 卖家ID
					schema.setC_img_url(src);// 阿里的绝对地址
					schema.setN_img_type("1");// 图片类型：1,普通图片;2,二维码图片
					bo.addGoodsInfoImage(schema);// 增加商品详情图片信息
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 开线程检查店铺的所有商品详情文件中是否有二维码图
	 * 
	 * @param shopId
	 */
	public static void goodsInfoFileCheckTake(String shopId) {
		LogUtil
				.writeCommodityLog("开线程检查店铺的所有商品详情文件中是否有二维码图GoodsUtil.goodsInfoFileCheckTake()...........start "
						+ shopId);
		ThreadPoolExecutor executor = com.framework.base.ThreadPool
				.getInstance().getThread();
		executor.execute(new GoodsInfoFileCheckTake(shopId));
		LogUtil
				.writeCommodityLog("开线程检查店铺的所有商品详情文件中是否有二维码图GoodsUtil.goodsInfoFileCheckTake()...........end "
						+ shopId);
	}

	/**
	 * 检查店铺商品详情页是否包含二维码图，且过滤二维码图
	 * 
	 * @param shopId店铺ID
	 * @param isAli是否抓阿里的页面（==1是抓阿里的页面）
	 * @param isimgcheck是否要检查二维码图片（==1检查二维码图）
	 * @param num检查图片数量（不填或者填0检查所有图片）
	 * @param imgurlcheck二维码图片地址（这是知道二维码地址的情况下做过滤）
	 * @throws Exception
	 */
	public static void htmlInfoZxing(String shopId, String isAli,
			String isimgcheck, String num, String imgurlcheck) throws Exception {
		LogUtil
				.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图GoodsUtil.htmlInfoZxing()...........start shopid:"
						+ shopId
						+ " isali:"
						+ isAli
						+ " isimgcheck:"
						+ isimgcheck
						+ " num:"
						+ num
						+ " imgurlcheck："
						+ imgurlcheck);
		CommodityBO bo = new CommodityBO();
		List<GoodsSchema> goodsList = bo.getGoodsByshopId(shopId);
		for (int k = 0; k < goodsList.size(); k++) {
			GoodsSchema goods = goodsList.get(k);
			String goodsId = goods.getN_goods_id();
			String cThirdPlatformId = goods.getC_third_platform_id();
			LogUtil
					.writeCommodityLog("GoodsUtil.htmlInfoZxing()...........start "
							+ cThirdPlatformId + " " + shopId + " " + goodsId);

			long begin = java.lang.System.currentTimeMillis(); // 开始时间
			Document doc = null;
			if (StringUtils.isNotEmpty(isAli) && isAli.equals("1")) {
				doc = JsoupUtil.getDocument("http://detail.1688.com/offer/"
						+ cThirdPlatformId + ".html");
			} else {
				String fileUrl = StartContent.getInstance().getWeigouIp()
						+ "/html/" + shopId + "/" + cThirdPlatformId + ".html";
				String htmlXml = JsoupUtil.conUrl(fileUrl, false, "utf-8", "",
						0);
				doc = Jsoup.parse(htmlXml);
			}

			String pathUrl = "";
			Elements htmlEle = doc.select("[id=de-description-detail]");
			Elements imgEle = htmlEle.select("img");
			List<String> imgList = new ArrayList<String>();// 存放二维码图片对应的html
			List<String> imgSrcList = new ArrayList<String>();// 存放二维码图片地址
			if (StringUtils.isNotEmpty(isimgcheck) && isimgcheck.equals("1")) {
				int numInt = imgEle.size();
				if (StringUtils.isNotEmpty(num) && !num.equals("0")) {
					numInt = Integer.parseInt(num);
				}
				for (int i = 0; i < imgEle.size(); i++) {// 存储详情页中的图片
					if (i < numInt) {
						try {
							String src = imgEle.get(i).attr("src");// 图片地址
							if (StringUtils.isNotEmpty(imgurlcheck)) {// 已知二维码图片地址的情况
								if (src.substring(src.lastIndexOf("/")).equals(
										imgurlcheck.substring(imgurlcheck
												.lastIndexOf("/")))) {
									LogUtil
											.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 已知图片地址src："
													+ shopId
													+ " "
													+ src
													+ "是否为二维码图 true");
									imgList.add(imgEle.get(i).toString());// 存放二维码图片对应的html
								} else {
									LogUtil
											.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... src："
													+ shopId
													+ " "
													+ src
													+ "是否为二维码图 false");
								}
							} else {
								if (k < 100) {// 前100个商品都把图片下载下来检查二维码图
									if (StringUtils.isNotEmpty(decode(shopId,
											src))) {// 检查是否为二维码图
										LogUtil
												.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 前100个商品图片地址src："
														+ shopId
														+ " "
														+ src
														+ "是否为二维码图 true");
										imgSrcList.add(src);// 存放二维码图片地址
										imgList.add(imgEle.get(i).toString());// 存放二维码图片对应的html

										String fileName = src.substring(src
												.lastIndexOf("/") + 1, src
												.lastIndexOf("."));// 截取图片名称
										LogUtil
												.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 前100个商品图片地址src："
														+ shopId
														+ " 删除图片"
														+ fileName);
										bo.deleteGoodsInfoImageByUrl(shopId,
												fileName);// 删除商品详情图片信息
										GoodsInfoImageSchema infoImage = bo
												.getGoodsInfoImageByUrl(shopId,
														"2", fileName);// 查询详情图片(二维码图)
										if (infoImage == null) {
											// 存放二维码图片地址
											GoodsInfoImageSchema schema = new GoodsInfoImageSchema();
											schema.setN_goods_id(goodsId);// 商品ID
											schema.setN_shop_id(shopId);// 卖家ID
											schema.setC_img_url(src);// 阿里的绝对地址
											schema.setN_img_type("2");// 图片类型：1,普通图片;2,二维码图片
											bo.addGoodsInfoImage(schema);// 增加商品详情图片信息
										}

									} else {
										LogUtil
												.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 前100个商品图片地址src："
														+ shopId
														+ " "
														+ src
														+ "是否为二维码图 false");
									}
								} else {
									LogUtil
											.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 二维码图数量："
													+ shopId
													+ " "
													+ imgSrcList.size());
									boolean isZxing = false;// 是否为二维码图
									String srcName = src.substring(src
											.lastIndexOf("/"));
									for (String imgSrc : imgSrcList) {// 检测是否为二维码图片
										String imgSrcName = imgSrc
												.substring(imgSrc
														.lastIndexOf("/"));
										if (srcName.equals(imgSrcName)) {
											isZxing = true;
											break;
										}
									}
									if (isZxing) {
										LogUtil
												.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 后100个商品图片地址src："
														+ shopId
														+ " "
														+ src
														+ "是否为二维码图 true");
										imgList.add(imgEle.get(i).toString());// 存放二维码图片对应的html
									} else {
										LogUtil
												.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 后100个商品图片地址src："
														+ shopId
														+ " "
														+ src
														+ "是否为二维码图 false");
									}
								}
							}
						} catch (Exception e) {
							logger.error(
									"GoodsUtil.htmlInfoZxing()........... 检查二维码出错："
											+ shopId, e);
						}
					}
				}
			}
			LogUtil
					.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... 检查到的二维码图数量："
							+ shopId + " " + imgList.size());
			if (imgList.size() > 0) {
				String div = htmlEle.toString();// 获取商品介绍信息
				for (String img : imgList) {// 去除二维码图片地址
					div = div.replace(img, "");
				}
				String html = GoodsUtil.bodyStart() + div + GoodsUtil.bodyEnd();
				String htmlUrl = "/html/" + shopId + "/" + cThirdPlatformId
						+ ".html";
				boolean flag = GoodsUtil.file(html, shopId, htmlUrl);// 创建html文件
				if (flag) {
					pathUrl = htmlUrl;
				} else {
					pathUrl = "";
				}
			}
			long end = java.lang.System.currentTimeMillis(); // 结束时间
			LogUtil
					.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图GoodsUtil.htmlInfoZxing()...........end "
							+ cThirdPlatformId
							+ " "
							+ shopId
							+ " "
							+ goodsId
							+ " 图片数量："
							+ imgEle.size()
							+ " 二维码图片数量："
							+ imgList.size()
							+ " 耗时："
							+ (end - begin)
							+ " "
							+ pathUrl);
		}
	}

	/**
	 * 开线程检查店铺的上新商品详情文件中是否有二维码图
	 * 
	 * @param shopId
	 */
	public static void goodsInfoNewFileCheckTake(String shopId,
			List<String> cThirdPlatformIdList) {
		LogUtil
				.writeCommodityLog("开线程检查店铺的所有商品详情文件中是否有二维码图GoodsUtil.goodsInfoNewFileCheckTake()...........start "
						+ shopId);
		ThreadPoolExecutor executor = com.framework.base.ThreadPool
				.getInstance().getThread();
		executor.execute(new GoodsInfoNewFileCheckTake(shopId,
				cThirdPlatformIdList));
		LogUtil
				.writeCommodityLog("开线程检查店铺的所有商品详情文件中是否有二维码图GoodsUtil.goodsInfoNewFileCheckTake()...........end "
						+ shopId);
	}

	/**
	 * 检查店铺商品详情页是否包含二维码图，且过滤二维码图(上新)
	 * 
	 * @param shopId店铺ID
	 * @param cThirdPlatformId阿里商品id
	 * @throws Exception
	 */
	public static void htmlInfoZxingByNew(String shopId,
			List<String> cThirdPlatformIdList) throws Exception {
		LogUtil
				.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图(上新)GoodsUtil.htmlInfoZxingByNew()...........start shopid:"
						+ shopId
						+ " cThirdPlatformIdList.size():"
						+ cThirdPlatformIdList.size());
		CommodityBO bo = new CommodityBO();
		List<GoodsInfoImageSchema> infoImageList = bo
				.getGoodsInfoImageByshopId(shopId, "2");// 查询详情图片（二维码图）
		LogUtil
				.writeCommodityLog("GoodsUtil.htmlInfoZxingByNew()........... 二维码图数量："
						+ shopId + " " + infoImageList.size());

		for (String cThirdPlatformId : cThirdPlatformIdList) {
			LogUtil
					.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图(上新)GoodsUtil.htmlInfoZxingByNew()...........start shopid:"
							+ shopId + " cThirdPlatformId:" + cThirdPlatformId);
			long begin = java.lang.System.currentTimeMillis(); // 开始时间
			String fileUrl = StartContent.getInstance().getWeigouIp()
					+ "/html/" + shopId + "/" + cThirdPlatformId + ".html";
			String htmlXml = JsoupUtil.conUrl(fileUrl, false, "utf-8", "", 0);
			Document doc = Jsoup.parse(htmlXml);// 从服务器获得文件数据

			String pathUrl = "";
			Elements htmlEle = doc.select("[id=de-description-detail]");
			Elements imgEle = htmlEle.select("img");
			List<String> imgList = new ArrayList<String>();// 存放二维码图片对应的html
			for (int i = 0; i < imgEle.size(); i++) {// 存储详情页中的图片
				try {
					String src = imgEle.get(i).attr("src");// 图片地址
					boolean isZxing = false;// 是否为二维码图
					String srcName = src.substring(src.lastIndexOf("/"));
					for (GoodsInfoImageSchema infoImage : infoImageList) {// 检测是否为二维码图片
						String imgSrcName = infoImage.getC_img_url().substring(
								infoImage.getC_img_url().lastIndexOf("/"));
						if (srcName.equals(imgSrcName)) {
							isZxing = true;
							break;
						}
					}
					if (isZxing) {
						LogUtil
								.writeCommodityLog("GoodsUtil.htmlInfoZxingByNew()........... src："
										+ shopId + " " + src + "是否为二维码图 true");
						imgList.add(imgEle.get(i).toString());// 存放二维码图片对应的html
					} else {
						LogUtil
								.writeCommodityLog("GoodsUtil.htmlInfoZxingByNew()........... src："
										+ shopId + " " + src + "是否为二维码图 false");
					}
				} catch (Exception e) {
					logger.error(
							"GoodsUtil.htmlInfoZxing()........... 检查二维码图出错："
									+ shopId, e);
				}
			}
			LogUtil
					.writeCommodityLog("GoodsUtil.htmlInfoZxingByNew()........... 检查到的二维码图数量："
							+ shopId + " " + imgList.size());
			if (imgList.size() > 0) {
				String div = htmlEle.toString();// 获取商品介绍信息
				for (String img : imgList) {// 去除二维码图片地址
					div = div.replace(img, "");
				}
				String html = GoodsUtil.bodyStart() + div + GoodsUtil.bodyEnd();
				String htmlUrl = "/html/" + shopId + "/" + cThirdPlatformId
						+ ".html";
				boolean flag = GoodsUtil.file(html, shopId, htmlUrl);// 创建html文件
				if (flag) {
					pathUrl = htmlUrl;
				} else {
					pathUrl = "";
				}
			}
			long end = java.lang.System.currentTimeMillis(); // 结束时间
			LogUtil
					.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图(上新)GoodsUtil.htmlInfoZxingByNew()...........end "
							+ cThirdPlatformId
							+ " "
							+ shopId
							+ " "
							+ " 图片数量："
							+ imgEle.size()
							+ " 二维码图片数量："
							+ imgList.size()
							+ " 耗时：" + (end - begin) + " " + pathUrl);
		}
		LogUtil
				.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图(上新)GoodsUtil.htmlInfoZxingByNew()...........end shopid:"
						+ shopId
						+ " cThirdPlatformIdList.size():"
						+ cThirdPlatformIdList.size());
	}

	/**
	 * 解二维码图
	 * 
	 * @param imagePath
	 * @return
	 */
	private static String decode(String shopId, String imagePath) {
		LogUtil.writeCommodityLog("解二维码图GoodsUtil.decode()...........start "
				+ shopId + " " + imagePath);
		String contents = null;
		if (StringUtils.isEmpty(imagePath) || imagePath.indexOf(".gif") != -1) {// 图片地址为空或者为gif格式的图都直接返回空
			return "";
		}

		MultiFormatReader formatReader = new MultiFormatReader();
		String url = "";
		File file = null;
		try {
			url = imgFile(shopId, imagePath);// 上传图片
			if (StringUtils.isNotEmpty(url)) {
				file = new File(url);
				BufferedImage image = ImageIO.read(file);

				// 将图像数据转换为1 bit data
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				Binarizer binarizer = new HybridBinarizer(source);
				// BinaryBitmap是ZXing用来表示1 bit data位图的类，Reader对象将对它进行解析
				BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

				Map hints = new HashMap();
				hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

				// 对图像进行解码
				Result result = formatReader.decode(binaryBitmap, hints);
				contents = result.toString();
				// System.out.println("barcode encoding format :\t "
				// + result.getBarcodeFormat());
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			logger.error("解二维码图失败:" + imagePath + " 服务器地址:" + url, e);
		}
		if(file != null){
			file.delete();// 删除文件
		}
		LogUtil.writeCommodityLog("解二维码图GoodsUtil.decode()...........end "
				+ shopId + " " + imagePath + " " + contents);

		return contents;
	}

	/**
	 * 检查店铺商品详情页是否包含二维码图，且过滤二维码图
	 * 
	 * @param doc
	 *            详情页doc
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public static void htmlInfoZxingOne(String shopId, String cThirdPlatformId,
			String imgurlcheck) throws Exception {
		LogUtil
				.writeCommodityLog("检查店铺商品详情页是否包含二维码图，且过滤二维码图GoodsUtil.htmlInfoZxingOne()...........start "
						+ shopId);
		LogUtil
				.writeCommodityLog("GoodsUtil.htmlInfoZxingOne()...........start "
						+ cThirdPlatformId + " " + shopId);

		long begin = java.lang.System.currentTimeMillis(); // 开始时间
		Document doc = JsoupUtil.getDocument("http://detail.1688.com/offer/"
				+ cThirdPlatformId + ".html");

		String pathUrl = "";
		Elements htmlEle = doc.select("[id=de-description-detail]");
		Elements imgEle = htmlEle.select("img");
		List<String> imgList = new ArrayList<String>();
		boolean pic360 = true;// 360规格图是否存在的标识
		int numInt = imgEle.size();
		for (int i = 0; i < imgEle.size(); i++) {// 存储详情页中的图片
			try {
				String src = imgEle.get(i).attr("src");// 图片地址
				if (StringUtils.isNotEmpty(imgurlcheck)) {
					if (src.subSequence(src.lastIndexOf("/"), src.length())
							.equals(
									imgurlcheck.subSequence(imgurlcheck
											.lastIndexOf("/"), imgurlcheck
											.length()))) {
						LogUtil
								.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... src："
										+ src + " true");
						imgList.add(imgEle.get(i).toString());
					} else {
						LogUtil
								.writeCommodityLog("GoodsUtil.htmlInfoZxing()........... src："
										+ src + " false");
					}
				}
			} catch (Exception e) {
			}
		}
		String div = htmlEle.toString();// 获取商品介绍信息
		String html = GoodsUtil.bodyStart() + div + GoodsUtil.bodyEnd();
	}

	/**
	 * 内容写入到html文件
	 * 
	 * @param html
	 *            存储内容
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public static boolean file(String html, String shopId, String htmlUrl)
			throws Exception {
		LogUtil
				.writeCommodityLog("内容写入到html文件GoodsUtil.file()...........start "
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
		LogUtil.writeCommodityLog("内容写入到html文件GoodsUtil.file()...........end "
				+ flag);
		return flag;
	}

	/**
	 * html头部
	 * 
	 * @return
	 */
	public static String bodyStart() {
		LogUtil
				.writeCommodityLog("html头部GoodsUtil.bodyStart()...........start");

		StringBuffer sb = new StringBuffer();
		sb.append("<!doctype html>\n");
		sb.append("<html lang=\"zh-CN\">\n");
		sb.append("<head>\n");
		sb.append("<meta charset=\"utf-8\">\n");
		sb
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1,user-scalable=no\">\n");
		sb.append("<title>商品详情</title>\n");
		sb
				.append("<link href=\"http://cd-css.qiniudn.com/pure.min.css\" rel=\"stylesheet\">\n");
		sb
				.append("<style>table,tr{max-width:100% !important;overflow:scroll !important; }img{max-width:100% !important};.pure-table td, .pure-table th {	padding-bottom: 0;padding-left: 0;padding-right: 0;padding-top: 0px;}</style>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<div class=\"content\" id=\"item-show\">\n");
		LogUtil.writeCommodityLog("html头部GoodsUtil.bodyStart()...........end");
		return sb.toString();
	}

	/**
	 * html尾部
	 * 
	 * @return
	 */
	public static String bodyEnd() {
		LogUtil.writeCommodityLog("html尾部GoodsUtil.bodyEnd()...........start");

		StringBuffer sb = new StringBuffer();
		sb.append("\n<div class=\"footer\" align=\"center\">\n");
		sb.append("<div class=\"copyright\">\n");
		// sb.append("©<a href=\"http://www.truedian.com\">深圳微购科技</a>
		// 提供技术支持\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb
				.append("<script src=\"http://cd-lib.qiniudn.com/jquery.min.js\"></script>\n");
		sb
				.append("<script src=\"http://7pn6g8.com2.z0.glb.qiniucdn.com/detail.js\"></script>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		LogUtil.writeCommodityLog("html尾部GoodsUtil.bodyEnd()...........end");
		return sb.toString();
	}

	/**
	 * 获取图片
	 * 
	 * @param doc
	 */
	public static List<GoodsImageSchema> imgInfo(Document doc, String goodsId,
			String shopId, String url) {
		LogUtil.writeCommodityLog("获取图片GoodsUtil.imgInfo()...........start "
				+ goodsId + " " + shopId);
		List<GoodsImageSchema> goodsImageList = new ArrayList<GoodsImageSchema>();
		try {
			Elements ele = doc.select("[class=tab-content-container]");

			// 图片
			Elements imgEle = ele.select("img");
			// String[] urlArray = new String[imgEle.size()];// 所有图片地址
			// CommodityBO bo = new CommodityBO();
			boolean image_default = true;
			for (int i = 0; i < imgEle.size(); i++) {
				String src = imgEle.get(i).attr("src");
				if (src.indexOf(".jpg") < 0) {
					src = imgEle.get(i).attr("data-lazy-src");
				}
				src = src.substring(0, src.lastIndexOf(".jpg"));
				src = src.substring(0, src.lastIndexOf(".")) + ".jpg";
				LogUtil.writeCommodityLog("网络图片地址：" + src);
				int start = (int) System.currentTimeMillis(); // 开始时间
				try {
					// imgFile(src);//下载网页图片到本地并上传到七牛
					GoodsImageSchema schema = httpFile(shopId, src, url);// 网络图片上传到七牛
					if (StringUtils.isNotEmpty(schema.getN_width())) {
						String fileName = getPicName(shopId, src);// 图片名
						String path = getQiniuUrl() + fileName;

						schema.setN_goods_id(goodsId); // 商品ID，对应tb_goods表中的商品ID（主键）
						schema.setC_img_url(path); // 图片地址(访问时需要带上样式，例如：../jmg.jpg-class)
						schema.setN_shop_id(shopId);// 卖家ID
						if (image_default) {// 默认情况为1
							image_default = false;
							schema.setN_img_category("1"); // 商品图片分类：1列表页，2详情页
							// redult = bo.addGoodsImage(schema);// 增加商品图片信息
						} else {
							schema.setN_img_category("2"); // 商品图片分类：1列表页，2详情页
							// redult = bo.addGoodsImage(schema);// 增加商品图片信息
						}
						goodsImageList.add(schema);
						// if (redult) {
						// urlArray[i] = src;// 添加图片地址
						// }
						// LogUtil.writeCommodityLog("增加商品图片信息：" + redult);
					}
				} catch (Exception e) {
					logger.error("图片上传失败：", e);
				}
				int end = (int) System.currentTimeMillis(); // 结束时间
				int re = end - start; // 但图片生成处理时间
				LogUtil.writeCommodityLog("图片下载使用了: " + re + "毫秒");
			}
		} catch (Exception e) {
			logger.error("获取图片异常：" + goodsId + " " + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取图片异常：" + goodsId
					+ " " + shopId + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取图片GoodsUtil.imgInfo()...........end "
				+ goodsId + " " + shopId);
		return goodsImageList;
	}

	public static String getQiniuUrl() {
		LogUtil.writeCommodityLog("GoodsUtil.getQiniuUrl()...........");
		if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// 现网
			return "http://wg5.truedian.com/";//"http://7qnat4.com2.z0.glb.qiniucdn.com/";
		} else if (StartContent.getInstance().getDomainUrl().equals("www.81851.net")){
			return "http://wg201504.qiniudn.com/";
		}else{
			return "http://wg201503.qiniudn.com/";
		}
	}

	/**
	 * 获取图片名
	 * 
	 * @param shopId
	 * @param imgUrl
	 * @return
	 */
	public static String getPicName(String shopId, String imgUrl) {
		LogUtil
				.writeCommodityLog("获取图片名GoodsUtil.getPicName()...........start "
						+ shopId + "" + imgUrl);
		String fileName = "";
		if (imgUrl.indexOf(".jpg") > -1 || imgUrl.indexOf(".SS2") > -1) {
			fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
		} else {
			fileName = shopId + DateUtil.format.format(new Date()) + ".jpg";
		}
		// fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
		LogUtil.writeCommodityLog("获取图片名GoodsUtil.getPicName()...........end "
				+ fileName);
		return fileName;
	}

	/*public static void main(String[] args) {
	  String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQFz7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL19rVHVOd2JsN0VpVnlaZDBoMnhWAAIEdeQ6VwMEAAAAAA==";
	  System.out.println(url);
	  if(url.startsWith("https")){
		  url = "http"+url.replaceAll("https", "");
		  System.out.println(url);
	  }
	 }*/

	/**
	 * 
	 * 网络图片上传（七牛）
	 * 
	 * @param imgUrl
	 *            网络图片地址
	 * @param str
	 *            查错的一个标识
	 * @throws Exception
	 */
	public static GoodsImageSchema httpFile(String shopId, String imgUrl,
			String str) {
		LogUtil
				.writeCommodityLog("网络图片上传（七牛）GoodsUtil.httpFile()...........start "
						+ shopId + " " + imgUrl + " " + str);
		GoodsImageSchema schema = new GoodsImageSchema();
		try {
			if(imgUrl.startsWith("https")||imgUrl.startsWith("HTTPS")){
				imgUrl = "http"+imgUrl.replaceAll("https", "");
			}
			String fileName = getPicName(shopId, imgUrl);// 图片名
			Config.ACCESS_KEY = CommodityContent.ACCESS_KEY;
			Config.SECRET_KEY = CommodityContent.SECRET_KEY;
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			DateUtil.format2.format(new Date());
			PutPolicy putPolicy = new PutPolicy(CommodityContent
					.getBucketName());
			putPolicy.returnBody = "{\"width\":$(imageInfo.width),\"height\":$(imageInfo.height)}";// 设置响应速度
			String uptoken = putPolicy.token(mac);
			String key = fileName;

			HttpEntity en = getHttpEntity(imgUrl);
			PutExtra extra = new PutExtra();
			extra.mimeType = en.getContentType().getValue();
			PutRet ret = IoApi.Put(uptoken, key, en.getContent(), extra, en
					.getContentLength());
			LogUtil.writeCommodityLog("网络图片上传（七牛）：" + ret.ok() + " 响应数据："
					+ ret.toString());

			if (ret.ok()) {
				JSONObject json = new JSONObject(ret.toString());
				schema.setN_width(json.getString("width"));// 图片宽度
				schema.setN_height(json.getString("height"));// 图片高度
			}
		} catch (Exception e) {
			logger.error("网络图片上传（七牛）异常：" + str + " " + imgUrl, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "网络图片上传（七牛）异常：" + shopId
					+ " " + str + " " + imgUrl, LogUtil.getExceptionError(e));
		}
		LogUtil
				.writeCommodityLog("网络图片上传（七牛）GoodsUtil.httpFile()...........end "
						+ imgUrl);
		return schema;
	}

	/**
	 * 获得网络图片资源
	 * 
	 * @param imgUrl
	 *            网络图片地址
	 * @return
	 * @throws Exception
	 */
	private static HttpEntity getHttpEntity(String imgUrl) throws Exception {
		LogUtil
				.writeCommodityLog("获得网络图片资源GoodsUtil.getHttpEntity()...........start "
						+ imgUrl);

		HttpClient client = Http.getClient();
		HttpGet httpget = new HttpGet(imgUrl);
		HttpResponse res = client.execute(httpget);
		LogUtil
				.writeCommodityLog("获得网络图片资源GoodsUtil.getHttpEntity()...........end "
						+ imgUrl);
		return res.getEntity();
	}

	/**
	 * 下载网页图片到本地
	 * 
	 * @param imgUrl
	 */
	public static String imgFile(String shopId, String imgUrl) throws Exception {
		LogUtil
				.writeCommodityLog("下载网页图片到本地GoodsUtil.imgFile()...........start "
						+ shopId + " " + imgUrl);

		URL url;
		BufferedInputStream in = null;
		FileOutputStream file = null;
		String filePath = "";// 图片下载的根目录
		try {
			// LogUtil.writeCommodityLog("取网络图片");
			String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
			filePath = JsoupUtil.SystemPathImage(shopId);// 设置图片下载的根目录
			// LogUtil.writeCommodityLog(filePath);
			url = new URL(imgUrl);

			// 如果该目录不存在,则创建之(多重目录自动创建)
			File uploadFilePath = new File(filePath);
			if (uploadFilePath.exists() == false) {
				uploadFilePath.mkdirs();
			}

			in = new BufferedInputStream(url.openStream());
			filePath = filePath + fileName;
			file = new FileOutputStream(new File(filePath));

			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			// localFile(filePath, fileName);// 上传到七牛
			// LogUtil.writeCommodityLog("输出的图片大小：" + getPicSize(filePath +
			// fileName)
			// / 1024 + "KB");
		} catch (Exception e) {
			logger.error("图片下载失败" + shopId + " " + imgUrl + " " + filePath, e);
			// 发送邮件
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(), "图片下载失败" + shopId + "
			// "
			// + imgUrl + " " + filePath, LogUtil.getExceptionError(e));
			return "";
		} finally {
			if (file != null) {
				file.close();
			}
			if (in != null) {
				in.close();
			}
		}
		LogUtil.writeCommodityLog("下载网页图片到本地GoodsUtil.imgFile()...........end "
				+ shopId + " " + imgUrl + " " + filePath);
		return filePath;
	}

	/**
	 * 获得图片大小
	 * 
	 * @param path
	 *            图片路径
	 * @return
	 */
	public static long getPicSize(String path) {
		File file = new File(path);
		return file.length();
	}

	/**
	 * 本地图片上传（七牛）
	 * 
	 * @param localFile
	 *            图片路径
	 * @throws Exception
	 */
	public static boolean localFile(String localFile, String fileName)
			throws Exception {
		LogUtil
				.writeCommodityLog("本地图片上传（七牛）GoodsUtil.localFile()...........start "
						+ localFile + " " + fileName);

		Config.ACCESS_KEY = CommodityContent.ACCESS_KEY;// 开发者自助平台的AK
		Config.SECRET_KEY = CommodityContent.SECRET_KEY;// 开发者自助平台的SK
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		// 请确保该bucket已经存在
		// String bucketName = "truedian";// 空间名称
		PutPolicy putPolicy = new PutPolicy(CommodityContent.getBucketName());
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		String key = fileName;// 唯一key(图片名称)
		PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
		LogUtil.writeCommodityLog("本地图片上传（七牛）:" + ret.ok());
		LogUtil
				.writeCommodityLog("本地图片上传（七牛）GoodsUtil.localFile()...........end "
						+ ret.ok());
		return ret.ok();
	}
}
