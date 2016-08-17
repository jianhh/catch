package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 商品信息表tb_goods
 * 
 * @author tangbiao
 * 
 */
public class GoodsSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 商品ID

	private String n_goods_id = ""; // 商品编号,自己生成的ID

	private String c_third_platform_id = ""; // 第三方平台商品ID

	private String n_third_platform_type = ""; // 商品来源平台类型 0 自己平台 1 阿里 2 淘宝

	private String n_shop_id = ""; // 店铺ID

	private String n_parent_shop_id = "0"; // 上家店铺ID

	private String n_sys_cid = ""; // 商品标准类目ID

	private String n_sys_parent_cid = ""; // 商品标准类目父ID

	private String c_goods_name = ""; // 商品名称

	private String c_goods_state = ""; // 商品状态：1 上架，2 下架， 3 删除 4 缺货

	private String c_goods_detail_desc = ""; // 商品信息描述:静态页面链接，爬取widget-custom-container部分

	private String n_express_pay_type = "";// 运费承担方式：1卖家，2买家， 3自取，4运费到付

	private String n_buy_price = "0"; // 买入价,单位分

	private String n_sell_price = "0"; // 卖货价,单位分

	private String c_price = "0"; // 详情页展现的价格

	private String c_goods_unit = ""; // 商品单位

	private String n_total_stock = "0"; // 总库存量

	private String n_ownner_sell = "0"; // 自己的销量（爬取商品时用自己的销量存一下起批量，只是在业务中临时用一下）

	private String n_total_sell = "0"; // 总销量

	private String n_third_total_sell = "0"; // 第三方平台总销量

	private String c_tp_order_url = ""; // 第三方平台选购URL地址(阿里手机的详情页地址)

	private String t_create_time = "";// 添加时间

	private String t_last_update_time = ""; // 最后修改时间

	private String n_weight = ""; // 单位重量(克)

	private String c_tp_goods_url = ""; // 第三方商品地址

	private String c_art_no = ""; // 商品货号

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

	public String getC_third_platform_id() {
		return c_third_platform_id;
	}

	public void setC_third_platform_id(String c_third_platform_id) {
		this.c_third_platform_id = c_third_platform_id;
	}

	public String getN_third_platform_type() {
		return n_third_platform_type;
	}

	public void setN_third_platform_type(String n_third_platform_type) {
		this.n_third_platform_type = n_third_platform_type;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getN_parent_shop_id() {
		return n_parent_shop_id;
	}

	public void setN_parent_shop_id(String n_parent_shop_id) {
		this.n_parent_shop_id = n_parent_shop_id;
	}

	public String getN_sys_cid() {
		return n_sys_cid;
	}

	public void setN_sys_cid(String n_sys_cid) {
		this.n_sys_cid = n_sys_cid;
	}

	public String getN_sys_parent_cid() {
		return n_sys_parent_cid;
	}

	public void setN_sys_parent_cid(String n_sys_parent_cid) {
		this.n_sys_parent_cid = n_sys_parent_cid;
	}

	public String getC_goods_name() {
		return c_goods_name;
	}

	public void setC_goods_name(String c_goods_name) {
		this.c_goods_name = c_goods_name;
	}

	public String getC_goods_state() {
		return c_goods_state;
	}

	public void setC_goods_state(String c_goods_state) {
		this.c_goods_state = c_goods_state;
	}

	public String getC_goods_detail_desc() {
		return c_goods_detail_desc;
	}

	public void setC_goods_detail_desc(String c_goods_detail_desc) {
		this.c_goods_detail_desc = c_goods_detail_desc;
	}

	public String getN_express_pay_type() {
		return n_express_pay_type;
	}

	public void setN_express_pay_type(String n_express_pay_type) {
		this.n_express_pay_type = n_express_pay_type;
	}

	public String getN_buy_price() {
		return n_buy_price;
	}

	public void setN_buy_price(String n_buy_price) {
		this.n_buy_price = n_buy_price;
	}

	public String getN_sell_price() {
		return n_sell_price;
	}

	public void setN_sell_price(String n_sell_price) {
		this.n_sell_price = n_sell_price;
	}

	public String getC_goods_unit() {
		return c_goods_unit;
	}

	public void setC_goods_unit(String c_goods_unit) {
		this.c_goods_unit = c_goods_unit;
	}

	public String getN_total_stock() {
		return n_total_stock;
	}

	public void setN_total_stock(String n_total_stock) {
		this.n_total_stock = n_total_stock;
	}

	public String getN_ownner_sell() {
		return n_ownner_sell;
	}

	public void setN_ownner_sell(String n_ownner_sell) {
		this.n_ownner_sell = n_ownner_sell;
	}

	public String getN_total_sell() {
		return n_total_sell;
	}

	public void setN_total_sell(String n_total_sell) {
		this.n_total_sell = n_total_sell;
	}

	public String getN_third_total_sell() {
		return n_third_total_sell;
	}

	public void setN_third_total_sell(String n_third_total_sell) {
		this.n_third_total_sell = n_third_total_sell;
	}

	public String getC_tp_order_url() {
		return c_tp_order_url;
	}

	public void setC_tp_order_url(String c_tp_order_url) {
		this.c_tp_order_url = c_tp_order_url;
	}

	public String getT_create_time() {
		return t_create_time;
	}

	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}

	public String getT_last_update_time() {
		return t_last_update_time;
	}

	public void setT_last_update_time(String t_last_update_time) {
		this.t_last_update_time = t_last_update_time;
	}

	public String getN_weight() {
		return n_weight;
	}

	public void setN_weight(String n_weight) {
		this.n_weight = n_weight;
	}

	public String getC_price() {
		return c_price;
	}

	public void setC_price(String c_price) {
		this.c_price = c_price;
	}

	public String getC_tp_goods_url() {
		return c_tp_goods_url;
	}

	public void setC_tp_goods_url(String c_tp_goods_url) {
		this.c_tp_goods_url = c_tp_goods_url;
	}

	public String getC_art_no() {
		return c_art_no;
	}

	public void setC_art_no(String c_art_no) {
		this.c_art_no = c_art_no;
	}

}
