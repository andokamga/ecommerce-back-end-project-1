package org.onLineShop.service.from;

import lombok.Data;

@Data
public class SendSMS {
	private String destinationSmsNumber;
	private String smsMessage;
}
