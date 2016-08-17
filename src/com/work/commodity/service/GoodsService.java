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
 * ץȡ�̼���Ϣ
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
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID
		String url = request.getHeader("url");// ���̵�ַ
		String sync_type = request.getHeader("sync_type");// 1:�״�ץȡ��2:ͬ��������Ʒ,3:����ͬ��������Ʒ
		String task_id = request.getHeader("task_id");// ������

		String jsonStr = "";
		String xml = "";
		String sendUrl = "";
		try {
			CommodityBO bo = new CommodityBO();
			CatchRecordSchema schema = new CatchRecordSchema();// ������ȡ������Ϣ
			schema.setN_shop_id(sellerid);
			schema.setN_sync_type(sync_type);
			schema.setN_task_id(task_id);
			schema.setC_url(url);
			schema.setT_update_time(DateUtil.format8.format(new Date()));
			bo.addCatchRecord(schema);// ������ȡ����
			this.goods(sellerid, url, sync_type, task_id, 1);// ͬ����Ʒ
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=0&msg=�ɹ�";
			jsonStr = JsoupUtil.getJson("0", "�ɹ�");
		} catch (Exception e) {
			logger.error("ץȡ��Ʒ��Ϣʧ��:" + sellerid + " " + sync_type, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainIp()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=10000&msg=ʧ��";
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "ץȡ��Ʒ��Ϣʧ��:" + "����ID:"
					+ sellerid + " ���̵�ַ:" + url + " ץȡ����:" + sync_type
					+ " task_id:" + task_id, LogUtil.getExceptionError(e));
		}
		if (sync_type.equals("1")) {
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "ץȡ��Ʒ��Ϣ:" + "����ID:"
					+ sellerid + " ���̵�ַ:" + url + " ץȡ����:" + sync_type
					+ " task_id:" + task_id, jsonStr);
		}
		LogUtil.writeCommodityLog("ץȡ��Ʒ��ϢjsonStr:" + jsonStr);
		try {
			xml = JsoupUtil.conUrl(sendUrl, true, "utf-8", "", 0);
			LogUtil.writeInterfaceLog("��Ʒץȡ���֪ͨ�ӿ�:" + sendUrl + "\n" + xml);
		} catch (Exception e) {
			logger.error("ץȡ��Ʒ��Ϣʧ��:" + sellerid + " " + sync_type, e);
			jsonStr = JsoupUtil.getJson("10000", "ʧ��");
			sendUrl = "http://"
					+ StartContent.getInstance().getDomainUrl()
					+ "/service/inner_msg_reciver.jsp?act=goods_sync_notify&shop_id="
					+ sellerid + "&sync_type=" + sync_type + "&task_id="
					+ task_id + "&status=10000&msg=ʧ��";
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "ץȡ��Ʒ��Ϣʧ��:" + "����ID:"
					+ sellerid + " ���̵�ַ:" + url + " ץȡ����:" + sync_type
					+ " task_id:" + task_id, "sendUrl:" + sendUrl + "\n"
					+ LogUtil.getExceptionError(e));
		}
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("GoodsService:" + sellerid + " " + sync_type
				+ "������ȡ��Ʒ����ʱ: " + re + "����");
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
			bo.deleteGoods(sellerid);// ɾ����Ʒ����Ʒ��ص���Ϣ

			GoodsUtil.goodsInfoUrlByPage(sellerid, url
					+ "/page/offerlist.htm?sortType=timeup", 1, 1);// ��Ʒ��ȡ(����)

			GoodsUtil.goodsInfoUrlByTime(sellerid, url);// ��ȡ��30�����Ʒ

			bo.deleteCatchRecord(task_id);// ɾ����ȡ������Ϣ

			GoodsUtil.goodsInfoFileCheckTake(sellerid);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
		} else if (sync_type.equals("2")) {
			List<String> cThirdPlatformIdList = GoodsUtil.goodsInfoNew(
					sellerid, url, task_id, num);// ��ȡ��30�����Ʒ(����)

			int goodsNewNum = bo.getGoodsNewNum(task_id, num);
			if (cThirdPlatformIdList.size() > goodsNewNum) {
				logger.error("������Ʒ��" + num + "��ͬ����ȫ:" + "����ID:" + sellerid
						+ " ���̵�ַ:" + url + " ץȡ����:" + sync_type + " task_id:"
						+ task_id + " ������Ʒ������" + cThirdPlatformIdList.size()
						+ " ����ͬ����Ʒ������" + goodsNewNum);
				// �����ʼ�
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "������Ʒ��" + num
						+ "��ͬ����ȫ:" + "����ID:" + sellerid + " ���̵�ַ:" + url
						+ " ץȡ����:" + sync_type + " task_id:" + task_id,
						" ������Ʒ������" + cThirdPlatformIdList.size() + " ����ͬ����Ʒ������"
								+ goodsNewNum);
				int i = num + 1;
				this.goods(sellerid, url, sync_type, task_id, i);// �����ٴ���ȡ
			}

			// GoodsUtil.goodsInfoUrl(sellerid, url
			// + "/page/offerlist.htm?sortType=tradenumdown");// ��Ʒ��ȡ��Ϊ�˸������������ݣ�

			bo.deleteCatchRecord(task_id);// ɾ����ȡ������Ϣ

			GoodsUtil.goodsInfoNewFileCheckTake(sellerid, cThirdPlatformIdList);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
		} else if (sync_type.equals("1")) {
			int count = 1;// ������Ʒ������
			try {
				Document doc = JsoupUtil.getDocument(url
						+ "/page/offerlist.htm?sortType=timeup&pageNum=" + 1);
				Elements pageCountEle = doc.select("[class=offer-count]");
				String pageCount = pageCountEle.text();// ��ҳ��
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
				logger.error("��ȡ������Ʒ��" + num + "������ʧ��:" + sellerid + " "
						+ sync_type, e);
				// �����ʼ�
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "��ȡ������Ʒ��" + num
						+ "������ʧ��:" + "����ID:" + sellerid + " ���̵�ַ:" + url
						+ " ץȡ����:" + sync_type + " task_id:" + task_id, LogUtil
						.getExceptionError(e));
			}

			GoodsUtil.goodsInfoUrlByPage(sellerid, url
					+ "/page/offerlist.htm?sortType=timeup", 1, 1);// ��Ʒ��ȡ(����)

			GoodsUtil.goodsInfoUrlByTime(sellerid, url);// ��ȡ��30�����Ʒ

			bo.deleteCatchRecord(task_id);// ɾ����ȡ������Ϣ

			GoodsUtil.goodsInfoFileCheckTake(sellerid);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ

			try {
				List<GoodsSchema> goodsList = bo.getGoodsByshopId(sellerid);
				int goodsCount = goodsList.size() + 3;
				if (goodsCount < count) {
					logger.error("��Ʒ��" + num + "��ͬ����ȫ:" + "����ID:" + sellerid
							+ " ���̵�ַ:" + url + " ץȡ����:" + sync_type
							+ " task_id:" + task_id + " ������Ʒ������" + count
							+ " ����ͬ����Ʒ������" + goodsList.size());
					// �����ʼ�
					SimpleMailSender sms = MailSenderFactory.getSender();
					sms
							.send(CommodityContent.getRecipients(), "��Ʒ��" + num
									+ "��ͬ����ȫ:" + "����ID:" + sellerid + " ���̵�ַ:"
									+ url + " ץȡ����:" + sync_type + " task_id:"
									+ task_id, "������Ʒ������" + count + " ����ͬ����Ʒ������"
									+ goodsList.size());
					int i = num + 1;
					this.goods(sellerid, url, sync_type, task_id, i);// �����ٴ���ȡ
				}
			} catch (Exception e) {
				logger.error("��ȡ������Ʒ��" + num + "������ʧ��:" + sellerid + " "
						+ sync_type, e);
				// �����ʼ�
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "��ȡ������Ʒ��" + num
						+ "������ʧ��:" + "����ID:" + sellerid + " ���̵�ַ:" + url
						+ " ץȡ����:" + sync_type + " task_id:" + task_id, LogUtil
						.getExceptionError(e));
			}
		} else {
			List<String> cThirdPlatformIdList = GoodsUtil.goodsInfoNew(
					sellerid, url, task_id, num);// ��ȡ��30�����Ʒ(����)

			int goodsNewNum = bo.getGoodsNewNum(task_id, num);
			if (cThirdPlatformIdList.size() > goodsNewNum) {
				logger.error("������Ʒ��" + num + "��ͬ����ȫ:" + "����ID:" + sellerid
						+ " ���̵�ַ:" + url + " ץȡ����:" + sync_type + " task_id:"
						+ task_id + " ������Ʒ������" + cThirdPlatformIdList.size()
						+ " ����ͬ����Ʒ������" + goodsNewNum);
				// �����ʼ�
				SimpleMailSender sms = MailSenderFactory.getSender();
				sms.send(CommodityContent.getRecipients(), "������Ʒ��" + num
						+ "��ͬ����ȫ:" + "����ID:" + sellerid + " ���̵�ַ:" + url
						+ " ץȡ����:" + sync_type + " task_id:" + task_id,
						" ������Ʒ������" + cThirdPlatformIdList.size() + " ����ͬ����Ʒ������"
								+ goodsNewNum);
				int i = num + 1;
				this.goods(sellerid, url, sync_type, task_id, i);// �����ٴ���ȡ
			}

			bo.deleteCatchRecord(task_id);// ɾ����ȡ������Ϣ

			GoodsUtil.goodsInfoNewFileCheckTake(sellerid, cThirdPlatformIdList);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
		}
	}
}
