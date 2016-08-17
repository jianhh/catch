package com.work.commodity.schema;

import java.io.Serializable;

/**
 * ��Ʒ���±�tb_goods_sync_record
 * 
 * @author tangbiao
 * 
 */
public class GoodsSyncRecordSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // ����ID

	private String n_shop_id = ""; // ����ID

	private String n_task_id = ""; // ����ID

	private String n_goods_id = ""; // ��ƷID

	private String n_num = "";// ��Ʒ��ȡ����

	private String t_create_time = ""; // ����ʱ��

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

	public String getN_task_id() {
		return n_task_id;
	}

	public void setN_task_id(String n_task_id) {
		this.n_task_id = n_task_id;
	}

	public String getN_goods_id() {
		return n_goods_id;
	}

	public void setN_goods_id(String n_goods_id) {
		this.n_goods_id = n_goods_id;
	}

	public String getT_create_time() {
		return t_create_time;
	}

	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}

	public String getN_num() {
		return n_num;
	}

	public void setN_num(String n_num) {
		this.n_num = n_num;
	}

}
