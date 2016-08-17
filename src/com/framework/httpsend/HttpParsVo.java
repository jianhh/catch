package com.framework.httpsend;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpURLConnection�������Vo
 * 
 * @author tangbiao
 * 
 */
public class HttpParsVo {

	private String proxyHost = "";// �������ip

	private int proxyPort = 0;// �������˿�

	private String charset = "GBK";// ��Ӧ���ݵı����ʽ

	private boolean doOutput = true; // DoOutput��־����Ϊtrue����ʾ��Ӧ�ó��򣬴���д���ݵ�URL����

	private boolean doInput = true; // ��DoInput��־Ϊtrue������Ӧ�ó�������URL���Ӷ�ȡ���ݡ�

	private boolean isPost = true; // URL����ʽ��Ĭ��Ϊpost��ʽ

	private boolean useCaches = false; // UseCachesΪtrue������������ʹ���κο��õĻ��档���Ϊfalse�����涼�����ԡ�

	private boolean instanceFollowRedirects = true; // �����Ƿ�Ӧ�Զ�����HTTP�ض���

	private int readTimeout = 8000; // ���ö�ȡ��ʱָ���ĳ�ʱʱ�䣬��λΪ����

	private int connectTimeout = 8000; // ����ָ����ʱֵ���Ժ���Ϊ��λ

	private Map<String, String> headMap = new HashMap<String, String>(); // ����һ���������ԡ�����ؼ��������Ѿ����ڣ��������ļ�ֵ���µļ�ֵ��ע��HTTPҪ�������������Կ��ԺϷ���ӵ�ж��ʵ��ʹ����ͬ����Կ��ʹ���Զ��ŷָ����б��﷨��ʹ�������׷�ӵ�һ����һ�ĲƲ���

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
