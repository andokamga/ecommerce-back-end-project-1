package org.onLineShop.service;

import org.onLineShop.service.from.Body;
import org.onLineShop.service.from.StatusPayment;

public interface IPaymentService {
	public StatusPayment RequestMTNPayment(Body bodyClient,long id) throws Exception;
}
