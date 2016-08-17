package com.work.commodity.service;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.CfgSysCategorySchema;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * ��ȡ��Ʒ����
 * 
 * @author tangbiao
 * 
 */
public class GoodsCategoryService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsCategoryService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsCategoryService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String jsonStr = "";
		try {

			CommodityBO bo = new CommodityBO();
			String categoryId = "0";
			String url = "http://offer.1688.com/offer/asyn/category_selector.json?callback=jQuery17207204073464963585_1418739676341&loginCheck=N&dealType=getSubCatInfo&scene=offer&categoryId=";
			String xml = JsoupUtil
					.conUrl(url + categoryId, false, "GBK", "", 0);
			xml = xml.substring(xml.indexOf("(") + 1, xml.length() - 1);
			JSONObject json = new JSONObject(xml);
			JSONArray datas = json.getJSONArray("data");// ��һ����Ŀ
			for (int i = 0; i < datas.length(); i++) {
				JSONObject value = datas.getJSONObject(i);
				String name = value.getString("name");
				categoryId = value.getString("id");

				CfgSysCategorySchema schema = new CfgSysCategorySchema();// ��׼��Ʒ��Ŀ��Ϣ
				schema.setN_category_id(categoryId);// ����ID
				schema.setN_parent_id("0");// �����ID
				schema.setC_name(name);// ����
				schema.setN_level("1");// ����
				schema.setT_create_time(DateUtil.format8.format(new Date()));// ����ʱ��
				bo.addCfgSysCategorySchema(schema);// ���ӱ�׼��Ʒ��Ŀ��Ϣ

				String xml1 = JsoupUtil.conUrl(url + categoryId, false, "GBK",
						"", 0);
				xml1 = xml1.substring(xml1.indexOf("(") + 1, xml1.length() - 1);
				JSONObject json1 = new JSONObject(xml1);
				JSONArray datas1 = json1.getJSONArray("data");// �ڶ�����Ŀ
				for (int i1 = 0; i1 < datas1.length(); i1++) {
					JSONObject value1 = datas1.getJSONObject(i1);
					String name1 = value1.getString("name");
					String categoryId1 = value1.getString("id");

					CfgSysCategorySchema schema1 = new CfgSysCategorySchema();// ��׼��Ʒ��Ŀ��Ϣ
					schema1.setN_category_id(categoryId1);// ����ID
					schema1.setN_parent_id(categoryId);// �����ID
					schema1.setC_name(name1);// ����
					schema1.setN_level("2");// ����
					schema1.setT_create_time(DateUtil.format8
							.format(new Date()));// ����ʱ��
					bo.addCfgSysCategorySchema(schema1);// ���ӱ�׼��Ʒ��Ŀ��Ϣ

					String xml2 = JsoupUtil.conUrl(url + categoryId1, false,
							"GBK", "", 0);
					xml2 = xml2.substring(xml2.indexOf("(") + 1,
							xml2.length() - 1);
					JSONObject json2 = new JSONObject(xml2);
					JSONArray datas2 = json2.getJSONArray("data");// ��������Ŀ
					for (int i2 = 0; i2 < datas2.length(); i2++) {
						JSONObject value2 = datas2.getJSONObject(i2);
						String name2 = value2.getString("name");
						String categoryId2 = value2.getString("id");

						CfgSysCategorySchema schema2 = new CfgSysCategorySchema();// ��׼��Ʒ��Ŀ��Ϣ
						schema2.setN_category_id(categoryId2);// ����ID
						schema2.setN_parent_id(categoryId1);// �����ID
						schema2.setC_name(name2);// ����
						schema2.setN_level("3");// ����
						schema2.setT_create_time(DateUtil.format8
								.format(new Date()));// ����ʱ��
						bo.addCfgSysCategorySchema(schema2);// ���ӱ�׼��Ʒ��Ŀ��Ϣ
					}
				}
			}
		} catch (Exception e) {
			logger.error("��ȡ��Ʒ����ʧ��:", e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "��ȡ��Ʒ����ʧ��:", LogUtil
					.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("��ȡ��Ʒ����jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("GoodsCategoryService:��ȡ��Ʒ���๲��ʱ: " + re
				+ "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
