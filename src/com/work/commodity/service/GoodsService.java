package com.work.commodity.service;

import java.util.Date;
import java.util.List;

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
import com.framework.util.StartContent;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.JsoupUtil;

/**
 * 抓取商家信息
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
			this.goods(sellerid, url, sync_type, task_id, 1);// 同步商品
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
		if (sync_type.equals("1")) {
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
			String task_id, int num) throws Exception {
		LogUtil.writeCommodityLog("GoodsService: sellerid:" + sellerid
				+ " url:" + url + " sync_type:" + sync_type + " task_id:"
				+ task_id + " num:" + num);
		if ((num > 10) || (task_id.equals("1") && num > 1)) {
			return;
		}
		CommodityBO bo = new CommodityBO();
		// 10000 http://shop1403110510248.1688.com
		if (sync_type.equals("3")) {
			bo.deleteGoods(sellerid);// 删除商品及商品相关的信息

			GoodsUtil.goodsInfoUrlByPage(sellerid, url
					+ "/page/offerlist.htm?sortType=timeup", 1, 1);// 商品爬取(所有)

			GoodsUtil.goodsInfoUrlByTime(sellerid, url);// 爬取近30天的商品

			bo.deleteCatchRecord(task_id);// 删除爬取任务信息

			GoodsUtil.goodsInfoFileCheckTake(sellerid);// 开线程检查店铺的所有商品详情文件中是否有二维码图
		} else if (sync_type.equals("2")) {
			List<String> cThirdPlatformIdList = GoodsUtil.goodsInfoNew(
					sellerid, url, task_id, num);// 爬取近30天的商品(上新)

			int goodsNewNum = bo.getGoodsNewNum(task_id, num);
			if (cThirdPlatformIdList.size() > goodsNewNum) {
				logger.error("上新商品第" + num + "次同步不全:" + "店铺ID:" + sellerid
						+ " 店铺地址:" + url + " 抓取类型:" + sync_type + " task_id:"
						+ task_id + " 阿里商品总数：" + cThirdPlatformIdList.size()
						+ " 触店同步商品总数：" + goodsNewNum);
				// 发送邮件
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "上新商品第" + num
						+ "次同步不全:" + "店铺ID:" + sellerid + " 店铺地址:" + url
						+ " 抓取类型:" + sync_type + " task_id:" + task_id,
						" 阿里商品总数：" + cThirdPlatformIdList.size() + " 触店同步商品总数："
								+ goodsNewNum);
				int i = num + 1;
				this.goods(sellerid, url, sync_type, task_id, i);// 重新再次爬取
			}

			// GoodsUtil.goodsInfoUrl(sellerid, url
			// + "/page/offerlist.htm?sortType=tradenumdown");// 商品爬取（为了更新销售量数据）

			bo.deleteCatchRecord(task_id);// 删除爬取任务信息

			GoodsUtil.goodsInfoNewFileCheckTake(sellerid, cThirdPlatformIdList);// 开线程检查店铺的上新商品详情文件中是否有二维码图
		} else if (sync_type.equals("1")) {
			int count = 1;// 店铺商品总条数
			try {
				Document doc = JsoupUtil.getDocument(url
						+ "/page/offerlist.htm?sortType=timeup&pageNum=" + 1);
				Elements pageCountEle = doc.select("[class=offer-count]");
				String pageCount = pageCountEle.text();// 总页数
				if (StringUtils.isNotEmpty(pageCount)) {
					count = Integer.parseInt(pageCount);
				}
				if (count == 1) {
					Elements divEle = doc
							.select("[data-tracelog-exp=wp_widget_offerhand_main_disp]");
					if (divEle.size() == 0) {
						divEle = doc.select("[class=wp-offerlist-catalogs]");
					}
					Elements liEle = divEle.select("li");
					count = liEle.size();
				}
			} catch (Exception e) {
				logger.error("获取阿里商品第" + num + "次总数失败:" + sellerid + " "
						+ sync_type, e);
				// 发送邮件
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "获取阿里商品第" + num
						+ "次总数失败:" + "店铺ID:" + sellerid + " 店铺地址:" + url
						+ " 抓取类型:" + sync_type + " task_id:" + task_id, LogUtil
						.getExceptionError(e));
			}

			GoodsUtil.goodsInfoUrlByPage(sellerid, url
					+ "/page/offerlist.htm?sortType=timeup", 1, 1);// 商品爬取(所有)

			GoodsUtil.goodsInfoUrlByTime(sellerid, url);// 爬取近30天的商品

			bo.deleteCatchRecord(task_id);// 删除爬取任务信息

			GoodsUtil.goodsInfoFileCheckTake(sellerid);// 开线程检查店铺的所有商品详情文件中是否有二维码图

			try {
				List<GoodsSchema> goodsList = bo.getGoodsByshopId(sellerid);
				int goodsCount = goodsList.size() + 3;
				if (goodsCount < count) {
					logger.error("商品第" + num + "次同步不全:" + "店铺ID:" + sellerid
							+ " 店铺地址:" + url + " 抓取类型:" + sync_type
							+ " task_id:" + task_id + " 阿里商品总数：" + count
							+ " 触店同步商品总数：" + goodsList.size());
					// 发送邮件
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms
							.send(CommodityContent.getRecipients(), "商品第" + num
									+ "次同步不全:" + "店铺ID:" + sellerid + " 店铺地址:"
									+ url + " 抓取类型:" + sync_type + " task_id:"
									+ task_id, "阿里商品总数：" + count + " 触店同步商品总数："
									+ goodsList.size());
					int i = num + 1;
					this.goods(sellerid, url, sync_type, task_id, i);// 重新再次爬取
				}
			} catch (Exception e) {
				logger.error("获取触店商品第" + num + "次总数失败:" + sellerid + " "
						+ sync_type, e);
				// 发送邮件
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "获取触店商品第" + num
						+ "次总数失败:" + "店铺ID:" + sellerid + " 店铺地址:" + url
						+ " 抓取类型:" + sync_type + " task_id:" + task_id, LogUtil
						.getExceptionError(e));
			}
		} else {
			List<String> cThirdPlatformIdList = GoodsUtil.goodsInfoNew(
					sellerid, url, task_id, num);// 爬取近30天的商品(上新)

			int goodsNewNum = bo.getGoodsNewNum(task_id, num);
			if (cThirdPlatformIdList.size() > goodsNewNum) {
				logger.error("上新商品第" + num + "次同步不全:" + "店铺ID:" + sellerid
						+ " 店铺地址:" + url + " 抓取类型:" + sync_type + " task_id:"
						+ task_id + " 阿里商品总数：" + cThirdPlatformIdList.size()
						+ " 触店同步商品总数：" + goodsNewNum);
				// 发送邮件
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "上新商品第" + num
						+ "次同步不全:" + "店铺ID:" + sellerid + " 店铺地址:" + url
						+ " 抓取类型:" + sync_type + " task_id:" + task_id,
						" 阿里商品总数：" + cThirdPlatformIdList.size() + " 触店同步商品总数："
								+ goodsNewNum);
				int i = num + 1;
				this.goods(sellerid, url, sync_type, task_id, i);// 重新再次爬取
			}

			bo.deleteCatchRecord(task_id);// 删除爬取任务信息

			GoodsUtil.goodsInfoNewFileCheckTake(sellerid, cThirdPlatformIdList);// 开线程检查店铺的上新商品详情文件中是否有二维码图
		}
	}
}
