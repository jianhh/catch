package com.framework.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * �������صĳ�����Ϣ
 * 
 * @author tangbiao
 * 
 */
public class StartContent {
	// ���ԣ�www.81851.net;����������www.wegooooo.com;����������www.truedian.com
	private String domainUrl = "www.truedian.com";
	private String domainIp = "10.104.43.189";
	// ������http://120.24.55.91���ԣ�http://120.24.230.33:9080��ʽ��http://10.104.43.189
	private String weigouIp = "http://10.104.141.175:9080";

	private static StartContent instance = new StartContent();

	public void init(String startFileName) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(startFileName));

		this.domainUrl = properties.getProperty("startcontent.domain_url");
		this.domainIp = properties.getProperty("startcontent.domain_ip");
		this.weigouIp = properties.getProperty("startcontent.weigou_ip");
	}

	public static StartContent getInstance() {
		return instance;
	}

	public String getDomainUrl() {
		return this.domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public String getWeigouIp() {
		return this.weigouIp;
	}

	public void setWeigouIp(String weigouIp) {
		this.weigouIp = weigouIp;
	}

	public String getDomainIp() {
		return domainIp;
	}

	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
	}
}