package com.framework.httpsend;

import java.util.Map;

/**
 * HttpURLConnection���ʷ��ض���Vo
 * 
 * @author tangbiao
 * 
 */
public class HttpReturnVo {

	private String respString = ""; // http���󷵻ص�String
	private Map headers = null; // httpurlconnection.getHeaderFields()
	private byte[] tempb = null; // InputStream��Ӧ��byte[]

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
