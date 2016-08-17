package com.work.commodity.content;

import java.util.ArrayList;
import java.util.List;

import com.framework.util.StartContent;

/**
 * �̼���Ϣ����
 * 
 * @author tangbiao
 * 
 */
public class CommodityContent {

	public static String ACCESS_KEY = "23WtVnKHrj37_NmValDCRH1JDiGncma4dzDeWl3H";// ��ţ����������ƽ̨��AK
	public static String SECRET_KEY = "Yem50dRHdfSGAbEaH9DogRxc6-V8EEBxnkhCANVN";// ��ţ����������ƽ̨��SK
	
	public static final String KEY_PREFIX_PRICE_ALITAO = "alitao";
	// һ�����¼۸񿪹�1������0 ��
	public static final String IS_AUTO_PAY = "is_auto_pay";
	//�۸����÷�ʽ��1=�۸�2=����
	public static final String KEY_PRICE_SET_METHOD = "price_set_method";
	//�������ӻ��Ǽ��٣�1=���ӣ�2=����
	public static final String KEY_PRICE_ADD_OR_MINUS = "price_add_or_minus";
	//�۸�������õ�ֵ
	public static final String KEY_PRICE_SET_VALUE = "price_set_value";

	// ���������ַ
	public static List<String> getRecipients() {
		List<String> list = new ArrayList<String>();
		if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// ����
			list.add("tm891024@sina.com");
		}
		return list;
	}

	// ��ţ�ռ���bucketName
	public static String getBucketName() {
		// return "wg-test"; ���Ի�����
		if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// ����
			return "wg201505";
		} else if (StartContent.getInstance().getDomainUrl().equals("www.81851.net")){
			return "wg201504";
		}else{
			return "wg201503";
		}
	}
}
