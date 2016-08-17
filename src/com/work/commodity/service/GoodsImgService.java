package com.work.commodity.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
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
import com.framework.util.StringUtils;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.schema.GoodsImageSchema;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.util.GoodsUtil;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.util.FotoMix;
import com.work.util.JsoupUtil;

/**
 * �ϴ�ͼƬ����ţ
 * 
 * @author tangbiao
 * 
 */
public class GoodsImgService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(GoodsImgService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("GoodsImgService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String imgtype = request.getHeader("imgtype");// ͼƬ�ϴ����ͣ�1,�����ϴ�ͼƬ��2,�ϳɶ�ά��ͼ���ϴ�ͼƬ��3,���ɵ��̶�ά��ͼ���ϴ�ͼƬ;4,�Զ�����url���ɶ�ά��ͼƬ���ϴ���5,��������ͼ������ͼƬ
		String imgurl = request.getHeader("imgurl");// ͼƬ��ַ
		String shopid = request.getHeader("shopid");// ����ID
		String hands_code = request.getHeader("hands_code");// imgtype==3ʱ�����õ�
		String goodsid = request.getHeader("goodsid");// ��ƷID
		String cThirdPlatformId = request.getHeader("third_platform_id");// ��������ƷID
		String imgurl1 = request.getHeader("imgurl1");// ͼƬ��ַ
		String imgurl2 = request.getHeader("imgurl2");// ͼƬ��ַ
		String imgurl3 = request.getHeader("imgurl3");// ͼƬ��ַ
		String imgurl4 = request.getHeader("imgurl4");// ͼƬ��ַ
		String imgurl5 = request.getHeader("imgurl5");// ͼƬ��ַ
		String imgurl6 = request.getHeader("imgurl6");// ͼƬ��ַ
		String jsonStr = JsoupUtil.getJson("1", "ʧ��");
		try {
			LogUtil.writeCommodityLog("GoodsImgService:imgtype��" + imgtype
					+ " imgUrl��" + imgurl + " shopid:" + shopid + " goodsId:"
					+ goodsid);
			if (StringUtils.isNotEmpty(imgtype) && imgtype.equals("2")) {
				String[] urlArray = { imgurl1, imgurl2, imgurl3, imgurl4,
						imgurl5, imgurl6 };
				String fileName = FotoMix.wImage(shopid, goodsid, urlArray);
				if (StringUtils.isNotEmpty(fileName)) {
					String path = GoodsUtil.getQiniuUrl() + fileName;
					jsonStr = JsoupUtil.getGoodsImgJson("0", path, "0", "0");
				}
			} else if (StringUtils.isNotEmpty(imgtype) && imgtype.equals("3")) {
				String text = "http://"
						+ StartContent.getInstance().getDomainUrl()
						+ "/static/shop/shop_index.html?is_share=1&shop_id="
						+ shopid;// ��ά��ɨ���ַ
				if (StringUtils.isNotEmpty(hands_code)&&!"null".equalsIgnoreCase(hands_code)) {
					text += "&hands_code=" + hands_code;
				}
				String filePath = JsoupUtil.SystemPathImage(shopid + "_index");// ����ͼƬ���صĸ�Ŀ¼
				// �����Ŀ¼������,�򴴽�֮(����Ŀ¼�Զ�����)
				File uploadFilePath = new File(filePath);
				if (uploadFilePath.exists() == false) {
					uploadFilePath.mkdirs();
				}
				String fileName = shopid + "_truedian_index.jpg";// ͼƬ����
				if (StringUtils.isNotEmpty(hands_code)&&!"null".equalsIgnoreCase(hands_code)) {
					fileName = shopid + "_" + hands_code
							+ "_truedian_index.jpg";// ͼƬ����
				}
				filePath = filePath + fileName;// ͼƬ
				File qrFile = new File(filePath);
				FotoMix.createShopQRImage(qrFile, text, 640, "jpg");
				boolean ret = GoodsUtil.localFile(filePath, fileName);// �ϴ�����ţ
				if (ret) {
					qrFile.delete();// �ϴ��ɹ���ɾ��
					String path = GoodsUtil.getQiniuUrl() + fileName;
					jsonStr = JsoupUtil.getGoodsImgJson("0", path, "0", "0");
				}
			}else if(StringUtils.isNotEmpty(imgtype) && imgtype.equals("4")){
				String filePath = JsoupUtil.SystemPathImage("commission_code");// �ϻ��˶�ά��
				// �����Ŀ¼������,�򴴽�֮(����Ŀ¼�Զ�����)
				File uploadFilePath = new File(filePath);
				if (uploadFilePath.exists() == false) {
					uploadFilePath.mkdirs();
				}
				String fileName =(StringUtils.isNotEmpty(hands_code)?hands_code:"commission")+"_code_"+ shopid + "_" +DateUtil.getTimeNowStr()+ ".jpg";// ͼƬ����
				filePath = filePath + fileName;// ͼƬ
				
				File qrFile = new File(filePath);
				FotoMix.createShopQRImage(qrFile, imgurl, 640, "jpg");
				boolean ret = GoodsUtil.localFile(filePath, fileName);// �ϴ�����ţ
				if (ret) {
					qrFile.delete();// �ϴ��ɹ���ɾ��
					String path = GoodsUtil.getQiniuUrl() + fileName;
					jsonStr = JsoupUtil.getGoodsImgJson("0", path, "0", "0");
				}
			}else if(StringUtils.isNotEmpty(imgtype) && imgtype.equals("5")){//��������ͼ������ͼƬ
				CommodityBO bo = new CommodityBO();
				GoodsSchema info = bo.getGoodsByformId(shopid, cThirdPlatformId);// ���ݵ�����ƽ̨��ƷID��ѯ
				if(info != null && StringUtils.isNotEmpty(info.getC_goods_detail_desc())){
					if("1".equals(info.getN_third_platform_type())){//0 �Լ�ƽ̨ 1 ���� 2 �Ա�
						String detail_url = StartContent.getInstance().getWeigouIp() + info.getC_goods_detail_desc();
						String xml = JsoupUtil.conUrl(detail_url, false, "GBK", null, 0);
						Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));
		
						List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
						GoodsUtil.goodsInfoImg(doc, cThirdPlatformId, shopid, info.getN_goods_id(), infoImageList);
						if (infoImageList.size() > 0) {
							bo.updateGoodsInfoImageList(info, infoImageList);
						}
					}else if("2".equals(info.getN_third_platform_type())){//0 �Լ�ƽ̨ 1 ���� 2 �Ա�
						String detail_url = StartContent.getInstance().getWeigouIp() + info.getC_goods_detail_desc();
						String xml = JsoupUtil.conUrl(detail_url, false, "GBK", null, 0);
						Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));
						GoodsUtil.tbGoodsInfoImg(doc, cThirdPlatformId, shopid, info.getN_goods_id());
					}
					
					jsonStr = JsoupUtil.getGoodsImgJson("0", "", "0", "0");
				}
			}else {
				if (StringUtils.isNotEmpty(imgurl)) {
					String fileName = GoodsUtil.getPicName(shopid, imgurl);// ͼƬ��
					GoodsImageSchema schema = GoodsUtil.httpFile(shopid,
							imgurl, shopid);// ����ͼƬ�ϴ�����ţ
					if (StringUtils.isNotEmpty(schema.getN_width())) {
						String path = GoodsUtil.getQiniuUrl() + fileName;
						jsonStr = JsoupUtil.getGoodsImgJson("0", path, schema
								.getN_width(), schema.getN_height());
					}
				}
			}
		} catch (Exception e) {
			logger.error("�ϴ�ͼƬ����ţʧ�ܣ�imgType��" + imgtype + " imgUrl��" + imgurl
					+ " goodsId:" + goodsid, e);
			jsonStr = JsoupUtil.getJson("1", "ʧ��");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "�ϴ�ͼƬ����ţʧ�ܣ�imgType��"
					+ imgtype + " imgUrl��" + imgurl + " goodsId:" + goodsid,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(imgtype + "�ϴ�ͼƬ����ţjsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("GoodsImgService:imgType��" + imgtype
				+ "�ϴ�ͼƬ����ţ����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

}
