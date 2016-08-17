package com.work.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StartContent;

/**
 * ���ʼ����������ɵ�����Ⱥ����
 * 
 * @author tangbiao
 * 
 */
public class SimpleMailSender {
	private static JLogger logger = LoggerFactory
			.getLogger(SimpleMailSender.class);

	/**
	 * �����ʼ���props�ļ�
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * �ʼ���������¼��֤
	 */
	private transient MailAuthenticator authenticator;

	/**
	 * ����session
	 */
	private transient Session session;

	/**
	 * ��ʼ���ʼ�������
	 * 
	 * @param smtpHostName
	 *            SMTP�ʼ���������ַ
	 * @param username
	 *            �����ʼ����û���(��ַ)
	 * @param password
	 *            �����ʼ�������
	 */
	public SimpleMailSender(final String smtpHostName, final String username,
			final String password) {
		init(username, password, smtpHostName);
	}

	/**
	 * ��ʼ���ʼ�������
	 * 
	 * @param username
	 *            �����ʼ����û���(��ַ)�����Դ˽���SMTP��������ַ
	 * @param password
	 *            �����ʼ�������
	 */
	public SimpleMailSender(final String username, final String password) {
		// ͨ�������ַ������smtp���������Դ�������䶼����
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);

	}

	/**
	 * ��ʼ��
	 * 
	 * @param username
	 *            �����ʼ����û���(��ַ)
	 * @param password
	 *            ����
	 * @param smtpHostName
	 *            SMTP������ַ
	 */
	private void init(String username, String password, String smtpHostName) {
		// ��ʼ��props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		// ��֤
		authenticator = new MailAuthenticator(username, password);
		// ����session
		session = Session.getInstance(props, authenticator);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, Object content) {
		logger.debug("�����ʼ�:SimpleMailSender.send()");
		try {
			// ����mime�����ʼ�
			final MimeMessage message = new MimeMessage(session);
			// ���÷�����
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			// �����ռ���
			message.setRecipient(RecipientType.TO, new InternetAddress(
					recipient));
			// ��������
			message.setSubject(subject);
			// �����ʼ�����
			message.setContent(content.toString(), "text/html;charset=utf-8");
			// ����
			if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// ��������Ҫ���ʼ�
				Transport.send(message);
			}
		} catch (Exception e) {
			logger.error("�����ʼ�ʧ�ܣ�subject=" + subject + " content"
					+ content.toString(), e);
		}
	}

	/**
	 * Ⱥ���ʼ�
	 * 
	 * @param recipients
	 *            �ռ�����
	 * @param subject
	 *            ����
	 * @param content
	 *            ����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, String subject, Object content) {
		logger.debug("Ⱥ���ʼ�:SimpleMailSender.send()");
		try {
			if(recipients == null || recipients.size() == 0){
				return;
			}
			// ����mime�����ʼ�
			final MimeMessage message = new MimeMessage(session);
			// ���÷�����
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			// �����ռ�����
			final int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
				addresses[i] = new InternetAddress(recipients.get(i));
			}
			message.setRecipients(RecipientType.TO, addresses);
			// ��������
			message.setSubject(subject);
			// �����ʼ�����
			message.setContent(content.toString(), "text/html;charset=utf-8");
			// ����
			if (StartContent.getInstance().getDomainUrl().equals("www.truedian.com")) {// ��������Ҫ���ʼ�
				Transport.send(message);
			}
		} catch (Exception e) {
			logger.error("Ⱥ���ʼ�ʧ�ܣ�subject=" + subject + " content"
					+ content.toString(), e);
		}
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		// ���ԣ�tb891022@163.com;��ʽ��tb871022@163.com
		/*
		 * list.add("tangbiao@wegooooo.com"); list.add("lisong@wegooooo.com");
		 * list.add("longguangxing@wegooooo.com");
		 * list.add("chenglong@wegooooo.com");
		 */
		list.add("zhangwentao@wegooooo.com");
		SimpleMailSender sms = MailSenderFactory.getSender();
		sms.send(list, "�г���3��δ�����ȡ����Ϣ����ע��鿴", "�г���3��δ�����ȡ����Ϣ����ע��鿴!");
	}
}