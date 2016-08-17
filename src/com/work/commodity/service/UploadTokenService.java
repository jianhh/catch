package com.work.commodity.service;

import java.util.Date;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.PutPolicy;
import com.work.commodity.content.CommodityContent;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 获取七牛uploadToken
 * 
 * @author tangbiao
 * 
 */
public class UploadTokenService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(UploadTokenService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("uploadTokenService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String jsonStr = "";
		try {
			Config.ACCESS_KEY = CommodityContent.ACCESS_KEY;
			Config.SECRET_KEY = CommodityContent.SECRET_KEY;
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			DateUtil.format2.format(new Date());
			PutPolicy putPolicy = new PutPolicy(CommodityContent
					.getBucketName());
			putPolicy.returnBody = "{\"width\":$(imageInfo.width),\"height\":$(imageInfo.height)}";// 设置响应速度
			String uptoken = putPolicy.token(mac);
			jsonStr = JsoupUtil.getUptokenJson("0", "成功", uptoken);
		} catch (Exception e) {
			logger.error("获取七牛uploadToken失败:", e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取七牛uploadToken失败:",
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取七牛uploadToken,jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("uploadTokenService:"
				+ "获取七牛uploadToken共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
