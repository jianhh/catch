package com.work.admin.schema;

import java.io.Serializable;

/**
 * 批发商在平台的注册信息表tb_shop
 * 
 * @author tangbiao
 * 
 */
public class AdminSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String c_name = ""; // 商务人员新增页

	private String t_create_time = "";// 入驻时间

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
