package org.onLineShop.service;

import jakarta.mail.MessagingException;

public interface ICourierService {
	public void sendEmail(String to, String subject, String body);
	public void sendHtmlEmail()throws MessagingException;
	public String sendSMS(String number,String SmsMessage);
}
