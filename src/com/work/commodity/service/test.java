package com.work.commodity.service;

import com.work.commodity.content.CommodityContent;
import com.work.mail.MailSenderFactory;
import com.work.mail.SimpleMailSender;

/**
 * �ϴ�ͼƬ����ţ�ƴ洢
 * 
 * @author tangbiao
 * 
 * Sep 29, 2014 8:16:17 PM
 */
public class test {

	public static void main(String[] args) throws Exception {

		// �����ʼ�
		SimpleMailSender sms = MailSenderFactory.getSender();
		sms.send(CommodityContent.getRecipients(), "ץȡ���̼Ҵ��������Ϣʧ��:",
				"111111111111");
	}
}
