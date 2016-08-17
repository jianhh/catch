package com.work.taobao.schema;

import java.io.Serializable;

/**
 * 卖家基本信息表tb_shop_taobao
 * 
 * @author tangbiao
 * 
 */
public class ShopTaobaoSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_shop_id = ""; // 卖家ID:mem_seller表中自己生成的ID

	private String c_company_product = ""; // 主营产品

	private String c_company_industry = "";// 主营行业
	
	private String c_user_num_id = ""; //用户ＩＤ
	
	private String c_company_type = ""; //店铺类型
	
	private String c_user_nick = ""; //用户昵称
	
	private String c_credit_level = ""; //信用等级
	
	private String c_good_rate_percentage = ""; //好评率
	
	private String c_shop_title = "";  //店铺名称
	
	private String c_taobao_shop_id = ""; //店铺ＩＤ
	
	private String c_weitao_id = ""; //微淘ＩＤ
	
	private String c_fans_count = ""; //粉丝数量
	
	private String c_evaluateInfo_disc = ""; //描述相符
	
	private String c_evaluateInfo_disc_highgap = ""; //描述相符高于行业平均
	
	private String c_evaluateInfo_taodu = ""; //服务态度
	
	private String c_evaluateInfo_taodu_highgap = ""; //服务态度高于行业平均
	
	private String c_evaluateInfo_send = ""; //发货速度
	
	private String c_evaluateInfo_send_highgap = ""; //发货速度高于行业平均
	
	private String c_bail_amount = ""; //保证金
	
	private String c_pic_url = ""; //店铺头像
	
	private String c_starts = ""; //店铺创建时间

	private String c_shop_index_url = "";// 淘宝首页地址

	private String c_shop_domain = "";// 触店店铺地址

	public String getN_id() {
		return n_id;
	}

	public void setN_id(String n_id) {
		this.n_id = n_id;
	}

	public String getN_shop_id() {
		return n_shop_id;
	}

	public void setN_shop_id(String n_shop_id) {
		this.n_shop_id = n_shop_id;
	}

	public String getC_company_product() {
		return c_company_product;
	}

	public void setC_company_product(String c_company_product) {
		this.c_company_product = c_company_product;
	}

	public String getC_company_industry() {
		return c_company_industry;
	}

	public void setC_company_industry(String c_company_industry) {
		this.c_company_industry = c_company_industry;
	}

	public String getC_user_num_id() {
		return c_user_num_id;
	}

	public void setC_user_num_id(String c_user_num_id) {
		this.c_user_num_id = c_user_num_id;
	}

	public String getC_company_type() {
		return c_company_type;
	}

	public void setC_company_type(String c_company_type) {
		this.c_company_type = c_company_type;
	}

	public String getC_user_nick() {
		return c_user_nick;
	}

	public void setC_user_nick(String c_user_nick) {
		this.c_user_nick = c_user_nick;
	}

	public String getC_credit_level() {
		return c_credit_level;
	}

	public void setC_credit_level(String c_credit_level) {
		this.c_credit_level = c_credit_level;
	}

	public String getC_good_rate_percentage() {
		return c_good_rate_percentage;
	}

	public void setC_good_rate_percentage(String c_good_rate_percentage) {
		this.c_good_rate_percentage = c_good_rate_percentage;
	}

	public String getC_shop_title() {
		return c_shop_title;
	}

	public void setC_shop_title(String c_shop_title) {
		this.c_shop_title = c_shop_title;
	}

	public String getC_taobao_shop_id() {
		return c_taobao_shop_id;
	}

	public void setC_taobao_shop_id(String c_taobao_shop_id) {
		this.c_taobao_shop_id = c_taobao_shop_id;
	}

	public String getC_weitao_id() {
		return c_weitao_id;
	}

	public void setC_weitao_id(String c_weitao_id) {
		this.c_weitao_id = c_weitao_id;
	}

	public String getC_fans_count() {
		return c_fans_count;
	}

	public void setC_fans_count(String c_fans_count) {
		this.c_fans_count = c_fans_count;
	}

	public String getC_evaluateInfo_disc() {
		return c_evaluateInfo_disc;
	}

	public void setC_evaluateInfo_disc(String info_disc) {
		c_evaluateInfo_disc = info_disc;
	}

	public String getC_evaluateInfo_disc_highgap() {
		return c_evaluateInfo_disc_highgap;
	}

	public void setC_evaluateInfo_disc_highgap(String info_disc_highgap) {
		c_evaluateInfo_disc_highgap = info_disc_highgap;
	}

	public String getC_evaluateInfo_taodu() {
		return c_evaluateInfo_taodu;
	}

	public void setC_evaluateInfo_taodu(String info_taodu) {
		c_evaluateInfo_taodu = info_taodu;
	}

	public String getC_evaluateInfo_taodu_highgap() {
		return c_evaluateInfo_taodu_highgap;
	}

	public void setC_evaluateInfo_taodu_highgap(String info_taodu_highgap) {
		c_evaluateInfo_taodu_highgap = info_taodu_highgap;
	}

	public String getC_evaluateInfo_send() {
		return c_evaluateInfo_send;
	}

	public void setC_evaluateInfo_send(String info_send) {
		c_evaluateInfo_send = info_send;
	}

	public String getC_evaluateInfo_send_highgap() {
		return c_evaluateInfo_send_highgap;
	}

	public void setC_evaluateInfo_send_highgap(String info_send_highgap) {
		c_evaluateInfo_send_highgap = info_send_highgap;
	}

	public String getC_bail_amount() {
		return c_bail_amount;
	}

	public void setC_bail_amount(String c_bail_amount) {
		this.c_bail_amount = c_bail_amount;
	}

	public String getC_pic_url() {
		return c_pic_url;
	}

	public void setC_pic_url(String c_pic_url) {
		this.c_pic_url = c_pic_url;
	}

	public String getC_starts() {
		return c_starts;
	}

	public void setC_starts(String c_starts) {
		this.c_starts = c_starts;
	}

	public String getC_shop_index_url() {
		return c_shop_index_url;
	}

	public void setC_shop_index_url(String c_shop_index_url) {
		this.c_shop_index_url = c_shop_index_url;
	}

	public String getC_shop_domain() {
		return c_shop_domain;
	}

	public void setC_shop_domain(String c_shop_domain) {
		this.c_shop_domain = c_shop_domain;
	}

	

}
