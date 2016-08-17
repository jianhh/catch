package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 商品SKU列表tb_goods_sku_list
 * 
 * @author tangbiao
 * 
 */
public class GoodsSkuListSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_goods_id = ""; // 商品ID

	private String n_shop_id = ""; // 店铺ID

	private String c_sku_name = ""; // sku名称，该节点值的名称，如红色、XL

	private String c_sku_prop = ""; // sku属性名，如颜色

	private String n_sku_level = ""; // 层级，如0，1，2

	private String c_sku_desc = ""; // sku描述：便于查看其含义，程序填充其指

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
