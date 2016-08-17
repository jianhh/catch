package com.work.taobao.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 查询店铺是否有上新商品
 * 
 * @author tangbiao
 * 
 */
public class GoodsNewService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsNewService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsNewService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID
		String url = request.getHeader("url");// 店铺地址

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsNewService: sellerid:" + sellerid
					+ " url:" + url);
			int code = GoodsUtil.goodsNew(sellerid, url);// 0：有上新;1：没上新
			// 其他：失败
			if (code == 0) {
				jsonStr = JsoupUtil.getJson(code + "", "有上新");
			} else if (code == 1) {
				jsonStr = JsoupUtil.getJson(code + "", "没上新");
			} else {
				jsonStr = JsoupUtil.getJson(code + "", "失败");
			}
		} catch (Exception e) {
			logger.error("查询店铺是否有上新商品失败:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "查询店铺是否有上新商品失败:"
					+ sellerid + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("查询店铺是否有上新商品jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsNewService:" + sellerid + " " + url
				+ "查询店铺是否有上新商品共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
