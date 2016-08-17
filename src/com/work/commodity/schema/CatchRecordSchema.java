package com.work.commodity.schema;

import java.io.Serializable;

/**
 * 爬取任务表tb_catch_record
 * 
 * @author tangbiao
 * 
 */
public class CatchRecordSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_shop_id = ""; // 店铺ID

	private String n_sync_type = ""; // 爬取类型：1:首次抓取，2:同步上新商品,3:同步所有商品

	private String n_task_id = ""; // 任务编号

	private String c_url = ""; // 店铺地址

	private String n_process_status = ""; // 处理状态：0 爬取开始，1 爬取完成

	private String t_create_time = ""; // 创建时间

	private String t_update_time = ""; // 修改时间

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

	public String getN_sync_type() {
		return n_sync_type;
	}

	public void setN_sync_type(String n_sync_type) {
		this.n_sync_type = n_sync_type;
	}

	public String getN_task_id() {
		return n_task_id;
	}

	public void setN_task_id(String n_task_id) {
		this.n_task_id = n_task_id;
	}

	public String getC_url() {
		return c_url;
	}

	public void setC_url(String c_url) {
		this.c_url = c_url;
	}

	public String getN_process_status() {
		return n_process_status;
	}

	public void setN_process_status(String n_process_status) {
		this.n_process_status = n_process_status;
	}

	public String getT_create_time() {
		return t_create_time;
	}

	public void setT_create_time(String t_create_time) {
		this.t_create_time = t_create_time;
	}

	public String getT_update_time() {
		return t_update_time;
	}

	public void setT_update_time(String t_update_time) {
		this.t_update_time = t_update_time;
	}

}
