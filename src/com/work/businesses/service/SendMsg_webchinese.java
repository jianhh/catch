package com.work.businesses.service;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 短信发送
 * 
 * @author tangbiao
 * 
 */
public class SendMsg_webchinese {
	// 申请账号地址：http://sms.webchinese.cn/default.shtml
	public static void main(String[] args) throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", "本站用户名"),
				new NameValuePair("Key", "接口安全密码"),
				new NameValuePair("smsMob", "15767630205"),
				new NameValuePair("smsText", "短信内容测试") };
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println(result);

		post.releaseConnection();

	}
}
