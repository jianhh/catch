package com.work.taobao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.framework.util.StringUtils;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.CfgSysCategorySchema;
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
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.schema.ShopTaobaoSchema;
import com.work.util.JsoupUtil;

/**
 * 商家商品信息爬取
 * 
 * @author tangbiao
 * 
 */
public class GoodsUtil {

	static JLogger logger = LoggerFactory.getLogger(GoodsUtil.class);
	public static DefaultHttpClient httpclient = null;// HttpClient对象

	public final static String GOODS_CATCH_FIRST = "1";// 1:首次抓取，2:同步上新商品,3:重新同步所有商品
	public final static String GOODS_CATCH_UPDATE_NEW = "2";
	public final static String GOODS_CATCH_ALL = "3";
	public final static String GOODS_CATCH_APPEND = "4"; // 增加漏爬取的商品
	public final static String GOODS_UPDATE_SINGLE = "10";

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
		LogUtil.writeCommodityLog("查询店铺商品是否有上新GoodsUtil.goodsNew()...........start " + shopId + " " + url);

		int state = 10000;
		try {
			String href = "";

			Document doc = getDoc(url + "/search.htm?search=y&pageNo=1&orderType=newOn_desc");// 通过getDoc得到Document对象
			Document goodsDoc = getGoodsListUrl(doc, url);
			if (goodsDoc == null) {
				goodsDoc = doc;
			}
			ArrayList<GoodsItem> goodsItemArr = new ArrayList();
			// 抓取所有商品的列表
			Elements divEle = goodsDoc.select("dl");
			String item_id = "";
			if (divEle.size() > 0) {
				item_id = divEle.get(0).attr("data-id");
				CommodityBO bo = new CommodityBO();
				GoodsSchema info = bo.getGoodsByformId(shopId, item_id);// 根据第三方平台商品ID查询
				if (info != null) {
					state = 1;
				} else {
					state = 0;
				}
			}else{
				state = 1;
			}

		} catch (Exception e) {
			logger.error("查询店铺商品是否有上新异常：" + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "查询店铺商品是否有上新异常：" + shopId + " " + url, LogUtil
					.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("查询店铺商品是否有上新GoodsUtil.goodsNew()...........end " + state);
		return state;
	}

	/**
	 * 得到网页DOM
	 * 
	 * @param url
	 *            商品列表地址
	 */
	public static Document getDoc(String url) throws ClientProtocolException, IOException {
		logger.debug("url=" + url);
		// if (null == httpclient) {
		// httpclient = new DefaultHttpClient();
		// }
		// HttpGet meGet = new HttpGet(url);
		// HttpResponse meResponse = httpclient.execute(meGet);
		String xml = "";
		// if (meResponse != null || !meResponse.equals("")) {
		// HttpEntity httpEntity = meResponse.getEntity();
		// xml = EntityUtils.toString(httpEntity);// 取出应答字符串
		// }
		xml = JsoupUtil.getUrl(url);
		// logger.debug("xml="+xml);
		Document doc = Jsoup.parse(xml);
		return doc;
	}

	/**
	 * 得到商品列表的url
	 * 
	 * @param url
	 * 
	 */
	public static Document getGoodsListUrl(Document doc, String url) throws ClientProtocolException, IOException {
		String href = "";
		Document goodsDoc = null;
		// 得到商品列表的url
		Elements spanEle = doc.select("[id=J_ShopAsynSearchURL]");
		if (spanEle.size() > 0) {
			System.out.println("doc:" + doc.toString());
			href = spanEle.attr("value");
			goodsDoc = getDoc(url + getReplace(href));
			String html1 = getReplace(goodsDoc.html());
			String html = html1.replaceAll("\\\\\"", "").replace("(\"", "").replace("\")", "");
			// logger.debug("----------lin----" + html);
			goodsDoc = Jsoup.parse(html);
		}

		return goodsDoc;
	}

	// url对特殊字符转义
	public static String getReplace(String url) {

		return url.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&apos;", "'")
				.replaceAll("&quot;", "\"");
	}

	/**
	 * 得到店铺的卖家信息的url
	 * 
	 * @param url
	 * 
	 */
	public static String getShopUrl(Document doc) throws ClientProtocolException, IOException {
		String href = "";
		// 得到存进入卖家的url
		Elements spanEle = doc.select("[class=shop-rank]");
		Elements aEle = spanEle.select("a");
		if (aEle.size() > 0) {
			href = aEle.attr("href");
		}
		return href;
	}

	// public static ArrayList<GoodsItem> goodsItemArr = new ArrayList();
	static class GoodsItem {
		String item_id;
		String sale_num;
		String goods_price;
	}

	public static ArrayList<GoodsItem> getAllGoodsDetail(String shopId, String url, ArrayList<GoodsItem> goodsItemArr,
			String sync_type, String taskId, int num, boolean getShopInfo) {
		boolean bCathSeller = getShopInfo;
		ArrayList<GoodsItem> goodsItemArrFail = new ArrayList<GoodsItem>();
		// 爬取所有商品
		LogUtil.writeCommodityLog("开始爬取商品数量:" + goodsItemArr.size());
		System.out.println("开始爬取商品数量:" + goodsItemArr.size());

		if (sync_type.equals(GOODS_CATCH_APPEND)) {
			// 补漏
			for (int i = goodsItemArr.size() - 1; i >= 0; i--) {
				CommodityBO bo = new CommodityBO();
				try {
					GoodsSchema info = bo.getGoodsByformId(shopId, goodsItemArr.get(i).item_id);// 根据第三方平台商品ID查询
					if (info == null) {
						boolean succ = getGoodsItemInfo(shopId, goodsItemArr.get(i).item_id,
								goodsItemArr.get(i).sale_num, goodsItemArr.get(i).goods_price, bCathSeller, url, "",
								sync_type, taskId, num);
						if (succ) {
							bCathSeller = false;
						} else {
							goodsItemArrFail.add(goodsItemArr.get(i));
						}
					}
				} catch (Exception e) {

				}
			}

		} else {
			for (int i = goodsItemArr.size() - 1; i >= 0; i--) {
				System.out.println("爬取第" + i + "个:" + goodsItemArr.get(i).item_id);
				boolean succ = getGoodsItemInfo(shopId, goodsItemArr.get(i).item_id, goodsItemArr.get(i).sale_num,
						goodsItemArr.get(i).goods_price, bCathSeller, url, "", sync_type, taskId, num);
				if (succ) {
					bCathSeller = false;
				} else {
					System.out.println("爬取第" + i + "个:" + goodsItemArr.get(i).item_id + "失败");
					goodsItemArrFail.add(goodsItemArr.get(i));
				}
			}
		}

		return goodsItemArrFail;

	}

	public static ArrayList<GoodsItem> getGoodsList(String shopId, String url, int num, int[] goodsTotal,
			String sync_type) throws ClientProtocolException, IOException {
		Document doc = getDoc(url + "/search.htm?search=y&pageNo=" + num + "&orderType=newOn_desc");// 通过getDoc得到Document对象
		Document goodsDoc = getGoodsListUrl(doc, url);
		if (goodsDoc == null) {
			goodsDoc = doc;
		}
		ArrayList<GoodsItem> goodsItemArr = new ArrayList();
		// 抓取所有商品的列表
		Elements divEle = goodsDoc.select("dl");
		for (int i = 0; i < divEle.size(); i++) {
			String itemID = divEle.get(i).attr("data-id");// 商品ＩＤ
			String saleNum = divEle.get(i).select("[class=sale-num]").text();// 成交量
			String goodsPrice = divEle.get(i).select("[class=c-price]").text();// 价格
			GoodsItem item = new GoodsItem();
			item.item_id = itemID;
			item.sale_num = saleNum;
			item.goods_price = goodsPrice;
			if (sync_type.equals(GOODS_CATCH_UPDATE_NEW)) {
				CommodityBO bo = new CommodityBO();
				try {
					GoodsSchema info = bo.getGoodsByformId(shopId, item.item_id);// 根据第三方平台商品ID查询
					if (info != null) {
						// 当前数据库中有些商品，则不再进行后面商品的爬取。商品是按时间排序
						return goodsItemArr;
					}
				} catch (Exception e) {
				}
			}
			goodsItemArr.add(item);
		}

		int count = 0;// 总页数
		Elements pageCountEle = goodsDoc.select("[class=page-info]");
		if (pageCountEle.size()<=0){
			pageCountEle = goodsDoc.select("[class=ui-page-s-len]");
		}
		
		String pageCount = pageCountEle.text();// 总页数
		if (StringUtils.isNotEmpty(pageCount)) {
			String[] a = pageCount.split("/");
			count = Integer.parseInt(a[1]);
		}
		if (num < count) {
			int i = num + 1;
			goodsItemArr.addAll(getGoodsList(shopId, url, i, goodsTotal, sync_type));// 叠加请求一页页获取数据
		} else {
			Elements offerCountEle = goodsDoc.select("[class=search-result]");
			Elements offerCount = offerCountEle.select("span");
			if (num == 1) {
				LogUtil.writeCommodityLog(shopId + " 店铺的商品总条数：" + offerCount.text());
			}
			if (goodsTotal != null) {
				try {
					goodsTotal[0] = Integer.parseInt(offerCount.text());
				} catch (Exception e) {
					goodsTotal[0] = 0;
				}

			}

		}
		return goodsItemArr;
	}

	public static void getGoods(String shopId, String url, String sync_type, int num, String task_id, boolean getShopInfo)
			throws ClientProtocolException, IOException {
		if (shopId == null || shopId.isEmpty()) {
			shopId = "1";
		}
		int[] goodsTotal = new int[1];
		goodsTotal[0] = 0;
		ArrayList<GoodsItem> goodsCatchFailArr = getAllGoodsDetail(shopId, url, getGoodsList(shopId, url, 1,
				goodsTotal, sync_type), sync_type, task_id, num, getShopInfo);
		CommodityBO bo = new CommodityBO();
		LogUtil.writeCommodityLog("商品总数：" + goodsTotal[0]);
		try {
			// List<GoodsSchema> goodsList = bo.getGoodsByshopId(shopId);
			// if (goodsTotal[0] != 0 && goodsTotal[0] > goodsList.size()) {
			// num++;
			// LogUtil.writeCommodityLog("获取触店商品重试中：" + num + ",商品总数" +
			// goodsTotal[0] + ",已经爬取商品数" + goodsList.size());
			// if (num < 10) {
			// getGoods(shopId, url, sync_type, num, task_id);
			// }
			int times = 0;
			while (goodsCatchFailArr.size() > 0) {
				times++;
				goodsCatchFailArr = getAllGoodsDetail(shopId, url, goodsCatchFailArr, sync_type, task_id, times, getShopInfo);
				if (times >= 10) {
					return;
				}
				LogUtil.writeCommodityLog("获取触店商品第" + times + "次补漏:" + shopId + "还有" + goodsCatchFailArr.size()
						+ "个商品未爬取成功，商品总数" + goodsTotal[0]);
			}

			// }
		} catch (Exception e) {
			logger.error("获取触店商品第" + num + "次总数失败:" + shopId + " " + sync_type, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取触店商品第" + num + "次总数失败:" + "店铺ID:" + shopId + " 店铺地址:" + url
					+ " 抓取类型:" + sync_type + " task_id:" + task_id, LogUtil.getExceptionError(e));
		}
	}

	/**
	 * 
	 * @Title: getGoodsItemInfo
	 * @Description: 获取商品信息及商家信息
	 * @param
	 * @param id
	 * @param
	 * @param saleNum
	 * @param
	 * @param price
	 * @return void
	 * @throws
	 */
	public static boolean getGoodsItemInfo(String shopId, String id, String saleNum, String price, boolean bCathSeller,
			String d_url, String goods_id, String sync_type, String taskId, int num) {
		if(id == null || id.length() <= 0){
			logger.error("店铺"+shopId+"url:"+d_url+"传入的id为空");
			return false;
		}
		String url = "http://hws.m.taobao.com/cache/wdetail/5.0/?id=" + id + "&ttid=2013@taobao_h5_1.0.0&exParams={}";
//		String xml = JsoupUtil.conUrl(url, false, "utf-8", "", 0);
		String xml = JsoupUtil.getUrl(url,100, "utf-8");
		
		// System.out.println(xml);
		try {
			JSONObject json = new JSONObject(xml);
			JSONObject dataObj = json.getJSONObject("data");
			JSONObject sellerObj = dataObj.getJSONObject("seller");

			// 获取商家信息
			if (bCathSeller && (sellerObj != null)) {
				catchShopInfo(shopId, url, sellerObj, d_url, true);
			}
			// 获取商品信息
			if (sync_type.equalsIgnoreCase(GOODS_UPDATE_SINGLE)){
				CommodityBO bo = new CommodityBO();
				bo.deleteGoodsById(goods_id, shopId);// 删除商品及商品相关的信息
			}
			boolean ret = goodsInfoCatch(shopId, url, goods_id, dataObj, saleNum, price, sync_type, taskId, num);
			System.out.println(json.toString());
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("获取商家信息失败:" + shopId + " " + url+"----xml:"+xml, e);
			return false;
		} catch (Exception e) {
			logger.error("单品同步中删除出错", e);
			return false;
		}
	}

	/**
	 * 获取商家信息
	 * 
	 * @param shopId
	 * @param sellerObj
	 */
	public static ShopTaobaoSchema catchShopInfo(String shopId, String url, JSONObject sellerObj, String d_url, boolean update)
			throws JSONException {
		CommodityBO bo = new CommodityBO();

		if (shopId != null) {
			try {
				boolean flag = bo.addShopTaobao(shopId, d_url);// 增加店铺基本信息
				LogUtil.writeCommodityLog(shopId + "增加店铺基本信息结果:" + flag);
			} catch (Exception e) {
			}
		}

		ShopTaobaoSchema schema = new ShopTaobaoSchema();
		schema.setC_company_industry("");
		schema.setC_company_product("");
		if (sellerObj.has("bailAmount")) {
			schema.setC_bail_amount(sellerObj.getString("bailAmount"));
		} else {
			schema.setC_bail_amount("0");
		}

		schema.setC_company_type(sellerObj.getString("type"));
		schema.setC_credit_level(sellerObj.getString("creditLevel"));

		JSONArray evaluateInfoArr = sellerObj.getJSONArray("evaluateInfo");
		schema.setC_evaluateInfo_disc(evaluateInfoArr.getJSONObject(0).getString("score"));
		schema.setC_evaluateInfo_disc_highgap(evaluateInfoArr.getJSONObject(0).getString("highGap"));
		schema.setC_evaluateInfo_send(evaluateInfoArr.getJSONObject(1).getString("score"));
		schema.setC_evaluateInfo_send_highgap(evaluateInfoArr.getJSONObject(1).getString("highGap"));
		schema.setC_evaluateInfo_taodu(evaluateInfoArr.getJSONObject(2).getString("score"));
		schema.setC_evaluateInfo_taodu_highgap(evaluateInfoArr.getJSONObject(2).getString("highGap"));

		schema.setC_fans_count(sellerObj.getString("fansCount"));
		schema.setC_good_rate_percentage(sellerObj.getString("goodRatePercentage"));
		if (sellerObj.has("picUrl")) {
			schema.setC_pic_url(sellerObj.getString("picUrl"));
		} else {
			schema.setC_pic_url("");
		}
		// schema.setC_shop_domain("");
		schema.setC_taobao_shop_id(sellerObj.getString("shopId"));
		if (d_url==null){
			schema.setC_shop_index_url("https://shop"+schema.getC_taobao_shop_id()+".taobao.com");
		}else{
			schema.setC_shop_index_url(d_url);
		}
		schema.setC_shop_title(sellerObj.getString("shopTitle"));
		schema.setC_starts(sellerObj.getString("starts"));
		schema.setC_user_nick(sellerObj.getString("nick"));
		schema.setC_user_num_id(sellerObj.getString("userNumId"));
		if (sellerObj.has("weitaoId")){
			schema.setC_weitao_id(sellerObj.getString("weitaoId"));
		}
		// schema.setN_id(sellerObj.getString(""));
		schema.setN_shop_id(shopId);
		if (shopId != null) {
			try {
				boolean flag = bo.updateShopTaobao(schema);// 增加店铺基本信息
				LogUtil.writeCommodityLog(shopId + "增加店铺基本信息结果:" + flag);
			} catch (Exception e) {
				logger.error("写入商家信息失败:" + shopId + " " + url, e);
			}
		}
		return schema;
	}

	private static HashMap<Integer, String> getCategoryMap(){
		HashMap<Integer, String> map = new HashMap<Integer, String>() {
		    {
		    	put(1050011159, "1031897,10165");//,男式风衣');
    			put(1050012042, "121916001,1038378");//,帆布鞋');
    			put(1050012032, "1034340,1038378");//,凉鞋');
    			put(1050012047, "1032755,1038378");//,雨鞋、雨靴');
    			put(1050011123, "314,10165");//,男式衬衫');
    			put(1050010158, "317,10165");//,男式夹克');
    			put(1050001748, "1042833,10165");//,中山装、唐装、民族服装');
    			put(1050010167, "1031888,10165");//,男式牛仔裤');
    			put(1050010402, "122232007,10165");//,男式Polo衫');
    			put(1050011161, "123688002,123694002");//,男式PU皮衣');
    			put(1050000436, "315,10165");//,男式T恤');
    			put(1124730001, "122238007,10165");//,男式休闲套装');
    			put(1050010159, "1036995,10165");//,男式卫衣');
    			put(1000003035, "1031890,10165");//,男式休闲裤');
    			put(1050010160, "123692001,123694001");//,男式商务西服');
    			put(1050011129, "1031889,10165");//,男式西裤');
    			put(1050011130, "1036997,10165");//,男式西服套装');
    			put(1050025884, "123698001,10165");//,男式羽绒裤');
    			put(1050011167, "1034163,10165");//,男式羽绒服');
    			put(1050000557, "123682001,123650006");//,男式针织衫');
    			put(1000001623, "1031912,10166");//,半身裙');
    			put(1000162104, "1031918,10166");//,女式衬衫');
    			put(1000001629, "1046799,10166");//,大码女装');
    			put(1050008901, "319,10166");//,女式风衣、大衣');
    			put(1050011404, "320,10166");//,婚纱、礼服');
    			put(1050010850, "1031910,10166");//,连衣裙');
    			put(1000162116, "1048182,10166");//,蕾丝衫、雪纺衫');
    			put(1050000697, "10163,10166");//,女式针织衫');
    			put(1000162103, "124160002,10166");//,女式毛衣');
    			put(1050008900, "303,10166");//,女式棉衣');
    			put(1121434004, "122444002,122478003");//,裹胸、抹胸');
    			put(1000162205, "1031869,10166");//,女式牛仔裤');
    			put(1050008904, "1037019,10166");//,女式皮衣');
    			put(1050008905, "304,10166");//,皮草');
    			put(1050000671, "1031919,10166");//,女式T恤');
    			put(1000001624, "1048256,10166");//,女式休闲套装');
    			put(1050008906, "305,10166");//,旗袍、唐装');
    			put(1050008898, "1037021,10166");//,女式卫衣、绒衫');
    			put(1050008897, "1037020,10166");//,小西装');
    			put(1050008899, "123860002,10166");//,女式羽绒服');
    			put(1050000852, "1046800,10166");//,中老年女装');
    			put(1121400005, "1045062,2122");//,卡包卡套');
    			put(1050012019, "10283,122210001");//,旅行箱');
    			put(1050050199, "122216001,122210001");//,旅行包、旅行袋');
    			put(1121434005, "122228002,1042954");//,钱包、钥匙包');;
    			put(1121384005, "122218002,122228002");//,手机包');
    			put(1122690003, "124736008,121658001");//,男式双肩包');
    			put(1121392003, "123290005,92");//,静电棒/钥匙包');
    			put(1121368002, "1037379,1042954");//,护照夹、证件夹');
    			put(1050012777, "1048322,122558002");//,保暖裤');
    			put(1050012778, "122512003,122558002");//,保暖套装');
    			put(1050012786, "1037026,122528003");//,胸垫、插片');
    			put(1050006846, "122460002,1043349");//,连裤袜、打底袜、踩脚袜');
    			put(1050010394, "1031920,10166");//,小背心、吊带衫、裹胸');
    			put(1050012785, "122538004,122528003");//,吊袜带');
    			put(1050012787, "122566002,122528003");//,肩带、搭扣');
    			put(1050012784, "122566002,122528003");//,肩带、搭扣');
    			put(1050008888, "122444002,122478003");//,裹胸、抹胸');
    			put(1050008882, "122450001,312");//,内裤');
    			put(1050008889, "122530002,122528003");//,胸贴、乳贴');
    			put(1050008884, "1037932,122484001");//,塑身上衣');
    			put(1050012776, "122488001,122484001");//,塑身分体套装');
    			put(1050012781, "122478002,122484001");//,塑身连体衣');
    			put(1050008886, "122500001,312");//,睡衣、家居服、睡袍、浴袍');
    			put(1050012771, "122460003,122500001");//,睡裙');
    			put(1050012772, "122500001,312");//,睡衣、家居服、睡袍、浴袍');
    			put(1050012773, "122454002,122500001");//,睡袍、浴袍');
    			put(1050008881, "124188007,312");//,文胸');
    			put(1050008883, "124186010,124188007");//,文胸套装');
    			put(1000164206, "364,320");//,婚纱礼服配件');
    			put(1050001248, "326,54");//,领带领结领带夹');
    			put(1000302910, "1829,54");//,帽子');
    			put(1050009047, "364,1829");//,其他');
    			put(1050010410, "1034419,54");//,手套');
    			put(1050009035, "1043475,1043472");//,手帕');
    			put(1050007003, "325,54");//,围巾丝巾披肩');
    			put(1000302909, "122388003,122322004");//,袖扣');
    			put(1050009032, "1043131,54");//,腰带、腰链、腰饰');
    			put(1050014227, "122704002,54");//,耳饰');
    			put(1050013878, "1752,54");//,发饰、头饰');
    			put(1050013871, "1745,54");//,脚饰');
    			put(1050013875, "122706005,122706003");//,戒指');
    			put(1050013869, "1037268,122706003");//,手链');
    			put(1050013870, "122722002,122706003");//,手镯');
    			put(1050013865, "122306008,122322004");//,长款项链');
    			put(1050013868, "122384002,122322005");//,项坠');
    			put(1050013876, "122306007,122322004");//,胸针');
    			put(1050010527, "1037011,311");//,童衬衫');
    			put(1121476002, "1037002,311");//,围嘴围兜/口水巾');
    			put(1050006584, "1043351,311");//,儿童袜');
    			put(1050152002, "122086002,311");//,童旗袍/唐装/民族服装');
    			put(1121380002, "1036999,311");//,童内衣内裤');
    			put(1121364003, "1042754,311");//,童家居服');
    			put(1121392004, "122086001,311");//,童表演服/舞蹈服');
    			put(1121506001, "1037649,311");//,童礼服');
    			put(1050146004, "124958003,122380006");//,罩衣');
    			put(1050013618, "1037004,311");//,童裤');
    			put(1050010537, "1037003,311");//,连身衣、爬服');
    			put(1050010524, "1042841,311");//,童马甲');
    			put(1050010539, "1037010,311");//,童毛衣');;
    			put(1121476003, "122444002,122478003");//,裹胸、抹胸');
    			put(1050010530, "122704004,311");//,童披风/斗蓬');
    			put(1050012424, "1037039,311");//,亲子装');
    			put(1050013189, "1037012,311");//,童T恤');
    			put(1050010540, "1037192,311");//,童套装');
    			put(1050010518, "1042840,311");//,童卫衣');
    			put(1050012308, "1037648,311");//,童外套/夹克/大衣');
    			put(1050016450, "122088001,311");//,校服/校服定制');
    			put(1050014512, "122698004,311");//,婴儿礼盒');
    			put(1124208010, "1037008,311");//,童羽绒服/羽绒内胆');
    			put(1050012343, "121916001,1038378");//,帆布鞋');
    			put(1050012346, "1034340,1038378");//,凉鞋');
    			put(1050012348, "1043192,1038378");//,棉鞋');
    			put(1050012349, "1038539,1038378");//,家居拖鞋');
    			put(1050017903, "123666005,1038378");//,舞蹈鞋');
    			put(1050012345, "1038532,1038378");//,童鞋、婴儿鞋');
    			put(1050012347, "1043191,1038378");//,雪地靴');
    			put(1050012341, "121876002,18");//,运动鞋');
    			put(1050012353, "1032755,1038378");//,雨鞋、雨靴');
    			put(1050010825, "122210003,123084001");//,蚕丝被');
    			put(1050001865, "1031732,10132");//,被套');
    			put(1121458010, "10135,10132");//,床单、被单');
    			put(1121482007, "1031731,10132");//,床罩、床笠、床裙');
    			put(1050008779, "122206003,10132");//,四件套');
    			put(1121394007, "1031731,10132");//,床罩、床笠、床裙');
    			put(1121386009, "1031728,123084002");//,蚊帐、床幔');
    			put(1121414009, "1031731,10132");//,床罩、床笠、床裙');
    			put(1050008565, "1047945,122930001");//,床垫、席梦思');
    			put(1122970005, "1047991,1047981");//,电热毯、电热垫');
    			put(1122854005, "121188019,121188018");//,婴童床品套件');
    			put(1050008246, "122220004,123084002");//,功能凉席');
    			put(1050006101, "1031727,123084002");//,其他凉席');
    			put(1050022514, "1042611,122380006");//,童睡袋');
    			put(1121452006, "121190024,121188018");//,婴童蚊帐');
    			put(1050001871, "122240002,123088001");//,羊毛毯、羊绒毯');
    			put(1121416008, "1031730,10132");//,枕套');
    			put(1050002777, "122254005,123086001");//,竹炭枕');
    			put(1121434010, "1043476,1043472");//,枕巾');
    			put(1050013228, "1048090,1048069");//,运动T恤、POLO衫');
    			put(1050011717, "1048096,1048069");//,运动卫衣');
    			put(1050011739, "123118001,1048069");//,运动外套');
    			put(1050011720, "1048099,1048069");//,运动棉衣');
    			put(1050011721, "1048101,1048069");//,运动羽绒服');
    			put(1050022728, "123116001,1048069");//,健身套装');
    			put(1050022889, "1048090,1048069");//,运动T恤、POLO衫');
    			put(1050023105, "1048092,1048069");//,运动裤');
    			put(1050023109, "1048098,1048069");//,运动裙');
    			put(1050023415, "1048021,18");//,运动、武术、球服');
    			put(1050023110, "1048093,1048069");//,运动马甲');
    			put(1050012043, "1043193,1038378");//,板鞋');
    			put(1050017865, "121884002,121876002");//,棒球鞋');
    			put(1050012031, "121908001,121876002");//,篮球鞋')
    			put(1050012036, "121912001,121876002");//,跑步鞋');
    			put(1050012946, "121888002,121876002");//,乒乓球鞋');
    			put(1050017619, "121910001,121876002");//,排球鞋');
    			put(1050012064, "121910004,121876002");//,其它运动鞋');
    			put(1050012041, "121910003,121876002");//,综合训练鞋、室内健身鞋');
    			put(1050026312, "1038532,1038378");//,童鞋、婴儿鞋');
    			put(1050012037, "121894002,121876002");//,网球鞋');
    			put(1050012331, "121896002,121876002");//,羽毛球鞋');
    			put(1050012038, "121910002,121876002");//,足球鞋');
    			put(1050015943, "122198003,1048300");//,防雨罩');
    			put(1050014503, "121870002,1048300");//,运动挎包、挂包');
    			put(1050015370, "122128001,122098001");//,骑行手套');
    			put(1050015371, "1048301,121898001");//,运动帽、头巾');
    			put(1121400019, "122108004,122098001");//,袖、腿套、束管带');
    			put(1050019690, "121914002,121900002");//,减震、充气鞋垫');
    			put(1121478018, "1043956,1556");//,鞋套、鞋油、鞋刷、鞋用品');
    			put(1050023100, "122216001,122210001");//,旅行包、旅行袋');
    			put(1050018244, "1043131,54");//,腰带、腰链、腰饰');
    			put(1050015377, "124014009,124016003");//,分体雨衣、雨披');
    			put(1050015369, "1048301,121898001");//,运动帽、头巾');
    			put(1050516004, "2040,18");//,运动护具');
    			put(1050522001, "1046723,281904");//,运动水壶、折叠水袋');
    			put(1121384022, "121900001,1048300");//,运动腰包、配件包');
		    }
		};
		return map;
	}
	
	 public static void main(String[] args) {
		 HashMap<Integer, String> hash = getCategoryMap();
		 if (hash.containsKey(1050015369)){
				String cateobj = (String)hash.get(1050015369);
				String[] cateArr = cateobj.split(",");
			 System.out.println(cateArr[0]+"--"+cateArr[1]);
		 }else{
			 System.out.println("222");
		 }
	 }
	
	public static boolean goodsInfoCatch(String shopId, String url, String gid, JSONObject dataObj, String saleNum,
			String price, String sync_type, String taskId, int num) {
		LogUtil.writeCommodityLog("获取商品详情信息GoodsUtil.goodsInfoCatch()...........start " + shopId + " " + url);
		try {
			JSONObject itemInfoModelObj = dataObj.getJSONObject("itemInfoModel");
			JSONArray apiStackArr = dataObj.getJSONArray("apiStack"); // cartSupport下架
			String valueJson = apiStackArr.getJSONObject(0).getString("value");
			JSONObject extrasObj = dataObj.getJSONObject("extras");
			String defDynJson = extrasObj.getString("defDyn");
			JSONObject valueJsonObj = new JSONObject(valueJson);
			JSONObject defDynJsonObj = new JSONObject(defDynJson);

			String cThirdPlatformId = itemInfoModelObj.getString("itemId");
			CommodityBO bo = new CommodityBO();
			GoodsSchema info = bo.getGoodsByformId(shopId, cThirdPlatformId);// 根据第三方平台商品ID查询

			if (info == null) {
				GoodsSchema schema = new GoodsSchema();
				String goodsId = gid;// 获取商品ID
				if (StringUtils.isEmpty(gid)) {
					goodsId = JsoupUtil.goodsId();
				}
				if (goodsId == "") {
					logger.error(shopId + ":" + cThirdPlatformId + "goodsId获取失败");
					return false;
				}
				System.out.println(goodsId);
				LogUtil.writeCommodityLog(shopId + " 商家的商品ID：" + goodsId);

				schema.setN_id("");
				schema.setN_goods_id(goodsId);// 商品ID
				schema.setC_third_platform_id(cThirdPlatformId);// 第三方平台商品ID
				schema.setC_tp_goods_url(null);// 第三方商品地址
				schema.setN_third_platform_type("2");// 商品来源平台类型:0,自己平台;1,阿里;2,淘宝
				schema.setN_shop_id(shopId);// 卖家ID:对应mem_seller表中的主键ID
				schema.setN_parent_shop_id(shopId);
				schema.setC_goods_name(itemInfoModelObj.getString("title"));// 商品名称
				schema.setN_express_pay_type("2");// 运费承担方式：1卖家，2买家，
				schema.setC_goods_state("1");// // 商品状态：1上架，2下架，3删除 4缺货
				String cate_id = itemInfoModelObj.getString("categoryId");
				int mcate_id = 0;
				// TODO:更新商品样式与阿里一致
				try {
					mcate_id = Integer.parseInt(cate_id) + 1000000000;
				} catch (NumberFormatException e) {

				}
				
				HashMap map = getCategoryMap();
				if (map.containsKey(mcate_id)){
					String cateobj = (String)map.get(mcate_id);
					String[] cateArr = cateobj.split(",");
					schema.setN_sys_cid(cateArr[0]);
					schema.setN_sys_parent_cid(cateArr[1]);
				}else{
					if (mcate_id == 0) {
						schema.setN_sys_cid(cate_id);
					} else {
						schema.setN_sys_cid(mcate_id + "");
					}

					CfgSysCategorySchema cat = bo.getTaobaoCfgSysCategorySchemaByCatId(schema.getN_sys_cid());
					if (cat != null) {
						schema.setN_sys_parent_cid(cat.getN_parent_id());
					} else {
						schema.setN_sys_parent_cid("0");
					}
				}

				String unitStr = "件"; 
				String unitStrUtf8 = new String(unitStr.getBytes(), "UTF-8");
				schema.setC_goods_unit(unitStrUtf8);
				// 3自取，4运费到付
				schema.setC_goods_detail_desc(htmlInfo(cThirdPlatformId, shopId, goodsId));// 商品信息描述:静态页面链接
				schema.setC_tp_order_url(itemInfoModelObj.getString("itemUrl"));// 第三方平台选购URL地址(淘宝手机的详情页地址)
				schema.setT_create_time(DateUtil.format8.format(new Date()));// 添加时间
				schema.setT_last_update_time(DateUtil.format8.format(new Date()));// 最后修改时间
				if (StringUtils.isNotEmpty(saleNum)) {
					schema.setN_third_total_sell(saleNum);// 第三方平台总销量
					schema.setN_total_sell(saleNum);
				}else{
					schema.setN_third_total_sell("0");// 第三方平台总销量
					schema.setN_total_sell("0");
				}
				schema.setN_weight("0");
//				if (price != null) {
//					schema.setN_sell_price(JsoupUtil.priceFen(price));
//				}else{
//					schema.setN_sell_price("0");
//				}
				String goods_price = defDynJsonObj.getJSONObject("itemInfoModel").getJSONArray("priceUnits").getJSONObject(0).getString("price");
				schema.setC_price(goods_price);
				String[] priceArr = goods_price.split("-");
				if (priceArr.length > 0){
					schema.setN_sell_price(JsoupUtil.priceFen(priceArr[priceArr.length-1]));
				}else{
					schema.setN_sell_price(JsoupUtil.priceFen(price));
				}
				
				System.out.println("sell_price"+schema.getN_sell_price()+",C_price:"+schema.getC_price());
				// 库存
				String quantity = valueJsonObj.getJSONObject("data").getJSONObject("itemInfoModel").getString(
						"quantity");
				schema.setN_total_stock(quantity);

				// TODO：货号

				boolean redult = bo.addGoods(shopId, schema);// 增加商家商品信息
				LogUtil.writeCommodityLog("增加商家商品信息：" + redult);

				if (redult) {// 商品增加失败，相关联的数据就不查了
					if (sync_type.equals(GoodsUtil.GOODS_CATCH_UPDATE_NEW)) {
						try {
							GoodsSyncRecordSchema goodsSyncRecordSchema = new GoodsSyncRecordSchema();// 商品上新
							goodsSyncRecordSchema.setN_shop_id(shopId);// 店铺ID
							goodsSyncRecordSchema.setN_goods_id(goodsId);// 商品ID
							goodsSyncRecordSchema.setN_task_id(taskId);// 任务ID
							goodsSyncRecordSchema.setN_num(num + "");// 商品爬取次数
							bo.addGoodsSyncRecord(goodsSyncRecordSchema);// 增加商品上新
						} catch (Exception e) {
							logger.error(shopId + " " + cThirdPlatformId + " " + taskId + " 增加商品上新失败：" + url, e);
						}
					}
					// 获取商品扩展属性，ＳＫＵ
					if (dataObj.has("props")){
						extendsInfo(goodsId, shopId, url, dataObj.getJSONArray("props"));// 获取商品的扩展属性
					}else{
						logger.error(url + " 没有props：");
					}
					// expressInfo(doc, goodsId, shopId, url);// 获取默认物流
					// getExpress(data, goodsId, shopId);// 获取默认物流
					// extendsInfo(doc, goodsId, shopId, url);// 获取商品的扩展属性
					skuAndPrice(dataObj, goodsId, shopId, schema.getC_goods_name(), url,goods_price,quantity);// 获取SKU和起批量
//					int shopIdInt = Integer.parseInt(shopId);
//					if (shopIdInt > 100) {// 100以下的店铺用来做测试
					if (itemInfoModelObj.has("picsPath")){
						boolean isImg = imgInfo(itemInfoModelObj.getJSONArray("picsPath"), goodsId, shopId, url);// 获取图片
					}
//						if (isImg) {// true表示(图片都没有，就删掉goods数据)
//							bo.deleteGoodsId(goodsId, shopId);// 删除商品信息
//						}
//					}
				} else {
					return false;
				}
			} else {
				// 商品已经存在
			}
		} catch (Exception e) {
			logger.error(shopId + " 获取商品详情信息失败：" + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), shopId + " 获取商品详情信息失败：" + url, LogUtil.getExceptionError(e));
			return false;
		}
		LogUtil.writeCommodityLog("获取商品详情信息GoodsUtil.goodsInfo()...........end " + shopId + " " + url);
		return true;
	}

