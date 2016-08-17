package com.work.admin.schema;

import java.io.Serializable;

/**
 * 绑定时间
 * 
 * @author zhangwentao
 * 
 */
public class WechatPublicSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String n_id = ""; // 主键ID

	private String n_shop_id = ""; // 卖家ID:mem_seller表中自己生成的ID

	
	private String t_bind_time = "";// 绑定时间


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


	public String getT_bind_time() {
		return t_bind_time;
	}


	public void setT_bind_time(String t_bind_time) {
		this.t_bind_time = t_bind_time;
	}

	

}
