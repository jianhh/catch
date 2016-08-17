package com.work.admin.schema;

import java.io.Serializable;
import java.util.Date;

import com.framework.util.DateUtil;


/**
 * ��Ӧ�̲�ѯҳ
 * 
 * @author tangbiao
 * 
 */
public class SupplierListInfo implements Serializable {

	private static final long serialVersionUID = 201033331L;
	private String nick_name = ""; // �ǳ�
	private String account_id="";
	private String shop_id = "";// ����id
	private String shop_name = ""; // ��������
	private String join_time;// ��פʱ��
	private String bind_time = "";// ��ʱ��
	private String trader_num = "";// ����������
	private String order_num = "";// ��������
	private String fans_num = "";// ��˿����
	private String admin_name = "";// ������Ա����
	private String admin_id="";//����id
	private String examine_num="";//�����
	private String order_generation="";//��������
	private String order_pay="";//����֧��
	private String order_delivery="";//��������
	private String order_complete=""; //�������
	private String order_closed="";//�����ر�
	private String conversion_rate="";//����ת����
	private String f_retention="";//������������
    private String active_user="";//��Ծ����������
    private String trader_user="";//�ܷ���������
    private String morder_num="";//�¶�������
    private String morder_pay="";//�¶���֧��
    private String n_phone="";//�绰
    private String n_mobile="";//����
    private String create_time="";//����ʱ��
    private String wechat_name="";//΢�ź�
	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}


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
	public String getJoin_time() {
		Date join=DateUtil.getStringToDate(join_time,"yyyy-MM-dd HH:mm:ss");
		return DateUtil.changeDateToString(join, "yyyy-MM-dd HH:mm:ss");
	}

	public void setJoin_time(String join_time) {
		this.join_time = join_time;
	}

	public String getBind_time() {
		
		return bind_time;
	}

	public void setBind_time(String bind_time) {
		this.bind_time = bind_time;
	}

	public String getTrader_num() {
		return trader_num;
	}

	public void setTrader_num(String trader_num) {
		this.trader_num = trader_num;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getFans_num() {
		return fans_num;
	}

	public void setFans_num(String fans_num) {
		this.fans_num = fans_num;
	}
	
	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getExamine_num() {
		return examine_num;
	}

	public void setExamine_num(String examine_num) {
		this.examine_num = examine_num;
	}

	public String getOrder_generation() {
		return order_generation;
	}

	public void setOrder_generation(String order_generation) {
		this.order_generation = order_generation;
	}

	public String getOrder_pay() {
		return order_pay;
	}

	public void setOrder_pay(String order_pay) {
		this.order_pay = order_pay;
	}

	public String getOrder_delivery() {
		return order_delivery;
	}

	public void setOrder_delivery(String order_delivery) {
		this.order_delivery = order_delivery;
	}

	public String getOrder_complete() {
		return order_complete;
	}

	public void setOrder_complete(String order_complete) {
		this.order_complete = order_complete;
	}

	public String getOrder_closed() {
		return order_closed;
	}

	public void setOrder_closed(String order_closed) {
		this.order_closed = order_closed;
	}

	public String getConversion_rate() {
		return conversion_rate;
	}

	public void setConversion_rate(String conversion_rate) {
		this.conversion_rate = conversion_rate;
	}

	public String getF_retention() {
		return f_retention;
	}

	public void setF_retention(String f_retention) {
		this.f_retention = f_retention;
	}

	public String getActive_user() {
		return active_user;
	}

	public void setActive_user(String active_user) {
		this.active_user = active_user;
	}

	public String getTrader_user() {
		return trader_user;
	}

	public void setTrader_user(String trader_user) {
		this.trader_user = trader_user;
	}

	public String getMorder_num() {
		return morder_num;
	}

	public void setMorder_num(String morder_num) {
		this.morder_num = morder_num;
	}

	public String getMorder_pay() {
		return morder_pay;
	}

	public void setMorder_pay(String morder_pay) {
		this.morder_pay = morder_pay;
	}

	public String getN_phone() {
		return n_phone;
	}

	public void setN_phone(String n_phone) {
		this.n_phone = n_phone;
	}

	public String getN_mobile() {
		return n_mobile;
	}

	public void setN_mobile(String n_mobile) {
		this.n_mobile = n_mobile;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getWechat_name() {
		return wechat_name;
	}

	public void setWechat_name(String wechat_name) {
		this.wechat_name = wechat_name;
	}
	
	
}
