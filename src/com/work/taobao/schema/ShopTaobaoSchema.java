package com.work.taobao.schema;

import java.io.Serializable;

/**
 * ���һ�����Ϣ��tb_shop_taobao
 * 
 * @author tangbiao
 * 
 */
public class ShopTaobaoSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_shop_id = ""; // ����ID:mem_seller�����Լ����ɵ�ID

	private String c_company_product = ""; // ��Ӫ��Ʒ

	private String c_company_industry = "";// ��Ӫ��ҵ
	
	private String c_user_num_id = ""; //�û��ɣ�
	
	private String c_company_type = ""; //��������
	
	private String c_user_nick = ""; //�û��ǳ�
	
	private String c_credit_level = ""; //���õȼ�
	
	private String c_good_rate_percentage = ""; //������
	
	private String c_shop_title = "";  //��������
	
	private String c_taobao_shop_id = ""; //���̣ɣ�
	
	private String c_weitao_id = ""; //΢�ԣɣ�
	
	private String c_fans_count = ""; //��˿����
	
	private String c_evaluateInfo_disc = ""; //�������
	
	private String c_evaluateInfo_disc_highgap = ""; //�������������ҵƽ��
	
	private String c_evaluateInfo_taodu = ""; //����̬��
	
	private String c_evaluateInfo_taodu_highgap = ""; //����̬�ȸ�����ҵƽ��
	
	private String c_evaluateInfo_send = ""; //�����ٶ�
	
	private String c_evaluateInfo_send_highgap = ""; //�����ٶȸ�����ҵƽ��
	
	private String c_bail_amount = ""; //��֤��
	
	private String c_pic_url = ""; //����ͷ��
	
	private String c_starts = ""; //���̴���ʱ��

	private String c_shop_index_url = "";// �Ա���ҳ��ַ

	private String c_shop_domain = "";// ������̵�ַ

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
