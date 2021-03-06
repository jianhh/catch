package com.work.commodity.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 抓取商家商品信息(补充全量同步某个商品失败的情况)
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoService.class);

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
			if (StringUtils.isNotEmpty(sync_type) && sync_type.equals("1")) {// 1:商品爬取翻页请求超时
				int num = Integer.parseInt(pageNum);

				GoodsUtil.goodsInfoUrlByPage(sellerid, url
						+ "/page/offerlist.htm?sortType=timeup", num, 1);// 商品爬取
				GoodsUtil.goodsInfoUrlByTime(sellerid, url);// 爬取近30天的商品
				GoodsUtil.goodsInfoFileCheckTake(sellerid);// 开线程检查店铺的所有商品详情文件中是否有二维码图
				jsonStr = JsoupUtil.getJson("0", "成功");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("2")) {// 2:单个商品重新爬取
				String nid = "";
				String ngoodsid = "";
				String n_third_total_sell = "0";
				GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// 根据第三方平台商品ID查询
				if (info != null) {
					nid = info.getN_id();
					ngoodsid = info.getN_goods_id();
					n_third_total_sell = info.getN_third_total_sell();
					bo.deleteGoodsById(info.getN_goods_id(), sellerid);// 删除商品及商品相关的信息
				}

				LogUtil.writeCommodityLog("GoodsInfoService:sellerid："
						+ sellerid + " goodsid：" + goodsid);
				// 10000 http://detail.1688.com/offer/41455062632.html
				GoodsUtil.goodsInfoCatch(sellerid,
						"http://detail.1688.com/offer/" + goodsid + ".html",
						false, false, nid, ngoodsid, n_third_total_sell,
						goodsid);

				jsonStr = JsoupUtil.getJson("0", "成功");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("3")) {// 3：单个商品更新
				if (StringUtils.isEmpty(sellerid)
						|| StringUtils.isEmpty(goodsid)) {
					jsonStr = JsoupUtil.getJson("2", "参数不对");
				}
				int refult = GoodsUtil.goodsInfoUpdateCatch(sellerid, goodsid);
				if (refult == 1) {
					jsonStr = JsoupUtil.getJson("1", "商品已下架");
				} else if (refult == 2) {
					jsonStr = JsoupUtil.getJson("2", "商品不存在");
				} else {
					List<String> cThirdPlatformIdList = new ArrayList<String>();
					cThirdPlatformIdList.add(goodsid);
					GoodsUtil.goodsInfoNewFileCheckTake(sellerid,
							cThirdPlatformIdList);// 开线程检查店铺的上新商品详情文件中是否有二维码图
					jsonStr = JsoupUtil.getJson("0", "成功");
				}
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("4")) {// 4：同步单个商品的图文详情
				Document doc = JsoupUtil.getDocument(url);
				GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// 根据第三方平台商品ID查询
				List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
				GoodsUtil.htmlInfo(doc, goodsid, sellerid,
						info.getN_goods_id(), infoImageList);
				if (infoImageList.size() > 0) {
					bo.updateGoodsInfoImageList(info, infoImageList);
				}
				jsonStr = JsoupUtil.getJson("0", "成功");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("5")) {// 5：重新同步店铺的图文详情
				GoodsUtil.goodsInfoHtml(sellerid, url
						+ "/page/offerlist.htm?sortType=timeup", 1, 1);
				jsonStr = JsoupUtil.getJson("0", "成功");
			}
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
