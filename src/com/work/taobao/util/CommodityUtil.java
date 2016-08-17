package com.work.taobao.util;

import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.util.JsoupUtil;
import com.work.util.TaobaoGetUrlUtil;

/**
 * �̼���Ʒ��Ϣ��ȡ
 * 
 * @author tangbiao
 * 
 */
public class CommodityUtil {
	private static String TAG = CommodityUtil.class.getSimpleName();
	static JLogger logger = LoggerFactory.getLogger(CommodityUtil.class);

	/**
	 * ��ȡ�Ա��û���¼��
	 * 
	 * @param url
	 *            �Ա�������ַ
	 * @return
	 * @throws JSONException
	 */
	public static String tbLoginUserName(Document doc, String url) throws JSONException {
		LogUtil.writeCommodityLog(TAG + "��ȡ�Ա��û���¼��tbLoginUserName()...........start " + url);
		String loginUserName = "";// ��˾��
		Elements contactEle = doc.select("[class=service-content wws]");
		System.out.println(contactEle);
		String data = "";
		for (int i = 0; i < contactEle.size(); i++) {
			data = contactEle.get(i).attr("data-nick");
			if (StringUtils.isNotEmpty(data)) {
				data = JsoupUtil.getReplaceBy(data);
				break;
			}
		}

		loginUserName = JsoupUtil.urlDecodeByUtf(data);
		LogUtil.writeCommodityLog(TAG + "��ȡ�Ա��û���¼��CommodityUtil.tbLoginUserName()...........end " + loginUserName);
		return loginUserName;
	}
	
	
	public static String getRateUrl(Document doc, String url) throws JSONException {
		LogUtil.writeCommodityLog(TAG + "��ȡ�Ա�RateUrl...........start " + url);
		Elements contactEleEle = doc.select("[class=rank-icon-v2]");
		System.out.println(contactEleEle);
		String rateUrl = "";
		Elements aEle = contactEleEle.select("a");
		if (aEle.size() > 0) {
			rateUrl = aEle.attr("href");
		}
		LogUtil.writeCommodityLog(TAG + "��ȡ�Ա�RateUrl...........end " + url);
		return rateUrl;

	}
	
	public static boolean openShopSuccess(String rateUrl){
		//TODO:��֤��
		Document doc = TaobaoGetUrlUtil.getDocumentUrl(rateUrl);
		Elements contactEleEle = doc.select("[class=charge]");
		return false;
	}

	/**
	 * ��ȡ�����û���¼����Doc
	 * 
	 * @param url
	 *            ����������ַ
	 * @return
	 * @throws JSONException
	 */
	public static Document tbLoginDoc(String url) throws JSONException {
		LogUtil.writeCommodityLog("��ȡ�Ա��û���¼����DocCommodityUtil.tbLoginDoc()...........start " + url);
		Document doc = TaobaoGetUrlUtil.getDocumentUrl(url);
		LogUtil.writeCommodityLog("��ȡ�Ա��û���¼����DocCommodityUtil.tbLoginDoc()...........end " + url);
		return doc;
	}

	/**
	 * ��ȡ��˾��
	 * 
	 * @param doc
	 * @return
	 */
	public static String commodityName(Document doc, String url) {
		LogUtil.writeCommodityLog(TAG + "��ȡ��˾��CommodityUtil.commodityName()...........start " + url);
		String companyName = "";// ��˾��
		// Elements offerlistEle = doc.select("[data-page-name=offerlist]");//
		// ��ȡ������Ʒ����
		// if (StringUtils.isNotEmpty(offerlistEle.text())) {//
		// ��������Ʒ���ӣ�֤�����̵�ַû���⣬���Բ�ѯ��������
		Elements companyEle = doc.select("[class=company-name]");
		companyName = companyEle.text();// ��˾��
		if (StringUtils.isEmpty(companyName)) {
			// Elements titleEle = doc.select("title");
			// companyName = titleEle.text();// ��˾��
			companyName = shopName(doc); // ��˾����������ȡ������
		}
		// }
		LogUtil.writeCommodityLog(TAG + "��ȡ��˾��CommodityUtil.commodityName()...........end " + companyName);
		return companyName;
	}

	/**
	 * ��ȡ������
	 * 
	 * @param doc
	 * @return
	 */
	public static String shopName(Document doc) {
		LogUtil.writeCommodityLog(TAG + "��ȡ������CommodityUtil.shopName()...........start ");
		String shopName = "";// ��˾��
		Elements companyEle = doc.select("[class=shop-name]");
		for (int i = 0; i < companyEle.size(); i++) {
			String text = companyEle.get(i).text();
			if (StringUtils.isEmpty(text)) {
				continue;
			}
			if (StringUtils.isNotEmpty(shopName)) {
				if (text.length() < shopName.length()) {
					shopName = text;
				}
			} else {
				shopName = text;
			}

		}

		if (StringUtils.isEmpty(shopName)) {
			Elements titleEle = doc.select("title");
			shopName = titleEle.text();// ��˾��
		}
		LogUtil.writeCommodityLog(TAG + "��ȡ��˾��CommodityUtil.commodityName()...........end " + shopName);
		return shopName;

	}

}