	public static void extendsInfo(String goodsId, String shopId, String url, JSONArray props) throws JSONException {
		LogUtil.writeCommodityLog("taobao获取商品的扩展属性GoodsUtil.extendsInfo()...........start " + goodsId + " " + shopId);
		CommodityBO bo = new CommodityBO();
		for (int i = 0; i < props.length(); i++) {
			String key = props.getJSONObject(i).getString("name");
			String val = props.getJSONObject(i).getString("value");
			GoodsExtendsPropertySchema schema = new GoodsExtendsPropertySchema();
			schema.setN_goods_id(goodsId);// 商品ID
			schema.setC_prop_key_label(key);// 扩展属性键label
			schema.setC_prop_value(val);// 扩展属性值
			schema.setN_shop_id(shopId);// 卖家ID
			try {
				boolean redult = bo.addGoodsExtendsProperty(schema);// 增加商品扩展属性
				LogUtil.writeCommodityLog("taobao增加商品扩展属性：" + redult);
			} catch (Exception e) {
				logger.error("taobao获取商品的扩展属性：" + goodsId + " " + shopId + " " + url, e);
			}

		}
		LogUtil.writeCommodityLog("taobao获取商品的扩展属性GoodsUtil.extendsInfo()...........end " + goodsId + " " + shopId);
	}

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
	public static String htmlInfo(String cThirdPlatformId, String shopId, String goodsId) throws Exception {
		LogUtil.writeCommodityLog("获取详情页商品介绍并创建html文件GoodsUtil.htmlInfo()...........start " + cThirdPlatformId + " "
				+ shopId + " " + goodsId);

		long begin = java.lang.System.currentTimeMillis(); // 开始时间
//		String url = "http://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?data=%7B%22item_num_id%22%3A%22"
//				+ cThirdPlatformId + "%22%7D";
		String url = "http://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?data={\"item_num_id\":\""+cThirdPlatformId+"\"}";
//		String xml = JsoupUtil.conUrl(url, false, "utf-8", "", 0);
		String xml = JsoupUtil.getUrl(url,20,"utf-8");
//		logger.error("---------lin-----"+xml);
		JSONObject json = new JSONObject(xml);
		JSONObject dataObj = json.getJSONObject("data");
		JSONArray pagesArr = dataObj.getJSONArray("pages");
		JSONArray imagesArr = dataObj.getJSONArray("images");

		String pathUrl = "";

		CommodityBO bo = new CommodityBO();
		for (int i = 0; i < imagesArr.length(); i++) {// 存储详情页中的图片
			try {
				String src = imagesArr.get(i).toString();// 图片地址
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

		String div = "";
		for (int i = 0; i < pagesArr.length(); i++) {

			div += pagesArr.get(i).toString();
		}
		Document doc = Jsoup.parse(div);
		Elements eles = doc.select("img");
		for (int i = 0; i < eles.size(); i++) {
			eles.get(i).attr("src", eles.get(i).nextSibling().toString());
			eles.get(i).nextSibling().remove();
		}
		div = doc.select("body").html();

		long end = java.lang.System.currentTimeMillis(); // 结束时间
		String html = bodyStart() + div + bodyEnd();
		String htmlUrl = "/html/" + shopId + "/" + cThirdPlatformId + ".html";
		boolean flag = file(html, shopId, htmlUrl);// 创建html文件
		if (flag) {
			pathUrl = htmlUrl;
		} else {
			pathUrl = "";
		}
		LogUtil.writeCommodityLog("获取详情页商品介绍并创建html文件GoodsUtil.htmlInfo()...........end " + cThirdPlatformId + " "
				+ shopId + " " + goodsId + " 图片数量：" + imagesArr.length() + " 耗时：" + (end - begin) + " " + pathUrl);
		return pathUrl;
	}

	/**
	 * html头部
	 * 
	 * @return
	 */
	public static String bodyStart() {
		LogUtil.writeCommodityLog("html头部GoodsUtil.bodyStart()...........start");

		StringBuffer sb = new StringBuffer();
		sb.append("<!doctype html>\n");
		sb.append("<html lang=\"zh-CN\">\n");
		sb.append("<head>\n");
		sb.append("<meta charset=\"utf-8\">\n");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1,user-scalable=no\">\n");
		sb.append("<title>商品详情</title>\n");
		sb.append("<link href=\"http://cd-css.qiniudn.com/pure.min.css\" rel=\"stylesheet\">\n");
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
		sb.append("<script src=\"http://cd-lib.qiniudn.com/jquery.min.js\"></script>\n");
		sb
				.append("<script>$(document).ready(function(){$(\"[href*='1688.com']\").each(function(index, element) {$(this).remove()});$(\"[src*='taobao.com']\").each(function(index, element) {$(this).remove()});$(\"[usemap*='#Map']\").each(function(index, element) {$(this).remove()});});</script>\n");
		sb
				.append("<script>$(\"table\").removeClass(\"pure-table\");$(\"td\").removeAttr(\"width\").removeAttr(\"height\").removeAttr(\"style\"); $(\"img\").removeAttr(\"width\");$(\"img\").removeAttr(\"height\");$(\"span\").removeAttr(\"width\");$(\"span\").removeAttr(\"height\");$(\"p\").removeAttr(\"width\");$(\"p\").removeAttr(\"height\");$(\"img\").removeAttr(\"style\");$(\"div\").removeAttr(\"style\");$(\"table\").removeAttr(\"style\");$(\"table\").removeAttr(\"background\");$(\"p\").removeAttr(\"style\");$(\"span\").removeAttr(\"style\");document.body.innerHTML=document.body.innerHTML.replace(/[巴巴|阿里|代理|价格|购买|元|货号|编号]/g,\"\");</script>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		LogUtil.writeCommodityLog("html尾部GoodsUtil.bodyEnd()...........end");
		return sb.toString();
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
	public static boolean file(String html, String shopId, String htmlUrl) throws Exception {
		LogUtil.writeCommodityLog("内容写入到html文件GoodsUtil.file()...........start " + shopId + " " + htmlUrl);

		boolean flag = false;
		OutputStreamWriter write = null;
		BufferedWriter writer = null;
		try {
			String filePath = JsoupUtil.SystemPath() + htmlUrl.substring(0, htmlUrl.lastIndexOf("/") + 1);
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
			sms.send(CommodityContent.getRecipients(), "内容写入到html文件失败" + shopId + " " + htmlUrl, LogUtil
					.getExceptionError(e));
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (write != null) {
				write.close();
			}
		}
		LogUtil.writeCommodityLog("内容写入到html文件GoodsUtil.file()...........end " + flag);
		return flag;
	}

	public static void skuAndPrice(JSONObject dataObj, String goodsId, String shopId, String goodsName, String url, String mPrice, String mStock) {
		CommodityBO bo = new CommodityBO();
		try {
			JSONObject skuModelObj = dataObj.getJSONObject("skuModel");
			if (!skuModelObj.has("skuProps")) {
//				logger.error("获取SKU异常,没有skuProps：" + goodsId + " " + shopId + " " + goodsName + " " + url);
				GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
				schema.setN_goods_id(goodsId);// 商品ID
				schema.setN_num_start("1");// 开始数量
				schema.setN_num_end("9999999");// 结束数量
				schema.setN_price(JsoupUtil.priceFen(mPrice));// 价格
				schema.setN_shop_id(shopId);// 卖家ID
				try {
					boolean redult = bo.addGoodsPriceSection(shopId, schema);// 增加商品价格区间信息
					LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
				} catch (Exception e) {
					logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId + " " + goodsName + " " + url, e);
				}
				
				GoodsSkuListSchema schemaSkuList = new GoodsSkuListSchema();
				schemaSkuList.setN_goods_id(goodsId);// 商品ID
				schemaSkuList.setN_shop_id(shopId);// 店铺ID
				schemaSkuList.setC_sku_name(goodsName);// sku名称，该节点值的名称，如红色
				schemaSkuList.setC_sku_prop("商品");// sku属性名，如颜色
				schemaSkuList.setN_sku_level("0");// 层级，如0，1，2……
				schemaSkuList.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

				boolean addSkuListRedult = bo.addGoodsSkuList(schemaSkuList);
				// 增加商品SKU列表信息
				LogUtil
						.writeCommodityLog("增加商品SKU列表信息："
								+ addSkuListRedult);

				List<GoodsSkuListSchema> skuList = bo.getGoodsSkuListBygId(goodsId);// 获取SKU列表信息
				
				GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
				schemaInfo.setC_sku_id(goodsId + "_" + 0);// SKUID
				schemaInfo.setN_goods_id(goodsId);// 商品ID
				schemaInfo.setN_shop_id(shopId);// 店铺ID
				schemaInfo.setN_stock_num(mStock);// 库存
				schemaInfo.setN_discount_price("0");// 折扣价格
				schemaInfo.setN_price(JsoupUtil.priceFen(mPrice));// 价格
				
				if (skuList != null && skuList.size() > 0) {
					schemaInfo.setC_sku_list_id(skuList.get(0).getN_id());// sku列表的ID集合
				}
				schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

				boolean redult1 = bo.addGoodsSkuInfo(shopId, schemaInfo);// 增加商品SKU详情信息
				LogUtil.writeCommodityLog("增加商品SKU详情信息：" + redult1);
				
				return;
			}
			JSONArray skuPropsArr = skuModelObj.getJSONArray("skuProps");
			Map<String, String> keyNameMap = new HashMap<String, String>();
			Map<String, String> skuListMapBy = bo.getGoodsSkuListBygoodsId(goodsId);// map<sku名称,skuId>
			for (int i = 0; i < skuPropsArr.length(); i++) {
				JSONObject itemObj = skuPropsArr.getJSONObject(i);

				GoodsSkuListSchema schema = new GoodsSkuListSchema();
				schema.setN_goods_id(goodsId);// 商品ID
				schema.setN_shop_id(shopId);// 店铺ID

				schema.setC_sku_prop(itemObj.getString("propName"));// sku属性名，如颜色
				schema.setN_sku_level(i + "");// 层级，如0，1，2……
				schema.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指
				JSONArray valuesArr = itemObj.getJSONArray("values");

				for (int j = 0; j < valuesArr.length(); j++) {
					String name = valuesArr.getJSONObject(j).getString("name");
					if (!skuListMapBy.containsKey(name)) {// 数据库中没有就添加
						schema.setC_sku_name(name);// sku名称，该节点值的名称，如红色
						boolean redult = bo.addGoodsSkuList(schema);
						// 增加商品SKU列表信息
						LogUtil.writeCommodityLog("增加商品SKU列表信息：" + redult);
					}
					keyNameMap.put(valuesArr.getJSONObject(j).getString("valueId"), name);
				}

			}

			Map<String, String> skuListMap = bo.getGoodsSkuListBygoodsId(goodsId);// map<sku名称,skuId>
			Map<String, GoodsSkuInfoSchema> skuInfoMap = bo.getGoodsSkuInfoBygoodsId(goodsId);// Map<sku_list_id,skuInfo对象>

			JSONObject ppathIdmapObj = skuModelObj.getJSONObject("ppathIdmap");
			Iterator it = ppathIdmapObj.keys();
			Map<String, String> ppathIdmapObjMap = new HashMap<String, String>();
			Map<String, String> ppathIdmapObjMapLink = new HashMap<String, String>();
			while (it.hasNext()) {
				String key1 = "";
				String key = (String) it.next();
				String value = ppathIdmapObj.getString(key);
				// 转换key:20509:28314;1627207:28320-->100;100
				String[] keyArr = key.split(";");
				for (int i = 0; i < keyArr.length; i++) {
					String[] tempArr = keyArr[i].split(":");
					if (keyNameMap.containsKey(tempArr[1]) && skuListMap.containsKey(keyNameMap.get(tempArr[1]))) {
						key1 += (key1.isEmpty() ? "" : ";") + skuListMap.get(keyNameMap.get(tempArr[1]));
					}
				}
				if (!key1.isEmpty()) {
					ppathIdmapObjMap.put(key1, value);
				}
				ppathIdmapObjMapLink.put(key1, key);
			}

			int skuId = 0;
			int totalStock = 0;
			JSONObject extrasObj = dataObj.getJSONObject("extras");
			String defDynJson = extrasObj.getString("defDyn");
			JSONObject defDynJsonObj = new JSONObject(defDynJson);
			JSONObject skusObj = defDynJsonObj.getJSONObject("skuModel").getJSONObject("skus");

			String price = "";
			for (String key : ppathIdmapObjMap.keySet()) {
				// System.out.println("key= " + key + " and value= "
				// + ppathIdmapObjMap.get(key));

				JSONObject skusKeyObj = skusObj.getJSONObject(ppathIdmapObjMap.get(key));
				price = skusKeyObj.getJSONArray("priceUnits").getJSONObject(0).getString("price"); // 398.00
				totalStock = skusKeyObj.getInt("quantity");
				if (!skuInfoMap.containsKey(key)) {// 不存在就新增
					GoodsSkuInfoSchema schemaInfo = new GoodsSkuInfoSchema();
					schemaInfo.setC_sku_id(goodsId + "_" + skuId);// SKUID
					schemaInfo.setN_goods_id(goodsId);// 商品ID
					schemaInfo.setN_shop_id(shopId);// 店铺ID
					schemaInfo.setN_stock_num(totalStock + "");// 库存
					schemaInfo.setN_discount_price("0");// 折扣价格
					schemaInfo.setN_price(JsoupUtil.priceFen(price));// 价格
					schemaInfo.setC_sku_list_id(key);// sku列表的ID集合
					schemaInfo.setC_sku_desc(goodsName);// sku描述：便于查看其含义，程序填充其指

					boolean redult1 = bo.addGoodsSkuInfo(shopId, schemaInfo);// 增加商品SKU详情信息
					skuId = skuId + 1;
					LogUtil.writeCommodityLog("增加商品SKU详情信息：" + redult1);
				} else {// 存在就需要修改库存或价格
					GoodsSkuInfoSchema skuInfo = skuInfoMap.get(key);
					int stockTp = Integer.parseInt(skuInfo.getN_stock_num());
					if (stockTp != totalStock || skuInfo.getN_price() != JsoupUtil.priceFen(price)) {// 库存不相等或者价格不相等的情况
						boolean redult1 = bo.updateGoodsSkuInfoById(totalStock + "", JsoupUtil.priceFen(price), skuInfo
								.getN_id());// 修改商品SKU详情信息
						LogUtil.writeCommodityLog("修改商品SKU详情信息：" + redult1);
					}
				}

			}

			GoodsPriceSectionSchema schema = new GoodsPriceSectionSchema();
			schema.setN_goods_id(goodsId);// 商品ID
			schema.setN_num_start("1");// 开始数量
			schema.setN_num_end("9999999");// 结束数量
			schema.setN_price(JsoupUtil.priceFen(price));// 价格
			schema.setN_shop_id(shopId);// 卖家ID
			try {
				boolean redult = bo.addGoodsPriceSection(shopId, schema);// 增加商品价格区间信息
				LogUtil.writeCommodityLog("增加商品价格区间信息：" + redult);
			} catch (Exception e) {
				logger.error("获取SKU和起批量异常：" + goodsId + " " + shopId + " " + goodsName + " " + url, e);
			}

		} catch (Exception e) {
			logger.error("获取SKU异常：" + goodsId + " " + shopId + " " + goodsName + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取SKU异常：" + goodsId + " " + shopId + " " + goodsName + " "
					+ url, LogUtil.getExceptionError(e));
		}

	}

	/**
	 * 获取图片
	 * 
	 * @param doc
	 */
	public static boolean imgInfo(JSONArray picsPathArr, String goodsId, String shopId, String url) {
		LogUtil.writeCommodityLog("获取图片GoodsUtil.imgInfo()...........start " + goodsId + " " + shopId);
		boolean image_default = true;// 图片默认标识
		try {
			CommodityBO bo = new CommodityBO();
			for (int i = 0; i < picsPathArr.length(); i++) {
				String src = picsPathArr.getString(i);
				LogUtil.writeCommodityLog("网络图片地址：" + src);
				int start = (int) System.currentTimeMillis(); // 开始时间
				// imgFile(src);//下载网页图片到本地并上传到七牛
				boolean redult = false;
				GoodsImageSchema schema = com.work.commodity.util.GoodsUtil.httpFile(shopId, src, url);// 网络图片上传到七牛
				if (StringUtils.isNotEmpty(schema.getN_width())) {
					String fileName = com.work.commodity.util.GoodsUtil.getPicName(shopId, src);// 图片名
					String path = com.work.commodity.util.GoodsUtil.getQiniuUrl() + fileName;

					schema.setN_goods_id(goodsId); // 商品ID，对应tb_goods表中的商品ID（主键）
					schema.setC_img_url(path); // 图片地址(访问时需要带上样式，例如：../jmg.jpg-class)
					schema.setN_shop_id(shopId);// 卖家ID
					if (image_default) {// 默认情况为1
						image_default = false;
						schema.setN_img_category("1"); // 商品图片分类：1列表页，2详情页
						redult = bo.addGoodsImage(schema);// 增加商品图片信息
					} else {
						schema.setN_img_category("2"); // 商品图片分类：1列表页，2详情页
						redult = bo.addGoodsImage(schema);// 增加商品图片信息
					}
					LogUtil.writeCommodityLog("增加商品图片信息：" + redult);
				} else {
					LogUtil.writeCommodityLog("增加商品图片信息1：" + redult);
				}
				int end = (int) System.currentTimeMillis(); // 结束时间
				int re = end - start; // 但图片生成处理时间
				LogUtil.writeCommodityLog("图片下载使用了: " + re + "毫秒");
			}
		} catch (Exception e) {
			logger.error("获取图片异常：" + goodsId + " " + shopId + " " + url, e);
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取图片异常：" + goodsId + " " + shopId + " " + url, LogUtil
					.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取图片GoodsUtil.imgInfo()...........end " + goodsId + " " + shopId);
		return image_default;
	}

}
