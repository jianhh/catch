package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.schema.ShopSchema;
import com.work.commodity.util.CommodityUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * ���ɵ�����ҳ
 * 
 * @author tangbiao
 * 
 */
public class CommodityInfoService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityInfoService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityInfoService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID

		String jsonStr = JsoupUtil.getJson("10000", "ʧ��");
		try {
			LogUtil.writeCommodityLog("CommodityInfoService: sellerid:"
					+ sellerid);
			CommodityBO bo = new CommodityBO();
			ShopSchema info = bo.getShopById(sellerid);
			if (info == null) {// ���̲�����
				jsonStr = JsoupUtil.getJson("1", "���̲�����");
			} else {
				ShopAliSchema infoSchema = bo.getShopAliById(sellerid);
				if (infoSchema != null) {
					String html = CommodityUtil.getCommodityIndexHtml(info,
							infoSchema);// ������ҳhtml
					String htmlUrl = "/html/" + sellerid + "/index.html";
					boolean htmlflag = CommodityUtil.file(html, sellerid,
							htmlUrl);// ����html�ļ�
					if (htmlflag) {
						boolean redult = bo.updateShopByUrl(sellerid, htmlUrl);
						LogUtil.writeCommodityLog(sellerid + "������ҳ���ɽ��:"
								+ redult);
						jsonStr = JsoupUtil.getJson("0", "�ɹ�");
					}
				}
			}
		} catch (Exception e) {
			logger.error("���ɵ�����ҳʧ��:" + sellerid, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "���ɵ�����ҳʧ��:" + sellerid,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(sellerid + "���ɵ�����ҳjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("CommodityInfoService:" + sellerid
				+ "���ɵ�����ҳ����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
