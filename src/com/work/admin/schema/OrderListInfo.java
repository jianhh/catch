package com.work.admin.schema;

import java.io.Serializable;
import java.util.Date;

import com.framework.util.DateUtil;
import com.framework.util.StringUtils;


/**
 * 供应商查询页
 * 
 * @author tangbiao
 * 
 */
public class OrderListInfo implements Serializable {

	private static final long serialVersionUID = 201033331L;
    private String n_id="";
    private String c_order_id="";
    private String c_parent_order_id="";
    private String n_buyer_id="";
    private String n_account_id="";
    private String n_order_state="";
    private String n_order_refund_state="";
    private String n_shop_id="";
    private String n_shop_name="";
    private String n_parent_shop_id="";
    private String n_parent_shop_name="";
    private String n_goods_num="";
    private String n_express_pay_type="";
    private String n_express_fee="";
    private String n_discount_fee="";
    private String n_goods_fee="";
    private String pay_fee="";
    private String c_pay_plat="";
    private String c_buyer_remark="";
    private String c_seller_remark="";
    private String n_is_notify="";
    private String t_create_time;
	private String t_update_time ;// 店铺id
	private String t_pay_time ; // 店铺名称
	public String getN_id() {
		return n_id;
	}
	public void setN_id(String n_id) {
		this.n_id = n_id;
	}
	public String getC_order_id() {
		return c_order_id;
	}
	public void setC_order_id(String c_order_id) {
		this.c_order_id = c_order_id;
	}
	public String getC_parent_order_id() {
		return c_parent_order_id;
	}
	public void setC_parent_order_id(String c_parent_order_id) {
		this.c_parent_order_id = c_parent_order_id;
	}
	public String getN_buyer_id() {
		return n_buyer_id;
	}
	public void setN_buyer_id(String n_buyer_id) {
		this.n_buyer_id = n_buyer_id;
	}
	public String getN_account_id() {
		return n_account_id;
	}
	public void setN_account_id(String n_account_id) {
		this.n_account_id = n_account_id;
	}
	public String getN_order_state() {
		return n_order_state;
	}
	public void setN_order_state(String n_order_state) {
		this.n_order_state = n_order_state;
	}
	public String getN_order_refund_state() {
		return n_order_refund_state;
	}
	public void setN_order_refund_state(String n_order_refund_state) {
		this.n_order_refund_state = n_order_refund_state;
	}
	public String getN_shop_id() {
		return n_shop_id;
	}
	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}
	public String getN_shop_name() {
		return n_shop_name;
	}
	public void setN_shop_name(String n_shop_name) {
		this.n_shop_name = n_shop_name;
	}
	public String getN_parent_shop_id() {
		return n_parent_shop_id;
	}
	public void setN_parent_shop_id(String n_parent_shop_id) {
		this.n_parent_shop_id = n_parent_shop_id;
	}
	public String getN_parent_shop_name() {
		return n_parent_shop_name;
	}
	public void setN_parent_shop_name(String n_parent_shop_name) {
		this.n_parent_shop_name = n_parent_shop_name;
	}
	public String getN_goods_num() {
		return n_goods_num;
	}
	public void setN_goods_num(String n_goods_num) {
		this.n_goods_num = n_goods_num;
	}
	public String getN_express_pay_type() {
		return n_express_pay_type;
	}
	public void setN_express_pay_type(String n_express_pay_type) {
		this.n_express_pay_type = n_express_pay_type;
	}
	public String getN_express_fee() {
		return n_express_fee;
	}
	public void setN_express_fee(String n_express_fee) {
		this.n_express_fee = n_express_fee;
	}
	public String getN_discount_fee() {
		return n_discount_fee;
	}
	public void setN_discount_fee(String n_discount_fee) {
		this.n_discount_fee = n_discount_fee;
	}
	public String getN_goods_fee() {
		return n_goods_fee;
	}
	public void setN_goods_fee(String n_goods_fee) {
		this.n_goods_fee = n_goods_fee;
	}
	
	public String getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(String pay_fee) {
		this.pay_fee = pay_fee;
	}
	public String getC_pay_plat() {
		return c_pay_plat;
	}
	public void setC_pay_plat(String c_pay_plat) {
		this.c_pay_plat = c_pay_plat;
	}
	public String getC_buyer_remark() {
		return c_buyer_remark;
	}
	public void setC_buyer_remark(String c_buyer_remark) {
		this.c_buyer_remark = c_buyer_remark;
	}
	public String getC_seller_remark() {
		return c_seller_remark;
	}
	public void setC_seller_remark(String c_seller_remark) {
		this.c_seller_remark = c_seller_remark;
	}
	public String getN_is_notify() {
		return n_is_notify;
	}
	public void setN_is_notify(String n_is_notify) {
		this.n_is_notify = n_is_notify;
	}
	public String getT_create_time() {
		Date join=DateUtil.getStringToDate(t_create_time,"yyyy-MM-dd HH:mm:ss");
		return DateUtil.changeDateToString(join, "yyyy-MM-dd HH:mm:ss");
	}
	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}
	public String getT_update_time() {
		Date join=DateUtil.getStringToDate(t_update_time,"yyyy-MM-dd HH:mm:ss");
		return DateUtil.changeDateToString(join, "yyyy-MM-dd HH:mm:ss");
	}
	public void setT_update_time(String t_update_time) {
		
		this.t_update_time = t_update_time;
	}
	public String getT_pay_time() {
		if(StringUtils.isNotEmpty(t_pay_time)){
			Date join = DateUtil.getStringToDate(t_pay_time, "yyyy-MM-dd HH:mm:ss");
			return DateUtil.changeDateToString(join, "yyyy-MM-dd HH:mm:ss");
		}
		return "";
	}
	public void setT_pay_time(String t_pay_time) {
		this.t_pay_time = t_pay_time;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
