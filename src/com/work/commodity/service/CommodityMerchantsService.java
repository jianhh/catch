package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.util.CommodityUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 抓取商家代理加盟信息
 * 
 * @author tangbiao
 * 
 */
public class CommodityMerchantsService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityMerchantsService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityMerchantsService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID
		String url = request.getHeader("url");// 店铺地址

		String jsonStr = JsoupUtil.getJson("10001", "卖家未提供加盟信息");
		try {
			LogUtil.writeCommodityLog("CommodityMerchantsService: sellerid:"
					+ sellerid + " url:" + url);
			String htmlUrl = CommodityUtil.htmlMerchants(sellerid, url);// 获取店铺代理加盟介绍并创建html文件
			if (StringUtils.isNotEmpty(htmlUrl)) {
				CommodityBO bo = new CommodityBO();
				// boolean redult = bo.updateShop(sellerid, htmlUrl);
				// LogUtil.writeCommodityLog("修改商家信息：" + redult);
				// if (redult) {
				// jsonStr = JsoupUtil.getJson("0", "成功");
				// }
			}
		} catch (Exception e) {
			logger.error("抓取商家代理加盟信息失败:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "抓取商家代理加盟信息失败:"
					+ sellerid + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(sellerid + "抓取商家代理加盟信息jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CommodityMerchantsService:" + sellerid
				+ "抓取商家代理加盟信息共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) throws Exception {

		String htmlUrl = CommodityUtil.htmlMerchants("1000",
				"http://shop1408351627058.1688.com/");// 获取店铺代理加盟介绍并创建html文件
	}
}
