package com.work.taobao.schema;

import java.io.Serializable;

/**
 * ��׼��Ʒ��Ŀ��tb_taobao_cfg_sys_category
 * 
 * @author tangbiao
 */
public class TaobaoCfgSysCategorySchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_category_id = ""; // ����id
	
	private String n_category_id_sec = ""; // ���ַ���id��ͬ

	private String n_parent_id = ""; // �����ID
	
	private String n_parent_id_sec = "";// ����������ͬ�����ID

	private String c_name = ""; // ����

	private String c_desc = ""; // ˵��

	private String c_icon = ""; // ����ͼ����Ե�ַ

	private String n_order = ""; // ���

	private String n_level = "";// ����

	private String t_create_time = "";// ����ʱ��

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getN_parent_id() {
		return n_parent_id;
	}

	public void setN_parent_id(String n_parent_id) {
		this.n_parent_id = n_parent_id;
	}

	public String getN_parent_id_sec() {
		return n_parent_id_sec;
	}

	public void setN_parent_id_sec(String n_parent_id_sec) {
		this.n_parent_id_sec = n_parent_id_sec;
	}
	
	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_desc() {
		return c_desc;
	}

	public void setC_desc(String c_desc) {
		this.c_desc = c_desc;
	}

	public String getC_icon() {
		return c_icon;
	}

	public void setC_icon(String c_icon) {
		this.c_icon = c_icon;
	}

	public String getN_order() {
		return n_order;
	}

	public void setN_order(String n_order) {
		this.n_order = n_order;
	}

	public String getN_level() {
		return n_level;
	}

	public void setN_level(String n_level) {
		this.n_level = n_level;
	}

	public String getT_create_time() {
		return t_create_time;
	}

	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}

	public String getN_category_id() {
		return n_category_id;
	}

	public void setN_category_id(String n_category_id) {
		this.n_category_id = n_category_id;
	}

	public String getN_category_id_sec() {
		return n_category_id_sec;
	}

	public void setN_category_id_sec(String n_category_id_sec) {
		this.n_category_id_sec = n_category_id_sec;
	}
	
}
