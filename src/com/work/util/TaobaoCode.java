package com.work.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
public class TaobaoCode {

	private static JLogger logger = LoggerFactory.getLogger(TaobaoCode.class);
	// 发短信连接：http://aq.taobao.com/durex/sendcode?param=
	// 带短信验证码连接：http://aq.taobao.com/durex/checkcode?param=
	// private static String loginUrl =
	// "http://aq.taobao.com/durex/sendcode?param=uitbf9qnRr%2Bhal%2BcYPnWP88A8zIM%2BwASO%2FECvsEkJPqTL8P6EvbPSZvgjHI0iJz1Xtt62A%2BK8rh31gbDBPKaZh6xEXCaa9JYp%2Fv7GTPNTGYQmEubq56Wnhrt4gnOVLdoHbvyGMiEhySeGDXssVZDdPMlUnEjx1gDSJ2W6NhtOZENtzeU0XysoewEjf6kcj%2B3w%2Bnjccc7slFrmLaqrV4nlStP6Dna2QpSUKwJX%2BXhPqtuxPxN5%2BZ37UatkgAYFxC%2BaoTLL1vE9cLaDhW%2B0wFNoDhhjdV9bYrVanC8DEc%2B5SY%2F%2Fe9bJYUG6Fmo8CTqi%2BelE02pgSTTRsos7sAiV9yft1WoQe8okDGban9J2Vp2rgoHEoP79%2BD8DK2ph50kPpYHRMBHKgjiembuV9WXvWWzxTMkrfmJh4JLkugwv9m2R4xOYoAu8inoShYqD6sNaCLF";
	private static String tbToken = null;// 淘宝的token
	public static String codeUrl = "";// 验证码地址
	public static String safeUrl = "";// 安全校验码地址
	public static String sellerUrl = "";// 店铺域名

	private static DefaultHttpClient httpclient = null;// HttpClient对象
	private static HttpResponse response = null;
	public static String userName = "";// 用户名
	public static String passWord = "";// 密码明文
	private static String redirectUrl = "http://me.1688.com/";// 个人主页

	/**
	 * 构造函数
	 * 
	 * @param userName
	 * @param passWord
	 */
	public TaobaoCode(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}

	public static void main(String[] args) throws IOException {
		// tangbiao891022 tangbiao_130803
		// wegoooooo mingming111
		TaobaoCode tb = new TaobaoCode("tangbiao891022", "tangbiao_1308031");
		String url = "http://aq.taobao.com/durex/sendcode?param=uitbf9qnRr%2Bhal%2BcYPnWP88A8zIM%2BwASO%2FECvsEkJPqTL8P6EvbPSZvgjHI0iJz1Xtt62A%2BK8rh31gbDBPKaZh6xEXCaa9JYp%2Fv7GTPNTGYQmEubq56Wnhrt4gnOVLdoHbvyGMiEhySeGDXssVZDdPMlUnEjx1gDSJ2W6NhtOZENtzeU0XysoU5kS3xITMlVPB9kShlSBU9rmLaqrV4nlStP6Dna2QpSUKwJX%2BXhPqutDm0gZjOTpDmM5DBO2AOHmPeVVoG5Y%2BtwzXHsaOmzMB2AriqN%2B3fqanC8DEc%2B5Sb73lfYQjVQQBUKRki7tFfXJuv3ymZATvtYMVV87YVgpkWqwj0AtQFgan9J2Vp2rgprOyoB61fhVeAQKft0cdJbZ3osd5EhsrghOK6jEm2s3w%3D%3D";
		Integer state = tb
				.loginCode(
						url,
						"D8435ED6744C59FF10AAF7148F09DE8F76A4CB9B5950638DAC93CBD0E3453439EA8C05377646271E6DEA5A314F",
						"");
		System.out.println(state);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("请输入验证码：");
		String code = reader.readLine();
		String codeurl = "http://aq.taobao.com/durex/checkcode?param=uitbf9qnRr%2Bhal%2BcYPnWP88A8zIM%2BwASO%2FECvsEkJPqTL8P6EvbPSZvgjHI0iJz1Xtt62A%2BK8rh31gbDBPKaZh6xEXCaa9JYp%2Fv7GTPNTGYQmEubq56Wnhrt4gnOVLdoHbvyGMiEhySeGDXssVZDdPMlUnEjx1gDSJ2W6NhtOZENtzeU0XysoU5kS3xITMlVPB9kShlSBU9rmLaqrV4nlStP6Dna2QpSUKwJX%2BXhPqutDm0gZjOTpDmM5DBO2AOHmPeVVoG5Y%2BtwzXHsaOmzMB2AriqN%2B3fqanC8DEc%2B5Sb73lfYQjVQQBUKRki7tFfXJuv3ymZATvtYMVV87YVgpkWqwj0AtQFgan9J2Vp2rgprOyoB61fhVeAQKft0cdJbZ3osd5EhsrghOK6jEm2s3w%3D%3D";
		state = tb
				.loginCode(
						codeurl,
						"D8435ED6744C59FF10AAF7148F09DE8F76A4CB9B5950638DAC93CBD0E3453439EA8C05377646271E6DEA5A314F",
						code);
		System.out.println(state);
	}

	/**
	 * 登陆淘宝(短信校验)
	 * 
	 * @return(0:成功；5：账号名不存在；3425：要输入验证码；1000：验证码错误；3501：账号密码错误；3526：需要安全校验；10001：登陆请求出错；10002：域名获取失败；10003：短信验证码获取失败;10000：其他错误)
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
		// D84303A9A530224AD526907832702326B6175C4B8A1940751CFC643FAF4C03352D5DC95C64AE40B865DDD3783D
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
		loginPost.addHeader("Host", "login.taobao.com");
		try {
			loginPost.setEntity(new UrlEncodedFormEntity(loginParams,
					HTTP.UTF_8));

			// 获取登陆应答内容
			response = httpclient.execute(loginPost);
			HttpEntity resEntity = response.getEntity();
			String bufferPageHtml = EntityUtils.toString(resEntity, HTTP.UTF_8);
			JSONObject map = JSONObject.fromObject(bufferPageHtml);
			System.out.println("map:" + map.toString());
			boolean isSuccess = map.getBoolean("isSuccess");
			if (StringUtils.isEmpty(checkCode)) {
				if (isSuccess) {
					return 0;
				} else {
					return 10003;
				}
			} else {// 短信验证码校验的情况
				if (!isSuccess) {
					return 10004;
				}
			}

			HttpGet meGet = new HttpGet(redirectUrl);
			HttpResponse meResponse = httpclient.execute(meGet);
			HttpEntity httpEntity = meResponse.getEntity();
			String xml = EntityUtils.toString(httpEntity);// 取出应答字符串
			// System.out.println(xml);
			Document doc = Jsoup.parse(xml);
			Elements companyEle = doc.select("[class=cell]");
			if (companyEle != null) {
				String href = companyEle.attr("href");
				LogUtil.writeUserloginLog("阿里域名：" + href);
				if (StringUtils.isNotEmpty(href)) {
					String url = href.substring(href.indexOf(".com:80") + 7);
					if (url.length() == 0) {
						sellerUrl = href;
						return 0;
					}
				}
			}
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
			logger.debug("map:" + map.toString());
			Boolean state = (Boolean) map.get("state");

			if (state == false) {
				JSONObject dataObj = map.getJSONObject("data");
				Integer code = (Integer) dataObj.get("code");
				LogUtil.writeUserloginLog("淘宝响应的错误码:" + code);

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
					tbToken = cookie.getValue();
					logger.debug("淘宝令牌:" + tbToken);
				}
			}
		}
	}
}