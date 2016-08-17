package com.work.commodity.service;

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
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.AliGetUrlUtil;
import com.work.util.JsoupUtil;

/**
 * ��ȡ���̵�ַ
 * 
 * @author tangbiao
 * 
 */
public class CompanyUrlService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(CompanyUrlService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("CompanyUrlService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String accountId = request.getHeader("account_id");// �ʺ�ID
		String goodsId = request.getHeader("goods_id");// ��ƷID

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("CompanyUrlService: accountId:"
					+ accountId + " goodsId:" + goodsId);
			Document doc = AliGetUrlUtil
					.getDocumentUrl("http://detail.1688.com/offer/" + goodsId
							+ ".html");
			String goodsName = GoodsUtil.titleInfo(doc);
			if (StringUtils.isNotEmpty(goodsName)) {
				if (goodsName.toLowerCase().indexOf("cd" + accountId) >= 0) {
					String url = GoodsUtil.indexUrl(doc);// ��ȡ������ҳ��ַ
					jsonStr = JsoupUtil.getCompanyUrlJson("0", "�ɹ�", url);
				} else {
					jsonStr = JsoupUtil.getJson("1", "��Ʒ��ƥ��");
				}
			} else {
				jsonStr = JsoupUtil.getJson("2", "��ƷID�������");
			}
		} catch (Exception e) {
			logger.error("��ȡ��������ʧ��:" + accountId + " " + goodsId, e);
			jsonStr = JsoupUtil.getJson("10000", "��ȡ���̵�ַʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "��ȡ���̵�ַʧ��:" + accountId
					+ " " + goodsId, LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(accountId + " " + goodsId + "��ȡ���̵�ַjsonStr:"
				+ jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("CompanyUrlService: accountId:" + accountId
				+ " goodsId:" + goodsId + "��ȡ���̵�ַ����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
