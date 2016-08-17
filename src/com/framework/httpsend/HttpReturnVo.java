package com.framework.httpsend;

import java.util.Map;

/**
 * HttpURLConnection访问返回对象Vo
 * 
 * @author tangbiao
 * 
 */
public class HttpReturnVo {

	private String respString = ""; // http请求返回的String
	private Map headers = null; // httpurlconnection.getHeaderFields()
	private byte[] tempb = null; // InputStream对应的byte[]

	public String getRespString() {

		return respString;
	}

	public void setRespString(String respString) {

		this.respString = respString;
	}

	public Map getHeaders() {

		return headers;
	}

	public void setHeaders(Map headers) {

		this.headers = headers;
	}

	public byte[] getTempb() {

		return tempb;
	}

	public void setTempb(byte[] tempb) {

		this.tempb = tempb;
	}

}
