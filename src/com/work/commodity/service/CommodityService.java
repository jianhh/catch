package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.schema.ShopSchema;
import com.work.commodity.util.CommodityUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 抓取商家信息
 * 
 * @author tangbiao
 * 
 */
public class CommodityService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID
		String url = request.getHeader("url");// 店铺地址

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CommodityService: sellerid:" + sellerid
					+ " url:" + url);
			String state = CommodityUtil.commodityInfo(sellerid, url);// 获取商家所有商品信息
			if (state.equals("0")) {
				jsonStr = JsoupUtil.getJson(state, "成功");
				try {
					CommodityBO bo = new CommodityBO();
					ShopSchema info = bo.getShopById(sellerid);// 店铺信息
					if (info != null) {
						ShopAliSchema infoSchema = CommodityUtil
								.memSellerBasicInfo(sellerid, url);// 获取店铺基本信息(修改了店铺信息中的公司介绍字段)
						if (infoSchema != null) {
							boolean flag = bo.updateShopAli(infoSchema);// 增加店铺基本信息
							LogUtil.writeCommodityLog(sellerid + "增加店铺基本信息结果:"
									+ flag);
						}
						// if (infoSchema != null) {
						// if (StringUtils.isNotEmpty(infoSchema
						// .getC_company_desc())) {
						// boolean flag = bo.updateMemSellerInfoByDesc(
						// sellerid, infoSchema
						// .getC_company_desc());
						// LogUtil.writeCommodityLog(sellerid
						// + "修改商家信息结果:" + flag);
						// }
						// String html = CommodityUtil.getCommodityIndexHtml(
						// info, infoSchema);// 商家在触店的首页html
						// String htmlUrl = "/html/" + sellerid
						// + "/index.html";
						// boolean htmlflag = CommodityUtil.file(html,
						// sellerid, htmlUrl);// 创建html文件
						// if (htmlflag) {
						// boolean redult = bo.updateMemSellerInfoByUrl(
						// sellerid, htmlUrl);
						// LogUtil.writeCommodityLog(sellerid
						// + "触店首页生成结果:" + redult);
						// }
						// }
					}
				} catch (Exception e) {
					logger.error("生成店铺主页失败:" + sellerid + " " + url, e);
					// 发送邮件
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), "生成店铺主页失败:"
							+ sellerid + " " + url, LogUtil
							.getExceptionError(e));
				}
			} else if (state.equals("1")) {
				jsonStr = JsoupUtil.getJson(state, "店铺不存在");
			} else if (state.equals("2")) {
				jsonStr = JsoupUtil.getJson(state, "域名输入错误");
			} else {
				jsonStr = JsoupUtil.getJson(state, "失败");
			}
		} catch (Exception e) {
			logger.error("抓取商家信息失败:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "抓取商家信息失败:" + sellerid
					+ " " + url, e);
		}
		LogUtil.writeCommodityLog(sellerid + "抓取商家信息jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CommodityService:" + sellerid + "店铺爬取共耗时: "
				+ re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
