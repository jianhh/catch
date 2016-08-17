package com.work.commodity.service;

import org.jsoup.nodes.Document;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.util.CommodityUtil;
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 阿里开店校验
 * 
 * @author tangbiao
 * 
 */
public class AliShopCheckService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(AliShopCheckService.class);

	@SuppressWarnings("static-access")
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("AliShopCheckService......................");

		String check_code = request.getHeader("check_code");// 校验码
		String goods_id = request.getHeader("goods_id");// 阿里商品id
		String is_cxt = request.getHeader("is_cxt");// 是否需要验证诚信通，0是；1否(测试使用传1)

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("AliShopCheckService: check_code:"
					+ check_code + ";goods_id=" + goods_id);
			if (StringUtils.isEmpty(check_code)) {
				jsonStr = JsoupUtil.getJson("1", "校验码为空");
			} else if (StringUtils.isEmpty(goods_id)) {
				jsonStr = JsoupUtil.getJson("2", "阿里商品ID为空");
			} else {
				String url = "http://detail.1688.com/offer/" + goods_id
						+ ".html";

				Document goodsDoc = JsoupUtil.getDocument(url);
				String goodsName = GoodsUtil.titleInfo(goodsDoc);
				if (StringUtils.isNotEmpty(goodsName)) {// 校验名称
					boolean is_check = false;
					if (!"1".equals(is_cxt)) {// 校验诚信通
						if (goodsName.toLowerCase().indexOf(
								check_code.toLowerCase()) > -1) {
							is_check = true;
						}
					} else {
						is_check = true;
					}
					if (is_check) {
						String aliUrl = CommodityUtil
								.aliIndexUrl(goodsDoc, url);// 获取阿里首页地址
						if (StringUtils.isNotEmpty(aliUrl)) {
							boolean cxt = true;
							if (!"1".equals(is_cxt)) {// 校验诚信通
								cxt = CommodityUtil.aliCXT(goodsDoc, url);// 阿里诚信通校验
							}
							if (cxt) {
								String companyName = CommodityUtil
										.commodityName(goodsDoc, url);// 获取公司名
								String shopName = CommodityUtil
										.shopName(companyName);// 店铺名称
								jsonStr = JsoupUtil.getShopCheckJson("0", "成功",
										shopName, aliUrl);
							} else {
								jsonStr = JsoupUtil.getJson("5", "不是诚信通店铺");
							}
						} else {
							jsonStr = JsoupUtil.getJson("4", "获取阿里首页地址失败");
						}
					} else {
						jsonStr = JsoupUtil.getJson("3", "宝贝标题中没有找到触店码("+check_code+")");
					}
				} else {
					jsonStr = JsoupUtil.getJson("6", "商品不存在");
				}
			}
		} catch (Exception e) {
			logger.error("阿里开店校验失败:" + goods_id, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "阿里开店校验失败:" + goods_id,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(goods_id + "阿里开店校验jsonStr:" + jsonStr);
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

}
