package com.work.commodity.service;

import java.util.List;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.util.CommodityUtil;
import com.work.util.JsoupUtil;

/**
 * 给店铺设置num值
 * 
 * @author tangbiao
 * 
 */
public class CompanyNumberService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CompanyNumberService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CompanyNumberService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CompanyNumberService:");
			CommodityBO bo = new CommodityBO();
			List<ShopAliSchema> shopAliList = bo.getShopAliSchema();// 获取全部ali店铺
			if (shopAliList.size() > 0) {
				for (ShopAliSchema shopAli : shopAliList) {
					String url = shopAli.getC_shop_index_url();
					if (StringUtils.isNotEmpty(url)) {
						String num = CommodityUtil.getTradeNumber(url);// 获取交易数量
						if (StringUtils.isNotEmpty(num)) {
							shopAli.setC_trade_number(num);
						} else {
							shopAli.setC_trade_number("0");
						}
					} else {
						shopAli.setC_trade_number("0");
					}
					bo.updateShopAli(shopAli);// 修改ali交易数量
				}
			}
		} catch (Exception e) {
			logger.error("修改ali交易数量失败:", e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
		}
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("CompanyNumberService:" + "修改ali交易数量共耗时: "
				+ re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
