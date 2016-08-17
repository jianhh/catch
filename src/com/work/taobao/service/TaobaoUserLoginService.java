package com.work.taobao.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;
import com.work.taobao.content.CommodityContent;
import com.work.taobao.util.CommodityUtil;
import com.work.util.JsoupUtil;
import com.work.util.Taobao;

/**
 * �Ա��̼ҵ�¼
 * 
 * @author tangbiao
 * 
 */
public class TaobaoUserLoginService extends BaseListener {

	private static JLogger logger = LoggerFactory.getLogger(TaobaoUserLoginService.class);
	private static ConcurrentMap<String, Taobao> tokens = new ConcurrentHashMap<String, Taobao>();

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("TaobaoUserLoginService......................");
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��

		String username = JsoupUtil.urlDecodeByUtf(request
				.getHeader("username"));// �û���
		String userpw = request.getHeader("userpw");// ��������
		String code = request.getHeader("code");// ��֤��(checkcode=1ʱΪ������֤��)
		String sendcode = request.getHeader("sendcode");// ��ȡ������֤��
		String checkcode = request.getHeader("checkcode");// ����֤��У��
		String target = request.getHeader("target");// ����У���sessionֵ(checkcode=1ʱ�ش�)
		String url = request.getHeader("url");// ��ȡ������֤���url(sendcode=1����checkcode=1ʱ�ش�,param��Ӧ��ֵ��url���棬��Ҫ��ȡ)
		String jsonStr = "";
		try {
			LogUtil.writeUserloginLog("UserLoginService:username��" + username + " userpw��" + userpw + " code:" + code
					+ " sendcode:" + sendcode + " checkcode:" + checkcode + " target:" + target + " url:" + url);
			// if (StringUtils.isNotEmpty(url)) {// ��ַ���˱��룬��Ҫת����
			// LogUtil.writeUserloginLog("UserLoginService:urlת��ǰ:" + url);
			// url = JsoupUtil.urlDecode(url);
			// LogUtil.writeUserloginLog("UserLoginService:urlת���:" + url);
			// }
			Integer state = 10000;// ��¼״̬
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(userpw)) {
				jsonStr = JsoupUtil.getLoginJson("10003", "�û���������Ϊ��", "", "", "", "");
			} else {
				Taobao tb = null;
				tb = tokens.get(username);
				if (tb == null) {
					logger.debug(username + "��tokens�ﲻ����");
					tb = new Taobao(username, userpw);
					state = tb.login("");
				} else {// �û��ڶ��������¼
					logger.debug(username + "��tokens�����");
					tb.userName = username;
					tb.passWord = userpw;
					if (StringUtils.isNotEmpty(sendcode) && sendcode.equals("1")) {// ��ȡ������֤������
						logger.debug("��ȡ������֤��");
						String param = url;// �õ�paramֵ
						if (StringUtils.isNotEmpty(param)) {
							if (param.indexOf("param=") > 0) {
								param = param.substring(param.indexOf("param=") + 6);
							}
						}
						String xml = JsoupUtil.conUrl("http://aq.taobao.com/durex/validate?param=" + param, false,
								"GBK", "", 0);
						Document doc = Jsoup.parse(xml);
						Elements inputEle = doc.select("input");
						String value = inputEle.attr("value");
						if (StringUtils.isNotEmpty(value)) {// �õ�target����ȡ��֤����Ҫ�õ�
							String data = value.substring(0, value.indexOf("tipsInfo") - 2) + "}";
							JSONObject json = new JSONObject(data);
							JSONArray options = json.getJSONArray("options");
							JSONObject optionsJson = options.getJSONObject(0);
							JSONArray optionText = optionsJson.getJSONArray("optionText");
							JSONObject optionTextJson = optionText.getJSONObject(0);
							target = optionTextJson.getString("code");

							state = tb.loginCode(
									"http://aq.taobao.com/durex/sendcode?param=" + param + "&checkType=phone", target,
									"");
							if (state == 0) {// ������֤���ȡ�ɹ�
								jsonStr = JsoupUtil.getLoginJson("1", "������֤���ȡ�ɹ�", JsoupUtil.urlEcode(url), target,
										username, userpw);
							} else if (state == 10003) {
								jsonStr = JsoupUtil.getLoginJson(state.toString(), Taobao.message,
										JsoupUtil.urlEcode(url), target, username, userpw);
							}
						} else {
							tokens.remove(username);
							jsonStr = JsoupUtil.getLoginJson("10005", "������֤�ѳ�ʱ,�뷵�����µ�¼", "", "", "", "");
						}
					} else if (StringUtils.isNotEmpty(checkcode) && checkcode.equals("1")) {// ������֤��У������
						logger.debug("������֤��У��");
						if (!StringUtils.isNum(code)) {// ������֤��ֻ���������֣��Ա���¼ʱ��Ҫ���ж�
							logger.debug("������֤�������");
							jsonStr = JsoupUtil.getLoginJson("10004", "������֤��У��ʧ��", JsoupUtil.urlEcode(url), target,
									username, userpw);
						} else {
							String param = url;// �õ�paramֵ
							if (StringUtils.isNotEmpty(param)) {
								if (param.indexOf("param=") > 0) {
									param = param.substring(param.indexOf("param=") + 6);
								}
							}
							state = tb.loginCode("http://aq.taobao.com/durex/checkcode?param=" + param, target, code);
							if (state == 10004) {// ������֤��У��ʧ��
								jsonStr = JsoupUtil.getLoginJson(state.toString(), Taobao.message,
										JsoupUtil.urlEcode(url), target, username, userpw);
							} else if (state == 10002) {
								// jsonStr = JsoupUtil.getLoginJson(state
								// .toString(), "������ȡʧ��", "", "", "", "");
								tokens.remove(username);
								tb = new Taobao(username, userpw);
								state = tb.login("");
							}
						}
					} else if (StringUtils.isNotEmpty(code)) {// ��ҳ��֤��У��
						state = tb.login(code);
						if (state == 0 && !userpw.equals(tb.passWord)) {// �û���һ�ε�¼�Ա�����ʾҪ������֤�룬���ܵڶ����û�������֤��������������ˣ��������Ҫ������������
							jsonStr = JsoupUtil.getLoginJson(state.toString(), "�������", "", "", "", "");
						}
					} else if (StringUtils.isNotEmpty(url)) {// У������
						// TODO:linyj
						if (url.equals("http://me.1688.com")) {
							state = 10003;// �����������
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
						} else if (url.equals("http://domain.1688.com")) {
							state = 10003;// �����������
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
						} else {
							Document doc = CommodityUtil.tbLoginDoc(url);// ��ȡ�Ա��û���¼����Doc
							String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// ��ȡ�Ա��û���¼��
							if (StringUtils.isEmpty(loginUserName)) {
								state = 10003;// �����������
								jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��ַ�����������������", "", "");
							} else {
								if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
									state = 0;// �ɹ�
									String shopName = CommodityUtil.shopName(doc);// ��������
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "��¼�ɹ�", url, shopName);
									tokens.remove(username);
								} else {
									state = 10004;// �������û���ƥ��
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "�������û���ƥ�䣬����������", "", "");
								}
							}
						}
					} else {
						logger.debug("��֤��Ϊ�գ����µ�¼");
						tokens.remove(username);
						tb = new Taobao(username, userpw);
						state = tb.login("");
					}
				}
				logger.debug("��¼״̬:" + state);
				if (StringUtils.isEmpty(jsonStr)) {
					if (state == 0) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "��¼�ɹ�", "https:"+tb.sellerUrl, "", "", "");
						tokens.remove(username);
					} else if (state == 5) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "�˺���������", "", "", "", "");
					} else if (state == 3425) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "��֤���ַ", tb.codeUrl, "", "", "");
						tokens.put(username, tb);
					} else if (state == 1000) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "��֤���������", tb.codeUrl, "", "", "");
					} else if (state == 3501) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "�˺��������", "", "", "", "");
						tokens.remove(username);
					} else if (state == 3526) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "����ͨ�˵�¼��������Ҫ��ȫУ��",
								JsoupUtil.urlEcode(tb.safeUrl), "", "", "");
						tokens.put(username, tb);
						// TODO:ȫ����֤û��������Ҫͨ���������ֻ�ȡ�û������������û������ǳƵ�¼

					} else if (state == 10001) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "��½�������", "", "", "", "");
						tokens.remove(username);
					} else if (state == 10002) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "������ȡʧ��", "", "", "", "");
						tokens.remove(username);
					} else {
						jsonStr = JsoupUtil.getJson("10000", "�����쳣�����Ժ�����");
						tokens.remove(username);
					}
				}
			}
		} catch (Exception e) {
			logger.error("�û���¼ʧ�ܣ�username��" + username + " userpw��" + userpw + " code:" + code, e);
			jsonStr = JsoupUtil.getJson("10000", "�����쳣�����Ժ�����");
			// �����ʼ�
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(),
					"�û���¼ʧ�ܣ�username��" + username + " userpw��" + userpw + " code:" + code,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeUserloginLog("�û���¼��Ӧ:" + username + jsonStr.toString());
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ����ʱ��
		LogUtil.writeCommodityLog("TaobaoUserLoginService:" + username + "�û���¼����ʱ: " + re + "����");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) throws JSONException {
		String url = "https://kumishoes.taobao.com";
		String username = "kumikiwaŮЬ";

		// String url = "https://icolorlady.taobao.com";
		// String username = "iamleesong";

		// У������
		Document doc = CommodityUtil.tbLoginDoc(url);// ��ȡ�Ա��û���¼����Doc
		String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// ��ȡ�Ա��û���¼��
		System.out.println("loginUserName:" + loginUserName);
		if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
			String companyName = CommodityUtil.commodityName(doc, url);// ��˾��
			String shopName = CommodityUtil.shopName(doc);// ��������
			System.out.println(companyName + "---" + "companyName");
			System.out.println(shopName + "---" + "shopName");
		} else {
			System.out.println("error");
		}

	}
}
