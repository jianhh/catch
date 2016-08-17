package com.work.taobao.util;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StartContent;
import com.work.commodity.schema.GoodsSchema;
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.service.GoodsService;
import com.work.util.JsoupUtil;

/**
 * ��Ŀ���������̼߳����Ʒû��ȡ�������
 * 
 * @author tangbiao
 * 
 */
public class TBCatchRecordTake implements Runnable {

	static JLogger logger = LoggerFactory.getLogger(TBCatchRecordTake.class);

	String shopId;// ����id
	String url;// ���̵�ַ
	String sync_type;// 1:�״�ץȡ��2:ͬ��������Ʒ,3:ͬ��������Ʒ
	String task_id;// ������

	public TBCatchRecordTake(String shopId, String url, String sync_type,
			String task_id) {

		this.shopId = shopId;
		this.url = url;
		this.sync_type = sync_type;
		this.task_id = task_id;
	}

	public void run() {

		logger.debug("CatchRecordTake  start  ...shopId��" + shopId + " url��"
				+ url + " sync_type��" + sync_type + " task_id��" + task_id);

		try {
			int start = (int) System.currentTimeMillis(); // ��ʼʱ��

			CommodityBO bo = new CommodityBO();
			GoodsSchema info = bo.getGoodsSchemaByshopId(shopId);// ������һ����Ʒ
			if (info != null) {
				bo.deleteGoodsById(info.getN_goods_id(), shopId);// ɾ����Ʒ����Ʒ��ص���Ϣ
			}
			GoodsService goods = new GoodsService();
			goods.goods(shopId, url, sync_type, task_id, false);

			String sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ shopId + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=0&msg=�ɹ�";
			String xml = JsoupUtil.conUrl(sendUrl, true, "utf-8", "", 0);
			LogUtil.writeInterfaceLog("��Ʒץȡ���֪ͨ�ӿ�:" + sendUrl + "\n" + xml);
			int end = (int) System.currentTimeMillis(); // ����ʱ��
			int re = end - start; // ����ʱ��
			LogUtil.writeCommodityLog("CatchRecordTake:" + shopId + " "
					+ sync_type + "������ȡ��Ʒ����ʱ: " + re + "����");
			logger.debug("CatchRecordTake  end  ...shopId��" + shopId + " url��"
					+ url + " sync_type��" + sync_type + " task_id��" + task_id);
		} catch (Exception e) {
			logger.error("ץȡ��Ʒ��Ϣʧ��:" + shopId + " " + sync_type, e);
		}
	}

}
