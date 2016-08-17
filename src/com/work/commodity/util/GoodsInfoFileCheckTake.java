package com.work.commodity.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * 商品爬取完成，开线程检查店铺的所有商品详情文件中是否有二维码图
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoFileCheckTake implements Runnable {

	static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoFileCheckTake.class);

	String shopId;// 音乐id

	public GoodsInfoFileCheckTake(String shopId) {

		this.shopId = shopId;

	}

	public void run() {

		logger.debug("GoodsInfoFileCheckTake  start  ..." + shopId);

		try {
			GoodsUtil.htmlInfoZxing(shopId, "0", "1", "", "");
			logger.debug("GoodsInfoFileCheckTake  end  ..." + shopId);
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
