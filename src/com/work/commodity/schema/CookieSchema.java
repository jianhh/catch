package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��t_cookie���ж�ȡ����
 * 
 * @author tangbiao
 * 
 */
public class CookieSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String id = ""; // ID

	private String cookie = ""; // cookieֵ

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
