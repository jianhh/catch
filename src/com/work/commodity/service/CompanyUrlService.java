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
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.AliGetUrlUtil;
import com.work.util.JsoupUtil;

/**
 * 获取店铺地址
 * 
 * @author tangbiao
 * 
 */
public class CompanyUrlService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CompanyUrlService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CompanyUrlService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String accountId = request.getHeader("account_id");// 帐号ID
		String goodsId = request.getHeader("goods_id");// 商品ID

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CompanyUrlService: accountId:"
					+ accountId + " goodsId:" + goodsId);
			Document doc = AliGetUrlUtil
					.getDocumentUrl("http://detail.1688.com/offer/" + goodsId
							+ ".html");
			String goodsName = GoodsUtil.titleInfo(doc);
			if (StringUtils.isNotEmpty(goodsName)) {
				if (goodsName.toLowerCase().indexOf("cd" + accountId) >= 0) {
					String url = GoodsUtil.indexUrl(doc);// 获取域名首页地址
					jsonStr = JsoupUtil.getCompanyUrlJson("0", "成功", url);
				} else {
					jsonStr = JsoupUtil.getJson("1", "商品不匹配");
				}
			} else {
				jsonStr = JsoupUtil.getJson("2", "商品ID输入错误");
			}
		} catch (Exception e) {
			logger.error("获取店铺名称失败:" + accountId + " " + goodsId, e);
			jsonStr = JsoupUtil.getJson("10000", "获取店铺地址失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取店铺地址失败:" + accountId
					+ " " + goodsId, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(accountId + " " + goodsId + "获取店铺地址jsonStr:"
				+ jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CompanyUrlService: accountId:" + accountId
				+ " goodsId:" + goodsId + "获取店铺地址共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
