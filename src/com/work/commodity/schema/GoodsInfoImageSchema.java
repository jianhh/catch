package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��Ʒ����ͼƬ��tb_goods_info_image
 * 
 * @author tangbiao
 */
public class GoodsInfoImageSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_goods_id = ""; // ��ƷID����Ӧtb_goods���е���ƷID��������

	private String n_shop_id = ""; // ����ID

	private String c_img_url = ""; // ����ľ��Ե�ַ

	private String n_img_type = ""; // ͼƬ���ͣ�1,��ͨͼƬ;2,��ά��ͼƬ

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

	public String getC_img_url() {
		return c_img_url;
	}

	public void setC_img_url(String c_img_url) {
		this.c_img_url = c_img_url;
	}

	public String getN_img_type() {
		return n_img_type;
	}

	public void setN_img_type(String n_img_type) {
		this.n_img_type = n_img_type;
	}

}
