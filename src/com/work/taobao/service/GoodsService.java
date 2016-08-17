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
 * ץȡ��Ʒ��Ϣ
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
			//��ȡ������Ϣ
			this.goods(sellerid, url, sync_type, task_id, true);// ͬ����Ʒ
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
		if (sync_type.equals(GoodsUtil.GOODS_CATCH_FIRST)) {
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
			String task_id, boolean getShopInfo) throws Exception {
		LogUtil.writeCommodityLog("GoodsService: sellerid:" + sellerid
				+ " url:" + url + " sync_type:" + sync_type + " task_id:"
				+ task_id);
		CommodityBO bo = new CommodityBO();
		if (sync_type.equals(GoodsUtil.GOODS_CATCH_ALL)) {
			bo.deleteGoods(sellerid);// ɾ����Ʒ����Ʒ��ص���Ϣ
		}
		
		GoodsUtil.getGoods(sellerid, url, sync_type, 1, task_id, getShopInfo);
		bo.deleteCatchRecord(task_id);// ɾ����ȡ������Ϣ
		
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
//		// �����ʼ�
//		SimpleMailSender sms = MailSenderFactory.getSender();
//		sms.send(CommodityContent.getRecipients(), "ץȡ��Ʒ��Ϣʧ��", "ץȡ��Ʒ��Ϣʧ��");
	}
}
