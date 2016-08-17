package com.work.taobao.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.work.commodity.schema.GoodsSchema;
import com.work.taobao.bo.CommodityBO;
import com.work.taobao.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * ץȡ�̼���Ʒ��Ϣ(����ȫ��ͬ��ĳ����Ʒʧ�ܵ����)
 * 
 * @author tangbiao
 * 
 */
public class OpenShopGoodsJudgeService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(OpenShopGoodsJudgeService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsInfoService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String sellerid = request.getHeader("sellerid");// ����ID
		String goodsid = request.getHeader("goodsid");// ��ƷID(������id)
		String sync_type = request.getHeader("sync_type");// 1:��Ʒ��ȡ��ҳ����ʱ;2:������Ʒ������ȡ��3��������Ʒ���£�4��ͬ��������Ʒ��ͼ�����飻5������ͬ�����̵�ͼ������
		String pageNum = request.getHeader("pageNum");// ��ǰҳ��
		String url = request.getHeader("url");// ����

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsInfoService: sellerid:" + sellerid
					+ " goodsid:" + goodsid + " sync_type:" + sync_type
					+ " pageNum:" + pageNum + " url:" + url);
			CommodityBO bo = new CommodityBO();
			String n_third_total_sell = "0";
			String c_price = "0";
			String ngoodsid = "";
			GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// ���ݵ�����ƽ̨��ƷID��ѯ
			if (info != null) {
				ngoodsid = info.getN_goods_id();
				n_third_total_sell = info.getN_third_total_sell();
				c_price = info.getN_sell_price();
				bo.deleteGoodsById(info.getN_goods_id(), sellerid);// ɾ����Ʒ����Ʒ��ص���Ϣ
			}

			GoodsUtil.getGoodsItemInfo(sellerid, goodsid, n_third_total_sell, c_price,false,url, ngoodsid, sync_type, null, 1);
			jsonStr = JsoupUtil.getJson("0", "�ɹ�");
					
		} catch (Exception e) {
			logger.error("ץȡ�̼���Ʒ��Ϣʧ��,sellerid:" + sellerid + " goodsid:"
					+ goodsid + " sync_type:" + sync_type + " pageNum:"
					+ pageNum + " url:" + url, e);
			jsonStr = JsoupUtil.getJson("1", "ʧ��");
		}
		LogUtil.writeCommodityLog("ץȡ�̼���Ʒ��ϢjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("GoodsInfoService:" + goodsid + "��Ʒ��ȡ��ʱ: "
				+ re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) {
		String url = "http://szbyfs8.1688.com";
		url = url.substring(url.indexOf("//") + 2, url.indexOf(".1688"));
		System.out.println(url);
	}
}
