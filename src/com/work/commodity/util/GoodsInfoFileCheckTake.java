package com.work.commodity.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * ��Ʒ��ȡ��ɣ����̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoFileCheckTake implements Runnable {

	static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoFileCheckTake.class);

	String shopId;// ����id

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
