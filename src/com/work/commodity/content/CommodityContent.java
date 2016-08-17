package com.work.commodity.content;

import java.util.ArrayList;
import java.util.List;

import com.framework.util.StartContent;

/**
 * 商家信息常量
 * 
 * @author tangbiao
 * 
 */
public class CommodityContent {

	public static String ACCESS_KEY = "23WtVnKHrj37_NmValDCRH1JDiGncma4dzDeWl3H";// 七牛开发者自助平台的AK
	public static String SECRET_KEY = "Yem50dRHdfSGAbEaH9DogRxc6-V8EEBxnkhCANVN";// 七牛开发者自助平台的SK
	
	public static final String KEY_PREFIX_PRICE_ALITAO = "alitao";
	// 一键上新价格开关1：开，0 关
	public static final String IS_AUTO_PAY = "is_auto_pay";
	//价格设置方式，1=价格，2=比例
	public static final String KEY_PRICE_SET_METHOD = "price_set_method";
	//调整增加还是减少，1=增加，2=减少
	public static final String KEY_PRICE_ADD_OR_MINUS = "price_add_or_minus";
	//价格调整设置的值
	public static final String KEY_PRICE_SET_VALUE = "price_set_value";

	// 发送邮箱地址
	public static List<String> getRecipients() {
		List<String> list = new ArrayList<String>();
		if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// 现网
			list.add("tm891024@sina.com");
		}
		return list;
	}

	// 七牛空间名bucketName
	public static String getBucketName() {
		// return "wg-test"; 测试环境用
		if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// 现网
			return "wg201505";
		} else if (StartContent.getInstance().getDomainUrl().equals("www.81851.net")){
			return "wg201504";
		}else{
			return "wg201503";
		}
	}
}
