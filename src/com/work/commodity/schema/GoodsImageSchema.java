package com.work.commodity.schema;

import java.io.Serializable;

import com.framework.util.StringUtils;

/**
 * 商品图片表tb_goods_image
 * 
 * @author tangbiao
 */
public class GoodsImageSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_goods_id = ""; // 商品ID，对应tb_goods表中的商品ID（主键）

	private String n_shop_id = ""; // 卖家ID

	private String n_img_category = ""; // 商品图片分类：1列表页，2详情页

	private String c_img_url = ""; // 图片地址(访问时需要带上样式，例如：../jmg.jpg-class)

	private String n_width = "";// 图片宽度

	private String n_height = "";// 图片高度

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

	public String getN_img_category() {
		return n_img_category;
	}

	public void setN_img_category(String n_img_category) {
		this.n_img_category = n_img_category;
	}

	public String getN_width() {
		if(StringUtils.isEmpty(n_width)){
			n_width = "0";
		}
		return n_width;
	}

	public void setN_width(String n_width) {
		this.n_width = n_width;
	}

	public String getN_height() {
		if(StringUtils.isEmpty(n_height)){
			n_height = "0";
		}
		return n_height;
	}

	public void setN_height(String n_height) {
		this.n_height = n_height;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getC_img_url() {
		return c_img_url;
	}

	public void setC_img_url(String c_img_url) {
		this.c_img_url = c_img_url;
	}

}
