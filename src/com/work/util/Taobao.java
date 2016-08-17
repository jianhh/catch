package com.work.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StringUtils;

/**
 * 淘宝登录
 * 
 */
public class Taobao {

	private static JLogger logger = LoggerFactory.getLogger(Taobao.class);

	private static String loginUrl = "https://login.taobao.com/member/login.jhtml?style=mini&amp;from=b2b&amp;full_redirect=true&amp;redirect_url=http%3A%2F%2Flogin.1688.com%2Fmember%2Fjump.htm%3Ftarget%3Dhttp%253A%252F%252Flogin.1688.com%252Fmember%252FmarketSigninJump.htm%253FDone%253Dhttp%25253A%25252F%25252Fme.1688.com%25252Fpersonal_profile.htm&amp;reg=http%3A%2F%2Fmember.1688.com%2Fmember%2Fjoin%2Fenterprise_join.htm%3Flead%3Dhttp%253A%252F%252Fme.1688.com%252Fpersonal_profile.htm%26leadUrl%3Dhttp%253A%252F%252Fme.1688.com%252Fpersonal_profile.htm%26tracelog%3Dnotracelog_s_reg";
	private static String redirectUrl = "http://store.taobao.com/shop/view_shop.htm";// 店铺主页
	// private static String tbToken = null;// 淘宝的token
	public static String codeUrl = "";// 验证码地址
	public static String safeUrl = "";// 安全校验码地址
	public static String sellerUrl = "";// 店铺域名
	public static String message = "";// 短信验证，淘宝返回的提示语

	public static String userName = "";// 用户名
	public static String passWord = "";// 密码明文

	public static DefaultHttpClient httpclient = null;// HttpClient对象
	public static HttpResponse response = null;

	/**
	 * 构造函数
	 * 
	 * @param userName
	 * @param passWord
	 */
	public Taobao(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
		this.codeUrl = "";
		this.safeUrl = "";
		this.sellerUrl = "";
		this.httpclient = null;
		this.response = null;
	}

