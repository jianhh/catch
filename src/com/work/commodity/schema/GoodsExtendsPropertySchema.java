package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 商品扩展属性表tb_goods_extends_property
 * 
 * @author tangbiao
 * 
 */
public class GoodsExtendsPropertySchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_goods_id = ""; // 商品ID

	private String n_shop_id = ""; // 卖家ID

	private String c_prop_key_label = ""; // 扩展属性键label

	private String c_prop_value = ""; // 扩展属性值

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getN_goods_id() {
		return n_goods_id;
	}

	public void setN_goods_id(String n_goods_id) {
		this.n_goods_id = n_goods_id;
	}

	public String getC_prop_key_label() {
		return c_prop_key_label;
	}

	public void setC_prop_key_label(String c_prop_key_label) {
		this.c_prop_key_label = c_prop_key_label;
	}

	public String getC_prop_value() {
		return c_prop_value;
	}

	public void setC_prop_value(String c_prop_value) {
		this.c_prop_value = c_prop_value;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

}
