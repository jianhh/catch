package com.work.taobao.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * ��ѯ�����Ƿ���������Ʒ
 * 
 * @author tangbiao
 * 
 */
public class GoodsNewService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsNewService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsNewService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID
		String url = request.getHeader("url");// ���̵�ַ

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsNewService: sellerid:" + sellerid
					+ " url:" + url);
			int code = GoodsUtil.goodsNew(sellerid, url);// 0��������;1��û����
			// ������ʧ��
			if (code == 0) {
				jsonStr = JsoupUtil.getJson(code + "", "������");
			} else if (code == 1) {
				jsonStr = JsoupUtil.getJson(code + "", "û����");
			} else {
				jsonStr = JsoupUtil.getJson(code + "", "ʧ��");
			}
		} catch (Exception e) {
			logger.error("��ѯ�����Ƿ���������Ʒʧ��:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "��ѯ�����Ƿ���������Ʒʧ��:"
					+ sellerid + " " + url, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("��ѯ�����Ƿ���������ƷjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("GoodsNewService:" + sellerid + " " + url
				+ "��ѯ�����Ƿ���������Ʒ����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
