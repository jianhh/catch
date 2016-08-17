package com.work.admin.schema;

import java.io.Serializable;
import java.util.Date;

import com.framework.util.DateUtil;

/**
 * 分销商页
 * 
 * @author tangbiao
 * 
 */
public class TraderListInfo implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String shop_name = ""; // 店铺名称
	private String shop_id = "";// 店铺id
	private String apply_time = "";// 创建时间
	private String arrearage_num="";//未付款订单数
	private String pay_num = "";// 已付款订单数
	private String shipping_num = "";// 已发货订单数
	private String end_num = "";// 已完成订单数
	private String close_num = "";// 已关闭订单数
	private String total_num = "";// 订单总数
	private String parent_shop_id="";
	private String supplier_shop_name="";// 供销商名称
	private String n_mobile="";
	private String n_phone="";//电话
    private String wechat_name="";//微信号
	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getApply_time() {
		Date join=DateUtil.getStringToDate(apply_time,"yyyy-MM-dd HH:mm:ss");
		return DateUtil.changeDateToString(join, "yyyy-MM-dd HH:mm:ss");
	}

	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}

	public String getPay_num() {
		return pay_num;
	}

	public void setPay_num(String pay_num) {
		this.pay_num = pay_num;
	}

	public String getShipping_num() {
		return shipping_num;
	}

	public void setShipping_num(String shipping_num) {
		this.shipping_num = shipping_num;
	}

	public String getEnd_num() {
		return end_num;
	}

	public void setEnd_num(String end_num) {
		this.end_num = end_num;
	}

	public String getClose_num() {
		return close_num;
	}

	public void setClose_num(String close_num) {
		this.close_num = close_num;
	}

	public String getTotal_num() {
		return total_num;
	}

	public void setTotal_num(String total_num) {
		this.total_num = total_num;
	}

	public String getParent_shop_id() {
		return parent_shop_id;
	}

	public void setParent_shop_id(String parent_shop_id) {
		this.parent_shop_id = parent_shop_id;
	}

	public String getArrearage_num() {
		return arrearage_num;
	}

	public void setArrearage_num(String arrearage_num) {
		this.arrearage_num = arrearage_num;
	}

	public String getSupplier_shop_name() {
		return supplier_shop_name;
	}

	public void setSupplier_shop_name(String supplier_shop_name) {
		this.supplier_shop_name = supplier_shop_name;
	}

	public String getN_mobile() {
		return n_mobile;
	}

	public void setN_mobile(String n_mobile) {
		this.n_mobile = n_mobile;
	}

	public String getN_phone() {
		return n_phone;
	}

	public void setN_phone(String n_phone) {
		this.n_phone = n_phone;
	}

	public String getWechat_name() {
		return wechat_name;
	}

	public void setWechat_name(String wechat_name) {
		this.wechat_name = wechat_name;
	}
	
}
