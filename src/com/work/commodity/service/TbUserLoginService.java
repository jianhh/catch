package com.work.commodity.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jsoup.nodes.Document;

import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.commodity.content.CommodityContent;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.util.CommodityUtil;
import com.work.util.JsoupUtil;
import com.work.util.Taobao;

/**
 * �Ա��̼ҵ�¼
 * 
 * @author tangbiao
 * 
 */
public class TbUserLoginService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(TbUserLoginService.class);
	private static ConcurrentMap<String, Taobao> tokens = new ConcurrentHashMap<String, Taobao>();

	@Override
	public void doPerform(Req request, Resp response) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("TbUserLoginService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String username = JsoupUtil.urlDecodeByUtf(request.getHeader("username"));// �û���
		String userpw = request.getHeader("userpw");// ��������
		String code = request.getHeader("code");// ��֤��
		String url = request.getHeader("url");// ����������ַ�������û�У�飩
		String jsonStr = "";
		try {
			LogUtil.writeUserloginLog("TbUserLoginService:username��" + username + " userpw��" + userpw + " code:" + code
					+ " url:" + url);
			// if (StringUtils.isNotEmpty(url)) {// ��ַ���˱��룬��Ҫת����
			// LogUtil.writeUserloginLog("UserLoginService:urlת��ǰ:" + url);
			// url = JsoupUtil.urlDecode(url);
			// LogUtil.writeUserloginLog("UserLoginService:urlת���:" + url);
			// }
			Integer state = 10000;// ��¼״̬
			if (StringUtils.isEmpty(username)) {
				jsonStr = JsoupUtil.getLoginUrlJson("10003", "�û���Ϊ��", "", "");
			} else {
				Taobao ali = null;
				ali = tokens.get(username);
				if (ali == null) {// �û��״������¼
					logger.debug(username + "��tokens�ﲻ����");
					ali = new Taobao(username, userpw);
					state = ali.login("");// ��¼
					if (state == 0) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", ali.sellerUrl, "");
						tokens.remove(username);
					}
				} else {// �û��ڶ��������¼
					logger.debug(username + "��tokens�����");
					ali.userName = username;
					ali.passWord = userpw;
					if (StringUtils.isNotEmpty(code)) {// ��֤��У��
						state = ali.login(code);
						if (state == 0 && !userpw.equals(ali.passWord)) {// �û���һ�ε�¼�Ա�����ʾҪ������֤�룬���ܵڶ����û�������֤��������������ˣ��������Ҫ������������
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "�������", "", "");
						} else if (state == 0) {
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", ali.sellerUrl, "");
							tokens.remove(username);
						}
					} else if (StringUtils.isNotEmpty(url)) {// У������
						if (url.toLowerCase().equals("http://me.1688.com")) {
							state = 10003;// �����������
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
						} else if (url.toLowerCase().equals("http://domain.1688.com")) {
							state = 10003;// �����������
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
						} else {
							url += "/page/creditdetail.htm";
							Document doc = CommodityUtil.tbLoginDoc(url);// ��ȡ�Ա��û���¼����Doc
							String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// ��ȡ�Ա��û���¼��
//							String rateUrl = CommodityUtil.getRateUrl(doc, url);
							if (StringUtils.isEmpty(loginUserName)) {
								state = 10003;// �����������
								jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
							} else {
								if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
									state = 0;// �ɹ�
									String companyName = CommodityUtil.commodityName(doc, url);// ��˾��
									String shopName = CommodityUtil.shopName(doc);// ��������
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", url, shopName);
									tokens.remove(username);
//									if (CommodityUtil.openShopSuccess(rateUrl)){
//										state = 0;// �ɹ�
//										String companyName = CommodityUtil.commodityName(doc, url);// ��˾��
//										String shopName = CommodityUtil.shopName(doc);// ��������
//										jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", url, shopName);
//										tokens.remove(username);
//									}else{
//										state = 10008;
//										jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "����δ��꣬�ݲ��ܿ���", "", "");
//									}
									
								} else {
									state = 10004;// �������û���ƥ��
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "�������û���ƥ�䣬����������", "", "");
								}
							}
						}
					} else {// ��¼
						ali = new Taobao(username, userpw);
						state = ali.login("");// ��¼
						if (state == 0) {
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", ali.sellerUrl, "");
							tokens.remove(username);
						}
					}
				}
				logger.debug("��¼״̬:" + state + " jsonStr=" + jsonStr);
				if (StringUtils.isEmpty(jsonStr)) {
					if (state == 5) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "�˺���������", "", "");
					} else if (state == 3425) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��֤���ַ", ali.codeUrl, "");
						tokens.put(username, ali);
					} else if (state == 1000) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��֤���������", ali.codeUrl, "");
					} else if (state == 3501 || state == 100025) {
						jsonStr = JsoupUtil.getLoginUrlJson("3501", "�˺��������", "", "");
						tokens.remove(username);
					} else if (state == 3526) {// �������ǰ����Ҫ�û���������
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "����ͨ�˵�¼��������Ҫ��ȫУ��", JsoupUtil
								.urlEcode(ali.safeUrl), "");
						tokens.put(username, ali);
					} else if (state == 10001) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��½�������", "", "");
						tokens.remove(username);
					} else if (state == 10002) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "������ȡʧ��", "", "");
						tokens.remove(username);
					} else {
						tokens.remove(username);
						jsonStr = JsoupUtil.getJson("10000", "�����쳣�����Ժ�����");
					}
				}
			}
		} catch (Exception e) {
			logger.error("�û���¼ʧ�ܣ�username��" + username + " userpw��" + userpw + " code:" + code, e);
			jsonStr = JsoupUtil.getJson("10000", "�����쳣�����Ժ�����");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "�û���¼ʧ�ܣ�username��" + username + " userpw��" + userpw + " code:"
					+ code, LogUtil.getExceptionError(e));
		}
		LogUtil.writeUserloginLog("�û���¼��Ӧ:" + username + jsonStr.toString());
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("AliUserLoginService:" + username + "�û���¼����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
