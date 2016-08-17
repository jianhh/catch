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
import com.work.commodity.util.CommodityUtil;
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * ���￪��У��
 * 
 * @author tangbiao
 * 
 */
public class AliShopCheckService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(AliShopCheckService.class);

	@SuppressWarnings("static-access")
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("AliShopCheckService......................");

		String check_code = request.getHeader("check_code");// У����
		String goods_id = request.getHeader("goods_id");// ������Ʒid
		String is_cxt = request.getHeader("is_cxt");// �Ƿ���Ҫ��֤����ͨ��0�ǣ�1��(����ʹ�ô�1)

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("AliShopCheckService: check_code:"
					+ check_code + ";goods_id=" + goods_id);
			if (StringUtils.isEmpty(check_code)) {
				jsonStr = JsoupUtil.getJson("1", "У����Ϊ��");
			} else if (StringUtils.isEmpty(goods_id)) {
				jsonStr = JsoupUtil.getJson("2", "������ƷIDΪ��");
			} else {
				String url = "http://detail.1688.com/offer/" + goods_id
						+ ".html";

				Document goodsDoc = JsoupUtil.getDocument(url);
				String goodsName = GoodsUtil.titleInfo(goodsDoc);
				if (StringUtils.isNotEmpty(goodsName)) {// У������
					boolean is_check = false;
					if (!"1".equals(is_cxt)) {// У�����ͨ
						if (goodsName.toLowerCase().indexOf(
								check_code.toLowerCase()) > -1) {
							is_check = true;
						}
					} else {
						is_check = true;
					}
					if (is_check) {
						String aliUrl = CommodityUtil
								.aliIndexUrl(goodsDoc, url);// ��ȡ������ҳ��ַ
						if (StringUtils.isNotEmpty(aliUrl)) {
							boolean cxt = true;
							if (!"1".equals(is_cxt)) {// У�����ͨ
								cxt = CommodityUtil.aliCXT(goodsDoc, url);// �������ͨУ��
							}
							if (cxt) {
								String companyName = CommodityUtil
										.commodityName(goodsDoc, url);// ��ȡ��˾��
								String shopName = CommodityUtil
										.shopName(companyName);// ��������
								jsonStr = JsoupUtil.getShopCheckJson("0", "�ɹ�",
										shopName, aliUrl);
							} else {
								jsonStr = JsoupUtil.getJson("5", "���ǳ���ͨ����");
							}
						} else {
							jsonStr = JsoupUtil.getJson("4", "��ȡ������ҳ��ַʧ��");
						}
					} else {
						jsonStr = JsoupUtil.getJson("3", "����������û���ҵ�������("+check_code+")");
					}
				} else {
					jsonStr = JsoupUtil.getJson("6", "��Ʒ������");
				}
			}
		} catch (Exception e) {
			logger.error("���￪��У��ʧ��:" + goods_id, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "���￪��У��ʧ��:" + goods_id,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(goods_id + "���￪��У��jsonStr:" + jsonStr);
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

}
