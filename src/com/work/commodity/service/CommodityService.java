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
 * ץȡ�̼���Ϣ
 * 
 * @author tangbiao
 * 
 */
public class CommodityService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CommodityService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CommodityService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID
		String url = request.getHeader("url");// ���̵�ַ

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CommodityService: sellerid:" + sellerid
					+ " url:" + url);
			String state = CommodityUtil.commodityInfo(sellerid, url);// ��ȡ�̼�������Ʒ��Ϣ
			if (state.equals("0")) {
				jsonStr = JsoupUtil.getJson(state, "�ɹ�");
				try {
					CommodityBO bo = new CommodityBO();
					ShopSchema info = bo.getShopById(sellerid);// ������Ϣ
					if (info != null) {
						ShopAliSchema infoSchema = CommodityUtil
								.memSellerBasicInfo(sellerid, url);// ��ȡ���̻�����Ϣ(�޸��˵�����Ϣ�еĹ�˾�����ֶ�)
						if (infoSchema != null) {
							boolean flag = bo.updateShopAli(infoSchema);// ���ӵ��̻�����Ϣ
							LogUtil.writeCommodityLog(sellerid + "���ӵ��̻�����Ϣ���:"
									+ flag);
						}
						// if (infoSchema != null) {
						// if (StringUtils.isNotEmpty(infoSchema
						// .getC_company_desc())) {
						// boolean flag = bo.updateMemSellerInfoByDesc(
						// sellerid, infoSchema
						// .getC_company_desc());
						// LogUtil.writeCommodityLog(sellerid
						// + "�޸��̼���Ϣ���:" + flag);
						// }
						// String html = CommodityUtil.getCommodityIndexHtml(
						// info, infoSchema);// �̼��ڴ������ҳhtml
						// String htmlUrl = "/html/" + sellerid
						// + "/index.html";
						// boolean htmlflag = CommodityUtil.file(html,
						// sellerid, htmlUrl);// ����html�ļ�
						// if (htmlflag) {
						// boolean redult = bo.updateMemSellerInfoByUrl(
						// sellerid, htmlUrl);
						// LogUtil.writeCommodityLog(sellerid
						// + "������ҳ���ɽ��:" + redult);
						// }
						// }
					}
				} catch (Exception e) {
					logger.error("���ɵ�����ҳʧ��:" + sellerid + " " + url, e);
					// �����ʼ�
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms.send(CommodityContent.getRecipients(), "���ɵ�����ҳʧ��:"
							+ sellerid + " " + url, LogUtil
							.getExceptionError(e));
				}
			} else if (state.equals("1")) {
				jsonStr = JsoupUtil.getJson(state, "���̲�����");
			} else if (state.equals("2")) {
				jsonStr = JsoupUtil.getJson(state, "�����������");
			} else {
				jsonStr = JsoupUtil.getJson(state, "ʧ��");
			}
		} catch (Exception e) {
			logger.error("ץȡ�̼���Ϣʧ��:" + sellerid + " " + url, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "ץȡ�̼���Ϣʧ��:" + sellerid
					+ " " + url, e);
		}
		LogUtil.writeCommodityLog(sellerid + "ץȡ�̼���ϢjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("CommodityService:" + sellerid + "������ȡ����ʱ: "
				+ re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
