package com.work.commodity.schema;

import java.io.Serializable;

public class ShopCfgSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;
	
	private String n_id = "";//主键
	private String n_shop_id = "";//店铺ID
	private String c_key = "";//配置的key
	private String c_value = "";//配置的value
	private String t_create_tiem = "";//创建时间
	private String t_update_time = "";//修改时间
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
	public String getC_key() {
		return c_key;
	}
	public void setC_key(String c_key) {
		this.c_key = c_key;
	}
	public String getC_value() {
		return c_value;
	}
	public void setC_value(String c_value) {
		this.c_value = c_value;
	}
	public String getT_create_tiem() {
		return t_create_tiem;
	}
	public void setT_create_tiem(String t_create_tiem) {
		this.t_create_tiem = t_create_tiem;
	}
	public String getT_update_time() {
		return t_update_time;
	}
	public void setT_update_time(String t_update_time) {
		this.t_update_time = t_update_time;
	}

}
