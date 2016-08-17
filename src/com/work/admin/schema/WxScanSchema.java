package com.work.admin.schema;

import java.io.Serializable;

/**
 * м╪пнап╠М
 * 
 * @author zhangwentao
 * 
 */
public class WxScanSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String c_scan_scene;

	private String num;


	public String getC_scan_scene() {
		return c_scan_scene;
	}

	public void setC_scan_scene(String c_scan_scene) {
		this.c_scan_scene = c_scan_scene;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}


}
