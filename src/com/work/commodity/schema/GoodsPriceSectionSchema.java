package com.work.commodity.schema;

import java.io.Serializable;

import com.framework.util.StringUtils;

/**
 * 商品价格区间配置表tb_goods_price_section
 * 
 * @author tangbiao
 * 
 */
public class GoodsPriceSectionSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_goods_id = ""; // 商品ID

	private String n_shop_id = ""; // 卖家ID

	private String n_num_start = ""; // 开始数量

	private String n_num_end = ""; // 结束数量

	private String n_price = ""; // 价格

	private String n_price_set_flag = ""; // 是否被批量改价设置过价格标示，1=是，0=否

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

	public String getN_num_start() {
		return n_num_start;
	}

	public void setN_num_start(String n_num_start) {
		this.n_num_start = n_num_start;
	}

	public String getN_num_end() {
		return n_num_end;
	}

	public void setN_num_end(String n_num_end) {
		this.n_num_end = n_num_end;
	}

	public String getN_price() {
		return n_price;
	}

	public void setN_price(String n_price) {
		this.n_price = n_price;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
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
