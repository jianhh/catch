package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��������ƽ̨��ע����Ϣ��tb_shop
 * 
 * @author tangbiao
 * 
 */
public class ShopSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_shop_id = ""; // ����ID:mem_seller�����Լ����ɵ�ID

	private String c_shop_name = ""; // ��������

	private String c_company_name = "";// ��˾��

	private String c_company_desc = "";// ��˾����

	private String t_join_time = "";// ��פʱ��

	private String n_shop_type = "";// �������ͣ��������ͣ�1���3��������

	private String n_shop_type_sublevel = "";// ����������:1���2�Ա���3���¹�Ӧ�̣�4�������ۣ�5ȫԱ���꣬6�̳ǣ�7΢���̳�

	private String c_location = "";// ���̵�ַ

	private String c_contact_name = "";// ��ϵ��

	private String c_contact_mobile = "";// ��ϵ�绰

	private String c_contact_phone = "";// �̶��绰

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getC_shop_name() {
		return c_shop_name;
	}

	public void setC_shop_name(String c_shop_name) {
		this.c_shop_name = c_shop_name;
	}

	public String getC_company_name() {
		return c_company_name;
	}

	public void setC_company_name(String c_company_name) {
		this.c_company_name = c_company_name;
	}

	public String getC_company_desc() {
		return c_company_desc;
	}

	public void setC_company_desc(String c_company_desc) {
		this.c_company_desc = c_company_desc;
	}

	public String getT_join_time() {
		return t_join_time;
	}

	public void setT_join_time(String t_join_time) {
		this.t_join_time = t_join_time;
	}

	public String getN_shop_type() {
		return n_shop_type;
	}

	public void setN_shop_type(String n_shop_type) {
		this.n_shop_type = n_shop_type;
	}

	public String getC_location() {
		return c_location;
	}

	public void setC_location(String c_location) {
		this.c_location = c_location;
	}

	public String getC_contact_name() {
		return c_contact_name;
	}

	public void setC_contact_name(String c_contact_name) {
		this.c_contact_name = c_contact_name;
	}

	public String getC_contact_mobile() {
		return c_contact_mobile;
	}

	public void setC_contact_mobile(String c_contact_mobile) {
		this.c_contact_mobile = c_contact_mobile;
	}

	public String getC_contact_phone() {
		return c_contact_phone;
	}

	public void setC_contact_phone(String c_contact_phone) {
		this.c_contact_phone = c_contact_phone;
	}

	public String getN_shop_type_sublevel() {
		return n_shop_type_sublevel;
	}

	public void setN_shop_type_sublevel(String n_shop_type_sublevel) {
		this.n_shop_type_sublevel = n_shop_type_sublevel;
	}

}
