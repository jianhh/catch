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
 * 生成店铺主页
 * 
 * @author tangbiao
 * 
 */
public class CommodityInfoService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityInfoService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityInfoService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID

		String jsonStr = JsoupUtil.getJson("10000", "失败");
		try {
			LogUtil.writeCommodityLog("CommodityInfoService: sellerid:"
					+ sellerid);
			CommodityBO bo = new CommodityBO();
			ShopSchema info = bo.getShopById(sellerid);
			if (info == null) {// 店铺不存在
				jsonStr = JsoupUtil.getJson("1", "店铺不存在");
			} else {
				ShopAliSchema infoSchema = bo.getShopAliById(sellerid);
				if (infoSchema != null) {
					String html = CommodityUtil.getCommodityIndexHtml(info,
							infoSchema);// 触店首页html
					String htmlUrl = "/html/" + sellerid + "/index.html";
					boolean htmlflag = CommodityUtil.file(html, sellerid,
							htmlUrl);// 创建html文件
					if (htmlflag) {
						boolean redult = bo.updateShopByUrl(sellerid, htmlUrl);
						LogUtil.writeCommodityLog(sellerid + "触店首页生成结果:"
								+ redult);
						jsonStr = JsoupUtil.getJson("0", "成功");
					}
				}
			}
		} catch (Exception e) {
			logger.error("生成店铺主页失败:" + sellerid, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "生成店铺主页失败:" + sellerid,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(sellerid + "生成店铺主页jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CommodityInfoService:" + sellerid
				+ "生成店铺主页共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
