package com.framework.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 启动加载的常量信息
 * 
 * @author tangbiao
 * 
 */
public class StartContent {
	// 测试：www.81851.net;开发域名：www.wegooooo.com;现网域名：www.truedian.com
	private String domainUrl = "www.truedian.com";
	private String domainIp = "10.104.43.189";
	// 开发：http://120.24.55.91测试：http://120.24.230.33:9080正式：http://10.104.43.189
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