package com.work.taobao.content;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家信息常量
 * 
 * @author tangbiao
 * 
 */
public class CommodityContent {

	public static String ACCESS_KEY = "23WtVnKHrj37_NmValDCRH1JDiGncma4dzDeWl3H";// 七牛开发者自助平台的AK
	public static String SECRET_KEY = "Yem50dRHdfSGAbEaH9DogRxc6-V8EEBxnkhCANVN";// 七牛开发者自助平台的SK

	// 发送邮箱地址
	public static List<String> getRecipients() {
		List<String> list = new ArrayList<String>();
		// 测试：tb891022@163.com;正式：tb871022@163.com
		list.add("tb871022@163.com");
		return list;
	}

	// 七牛空间名bucketName
	public static String getBucketName() {
		// return "wg-test"; 测试环境用
		return "wg201505";
	}
}
