package com.work.commodity.schema;

import java.io.Serializable;

/**
 * Ĭ��������tb_goods_default_express
 * 
 * @author tangbiao
 * 
 */
public class GoodsDefaultExpressSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_goods_id = ""; // ��ƷID

	private String n_shop_id = ""; // ����ID

	private String c_delivery_location = ""; // ������

	private String c_receive_location = ""; // �ջ���

	private String n_price = ""; // �۸�

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

	public String getC_delivery_location() {
		return c_delivery_location;
	}

	public void setC_delivery_location(String c_delivery_location) {
		this.c_delivery_location = c_delivery_location;
	}

	public String getC_receive_location() {
		return c_receive_location;
	}

	public void setC_receive_location(String c_receive_location) {
		this.c_receive_location = c_receive_location;
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

}
