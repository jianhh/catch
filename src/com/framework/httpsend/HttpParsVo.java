package com.framework.httpsend;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpURLConnection对象参数Vo
 * 
 * @author tangbiao
 * 
 */
public class HttpParsVo {

	private String proxyHost = "";// 请求代理ip

	private int proxyPort = 0;// 请求代理端口

	private String charset = "GBK";// 响应数据的编码格式

	private boolean doOutput = true; // DoOutput标志设置为true，表示该应用程序，打算写数据到URL连接

	private boolean doInput = true; // 将DoInput标志为true表明，应用程序打算从URL连接读取数据。

	private boolean isPost = true; // URL请求方式，默认为post方式

	private boolean useCaches = false; // UseCaches为true，则允许连接使用任何可用的缓存。如果为false，缓存都被忽略。

	private boolean instanceFollowRedirects = true; // 设置是否应自动跟随HTTP重定向

	private int readTimeout = 8000; // 设置读取超时指定的超时时间，单位为毫秒

	private int connectTimeout = 8000; // 设置指定超时值，以毫秒为单位

	private Map<String, String> headMap = new HashMap<String, String>(); // 设置一般请求属性。如果关键的属性已经存在，覆盖它的价值与新的价值。注：HTTP要求所有请求属性可以合法地拥有多个实例使用相同的密钥，使用以逗号分隔的列表语法，使多个属性追加到一个单一的财产。

	public boolean isDoOutput() {

		return doOutput;
	}

	public void setDoOutput(boolean doOutput) {

		this.doOutput = doOutput;
	}

	public boolean isDoInput() {

		return doInput;
	}

	public void setDoInput(boolean doInput) {

		this.doInput = doInput;
	}

	public boolean isPost() {

		return isPost;
	}

	public void setPost(boolean isPost) {

		this.isPost = isPost;
	}

	public boolean isUseCaches() {

		return useCaches;
	}

	public void setUseCaches(boolean useCaches) {

		this.useCaches = useCaches;
	}

	public boolean isInstanceFollowRedirects() {

		return instanceFollowRedirects;
	}

	public void setInstanceFollowRedirects(boolean instanceFollowRedirects) {

		this.instanceFollowRedirects = instanceFollowRedirects;
	}

	public int getReadTimeout() {

		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {

		this.readTimeout = readTimeout;
	}

	public int getConnectTimeout() {

		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {

		this.connectTimeout = connectTimeout;
	}

	public Map<String, String> getHeadMap() {

		return headMap;
	}

	public void setHeadMap(Map<String, String> headMap) {

		this.headMap = headMap;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
