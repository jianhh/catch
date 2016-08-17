package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 商品SKU表tb_goods_sku
 * 
 * @author tangbiao
 * 
 */
public class GoodsSkuSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String c_sku_id = ""; // SKU节点ID

	private String n_goods_id = ""; // 商品ID

	private String n_parent_id = ""; // 父节点ID

	private String n_shop_id = ""; // 店铺ID

	private String n_stock_num = ""; // 库存

	private String c_sku_name = ""; // sku名称，该节点值的名称，如红色、XL

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

	public String getN_parent_id() {
		return n_parent_id;
	}

	public void setN_parent_id(String n_parent_id) {
		this.n_parent_id = n_parent_id;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getN_stock_num() {
		return n_stock_num;
	}

	public void setN_stock_num(String n_stock_num) {
		this.n_stock_num = n_stock_num;
	}

	public String getC_sku_name() {
		return c_sku_name;
	}

	public void setC_sku_name(String c_sku_name) {
		this.c_sku_name = c_sku_name;
	}

	public String getC_sku_desc() {
		return c_sku_desc;
	}

	public void setC_sku_desc(String c_sku_desc) {
		this.c_sku_desc = c_sku_desc;
	}

	public String getC_sku_id() {
		return c_sku_id;
	}

	public void setC_sku_id(String c_sku_id) {
		this.c_sku_id = c_sku_id;
	}

}
