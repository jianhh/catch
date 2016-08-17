package com.work.commodity.util;

import java.util.List;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * 商品上新爬取完成，开线程检查店铺的所有商品详情文件中是否有二维码图
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoNewFileCheckTake implements Runnable {

	static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoNewFileCheckTake.class);

	String shopId;// 音乐id
	List<String> cThirdPlatformIdList;// 阿里店铺商品id

	public GoodsInfoNewFileCheckTake(String shopId,
			List<String> cThirdPlatformIdList) {

		this.shopId = shopId;
		this.cThirdPlatformIdList = cThirdPlatformIdList;

	}

	public void run() {
		if (logger.isDebugEnabled())
			logger.debug("GoodsInfoNewFileCheckTake  start  ..." + shopId);

		try {
			GoodsUtil.htmlInfoZxingByNew(shopId, cThirdPlatformIdList);
			if (logger.isDebugEnabled())
				logger.debug("GoodsInfoNewFileCheckTake  end  ..." + shopId);
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
