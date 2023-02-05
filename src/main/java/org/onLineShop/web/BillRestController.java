package org.onLineShop.web;

import org.onLineShop.service.IBillService;
import org.onLineShop.service.from.InfoBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bills")
public class BillRestController {
	@Autowired
	IBillService iBillService;
	@GetMapping(path ="/{id}")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<InfoBill> printBill(@PathVariable(name = "id")long id ){
		InfoBill info = iBillService.billOrde(id);
		if(info!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(info);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
