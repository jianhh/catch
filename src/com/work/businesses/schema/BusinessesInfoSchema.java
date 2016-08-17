package com.work.businesses.schema;

import java.io.Serializable;

/**
 * 商家信息表T_BUSINESSES_INFO
 * 
 * @author tangbiao
 * 
 */
public class BusinessesInfoSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String uid = ""; // 商家ID

	private String company_name = ""; // 公司名

	private String name = ""; // 联系人

	private String phone = ""; // 手机号

	private String number = ""; // 电话号码

	private String address = ""; // 公司注册地址

	private String shop_address = ""; // 公司经营地址

	private String business_number = "";// 交易数

	private String zipcode = ""; // 获取邮编

	private String companyUrl = ""; // 获取公司主页URL

	private String keyword = ""; // 搜索关键词

	private String create_time = ""; // 添加时间

	private String getUrl = ""; // 获取地址

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getBusiness_number() {
		return business_number;
	}

	public void setBusiness_number(String business_number) {
		this.business_number = business_number;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getGetUrl() {
		return getUrl;
	}

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}

}
