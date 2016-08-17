package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 检查店铺商品详情页是否包含二维码图
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoFileCheckService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoFileCheckService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsInfoFileCheckService......................");

		String shopid = request.getHeader("shopid");// 店铺ID
		String isali = request.getHeader("isali");// 是否抓阿里的页面（==1是抓阿里的页面）
		String isimgcheck = request.getHeader("isimgcheck");// 是否要检查二维码图片（==1检查二维码图）
		String num = request.getHeader("num");// 检查图片数量（不填或者填0检查所有图片）
		String imgurlcheck = request.getHeader("imgurlcheck");// 二维码图片地址（这是知道二维码地址的情况下做过滤）
		if (StringUtils.isEmpty(shopid)) {
			shopid = request.getParameter("shopid");
			isali = request.getParameter("isali");
			isimgcheck = request.getParameter("isimgcheck");
			num = request.getParameter("num");
			imgurlcheck = request.getParameter("imgurlcheck");
		}

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsInfoFileCheckService: shopid:"
					+ shopid + " isali:" + isali + " isimgcheck:" + isimgcheck
					+ " num:" + num + " imgurlcheck：" + imgurlcheck);
			GoodsUtil
					.htmlInfoZxing(shopid, isali, isimgcheck, num, imgurlcheck);
			jsonStr = JsoupUtil.getJson("0", "成功");
		} catch (Exception e) {
			logger.error("检查店铺商品详情页是否包含二维码图失败:" + shopid, e);
			jsonStr = JsoupUtil.getJson("0", "失败");
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
