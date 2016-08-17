package com.work.taobao.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.commodity.schema.GoodsSchema;
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 抓取商家商品信息(补充全量同步某个商品失败的情况)
 * 
 * @author tangbiao
 * 
 */
public class OpenShopGoodsJudgeService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(OpenShopGoodsJudgeService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsInfoService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID
		String goodsid = request.getHeader("goodsid");// 商品ID(第三方id)
		String sync_type = request.getHeader("sync_type");// 1:商品爬取翻页请求超时;2:单个商品重新爬取；3：单个商品更新；4：同步单个商品的图文详情；5：重新同步店铺的图文详情
		String pageNum = request.getHeader("pageNum");// 当前页数
		String url = request.getHeader("url");// 域名

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsInfoService: sellerid:" + sellerid
					+ " goodsid:" + goodsid + " sync_type:" + sync_type
					+ " pageNum:" + pageNum + " url:" + url);
			CommodityBO bo = new CommodityBO();
			String n_third_total_sell = "0";
			String c_price = "0";
			String ngoodsid = "";
			GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// 根据第三方平台商品ID查询
			if (info != null) {
				ngoodsid = info.getN_goods_id();
				n_third_total_sell = info.getN_third_total_sell();
				c_price = info.getN_sell_price();
				bo.deleteGoodsById(info.getN_goods_id(), sellerid);// 删除商品及商品相关的信息
			}

			GoodsUtil.getGoodsItemInfo(sellerid, goodsid, n_third_total_sell, c_price,false,url, ngoodsid, sync_type, null, 1);
			jsonStr = JsoupUtil.getJson("0", "成功");
					
		} catch (Exception e) {
			logger.error("抓取商家商品信息失败,sellerid:" + sellerid + " goodsid:"
					+ goodsid + " sync_type:" + sync_type + " pageNum:"
					+ pageNum + " url:" + url, e);
			jsonStr = JsoupUtil.getJson("1", "失败");
		}
		LogUtil.writeCommodityLog("抓取商家商品信息jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsInfoService:" + goodsid + "商品爬取耗时: "
				+ re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) {
		String url = "http://szbyfs8.1688.com";
		url = url.substring(url.indexOf("//") + 2, url.indexOf(".1688"));
		System.out.println(url);
	}
}
