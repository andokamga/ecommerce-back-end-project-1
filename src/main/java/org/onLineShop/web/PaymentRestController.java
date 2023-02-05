package org.onLineShop.web;

import org.onLineShop.service.IPaymentService;
import org.onLineShop.service.from.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payments")
public class PaymentRestController {
	@Autowired
	public IPaymentService IPaymentService;
	@PostMapping(path ="/MTN/{id}")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Void> paymentMTN(@RequestBody Body body, @PathVariable(name = "id") long id) throws Exception{
		String status = IPaymentService.RequestMTNPayment(body, id);
		if(status!=null) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
}
