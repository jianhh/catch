package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 卖家基本信息表tb_shop_ali
 * 
 * @author tangbiao
 * 
 */
public class ShopAliSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_shop_id = ""; // 卖家ID:mem_seller表中自己生成的ID

	private String c_company_product = ""; // 主营产品

	private String c_company_industry = "";// 主营行业

	private String c_company_model = "";// 经营模式

	private String c_company_work = "";// 是否提供加工定制

	private String c_company_capital = "";// 注册资本

	private String c_company_founding_time = "";// 公司成立时间

	private String c_company_address = "";// 公司注册地

	private String c_company_type = "";// 企业类型

	private String c_company_person = "";// 法定代表人

	private String c_company_no = "";// 工商注册号

	private String c_company_way = "";// 加工方式

	private String c_company_technology = "";// 工艺

	private String c_company_number = "";// 员工人数

	private String c_company_area = "";// 厂房面积

	private String c_company_region = "";// 主要销售区域

	private String c_company_custom = "";// 主要客户群体

	private String c_company_output = "";// 月产量

	private String c_company_turnover = "";// 年营业额

	private String c_company_exports = "";// 年出口额

	private String c_company_brand = "";// 品牌名称

	private String c_company_url = "";// 公司主页

	private String c_qualifications1 = "";// 公司资质1

	private String c_qualifications2 = "";// 公司资质2

	private String c_qualifications3 = "";// 公司资质3

	private String c_company_desc = "";// 公司介绍

	private String c_platform_id = "";// 阿里店铺ID

	private String c_ww_number = "";// 阿里旺旺号

	private String c_shop_index_url = "";// 阿里首页地址

	private String c_shop_domain = "";// 阿里店铺域名
	private String c_trade_number="";//交易数量

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

	public String getC_company_model() {
		return c_company_model;
	}

	public void setC_company_model(String c_company_model) {
		this.c_company_model = c_company_model;
	}

	public String getC_company_work() {
		return c_company_work;
	}

	public void setC_company_work(String c_company_work) {
		this.c_company_work = c_company_work;
	}

	public String getC_company_capital() {
		return c_company_capital;
	}

	public void setC_company_capital(String c_company_capital) {
		this.c_company_capital = c_company_capital;
	}

	public String getC_company_founding_time() {
		return c_company_founding_time;
	}

	public void setC_company_founding_time(String c_company_founding_time) {
		this.c_company_founding_time = c_company_founding_time;
	}

	public String getC_company_type() {
		return c_company_type;
	}

	public void setC_company_type(String c_company_type) {
		this.c_company_type = c_company_type;
	}

	public String getC_company_address() {
		return c_company_address;
	}

	public void setC_company_address(String c_company_address) {
		this.c_company_address = c_company_address;
	}

	public String getC_company_person() {
		return c_company_person;
	}

	public void setC_company_person(String c_company_person) {
		this.c_company_person = c_company_person;
	}

	public String getC_company_no() {
		return c_company_no;
	}

	public void setC_company_no(String c_company_no) {
		this.c_company_no = c_company_no;
	}

	public String getC_company_way() {
		return c_company_way;
	}

	public void setC_company_way(String c_company_way) {
		this.c_company_way = c_company_way;
	}

	public String getC_company_technology() {
		return c_company_technology;
	}

	public void setC_company_technology(String c_company_technology) {
		this.c_company_technology = c_company_technology;
	}

	public String getC_company_number() {
		return c_company_number;
	}

	public void setC_company_number(String c_company_number) {
		this.c_company_number = c_company_number;
	}

	public String getC_company_area() {
		return c_company_area;
	}

	public void setC_company_area(String c_company_area) {
		this.c_company_area = c_company_area;
	}

	public String getC_company_region() {
		return c_company_region;
	}

	public void setC_company_region(String c_company_region) {
		this.c_company_region = c_company_region;
	}

	public String getC_company_custom() {
		return c_company_custom;
	}

	public void setC_company_custom(String c_company_custom) {
		this.c_company_custom = c_company_custom;
	}

	public String getC_company_turnover() {
		return c_company_turnover;
	}

	public void setC_company_turnover(String c_company_turnover) {
		this.c_company_turnover = c_company_turnover;
	}

	public String getC_company_exports() {
		return c_company_exports;
	}

	public void setC_company_exports(String c_company_exports) {
		this.c_company_exports = c_company_exports;
	}

	public String getC_company_brand() {
		return c_company_brand;
	}

	public void setC_company_brand(String c_company_brand) {
		this.c_company_brand = c_company_brand;
	}

	public String getC_company_url() {
		return c_company_url;
	}

	public void setC_company_url(String c_company_url) {
		this.c_company_url = c_company_url;
	}

	public String getC_company_output() {
		return c_company_output;
	}

	public void setC_company_output(String c_company_output) {
		this.c_company_output = c_company_output;
	}

	public String getC_qualifications1() {
		return c_qualifications1;
	}

	public void setC_qualifications1(String c_qualifications1) {
		this.c_qualifications1 = c_qualifications1;
	}

	public String getC_qualifications2() {
		return c_qualifications2;
	}

	public void setC_qualifications2(String c_qualifications2) {
		this.c_qualifications2 = c_qualifications2;
	}

	public String getC_qualifications3() {
		return c_qualifications3;
	}

	public void setC_qualifications3(String c_qualifications3) {
		this.c_qualifications3 = c_qualifications3;
	}

	public String getC_company_desc() {
		return c_company_desc;
	}

	public void setC_company_desc(String c_company_desc) {
		this.c_company_desc = c_company_desc;
	}

	public String getC_platform_id() {
		return c_platform_id;
	}

	public void setC_platform_id(String c_platform_id) {
		this.c_platform_id = c_platform_id;
	}

	public String getC_ww_number() {
		return c_ww_number;
	}

	public void setC_ww_number(String c_ww_number) {
		this.c_ww_number = c_ww_number;
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

	public String getC_trade_number() {
		return c_trade_number;
	}

	public void setC_trade_number(String c_trade_number) {
		this.c_trade_number = c_trade_number;
	}
	

}
