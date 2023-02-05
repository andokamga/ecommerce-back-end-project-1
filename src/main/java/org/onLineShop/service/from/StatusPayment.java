package org.onLineShop.service.from;

import lombok.Data;

@Data
public class StatusPayment {
	private String financialTransactionId;
	private String externalId;
	private String amount;
	private String currency;
	private Payer payer;
	private String payerMessage;
	private String payeeNote;
	private String status;
}
