package com.work.taobao.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.taobao.util.GoodsUtil;

/**
 * ץȡ�̼���Ϣ,�Ƿ��Լ����̼������ж�
 * 
 * @author tangbiao
 * 
 */
public class TaobaoCommodityService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(TaobaoCommodityService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		// String sellerid = request.getHeader("sellerid");// ����ID
		String url = request.getHeader("url");// ���̵�ַ
		String item_id = request.getHeader("item_id");// ��������ƷID
//		GoodsUtil.getGoodsItemInfo(shopId, item_id, 0, 0, true, url, "");
		// String jsonStr = "";
		// try {
		// LogUtil.writeCommodityLog("CommodityService: sellerid:" + sellerid
		// + " url:" + url);
		// String state = CommodityUtil.commodityInfo(sellerid, url);//
		// ��ȡ�̼�������Ʒ��Ϣ
		// if (state.equals("0")) {
		// jsonStr = JsoupUtil.getJson(state, "�ɹ�");
		// try {
		// CommodityBO bo = new CommodityBO();
		// ShopSchema info = bo.getShopById(sellerid);// ������Ϣ
		// if (info != null) {
		// ShopAliSchema infoSchema = CommodityUtil
		// .memSellerBasicInfo(sellerid, url);// ��ȡ���̻�����Ϣ(�޸��˵�����Ϣ�еĹ�˾�����ֶ�)
		// if (infoSchema != null) {
		// boolean flag = bo.updateShopAli(infoSchema);// ���ӵ��̻�����Ϣ
		// LogUtil.writeCommodityLog(sellerid + "���ӵ��̻�����Ϣ���:"
		// + flag);
		// }
		// }
		// } catch (Exception e) {
		// logger.error("���ɵ�����ҳʧ��:" + sellerid + " " + url, e);
		// // �����ʼ�
		// SimpleMailSender sms = MailSenderFactory.getSender();
		// sms.send(CommodityContent.getRecipients(), "���ɵ�����ҳʧ��:"
		// + sellerid + " " + url, LogUtil
		// .getExceptionError(e));
		// }
		// } else if (state.equals("1")) {
		// jsonStr = JsoupUtil.getJson(state, "���̲�����");
		// } else if (state.equals("2")) {
		// jsonStr = JsoupUtil.getJson(state, "�����������");
		// } else {
		// jsonStr = JsoupUtil.getJson(state, "ʧ��");
		// }
		// } catch (Exception e) {
		// logger.error("ץȡ�̼���Ϣʧ��:" + sellerid + " " + url, e);
		// jsonStr = JsoupUtil.getJson("10000", "ʧ��");
		// // �����ʼ�
		// SimpleMailSender sms = MailSenderFactory.getSender();
		// sms.send(CommodityContent.getRecipients(), "ץȡ�̼���Ϣʧ��:" + sellerid
		// + " " + url, e);
		// }
//		LogUtil.writeCommodityLog(sellerid + "ץȡ�̼���ϢjsonStr:" + jsonStr);
//		int end = (int) System.currentTimeMillis(); // ����ʱ��
//		int re = end - start; // ����ʱ��
//		LogUtil.writeCommodityLog("CommodityService:" + sellerid + "������ȡ����ʱ: " + re + "����");
//		response.setContentType("text/json");
//		response.setCaseType(response.CASETYPE_FLUSH);
//		response.setFlushContent(jsonStr);
	}
}
