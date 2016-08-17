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
 * 淘宝商家登录
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
		int start = (int) System.currentTimeMillis(); // 开始时间

		String username = JsoupUtil.urlDecodeByUtf(request.getHeader("username"));// 用户名
		String userpw = request.getHeader("userpw");// 用密码名
		String code = request.getHeader("code");// 验证码
		String url = request.getHeader("url");// 阿里域名地址（根据用户校验）
		String jsonStr = "";
		try {
			LogUtil.writeUserloginLog("TbUserLoginService:username：" + username + " userpw：" + userpw + " code:" + code
					+ " url:" + url);
			// if (StringUtils.isNotEmpty(url)) {// 地址做了编码，需要转回来
			// LogUtil.writeUserloginLog("UserLoginService:url转码前:" + url);
			// url = JsoupUtil.urlDecode(url);
			// LogUtil.writeUserloginLog("UserLoginService:url转码后:" + url);
			// }
			Integer state = 10000;// 登录状态
			if (StringUtils.isEmpty(username)) {
				jsonStr = JsoupUtil.getLoginUrlJson("10003", "用户名为空", "", "");
			} else {
				Taobao ali = null;
				ali = tokens.get(username);
				if (ali == null) {// 用户首次请求登录
					logger.debug(username + "在tokens里不存在");
					ali = new Taobao(username, userpw);
					state = ali.login("");// 登录
					if (state == 0) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", ali.sellerUrl, "");
						tokens.remove(username);
					}
				} else {// 用户第二次请求登录
					logger.debug(username + "在tokens里存在");
					ali.userName = username;
					ali.passWord = userpw;
					if (StringUtils.isNotEmpty(code)) {// 验证码校验
						state = ali.login(code);
						if (state == 0 && !userpw.equals(ali.passWord)) {// 用户第一次登录淘宝先提示要输入验证码，可能第二次用户输入验证码后把密码输入错了，这种情况要重新输入密码
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "密码错误", "", "");
						} else if (state == 0) {
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", ali.sellerUrl, "");
							tokens.remove(username);
						}
					} else if (StringUtils.isNotEmpty(url)) {// 校验域名
						if (url.toLowerCase().equals("http://me.1688.com")) {
							state = 10003;// 域名输入错误
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
						} else if (url.toLowerCase().equals("http://domain.1688.com")) {
							state = 10003;// 域名输入错误
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
						} else {
							url += "/page/creditdetail.htm";
							Document doc = CommodityUtil.tbLoginDoc(url);// 获取淘宝用户登录名的Doc
							String loginUserName = CommodityUtil.tbLoginUserName(doc, url);// 获取淘宝用户登录名
//							String rateUrl = CommodityUtil.getRateUrl(doc, url);
							if (StringUtils.isEmpty(loginUserName)) {
								state = 10003;// 域名输入错误
								jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "地址输入错误，请重新输入", "", "");
							} else {
								if (loginUserName.toLowerCase().equals(username.toLowerCase())) {
									state = 0;// 成功
									String companyName = CommodityUtil.commodityName(doc, url);// 公司名
									String shopName = CommodityUtil.shopName(doc);// 店铺名称
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", url, shopName);
									tokens.remove(username);
//									if (CommodityUtil.openShopSuccess(rateUrl)){
//										state = 0;// 成功
//										String companyName = CommodityUtil.commodityName(doc, url);// 公司名
//										String shopName = CommodityUtil.shopName(doc);// 店铺名称
//										jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", url, shopName);
//										tokens.remove(username);
//									}else{
//										state = 10008;
//										jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "资质未达标，暂不能开店", "", "");
//									}
									
								} else {
									state = 10004;// 域名和用户不匹配
									jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "域名和用户不匹配，请重新输入", "", "");
								}
							}
						}
					} else {// 登录
						ali = new Taobao(username, userpw);
						state = ali.login("");// 登录
						if (state == 0) {
							jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登录成功", ali.sellerUrl, "");
							tokens.remove(username);
						}
					}
				}
				logger.debug("登录状态:" + state + " jsonStr=" + jsonStr);
				if (StringUtils.isEmpty(jsonStr)) {
					if (state == 5) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "账号名不存在", "", "");
					} else if (state == 3425) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "验证码地址", ali.codeUrl, "");
						tokens.put(username, ali);
					} else if (state == 1000) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "验证码输入错误", ali.codeUrl, "");
					} else if (state == 3501 || state == 100025) {
						jsonStr = JsoupUtil.getLoginUrlJson("3501", "账号密码错误", "", "");
						tokens.remove(username);
					} else if (state == 3526) {// 这中情况前端需要用户输入域名
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "您开通了登录保护，需要安全校验", JsoupUtil
								.urlEcode(ali.safeUrl), "");
						tokens.put(username, ali);
					} else if (state == 10001) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "登陆请求出错", "", "");
						tokens.remove(username);
					} else if (state == 10002) {
						jsonStr = JsoupUtil.getLoginUrlJson(state.toString(), "域名获取失败", "", "");
						tokens.remove(username);
					} else {
						tokens.remove(username);
						jsonStr = JsoupUtil.getJson("10000", "网络异常，请稍后再试");
					}
				}
			}
		} catch (Exception e) {
			logger.error("用户登录失败：username：" + username + " userpw：" + userpw + " code:" + code, e);
			jsonStr = JsoupUtil.getJson("10000", "网络异常，请稍后再试");
			// 发送邮件
			SimpleMailSender sms = MailSenderFactory.getSender();
			sms.send(CommodityContent.getRecipients(), "用户登录失败：username：" + username + " userpw：" + userpw + " code:"
					+ code, LogUtil.getExceptionError(e));
		}
		LogUtil.writeUserloginLog("用户登录响应:" + username + jsonStr.toString());
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 处理时间
		LogUtil.writeCommodityLog("AliUserLoginService:" + username + "用户登录共耗时: " + re + "毫秒");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
