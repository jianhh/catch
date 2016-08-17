package com.work.businesses.service;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * ���ŷ���
 * 
 * @author tangbiao
 * 
 */
public class SendMsg_webchinese {
	// �����˺ŵ�ַ��http://sms.webchinese.cn/default.shtml
	public static void main(String[] args) throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// ��ͷ�ļ�������ת��
		NameValuePair[] data = { new NameValuePair("Uid", "��վ�û���"),
				new NameValuePair("Key", "�ӿڰ�ȫ����"),
				new NameValuePair("smsMob", "15767630205"),
				new NameValuePair("smsText", "�������ݲ���") };
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
