package com.work.taobao.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.schema.ShopTaobaoSchema;
import com.work.taobao.util.CommodityUtil;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 阿里开店校验
 * 
 * @author tangbiao
 * 
 */
public class TbShopCheckService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(TbShopCheckService.class);

	@SuppressWarnings("static-access")
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("TbShopCheckService......................");

		String check_code = request.getHeader("check_code");// 校验码
		String tb_goods_id = request.getHeader("goods_id");// 淘宝商品id
		String is_cxt = request.getHeader("is_cxt");// 是否需要验证资质

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("TbShopCheckService: check_code:" + check_code + ";tb_goods_id=" + tb_goods_id);
			if (StringUtils.isEmpty(check_code)) {
				jsonStr = JsoupUtil.getJson("1", "校验码为空");
			} else if (StringUtils.isEmpty(tb_goods_id)) {
				jsonStr = JsoupUtil.getJson("2", "淘宝商品ID为空");
			} else {

				String url = "http://hws.m.taobao.com/cache/wdetail/5.0/?id=" + tb_goods_id
						+ "&ttid=2013@taobao_h5_1.0.0&exParams={}";
//				String xml = JsoupUtil.conUrl(url, false, "utf-8", "", 0);
				String xml = JsoupUtil.getUrl(url, 100, "utf-8");
//				logger.error("淘宝开店xml" + xml);
				ShopTaobaoSchema shopInfo = null;
				String goodsName = null;
				try {
					JSONObject json = new JSONObject(xml);
					JSONObject dataObj = json.getJSONObject("data");
					JSONObject sellerObj = dataObj.getJSONObject("seller");

					// 获取商家信息
					if (sellerObj != null) {
						shopInfo = GoodsUtil.catchShopInfo(null, url, sellerObj, null, false);
					}
					JSONObject itemInfoModelObj = dataObj.getJSONObject("itemInfoModel");
					goodsName = itemInfoModelObj.getString("title");
					System.out.println(json.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				int credit = 0;
				int bail = 0;
				try {
					credit = Integer.parseInt(shopInfo.getC_credit_level());
					bail = Integer.parseInt(shopInfo.getC_bail_amount());
				} catch (Exception e) {

				}
				if (StringUtils.isNotEmpty(goodsName)) {// 校验名称
					if ("1".equals(is_cxt) || goodsName.toLowerCase().indexOf(check_code.toLowerCase()) > -1) {
						if (shopInfo != null) {
							if (!"1".equals(is_cxt)) {// 校验资质
								String tbUrl = "https://shop" + shopInfo.getC_taobao_shop_id() + ".taobao.com";
								if (credit >= 3) {
									if (true/*bail >= 1000*/) {
										
										jsonStr = JsoupUtil.getShopCheckJson("0", "成功", shopInfo.getC_shop_title(),
												tbUrl);
									} else {
										jsonStr = JsoupUtil.getJson("5", "该淘宝店铺未缴纳保证金");
									}
								} else {
									Document doc = GoodsUtil.getDoc(tbUrl);
									Elements divEle = doc.select("[class=shop-type-icon-enterprise]");
									if (divEle.size() > 0) {
										jsonStr = JsoupUtil.getShopCheckJson("0", "成功", shopInfo.getC_shop_title(),
												tbUrl);
									}else{
										jsonStr = JsoupUtil.getJson("7", "店铺信用等级不够");
									}
								}
							} else {
								String tbUrl = "https://shop" + shopInfo.getC_taobao_shop_id() + ".taobao.com";
								jsonStr = JsoupUtil.getShopCheckJson("0", "成功", shopInfo.getC_shop_title(),
										tbUrl);
							}
							

						} else {
							jsonStr = JsoupUtil.getJson("4", "获取淘宝店铺信息失败");
						}

					} else {
						jsonStr = JsoupUtil.getJson("3", "宝贝标题中没有找到触店码("+check_code+")");
					}
				} else {
					jsonStr = JsoupUtil.getJson("6", "商品不存在");
				}
			}
		} catch (Exception e) {
			logger.error("淘宝开店校验失败:" + tb_goods_id, e);
			jsonStr = JsoupUtil.getJson("10000", "网络请求失败,请输入正确的商品ID并稍后重试");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "淘宝开店校验失败:" + tb_goods_id, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(tb_goods_id + "淘宝开店校验jsonStr:" + jsonStr);
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
	
	
	public static void main(String[] args) {}

}
