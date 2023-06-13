package org.onLineShop.web;

import org.onLineShop.entity.Orde;
import org.onLineShop.service.IOrderService;
import org.onLineShop.service.from.OrderForme;
import org.onLineShop.service.from.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
public class OrderRestController {
	@Autowired
	public IOrderService iOrderService;
	@PostMapping
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Orde> saveOrder(@RequestBody OrderForme orderForme ) {
		Orde orde = iOrderService.addOrder(orderForme);
		return ResponseEntity.status(HttpStatus.CREATED).body(orde);
	}
	@PostMapping(path ="/client")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Page<Orde>> listClientOrde(@RequestBody UrlData urlData){
			 PageRequest pageRequest = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"orderDate"));
			 Page<Orde> ordes = iOrderService.listOrdeUser(urlData.getIdUser(),pageRequest);
			 if(ordes!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(ordes);
			 }
			 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			 
	}
	@GetMapping(path ="/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Orde> getOneOrder(@PathVariable(name = "id")long id) {
		Orde orde = iOrderService.getOneOrder(id);
		if(orde!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(orde);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@PostMapping(path ="/shop")
	@PostAuthorize("hasAuthority('SELLER')")
	public ResponseEntity<Page<Orde>> getShopOrder(@RequestBody UrlData urlData){
		PageRequest pageRequest = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"orderDate"));
		Page<Orde> ordes = iOrderService.getShopOrder(urlData, pageRequest);
		if(ordes!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(ordes);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		 
	}
	@DeleteMapping(path ="/{id}")
	@PostAuthorize("hasAuthority('SELLER')")
	public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id")long id) {
		if(iOrderService.deleteOrder(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

}