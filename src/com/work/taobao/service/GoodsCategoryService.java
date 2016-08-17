package com.work.taobao.service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.schema.TaobaoCfgSysCategorySchema;
import com.work.util.JsoupUtil;

/**
 * 获取商品分类
 * 
 * @author tangbiao
 * 
 */
public class GoodsCategoryService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(GoodsCategoryService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsCategoryService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String jsonStr = "";
		try {

			CommodityBO bo = new CommodityBO();
			String url = "https://list.taobao.com/browse/cat-0.htm";
			Document doc = JsoupUtil.getDocument(url);
			Elements liEle = doc.select("[class=section clearfix]");
			String lastCat = "";
			int catSecNum = 0;
			for (int i = 0; i < liEle.size(); i++) {
				Elements subtitleA = liEle.get(i).select("[class=subtitle]").select("a");
				String href = subtitleA.attr("href");
				String subtitleCat = getCategory(href);
				if (subtitleCat != null) {
					if (!lastCat.equalsIgnoreCase(subtitleCat)){
						lastCat = subtitleCat;
						catSecNum = 0;
					}else{
						catSecNum++;
					}
					TaobaoCfgSysCategorySchema schema = new TaobaoCfgSysCategorySchema();// 标准商品类目信息
					schema.setN_category_id(subtitleCat);// 主键ID
					schema.setN_parent_id("0");// 父类别ID
					schema.setN_parent_id_sec("0");// 父类别ID
					schema.setC_name(subtitleA.text());// 名称
					schema.setN_level("1");// 级别
					schema.setN_category_id_sec(catSecNum+"");
					schema.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
					bo.addTaobaoCfgSysCategorySchema(schema);// 增加标准商品类目信息
					System.out.println(schema.getN_category_id()+","+schema.getC_name()+":"+schema.getN_category_id_sec());
				}

				Elements sublistA = liEle.get(i).select("[class=sublist]").select("a");
				for (int j = 0; j < sublistA.size(); j++) {
					String sublistCat = getCategory(sublistA.get(j).attr("href"));
					if (sublistCat != null){
						TaobaoCfgSysCategorySchema schema = new TaobaoCfgSysCategorySchema();// 标准商品类目信息
						schema.setN_category_id(sublistCat);// 主键ID
						if (subtitleCat!=null){
							schema.setN_parent_id(subtitleCat);// 父类别ID
							schema.setN_parent_id_sec(catSecNum+"");
						}else{
							schema.setN_parent_id("0");// 父类别ID
							schema.setN_parent_id_sec("0");
						}
						schema.setC_name(sublistA.get(j).text());// 名称
						schema.setN_level("2");// 级别
						schema.setN_category_id_sec("0");
						schema.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
						bo.addTaobaoCfgSysCategorySchema(schema);// 增加标准商品类目信息
						System.out.println(schema.getN_category_id()+","+schema.getC_name() +","+schema.getN_parent_id()+":"+schema.getN_parent_id_sec());
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("获取商品分类失败:", e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "获取商品分类失败:", LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog("获取商品分类jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsCategoryService:获取商品分类共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) {
		Document doc = Jsoup.parse("<img>https://img.alicdn.com/imgextra/i4/685316775/TB2V2YvbXXXXXbHXXXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i1/685316775/TB28SPgepXXXXbIXXXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i4/685316775/TB2ErqvepXXXXaxXpXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i4/685316775/TB2CDizepXXXXXvXpXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i3/685316775/TB2DGiTepXXXXXeXXXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i3/685316775/TB2b.eDepXXXXcNXXXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i1/685316775/TB25X5sepXXXXbkXpXXXXXXXXXX-685316775.jpg</img><img>https://img.alicdn.com/imgextra/i1/685316775/TB296qGepXXXXcnXXXXXXXXXXXX-685316775.jpg</img><txt>可爱俏皮 时尚减龄</txt><txt>又是一款古灵精怪的连衣裙~小女生能够穿出甜蜜女人味，熟女穿起来又非常非常减龄，萌萌可爱的充满童趣的图案，有趣味，有气质，也洒脱~</txt><img>https://img.alicdn.com/imgextra/i1/685316775/TB2KZynepXXXXczXpXXXXXXXXXX-685316775.png</img>");
		Elements eles = doc.select("img");
		for (int i=0; i < eles.size();i++){
			eles.get(i).attr("src", eles.get(i).nextSibling().toString());
			eles.get(i).nextSibling().remove();
		}
		
		System.out.print(doc.select("body").html());
		//		String url = "https://list.taobao.com/browse/cat-0.htm";
//		Document doc = JsoupUtil.getDocument(url);
//		Elements liEle = doc.select("[class=section clearfix]");
//		String lastCat = "";
//		int catSecNum = 0;
//		for (int i = 0; i < liEle.size(); i++) {
//			Elements subtitleA = liEle.get(i).select("[class=subtitle]").select("a");
//			String href = subtitleA.attr("href");
//			String subtitleCat = getCategory(href);
//			if (subtitleCat != null) {
//				if (!lastCat.equalsIgnoreCase(subtitleCat)){
//					lastCat = subtitleCat;
//					catSecNum = 0;
//				}else{
//					catSecNum++;
//				}
//				TaobaoCfgSysCategorySchema schema = new TaobaoCfgSysCategorySchema();// 标准商品类目信息
//				schema.setN_category_id(subtitleCat);// 主键ID
//				schema.setN_parent_id("0");// 父类别ID
//				schema.setC_name(subtitleA.text());// 名称
//				schema.setN_level("1");// 级别
//				schema.setN_category_id_sec(catSecNum+"");
//				schema.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
//				System.out.println(schema.getN_category_id()+","+schema.getC_name()+":"+schema.getN_category_id_sec());
//			}
//
//			Elements sublistA = liEle.get(i).select("[class=sublist]").select("a");
//			for (int j = 0; j < sublistA.size(); j++) {
//				String sublistCat = getCategory(sublistA.get(j).attr("href"));
//				if (sublistCat != null){
//					TaobaoCfgSysCategorySchema schema = new TaobaoCfgSysCategorySchema();// 标准商品类目信息
//					schema.setN_category_id(sublistCat);// 主键ID
//					if (subtitleCat!=null){
//						schema.setN_parent_id(subtitleCat);// 父类别ID
//						schema.setN_parent_id_sec(catSecNum+"");
//					}
//					schema.setC_name(sublistA.get(j).text());// 名称
//					schema.setN_level("2");// 级别
//					schema.setN_category_id_sec("0");
//					schema.setT_create_time(DateUtil.format8.format(new Date()));// 创建时间
//					System.out.println(schema.getN_category_id()+","+schema.getC_name() +","+schema.getN_parent_id()+":"+schema.getN_parent_id_sec());
//				}
//				
//			}
//		}
	}

	public static String getCategory(String href) {
		if (href == null){
			return null;
		}
		int index = href.indexOf("cat=");
		if (index >= 0) {
			String str = href.substring(index + 4);
			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			while (m.find()) {
				if (!"".equals(m.group())) {
					return m.group();
				}
			}
		}

		return null;
	}
}
