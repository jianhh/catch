package com.work.commodity.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StartContent;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.service.GoodsService;
import com.work.util.JsoupUtil;

/**
 * 项目启动，开线程检查商品没爬取完的数据
 * 
 * @author tangbiao
 * 
 */
public class CatchRecordTake implements Runnable {

	static JLogger logger = LoggerFactory.getLogger(CatchRecordTake.class);

	String shopId;// 店铺id
	String url;// 店铺地址
	String sync_type;// 1:首次抓取，2:同步上新商品,3:同步所有商品
	String task_id;// 任务编号

	public CatchRecordTake(String shopId, String url, String sync_type,
			String task_id) {

		this.shopId = shopId;
		this.url = url;
		this.sync_type = sync_type;
		this.task_id = task_id;
	}

	public void run() {

		logger.debug("CatchRecordTake  start  ...shopId：" + shopId + " url："
				+ url + " sync_type：" + sync_type + " task_id：" + task_id);

		try {
			int start = (int) System.currentTimeMillis(); // 开始时间

			CommodityBO bo = new CommodityBO();
			GoodsSchema info = bo.getGoodsSchemaByshopId(shopId);// 查出最后一条商品
			if (info != null) {
				bo.deleteGoodsById(info.getN_goods_id(), shopId);// 删除商品及商品相关的信息
			}
			GoodsService goods = new GoodsService();
			goods.goods(shopId, url, sync_type, task_id, 1);

			String sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ shopId + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=0&msg=成功";
			String xml = JsoupUtil.conUrl(sendUrl, true, "utf-8", "", 0);
			LogUtil.writeInterfaceLog("商品抓取完成通知接口:" + sendUrl + "\n" + xml);
			int end = (int) System.currentTimeMillis(); // 结束时间
			int re = end - start; // 处理时间
			LogUtil.writeCommodityLog("CatchRecordTake:" + shopId + " "
					+ sync_type + "店铺爬取商品共耗时: " + re + "毫秒");
			logger.debug("CatchRecordTake  end  ...shopId：" + shopId + " url："
					+ url + " sync_type：" + sync_type + " task_id：" + task_id);
		} catch (Exception e) {
			logger.error("抓取商品信息失败:" + shopId + " " + sync_type, e);
		}
	}

}
