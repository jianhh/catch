package com.work.admin.schema;

import java.io.Serializable;

/**
 * ��������ƽ̨��ע����Ϣ��tb_shop
 * 
 * @author tangbiao
 * 
 */
public class AdminSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String c_name = ""; // ������Ա����ҳ

	private String t_create_time = "";// ��פʱ��

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getT_create_time() {
		return t_create_time;
	}

	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}



}
