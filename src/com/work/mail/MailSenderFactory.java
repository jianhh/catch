package com.work.mail;

/**
 * 发件箱工厂
 * 
 * @author tangbiao
 * 
 */
public class MailSenderFactory {

	/**
	 * 服务邮箱
	 */
	private static SimpleMailSender serviceSms = null;

	/**
	 * 发件箱
	 * 
	 * @param type
	 *            邮箱类型
	 * @return 符合类型的邮箱
	 */
	public static SimpleMailSender getSender() {
		if (serviceSms == null) {
			// 测试：tb871022@163.com;正式:tb891022@163.com;开发：417287369@qq.com
			serviceSms = new SimpleMailSender("tm891024@sina.com", "tangbiao131130");
		}
		return serviceSms;
	}

}