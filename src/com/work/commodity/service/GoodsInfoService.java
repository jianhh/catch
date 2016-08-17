package com.work.commodity.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * ץȡ�̼���Ʒ��Ϣ(����ȫ��ͬ��ĳ����Ʒʧ�ܵ����)
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoService.class);

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
			if (StringUtils.isNotEmpty(sync_type) && sync_type.equals("1")) {// 1:��Ʒ��ȡ��ҳ����ʱ
				int num = Integer.parseInt(pageNum);

				GoodsUtil.goodsInfoUrlByPage(sellerid, url
						+ "/page/offerlist.htm?sortType=timeup", num, 1);// ��Ʒ��ȡ
				GoodsUtil.goodsInfoUrlByTime(sellerid, url);// ��ȡ��30�����Ʒ
				GoodsUtil.goodsInfoFileCheckTake(sellerid);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
				jsonStr = JsoupUtil.getJson("0", "�ɹ�");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("2")) {// 2:������Ʒ������ȡ
				String nid = "";
				String ngoodsid = "";
				String n_third_total_sell = "0";
				GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// ���ݵ�����ƽ̨��ƷID��ѯ
				if (info != null) {
					nid = info.getN_id();
					ngoodsid = info.getN_goods_id();
					n_third_total_sell = info.getN_third_total_sell();
					bo.deleteGoodsById(info.getN_goods_id(), sellerid);// ɾ����Ʒ����Ʒ��ص���Ϣ
				}

				LogUtil.writeCommodityLog("GoodsInfoService:sellerid��"
						+ sellerid + " goodsid��" + goodsid);
				// 10000 http://detail.1688.com/offer/41455062632.html
				GoodsUtil.goodsInfoCatch(sellerid,
						"http://detail.1688.com/offer/" + goodsid + ".html",
						false, false, nid, ngoodsid, n_third_total_sell,
						goodsid);

				jsonStr = JsoupUtil.getJson("0", "�ɹ�");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("3")) {// 3��������Ʒ����
				if (StringUtils.isEmpty(sellerid)
						|| StringUtils.isEmpty(goodsid)) {
					jsonStr = JsoupUtil.getJson("2", "��������");
				}
				int refult = GoodsUtil.goodsInfoUpdateCatch(sellerid, goodsid);
				if (refult == 1) {
					jsonStr = JsoupUtil.getJson("1", "��Ʒ���¼�");
				} else if (refult == 2) {
					jsonStr = JsoupUtil.getJson("2", "��Ʒ������");
				} else {
					List<String> cThirdPlatformIdList = new ArrayList<String>();
					cThirdPlatformIdList.add(goodsid);
					GoodsUtil.goodsInfoNewFileCheckTake(sellerid,
							cThirdPlatformIdList);// ���̼߳����̵�������Ʒ�����ļ����Ƿ��ж�ά��ͼ
					jsonStr = JsoupUtil.getJson("0", "�ɹ�");
				}
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("4")) {// 4��ͬ��������Ʒ��ͼ������
				Document doc = JsoupUtil.getDocument(url);
				GoodsSchema info = bo.getGoodsByformId(sellerid, goodsid);// ���ݵ�����ƽ̨��ƷID��ѯ
				List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
				GoodsUtil.htmlInfo(doc, goodsid, sellerid,
						info.getN_goods_id(), infoImageList);
				if (infoImageList.size() > 0) {
					bo.updateGoodsInfoImageList(info, infoImageList);
				}
				jsonStr = JsoupUtil.getJson("0", "�ɹ�");
			} else if (StringUtils.isNotEmpty(sync_type)
					&& sync_type.equals("5")) {// 5������ͬ�����̵�ͼ������
				GoodsUtil.goodsInfoHtml(sellerid, url
						+ "/page/offerlist.htm?sortType=timeup", 1, 1);
				jsonStr = JsoupUtil.getJson("0", "�ɹ�");
			}
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
