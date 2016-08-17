package com.work.taobao.service;

import org.jsoup.nodes.Document;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.util.CommodityUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.AliGetUrlUtil;
import com.work.util.JsoupUtil;

/**
 * ��ȡ��������
 * 
 * @author tangbiao
 * 
 */
public class CompanyNameService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CompanyNameService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CompanyNameService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String url = request.getHeader("url");// ���̵�ַ

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CompanyNameService: url:" + url);
			url += "/page/creditdetail.htm";
			// http://shop1403110510248.1688.com
			Document doc = AliGetUrlUtil.getDocumentUrl(url);
			String companyName = CommodityUtil.commodityName(doc, url);// ��˾��
			String shopName = CommodityUtil.shopName(companyName);// ��������
			if (StringUtils.isNotEmpty(shopName)) {
				if ("����Ͱ�".equals(companyName)) {
					jsonStr = JsoupUtil.getJson("2", "����Ƶ�ʹ���");
				} else {
					jsonStr = JsoupUtil.getCompanyNameJson("0", "�ɹ�", shopName);
				}
			} else {
				jsonStr = JsoupUtil.getJson("1", "�����������");
			}
		} catch (Exception e) {
			logger.error("��ȡ��������ʧ��:" + url, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "��ȡ��������ʧ��:" + url,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(url + "��ȡ��������jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("CompanyNameService:" + url + "��ȡ�������ƹ���ʱ: "
				+ re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
