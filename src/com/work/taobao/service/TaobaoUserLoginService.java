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
 * 淘宝商家登录
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
		int start = (int) System.currentTimeMillis(); // 开始时间

		String username = JsoupUtil.urlDecodeByUtf(request
				.getHeader("username"));// 用户名
		String userpw = request.getHeader("userpw");// 用密码名
		String code = request.getHeader("code");// 验证码(checkcode=1时为短信验证码)
		String sendcode = request.getHeader("sendcode");// 获取短信验证码
		String checkcode = request.getHeader("checkcode");// 用验证码校验
		String target = request.getHeader("target");// 短信校验的session值(checkcode=1时必传)
		String url = request.getHeader("url");// 获取短信验证码的url(sendcode=1或者checkcode=1时必传,param对应的值在url里面，需要截取)
		String jsonStr = "";
		try {
			LogUtil.writeUserloginLog("UserLoginService:username：" + username + " userpw：" + userpw + " code:" + code
					+ " sendcode:" + sendcode + " checkcode:" + checkcode + " target:" + target + " url:" + url);
			// if (StringUtils.isNotEmpty(url)) {// 地址做了编码，需要转回来
			// LogUtil.writeUserloginLog("UserLoginService:url转码前:" + url);
			// url = JsoupUtil.urlDecode(url);
			// LogUtil.writeUserloginLog("UserLoginService:url转码后:" + url);
			// }
			Integer state = 10000;// 登录状态
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(userpw)) {
				jsonStr = JsoupUtil.getLoginJson("10003", "用户名或密码为空", "", "", "", "");
			} else {
				Taobao tb = null;
				tb = tokens.get(username);
				if (tb == null) {
					logger.debug(username + "在tokens里不存在");
					tb = new Taobao(username, userpw);
					state = tb.login("");
				} else {// 用户第二次请求登录
					logger.debug(username + "在tokens里存在");
					tb.userName = username;
					tb.passWord = userpw;
					if (StringUtils.isNotEmpty(sendcode) && sendcode.equals("1")) {// 获取短信验证码的情况
						logger.debug("获取短信验证码");
						String param = url;// 得到param值
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
						if (StringUtils.isNotEmpty(value)) {// 得到target，获取验证码需要用到
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
							if (state == 0) {// 短信验证码获取成功
								jsonStr = JsoupUtil.getLoginJson("1", "短信验证码获取成功", JsoupUtil.urlEcode(url), target,
										username, userpw);
							} else if (state == 10003) {
								jsonStr = JsoupUtil.getLoginJson(state.toString(), Taobao.message,
										JsoupUtil.urlEcode(url), target, username, userpw);
							}
						} else {
							tokens.remove(username);
							jsonStr = JsoupUtil.getLoginJson("10005", "短信验证已超时,请返回重新登录", "", "", "", "");
						}
					} else if (StringUtils.isNotEmpty(checkcode) && checkcode.equals("1")) {// 短信验证码校验的情况
						logger.debug("短信验证码校验");
						if (!StringUtils.isNum(code)) {// 短信验证码只可能是数字，淘宝登录时需要做判断
							logger.debug("短信验证码非数字");
							jsonStr = JsoupUtil.getLoginJson("10004", "短信验证码校验失败", JsoupUtil.urlEcode(url), target,
									username, userpw);
						} else {
							String param = url;// 得到param值
							if (StringUtils.isNotEmpty(param)) {
								if (param.indexOf("param=") > 0) {
									param = param.substring(param.indexOf("param=") + 6);
								}
							}
							state = tb.loginCode("http://aq.taobao.com/durex/checkcode?param=" + param, target, code);
							if (state == 10004) {// 短信验证码校验失败
								jsonStr = JsoupUtil.getLoginJson(state.toString(), Taobao.message,
										JsoupUtil.urlEcode(url), target, username, userpw);
							} else if (state == 10002) {
								// jsonStr = JsoupUtil.getLoginJson(state
								// .toString(), "域名获取失败", "", "", "", "");
								tokens.remove(username);
								tb = new Taobao(username, userpw);
								state = tb.login("");
							}
						}
					} else if (StringUtils.isNotEmpty(code)) {// 网页验证码校验
						state = tb.login(code);
						if (state == 0 && !userpw.equals(tb.passWord)) {// 用户第一次登录淘宝先提示要输入验证码，可能第二次用户输入验证码后把密码输入错了，这种情况要重新输入密码
							jsonStr = JsoupUtil.getLoginJson(state.toString(), "密码错误", "", "", "", "");
						}
					} else if (StringUtils.isNotEmpty(url)) {// 校验域名
						// TODO:linyj
						if (url.equals("http://me.1688.com")) {
							state = 10003;// 域名输入错误
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
						} else if (url.equals("http://domain.1688.com")) {
							state = 10003;// 域名输入错误
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
						} else {
							Document doc = CommodityUtil.tbLoginDoc(url);// 获取淘宝用户登录名的Doc
							String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// 获取淘宝用户登录名
							if (StringUtils.isEmpty(loginUserName)) {
								state = 10003;// 域名输入错误
								jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
							} else {
								if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
									state = 0;// 成功
									String shopName = CommodityUtil.shopName(doc);// 店铺名称
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", url, shopName);
									tokens.remove(username);
								} else {
									state = 10004;// 域名和用户不匹配
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "域名和用户不匹配，请重新输入", "", "");
								}
							}
						}
					} else {
						logger.debug("验证码为空，重新登录");
						tokens.remove(username);
						tb = new Taobao(username, userpw);
						state = tb.login("");
					}
				}
				logger.debug("登录状态:" + state);
				if (StringUtils.isEmpty(jsonStr)) {
					if (state == 0) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "登录成功", "https:"+tb.sellerUrl, "", "", "");
						tokens.remove(username);
					} else if (state == 5) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "账号名不存在", "", "", "", "");
					} else if (state == 3425) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "验证码地址", tb.codeUrl, "", "", "");
						tokens.put(username, tb);
					} else if (state == 1000) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "验证码输入错误", tb.codeUrl, "", "", "");
					} else if (state == 3501) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "账号密码错误", "", "", "", "");
						tokens.remove(username);
					} else if (state == 3526) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "您开通了登录保护，需要安全校验",
								JsoupUtil.urlEcode(tb.safeUrl), "", "", "");
						tokens.put(username, tb);
						// TODO:全安验证没法做，需要通过店铺名字获取用户名，并提醒用户输入昵称登录

					} else if (state == 10001) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "登陆请求出错", "", "", "", "");
						tokens.remove(username);
					} else if (state == 10002) {
						jsonStr = JsoupUtil.getLoginJson(state.toString(), "域名获取失败", "", "", "", "");
						tokens.remove(username);
					} else {
						jsonStr = JsoupUtil.getJson("10000", "网络异常，请稍后再试");
						tokens.remove(username);
					}
				}
			}
		} catch (Exception e) {
			logger.error("用户登录失败：username：" + username + " userpw：" + userpw + " code:" + code, e);
			jsonStr = JsoupUtil.getJson("10000", "网络异常，请稍后再试");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(),
					"用户登录失败：username：" + username + " userpw：" + userpw + " code:" + code,
					LogUtil.getExceptionError(e));
		}
		LogUtil.writeUserloginLog("用户登录响应:" + username + jsonStr.toString());
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("TaobaoUserLoginService:" + username + "用户登录共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}

	public static void main(String[] args) throws JSONException {
		String url = "https://kumishoes.taobao.com";
		String username = "kumikiwa女鞋";

		// String url = "https://icolorlady.taobao.com";
		// String username = "iamleesong";

		// 校验域名
		Document doc = CommodityUtil.tbLoginDoc(url);// 获取淘宝用户登录名的Doc
		String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// 获取淘宝用户登录名
		System.out.println("loginUserName:" + loginUserName);
		if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
			String companyName = CommodityUtil.commodityName(doc, url);// 公司名
			String shopName = CommodityUtil.shopName(doc);// 店铺名称
			System.out.println(companyName + "---" + "companyName");
			System.out.println(shopName + "---" + "shopName");
		} else {
			System.out.println("error");
		}

	}
}
