package com.work.admin.content;

import java.util.ArrayList;
import java.util.List;

import com.framework.util.StartContent;

/**
 * 商家信息常量
 * 
 * @author tangbiao
 * 
 */
public class AdminContent {

	public static String ACCESS_KEY = "23WtVnKHrj37_NmValDCRH1JDiGncma4dzDeWl3H";// 七牛开发者自助平台的AK
	public static String SECRET_KEY = "Yem50dRHdfSGAbEaH9DogRxc6-V8EEBxnkhCANVN";// 七牛开发者自助平台的SK
	// 测试：www.81851.net;开发域名：wegooooo.com;现网域名：truedian.com
	// public static String DOMAIN_URL = "www.truedian.com";// 二级域名地址
	// int类型的常量
	public static int NUM_ZORE = 0;
	public static int NUM_FIRST = 1;
	public static int NUM_SECOND = 2;
	public static int NUM_THIRD = 3;
	public static int NUM_FOUR = 4;
	public static int NUM_FIVE = 5;
	public static int NUM_SIX = 6;
	public static int NUM_SEVEN = 7;
	public static int NUM_EIGHT = 8;
	public static int NUM_NINE = 9;
	public static int NUM_TENTH = 10;
	// String类型的常量
	public static String S_ZORE = "0";
	public static String S_FIRST = "1";
	public static String S_SECOND = "2";
	public static String S_THIRD = "3";
	public static String S_FOUR = "4";
	public static String S_FIVE = "5";
	public static String S_SIX = "6";
	public static String S_SEVEN = "7";
	public static String S_EIGHT = "8";
	public static String S_NINE = "9";
	public static String S_TENTH = "10";
	public static String SHOP_URL = "http://www.truedian.com/static/shop/shop_index.html?state=STATE&shop_id=";

	// 发送邮箱地址
	public static List<String> getRecipients() {
		List<String> list = new ArrayList<String>();
		// 测试：tb891022@163.com;正式：tb871022@163.com
		list.add("tb871022@163.com");
		return list;
	}

}
