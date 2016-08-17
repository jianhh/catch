package com.work.commodity.util;

import java.util.List;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;

/**
 * ��Ʒ������ȡ��ɣ����̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoNewFileCheckTake implements Runnable {

	static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoNewFileCheckTake.class);

	String shopId;// ����id
	List<String> cThirdPlatformIdList;// ���������Ʒid

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
