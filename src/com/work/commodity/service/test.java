package com.work.commodity.service;

import com.work.commodity.content.CommodityContent;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;

/**
 * 上传图片到七牛云存储
 * 
 * @author tangbiao
 * 
 * Sep 29, 2014 8:16:17 PM
 */
public class test {

	public static void main(String[] args) throws Exception {

		// 发送邮件
		SimpleMailSender sms = MailSenderFactory.getSender();
		sms.send(CommodityContent.getRecipients(), "抓取商商家代理加盟信息失败:",
				"111111111111");
	}
}
