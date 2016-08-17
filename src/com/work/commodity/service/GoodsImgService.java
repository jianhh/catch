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
 * 上传图片到七牛
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
		int start = (int) System.currentTimeMillis(); // 开始时间

		String imgtype = request.getHeader("imgtype");// 图片上传类型：1,单纯上传图片；2,合成二维码图并上传图片；3,生成店铺二维码图并上传图片;4,自动带有url生成二维码图片并上传；5,重新生成图文详情图片
		String imgurl = request.getHeader("imgurl");// 图片地址
		String shopid = request.getHeader("shopid");// 店铺ID
		String hands_code = request.getHeader("hands_code");// imgtype==3时可能用到
		String goodsid = request.getHeader("goodsid");// 商品ID
		String cThirdPlatformId = request.getHeader("third_platform_id");// 第三方商品ID
		String imgurl1 = request.getHeader("imgurl1");// 图片地址
		String imgurl2 = request.getHeader("imgurl2");// 图片地址
		String imgurl3 = request.getHeader("imgurl3");// 图片地址
		String imgurl4 = request.getHeader("imgurl4");// 图片地址
		String imgurl5 = request.getHeader("imgurl5");// 图片地址
		String imgurl6 = request.getHeader("imgurl6");// 图片地址
		String jsonStr = JsoupUtil.getJson("1", "失败");
		try {
			LogUtil.writeCommodityLog("GoodsImgService:imgtype：" + imgtype
					+ " imgUrl：" + imgurl + " shopid:" + shopid + " goodsId:"
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
						+ shopid;// 二维码扫描地址
				if (StringUtils.isNotEmpty(hands_code)&&!"null".equalsIgnoreCase(hands_code)) {
					text += "&hands_code=" + hands_code;
				}
				String filePath = JsoupUtil.SystemPathImage(shopid + "_index");// 设置图片下载的根目录
				// 如果该目录不存在,则创建之(多重目录自动创建)
				File uploadFilePath = new File(filePath);
				if (uploadFilePath.exists() == false) {
					uploadFilePath.mkdirs();
				}
				String fileName = shopid + "_truedian_index.jpg";// 图片名称
				if (StringUtils.isNotEmpty(hands_code)&&!"null".equalsIgnoreCase(hands_code)) {
					fileName = shopid + "_" + hands_code
							+ "_truedian_index.jpg";// 图片名称
				}
				filePath = filePath + fileName;// 图片
				File qrFile = new File(filePath);
				FotoMix.createShopQRImage(qrFile, text, 640, "jpg");
				boolean ret = GoodsUtil.localFile(filePath, fileName);// 上传到七牛
				if (ret) {
					qrFile.delete();// 上传成功就删除
					String path = GoodsUtil.getQiniuUrl() + fileName;
					jsonStr = JsoupUtil.getGoodsImgJson("0", path, "0", "0");
				}
			}else if(StringUtils.isNotEmpty(imgtype) && imgtype.equals("4")){
				String filePath = JsoupUtil.SystemPathImage("commission_code");// 合伙人二维码
				// 如果该目录不存在,则创建之(多重目录自动创建)
				File uploadFilePath = new File(filePath);
				if (uploadFilePath.exists() == false) {
					uploadFilePath.mkdirs();
				}
				String fileName =(StringUtils.isNotEmpty(hands_code)?hands_code:"commission")+"_code_"+ shopid + "_" +DateUtil.getTimeNowStr()+ ".jpg";// 图片名称
				filePath = filePath + fileName;// 图片
				
				File qrFile = new File(filePath);
				FotoMix.createShopQRImage(qrFile, imgurl, 640, "jpg");
				boolean ret = GoodsUtil.localFile(filePath, fileName);// 上传到七牛
				if (ret) {
					qrFile.delete();// 上传成功就删除
					String path = GoodsUtil.getQiniuUrl() + fileName;
					jsonStr = JsoupUtil.getGoodsImgJson("0", path, "0", "0");
				}
			}else if(StringUtils.isNotEmpty(imgtype) && imgtype.equals("5")){//重新生成图文详情图片
				CommodityBO bo = new CommodityBO();
				GoodsSchema info = bo.getGoodsByformId(shopid, cThirdPlatformId);// 根据第三方平台商品ID查询
				if(info != null && StringUtils.isNotEmpty(info.getC_goods_detail_desc())){
					if("1".equals(info.getN_third_platform_type())){//0 自己平台 1 阿里 2 淘宝
						String detail_url = StartContent.getInstance().getWeigouIp() + info.getC_goods_detail_desc();
						String xml = JsoupUtil.conUrl(detail_url, false, "GBK", null, 0);
						Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));
		
						List<GoodsInfoImageSchema> infoImageList = new ArrayList<GoodsInfoImageSchema>();
						GoodsUtil.goodsInfoImg(doc, cThirdPlatformId, shopid, info.getN_goods_id(), infoImageList);
						if (infoImageList.size() > 0) {
							bo.updateGoodsInfoImageList(info, infoImageList);
						}
					}else if("2".equals(info.getN_third_platform_type())){//0 自己平台 1 阿里 2 淘宝
						String detail_url = StartContent.getInstance().getWeigouIp() + info.getC_goods_detail_desc();
						String xml = JsoupUtil.conUrl(detail_url, false, "GBK", null, 0);
						Document doc = Jsoup.parse(xml.replace("&nbsp;", " "));
						GoodsUtil.tbGoodsInfoImg(doc, cThirdPlatformId, shopid, info.getN_goods_id());
					}
					
					jsonStr = JsoupUtil.getGoodsImgJson("0", "", "0", "0");
				}
			}else {
				if (StringUtils.isNotEmpty(imgurl)) {
					String fileName = GoodsUtil.getPicName(shopid, imgurl);// 图片名
					GoodsImageSchema schema = GoodsUtil.httpFile(shopid,
							imgurl, shopid);// 网络图片上传到七牛
					if (StringUtils.isNotEmpty(schema.getN_width())) {
						String path = GoodsUtil.getQiniuUrl() + fileName;
						jsonStr = JsoupUtil.getGoodsImgJson("0", path, schema
								.getN_width(), schema.getN_height());
					}
				}
			}
		} catch (Exception e) {
			logger.error("上传图片到七牛失败，imgType：" + imgtype + " imgUrl：" + imgurl
					+ " goodsId:" + goodsid, e);
			jsonStr = JsoupUtil.getJson("1", "失败");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "上传图片到七牛失败，imgType："
					+ imgtype + " imgUrl：" + imgurl + " goodsId:" + goodsid,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeCommodityLog(imgtype + "上传图片到七牛jsonStr:" + jsonStr);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("GoodsImgService:imgType：" + imgtype
				+ "上传图片到七牛共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

}
