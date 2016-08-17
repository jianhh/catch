package com.work.admin.schema;

import java.io.Serializable;
import java.util.Date;

import com.framework.util.DateUtil;

/**
 * м╪пнап╠М
 * 
 * @author zhangwentao
 * 
 */
public class ShopSerieSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String num;
	private String join_time;
	private String total;
	private String up;

	public String getNum() {
		
		return num;
	}



	public void setNum(String num) {
		this.num = num;
	}



	public String getJoin_time() {
		return join_time;
	}



	public void setJoin_time(String join_time) {
		this.join_time = join_time;
	}



	public String getTotal() {
		return total;
	}



	public void setTotal(String total) {
		this.total = total;
	}



	public String getUp() {
		return up;
	}



	public void setUp(String up) {
		this.up = up;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


}
