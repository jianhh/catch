package com.work.taobao.service;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.nodes.Document;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.DateUtil;
import com.framework.util.StartContent;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * 抓取商品信息
 * 
 * @author tangbiao
 * 
 */
public class GoodsService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(GoodsService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsService......................");
		int start = (int) System.currentTimeMillis(); // 开始时间

		String sellerid = request.getHeader("sellerid");// 店铺ID
		String url = request.getHeader("url");// 店铺地址
		String sync_type = request.getHeader("sync_type");// 1:首次抓取，2:同步上新商品,3:重新同步所有商品
		String task_id = request.getHeader("task_id");// 任务编号

		String jsonStr = "";
		String xml = "";
		String sendUrl = "";
		try {
			CommodityBO bo = new CommodityBO();
			CatchRecordSchema schema = new CatchRecordSchema();// 新增爬取任务信息
			schema.setN_shop_id(sellerid);
			schema.setN_sync_type(sync_type);
			schema.setN_task_id(task_id);
			schema.setC_url(url);
			schema.setT_update_time(DateUtil.format8.format(new Date()));
			bo.addCatchRecord(schema);// 增加爬取任务
			//获取店铺信息
			this.goods(sellerid, url, sync_type, task_id, true);// 同步商品
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=0&msg=成功";
			jsonStr = JsoupUtil.getJson("0", "成功");
		} catch (Exception e) {
			logger.error("抓取商品信息失败:" + sellerid + " " + sync_type, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=10000&msg=失败";
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "抓取商品信息失败:" + "店铺ID:"
					+ sellerid + " 店铺地址:" + url + " 抓取类型:" + sync_type
					+ " task_id:" + task_id, LogUtil.getExceptionError(e));
		}
		if (sync_type.equals(GoodsUtil.GOODS_CATCH_FIRST)) {
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "抓取商品信息:" + "店铺ID:"
					+ sellerid + " 店铺地址:" + url + " 抓取类型:" + sync_type
					+ " task_id:" + task_id, jsonStr);
		}
		LogUtil.writeCommodityLog("抓取商品信息jsonStr:" + jsonStr);
		try {
			xml = JsoupUtil.conUrl(sendUrl, true, "utf-8", "", 0);
			LogUtil.writeInterfaceLog("商品抓取完成通知接口:" + sendUrl + "\n" + xml);
		} catch (Exception e) {
			logger.error("抓取商品信息失败:" + sellerid + " " + sync_type, e);
			jsonStr = JsoupUtil.getJson("10000", "失败");
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainUrl()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=10000&msg=失败";
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "抓取商品信息失败:" + "店铺ID:"
					+ sellerid + " 店铺地址:" + url + " 抓取类型:" + sync_type
					+ " task_id:" + task_id, "sendUrl:" + sendUrl + "\n"
					+ LogUtil.getExceptionError(e));
		}
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsService:" + sellerid + " " + sync_type
				+ "店铺爬取商品共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public void goods(String sellerid, String url, String sync_type,
			String task_id, boolean getShopInfo) throws Exception {
		LogUtil.writeCommodityLog("GoodsService: sellerid:" + sellerid
				+ " url:" + url + " sync_type:" + sync_type + " task_id:"
				+ task_id);
		CommodityBO bo = new CommodityBO();
		if (sync_type.equals(GoodsUtil.GOODS_CATCH_ALL)) {
			bo.deleteGoods(sellerid);// 删除商品及商品相关的信息
		}
		
		GoodsUtil.getGoods(sellerid, url, sync_type, 1, task_id, getShopInfo);
		bo.deleteCatchRecord(task_id);// 删除爬取任务信息
		
	}
	
	
	
	
	private static void catchGoods(){
//		String url = "https://icolorlady.taobao.com";
		String url = "https://xiaowsm.taobao.com";
		
		
		try {
			GoodsUtil.getGoods("123", url, "1", 1, "", true);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
//		try{
//			Document doc = GoodsUtil.getDoc("https://detail.1688.com/offer/525556951195.html");
//			System.out.println(doc.html());
//		}catch(Exception e){
//			
//		}
				catchGoods();
//		// 发送邮件
//		SimpleMailSender sms = MailSenderFactory.getSender();
//		sms.send(CommodityContent.getRecipients(), "抓取商品信息失败", "抓取商品信息失败");
	}
}
