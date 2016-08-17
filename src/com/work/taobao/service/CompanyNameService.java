package com.work.taobao.service;

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
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.AliGetUrlUtil;
import com.work.util.JsoupUtil;

/**
 * 获取店铺名称
 * 
 * @author tangbiao
 * 
 */
public class CompanyNameService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CompanyNameService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CompanyNameService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String url = request.getHeader("url");// 店铺地址

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CompanyNameService: url:" + url);
			url += "/page/creditdetail.htm";
			// http://shop1403110510248.1688.com
			Document doc = AliGetUrlUtil.getDocumentUrl(url);
			String companyName = CommodityUtil.commodityName(doc, url);// 公司名
			String shopName = CommodityUtil.shopName(companyName);// 店铺名称
			if (StringUtils.isNotEmpty(shopName)) {
				if ("阿里巴巴".equals(companyName)) {
					jsonStr = JsoupUtil.getJson("2", "访问频率过高");
				} else {
					jsonStr = JsoupUtil.getCompanyNameJson("0", "成功", shopName);
				}
			} else {
				jsonStr = JsoupUtil.getJson("1", "域名输入错误");
			}
		} catch (Exception e) {
			logger.error("获取店铺名称失败:" + url, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取店铺名称失败:" + url,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(url + "获取店铺名称jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CompanyNameService:" + url + "获取店铺名称共耗时: "
				+ re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
