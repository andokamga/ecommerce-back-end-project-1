package org.onLineShop.service.from;

import lombok.Data;

@Data
public class Body {
	private String amount;
	private String  currency ;
	private String externalId;
	private String payerMessage;
	private String payeeNote;
	private Payer payer;
}
