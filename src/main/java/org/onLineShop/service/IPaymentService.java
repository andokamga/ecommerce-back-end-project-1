package org.onLineShop.service;

import org.onLineShop.service.from.Body;

public interface IPaymentService {
	public String RequestMTNPayment(Body bodyClient,long id) throws Exception;
}
