package com.work.taobao.content;

import java.util.ArrayList;
import java.util.List;

/**
 * �̼���Ϣ����
 * 
 * @author tangbiao
 * 
 */
public class CommodityContent {

	public static String ACCESS_KEY = "23WtVnKHrj37_NmValDCRH1JDiGncma4dzDeWl3H";// ��ţ����������ƽ̨��AK
	public static String SECRET_KEY = "Yem50dRHdfSGAbEaH9DogRxc6-V8EEBxnkhCANVN";// ��ţ����������ƽ̨��SK

	// ���������ַ
	public static List<String> getRecipients() {
		List<String> list = new ArrayList<String>();
		// ���ԣ�tb891022@163.com;��ʽ��tb871022@163.com
		list.add("tb871022@163.com");
		return list;
	}

	// ��ţ�ռ���bucketName
	public static String getBucketName() {
		// return "wg-test"; ���Ի�����
		return "wg201505";
	}
}
