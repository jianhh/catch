package com.work.mail;

/**
 * �����乤��
 * 
 * @author tangbiao
 * 
 */
public class MailSenderFactory {

	/**
	 * ��������
	 */
	private static SimpleMailSender serviceSms = null;

	/**
	 * ������
	 * 
	 * @param type
	 *            ��������
	 * @return �������͵�����
	 */
	public static SimpleMailSender getSender() {
		if (serviceSms == null) {
			// ���ԣ�tb871022@163.com;��ʽ:tb891022@163.com;������417287369@qq.com
			serviceSms = new SimpleMailSender("tm891024@sina.com", "tangbiao131130");
		}
		return serviceSms;
	}

}