package com.work.commodity.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.util.GoodsUtil;
import com.work.util.JsoupUtil;

/**
 * ��������Ʒ����ҳ�Ƿ������ά��ͼ
 * 
 * @author tangbiao
 * 
 */
public class GoodsInfoFileCheckService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsInfoFileCheckService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsInfoFileCheckService......................");

		String shopid = request.getHeader("shopid");// ����ID
		String isali = request.getHeader("isali");// �Ƿ�ץ�����ҳ�棨==1��ץ�����ҳ�棩
		String isimgcheck = request.getHeader("isimgcheck");// �Ƿ�Ҫ����ά��ͼƬ��==1����ά��ͼ��
		String num = request.getHeader("num");// ���ͼƬ���������������0�������ͼƬ��
		String imgurlcheck = request.getHeader("imgurlcheck");// ��ά��ͼƬ��ַ������֪����ά���ַ������������ˣ�
		if (StringUtils.isEmpty(shopid)) {
			shopid = request.getParameter("shopid");
			isali = request.getParameter("isali");
			isimgcheck = request.getParameter("isimgcheck");
			num = request.getParameter("num");
			imgurlcheck = request.getParameter("imgurlcheck");
		}

		String jsonStr = "";
		try {
			LogUtil.writeCommodityLog("GoodsInfoFileCheckService: shopid:"
					+ shopid + " isali:" + isali + " isimgcheck:" + isimgcheck
					+ " num:" + num + " imgurlcheck��" + imgurlcheck);
			GoodsUtil
					.htmlInfoZxing(shopid, isali, isimgcheck, num, imgurlcheck);
			jsonStr = JsoupUtil.getJson("0", "�ɹ�");
		} catch (Exception e) {
			logger.error("��������Ʒ����ҳ�Ƿ������ά��ͼʧ��:" + shopid, e);
			jsonStr = JsoupUtil.getJson("0", "ʧ��");
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
