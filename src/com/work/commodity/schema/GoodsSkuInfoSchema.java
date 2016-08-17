package com.work.commodity.schema;

import java.io.Serializable;

import com.framework.util.StringUtils;

/**
 * 商品SKU详情tb_goods_sku_info
 * 
 * @author tangbiao
 * 
 */
public class GoodsSkuInfoSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String c_sku_id = ""; // SKU节点ID

	private String n_goods_id = ""; // 商品ID

	private String n_shop_id = ""; // 店铺ID

	private String n_stock_num = ""; // 库存

	private String n_discount_price = ""; // 折扣价格

	private String n_price = ""; // 价格

	private String c_sku_list_id = ""; // sku列表的ID集合

	private String c_sku_desc = ""; // sku描述：便于查看其含义，程序填充其指

	private String n_price_set_flag = ""; // 是否被批量改价设置过价格标示，1=是，0=否

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getC_sku_id() {
		return c_sku_id;
	}

	public void setC_sku_id(String c_sku_id) {
		this.c_sku_id = c_sku_id;
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

	public String getN_stock_num() {
		return n_stock_num;
	}

	public void setN_stock_num(String n_stock_num) {
		this.n_stock_num = n_stock_num;
	}

	public String getC_sku_list_id() {
		return c_sku_list_id;
	}

	public void setC_sku_list_id(String c_sku_list_id) {
		this.c_sku_list_id = c_sku_list_id;
	}

	public String getC_sku_desc() {
		return c_sku_desc;
	}

	public void setC_sku_desc(String c_sku_desc) {
		this.c_sku_desc = c_sku_desc;
	}

	public String getN_discount_price() {
		return n_discount_price;
	}

	public void setN_discount_price(String n_discount_price) {
		this.n_discount_price = n_discount_price;
	}

	public String getN_price() {
		return n_price;
	}

	public void setN_price(String n_price) {
		this.n_price = n_price;
	}

	public String getN_price_set_flag() {
		if(StringUtils.isEmpty(n_price_set_flag)){
			n_price_set_flag = "0";
		}
		return n_price_set_flag;
	}

	public void setN_price_set_flag(String n_price_set_flag) {
		this.n_price_set_flag = n_price_set_flag;
	}

}
