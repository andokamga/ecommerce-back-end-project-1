package org.onLineShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class ICourierServiceImpl implements ICourierService{

	@Autowired
    private JavaMailSender mailSender;
	@Value("${TWILIO_ACCOUNT_SID}")
	String ACCOUNT_SID;
	@Value("${TWILIO_AUTH_TOKEN}")
	String AUTH_TOKEN;
	@Value("${TWILIO_TRIAL_NUMBER}")
	String TRIAL_NUMBER;
	@Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
	@Override
    public void sendHtmlEmail() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("kamga.ando@yahoo.fr"));
        message.setRecipients(MimeMessage.RecipientType.TO, "andokamga@gmail.com");
        message.setSubject("Test email from Spring");

        String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                             "<p>It can contain <strong>HTML</strong> content.</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }
	/*@PostConstruct
	public void setup() {
		Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
	}*/
	@Override
	public String sendSMS(String number,String SmsMessage) {
		Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
		Message message = Message.creator(
				new PhoneNumber(number), 
				new PhoneNumber(TRIAL_NUMBER), SmsMessage).create();
		
		return message.getStatus().toString();
	}
	
}
