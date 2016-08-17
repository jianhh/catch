package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��Ʒ��Ϣ��tb_goods
 * 
 * @author tangbiao
 * 
 */
public class GoodsSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ��ƷID

	private String n_goods_id = ""; // ��Ʒ���,�Լ����ɵ�ID

	private String c_third_platform_id = ""; // ������ƽ̨��ƷID

	private String n_third_platform_type = ""; // ��Ʒ��Դƽ̨���� 0 �Լ�ƽ̨ 1 ���� 2 �Ա�

	private String n_shop_id = ""; // ����ID

	private String n_parent_shop_id = "0"; // �ϼҵ���ID

	private String n_sys_cid = ""; // ��Ʒ��׼��ĿID

	private String n_sys_parent_cid = ""; // ��Ʒ��׼��Ŀ��ID

	private String c_goods_name = ""; // ��Ʒ����

	private String c_goods_state = ""; // ��Ʒ״̬��1 �ϼܣ�2 �¼ܣ� 3 ɾ�� 4 ȱ��

	private String c_goods_detail_desc = ""; // ��Ʒ��Ϣ����:��̬ҳ�����ӣ���ȡwidget-custom-container����

	private String n_express_pay_type = "";// �˷ѳе���ʽ��1���ң�2��ң� 3��ȡ��4�˷ѵ���

	private String n_buy_price = "0"; // �����,��λ��

	private String n_sell_price = "0"; // ������,��λ��

	private String c_price = "0"; // ����ҳչ�ֵļ۸�

	private String c_goods_unit = ""; // ��Ʒ��λ

	private String n_total_stock = "0"; // �ܿ����

	private String n_ownner_sell = "0"; // �Լ�����������ȡ��Ʒʱ���Լ���������һ����������ֻ����ҵ������ʱ��һ�£�

	private String n_total_sell = "0"; // ������

	private String n_third_total_sell = "0"; // ������ƽ̨������

	private String c_tp_order_url = ""; // ������ƽ̨ѡ��URL��ַ(�����ֻ�������ҳ��ַ)

	private String t_create_time = "";// ���ʱ��

	private String t_last_update_time = ""; // ����޸�ʱ��

	private String n_weight = ""; // ��λ����(��)

	private String c_tp_goods_url = ""; // ��������Ʒ��ַ

	private String c_art_no = ""; // ��Ʒ����

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