	public static void main(String[] args) throws IOException {
		// tangbiao891022 tangbiao_130803
		// iamleesong chenjunwu1314520
		Taobao tb = new Taobao("recoba888", "abc%123fuyu");
		Integer state = tb.login("");
		if (state != 1) {
			for (int i = 0; i < 100; i++) {
				System.out.println("登录状态：" + state);
				if (state != 0) {
					if (state == 3425 || state == 1000) {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(System.in));
						System.out.println("验证码地址：" + codeUrl);
						System.out.print("请输入验证码：");
						String code = reader.readLine();
						state = tb.login(code);
					} else if (state == 3526) {
						String safeUrl = tb.safeUrl;
						if (StringUtils.isNotEmpty(safeUrl)) {
							if (safeUrl.indexOf("param=") > 0) {
								safeUrl = safeUrl.substring(safeUrl
										.indexOf("param=") + 6);
							}
						}
						System.out.println("您开通了登录保护，需要安全校验" + safeUrl);
						System.out.println("安全校验地址：" + tb.safeUrl);
					} else if (state == 3501) {
						System.out.println("账号密码错误");
						break;
					} else if (state == 10001) {
						System.out.println("登陆请求出错");
						break;
					} else if (state == 10002) {
						System.out.println("域名获取失败");
						break;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
		if (state == 0) {
			System.out.println("登录成功");
		} else {
			System.out.println("登录失败");
		}
	}

	/**
	 * 登陆淘宝
	 * 
	 * @return(0:成功；5：账号名不存在；3425：要输入验证码；1000：验证码错误；3501：账号密码错误；3526：需要安全校验；10001：登陆请求出错；10002：域名获取失败；10000：其他错误)
	 */
	public Integer login(String code) {
		LogUtil.writeUserloginLog("userName:" + userName + " code:" + code
				+ " httpclient:" + httpclient);
		if (null == httpclient) {
			httpclient = new DefaultHttpClient();
		}

		// 设定cookie策略
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		// 登陆使用的表单数据
		List<NameValuePair> loginParams = new ArrayList<NameValuePair>();

		loginParams.add(new BasicNameValuePair("TPL_username", userName));
		loginParams.add(new BasicNameValuePair("TPL_password", passWord));
		loginParams.add(new BasicNameValuePair("CtrlVersion", "1,0,0,7"));
		loginParams.add(new BasicNameValuePair("TPL_redirect_url", ""));
		loginParams.add(new BasicNameValuePair("action", "Authenticator"));
		loginParams.add(new BasicNameValuePair("callback", "jsonp312"));
		loginParams.add(new BasicNameValuePair("css_style", ""));
		loginParams.add(new BasicNameValuePair("event_submit_do_login",
				"anything"));
		// loginParams.add(new BasicNameValuePair("TPL_redirect_url",
		// "http://login.1688.com/member/jump.htm?target=http%3A%2F%2Flogin.1688.com%2Fmember%2FmarketSigninJump.htm%3FDone%3Dhttp%253A%252F%252Fme.1688.com%252Fpersonal_profile.htm"));
		loginParams.add(new BasicNameValuePair("fc", Integer.toString(2)));
		loginParams.add(new BasicNameValuePair("from", "tb"));
		loginParams.add(new BasicNameValuePair("from_encoding", ""));
		loginParams.add(new BasicNameValuePair("guf", ""));
		loginParams.add(new BasicNameValuePair("gvfdcname", ""));
		loginParams.add(new BasicNameValuePair("isIgnore", ""));
		loginParams.add(new BasicNameValuePair("llnick", ""));
		loginParams
				.add(new BasicNameValuePair("loginType", Integer.toString(3)));
		loginParams
				.add(new BasicNameValuePair("longLogin", Integer.toString(0)));
		loginParams.add(new BasicNameValuePair("minipara", ""));
		loginParams.add(new BasicNameValuePair("minititle", ""));
		loginParams.add(new BasicNameValuePair("need_sign", ""));
		loginParams.add(new BasicNameValuePair("need_user_id", ""));
		loginParams.add(new BasicNameValuePair("not_duplite_str", ""));
		loginParams.add(new BasicNameValuePair("popid", ""));
		loginParams.add(new BasicNameValuePair("poy", ""));
		loginParams.add(new BasicNameValuePair("pstrong", ""));
		loginParams.add(new BasicNameValuePair("sign", ""));
		loginParams.add(new BasicNameValuePair("style", "default"));
		loginParams.add(new BasicNameValuePair("style", "000001"));
		loginParams.add(new BasicNameValuePair("tid", ""));

		// :TODO 设置验证码
		if (StringUtils.isNotEmpty(code)) {
			loginParams.add(new BasicNameValuePair("TPL_checkcode", code));
		}

		// 登陆post请求
		HttpPost loginPost = new HttpPost(loginUrl);
		loginPost.addHeader("Referer", loginUrl);
		loginPost.addHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		loginPost
				.addHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.2; Tablet PC 2.0)");
		loginPost.addHeader("Host", "login.taobao.com");
		try {
			loginPost.setEntity(new UrlEncodedFormEntity(loginParams,
					HTTP.UTF_8));

			// 获取登陆应答内容
			response = httpclient.execute(loginPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Integer status = getRedirectUrl();
				LogUtil.writeUserloginLog("userName:" + userName + " 登录状态:"
						+ status);
				if (status == 0) {
					// 有重定向说明成功了,获取token
					// getTbToken();
					HttpGet meGet = new HttpGet(redirectUrl);
					HttpResponse meResponse = httpclient.execute(meGet);
					HttpEntity httpEntity = meResponse.getEntity();
					String xml = EntityUtils.toString(httpEntity);// 取出应答字符串
					// System.out.println(xml);
					Document doc = Jsoup.parse(xml);
					Elements nameEle = doc.select("[class=shop-name]");
					Elements aEle = nameEle.select("a");
					if (aEle != null) {
						String href = aEle.attr("href");
						LogUtil.writeUserloginLog("userName:" + userName
								+ " 淘宝域名：" + href);
						if (StringUtils.isNotEmpty(href)) {
							sellerUrl = href;
							return 0;
						}
					}
					return 10002;
				} else {
					 if (StringUtils.isNotEmpty(code)) {
						 return 3526;// 前端需要用户输入域名
						 }
					return status;
				}
			} else {
				LogUtil.writeUserloginLog("userName:" + userName
						+ "登陆请求出错，post返回状态:"
						+ response.getStatusLine().getStatusCode());
				return 10001;
			}
		} catch (Exception e) {
			logger.error("登陆请求出错", e);
		} finally {
			loginPost.abort();
		}
		return 10000;
	}

	/**
	 * 登陆淘宝(短信校验)
	 * 
	 * @return(0:成功；10002：域名获取失败；10003：短信验证码获取失败；10004：短信验证码输入错误；10000：其他错误)
	 */
	public Integer loginCode(String loginUrl, String target, String checkCode) {
		LogUtil.writeUserloginLog("userName:" + userName + " loginUrl:"
				+ loginUrl + " target:" + target + " checkCode:" + checkCode
				+ " httpclient:" + httpclient);
		if (null == httpclient) {
			httpclient = new DefaultHttpClient();
		}

		// 设定cookie策略
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		// 登陆使用的表单数据
		List<NameValuePair> loginParams = new ArrayList<NameValuePair>();

		loginParams.add(new BasicNameValuePair("checkType", "phone"));
		loginParams.add(new BasicNameValuePair("target", target));
		if (StringUtils.isNotEmpty(checkCode)) {
			loginParams.add(new BasicNameValuePair("checkCode", checkCode));
		}

		// 登陆post请求
		HttpPost loginPost = new HttpPost(loginUrl);
		loginPost.addHeader("Referer", loginUrl);
		loginPost.addHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		loginPost
				.addHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.2; Tablet PC 2.0)");
		loginPost.addHeader("Host", "aq.taobao.com");
		try {
			loginPost.setEntity(new UrlEncodedFormEntity(loginParams,
					HTTP.UTF_8));

			// 获取登陆应答内容
			response = httpclient.execute(loginPost);
			HttpEntity resEntity = response.getEntity();
			String bufferPageHtml = EntityUtils.toString(resEntity, HTTP.UTF_8);
			JSONObject map = JSONObject.fromObject(bufferPageHtml);
			boolean isSuccess = map.getBoolean("isSuccess");
			String mess = map.getString("message");
			message = JsoupUtil.urlDecodeByUtf(mess);
			if (StringUtils.isEmpty(checkCode)) {// 获取短信验证码的情况
				LogUtil.writeUserloginLog("userName:" + userName
						+ " 获取短信验证码状态:" + isSuccess + " 获取短信验证码信息:" + message);
				if (isSuccess) {
					return 0;
				} else {
					return 10003;
				}
			} else {// 短信验证码校验的情况
				LogUtil.writeUserloginLog("userName:" + userName
						+ " 短信验证码校验状态:" + isSuccess + " 短信验证码校验状态:" + message);
				if (!isSuccess) {
					return 10004;
				}
			}

			HttpGet meGet = new HttpGet(redirectUrl);
			HttpResponse meResponse = httpclient.execute(meGet);
			HttpEntity httpEntity = meResponse.getEntity();
			String xml = EntityUtils.toString(httpEntity);// 取出应答字符串
			// LogUtil.writeUserloginLog("xml:" + xml);
			Document doc = Jsoup.parse(xml);
			Elements nameEle = doc.select("[class=shop-name]");
			Elements aEle = nameEle.select("a");
			if (aEle != null) {
				String href = aEle.attr("href");
				LogUtil.writeUserloginLog("userName:" + userName + " 淘宝域名："
						+ href);
				if (StringUtils.isNotEmpty(href)) {
					sellerUrl = href;
					return 0;
				}
			}
			return 10002;
		} catch (Exception e) {
			logger.error("登陆请求出错", e);
		} finally {
			loginPost.abort();
		}
		return 10000;
	}

	/**
	 * 获取淘宝重定向url
	 * 
	 * @return
	 */
	private Integer getRedirectUrl() {

		HttpEntity resEntity = response.getEntity();

		try {
			String bufferPageHtml = EntityUtils.toString(resEntity, HTTP.UTF_8);
			JSONObject map = JSONObject.fromObject(bufferPageHtml);
			LogUtil.writeUserloginLog("userName:" + userName + " map:"
					+ map.toString());
			Boolean state = (Boolean) map.get("state");

			if (state == false) {
				JSONObject dataObj = map.getJSONObject("data");
				Integer code = (Integer) dataObj.get("code");

				// 淘宝提示需要输入验证码或者验证码错误时，获取验证码地址
				if (code == 3425 || code == 1000) {
					codeUrl = (String) dataObj.get("ccurl");
				} else if (code == 3526) {// 安全校验
					safeUrl = (String) dataObj.get("url");
				}

				return code;
			} else {
				return 0;
			}

		} catch (Exception e) {
			logger.error("获取淘宝重定向url失败", e);
		}
		return 10000;
	}

	/**
	 * 获取淘宝登陆令牌 从httpclient对象的cookie中获取
	 * 
	 * @param redirectUrl
	 */
	private void getTbToken() {
		HttpGet itaobaoGet = new HttpGet(redirectUrl);
		try {
			httpclient.execute(itaobaoGet);
		} catch (Exception e) {
			logger.error("获取淘宝登陆令牌失败", e);
		} finally {
			itaobaoGet.abort();
		}

		CookieStore cookiestore = httpclient.getCookieStore();
		List<Cookie> cookies = cookiestore.getCookies();
		if (cookies.isEmpty()) {
			logger.debug("cookies is null!");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				Cookie cookie = cookies.get(i);
				logger.debug(cookies.get(i).toString());
				if (cookie.getName().equals("_tb_token_")) {
					// tbToken = cookie.getValue();
					// logger.debug("淘宝令牌:" + tbToken);
				}
			}
		}
	}
}