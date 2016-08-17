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
 * 获取商品分类
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
		int start = (int) System.currentTimeMillis(); // 开始时间

		String jsonStr = "";
		try {

			CommodityBO bo = new CommodityBO();
			String categoryId = "0";
			String url = "http://offer.1688.com/offer/asyn/category_selector.json?callback=jQuery17207204073464963585_1418739676341&loginCheck=N&dealType=getSubCatInfo&scene=offer&categoryId=";
			String xml = JsoupUtil
					.conUrl(url + categoryId, false, "GBK", "", 0);
			xml = xml.substring(xml.indexOf("(") + 1, xml.length() - 1);
			JSONObject json = new JSONObject(xml);
			JSONArray datas = json.getJSONArray("data");// 第一层类目
			for (int i = 0; i < datas.length(); i++) {
				JSONObject value = datas.getJSONObject(i);
				String name = value.getString("name");
				categoryId = value.getString("id");

				CfgSysCategorySchema schema = new CfgSysCategorySchema();// 标准商品类目信息
				schema.setN_category_id(categoryId);// 主键ID
				schema.setN_parent_id("0");// 父类别ID
				schema.setC_name(name);// 名称
				schema.setN_level("1");// 级别
				schema.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
				bo.addCfgSysCategorySchema(schema);// 增加标准商品类目信息

				String xml1 = JsoupUtil.conUrl(url + categoryId, false, "GBK",
						"", 0);
				xml1 = xml1.substring(xml1.indexOf("(") + 1, xml1.length() - 1);
				JSONObject json1 = new JSONObject(xml1);
				JSONArray datas1 = json1.getJSONArray("data");// 第二层类目
				for (int i1 = 0; i1 < datas1.length(); i1++) {
					JSONObject value1 = datas1.getJSONObject(i1);
					String name1 = value1.getString("name");
					String categoryId1 = value1.getString("id");

					CfgSysCategorySchema schema1 = new CfgSysCategorySchema();// 标准商品类目信息
					schema1.setN_category_id(categoryId1);// 主键ID
					schema1.setN_parent_id(categoryId);// 父类别ID
					schema1.setC_name(name1);// 名称
					schema1.setN_level("2");// 级别
					schema1.setT_create_time(DateUtil.format8
							.format(new Date()));// 创建时间
					bo.addCfgSysCategorySchema(schema1);// 增加标准商品类目信息

					String xml2 = JsoupUtil.conUrl(url + categoryId1, false,
							"GBK", "", 0);
					xml2 = xml2.substring(xml2.indexOf("(") + 1,
							xml2.length() - 1);
					JSONObject json2 = new JSONObject(xml2);
					JSONArray datas2 = json2.getJSONArray("data");// 第三层类目
					for (int i2 = 0; i2 < datas2.length(); i2++) {
						JSONObject value2 = datas2.getJSONObject(i2);
						String name2 = value2.getString("name");
						String categoryId2 = value2.getString("id");

						CfgSysCategorySchema schema2 = new CfgSysCategorySchema();// 标准商品类目信息
						schema2.setN_category_id(categoryId2);// 主键ID
						schema2.setN_parent_id(categoryId1);// 父类别ID
						schema2.setC_name(name2);// 名称
						schema2.setN_level("3");// 级别
						schema2.setT_create_time(DateUtil.format8
								.format(new Date()));// 创建时间
						bo.addCfgSysCategorySchema(schema2);// 增加标准商品类目信息
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取商品分类失败:", e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取商品分类失败:", LogUtil
					.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取商品分类jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsCategoryService:获取商品分类共耗时: " + re
				+ "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
