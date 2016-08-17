package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��ƷSKU�б�tb_goods_sku_list
 * 
 * @author tangbiao
 * 
 */
public class GoodsSkuListSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_goods_id = ""; // ��ƷID

	private String n_shop_id = ""; // ����ID

	private String c_sku_name = ""; // sku���ƣ��ýڵ�ֵ�����ƣ����ɫ��XL

	private String c_sku_prop = ""; // sku������������ɫ

	private String n_sku_level = ""; // �㼶����0��1��2

	private String c_sku_desc = ""; // sku���������ڲ鿴�京�壬���������ָ

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

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getC_sku_name() {
		return c_sku_name;
	}

	public void setC_sku_name(String c_sku_name) {
		this.c_sku_name = c_sku_name;
	}

	public String getN_sku_level() {
		return n_sku_level;
	}

	public void setN_sku_level(String n_sku_level) {
		this.n_sku_level = n_sku_level;
	}

	public String getC_sku_desc() {
		return c_sku_desc;
	}

	public void setC_sku_desc(String c_sku_desc) {
		this.c_sku_desc = c_sku_desc;
	}

	public String getC_sku_prop() {
		return c_sku_prop;
	}

	public void setC_sku_prop(String c_sku_prop) {
		this.c_sku_prop = c_sku_prop;
	}

}
