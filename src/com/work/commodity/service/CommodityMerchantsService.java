package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.util.CommodityUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * ץȡ�̼Ҵ��������Ϣ
 * 
 * @author tangbiao
 * 
 */
public class CommodityMerchantsService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityMerchantsService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityMerchantsService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID
		String url = request.getHeader("url");// ���̵�ַ

		String jsonStr = JsoupUtil.getJson("10001", "����δ�ṩ������Ϣ");
		try {
			LogUtil.writeCommodityLog("CommodityMerchantsService: sellerid:"
					+ sellerid + " url:" + url);
			String htmlUrl = CommodityUtil.htmlMerchants(sellerid, url);// ��ȡ���̴�����˽��ܲ�����html�ļ�
			if (StringUtils.isNotEmpty(htmlUrl)) {
				CommodityBO bo = new CommodityBO();
				// boolean redult = bo.updateShop(sellerid, htmlUrl);
				// LogUtil.writeCommodityLog("�޸��̼���Ϣ��" + redult);
				// if (redult) {
				// jsonStr = JsoupUtil.getJson("0", "�ɹ�");
				// }
			}
		} catch (Exception e) {
			logger.error("ץȡ�̼Ҵ��������Ϣʧ��:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "ץȡ�̼Ҵ��������Ϣʧ��:"
					+ sellerid + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(sellerid + "ץȡ�̼Ҵ��������ϢjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("CommodityMerchantsService:" + sellerid
				+ "ץȡ�̼Ҵ��������Ϣ����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) throws Exception {

		String htmlUrl = CommodityUtil.htmlMerchants("1000",
				"http://shop1408351627058.1688.com/");// ��ȡ���̴�����˽��ܲ�����html�ļ�
	}
}
