package org.onLineShop.web;

import java.util.List;

import org.onLineShop.entity.Shop;
import org.onLineShop.entity.Town;
import org.onLineShop.entity.UserApp;
import org.onLineShop.service.IShopService;
import org.onLineShop.service.from.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/shops")
public class ShopRestController {
	@Autowired
	public IShopService iShopService;
	@PostMapping("/town")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Town> saveTown(@RequestBody Town town ) {
		Town t = iShopService.addTown(town);	
		 if(t!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(t);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@PostMapping
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Shop> saveShop(@RequestBody Shop shop ) {
		Shop s = iShopService.addShop(shop);	
		if(s!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(s);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@DeleteMapping (path ="/town/{id}")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> delateTown(@PathVariable(name = "id") long id) {
		if(iShopService.delateTown(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@DeleteMapping (path ="/{id}")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> delateShop(@PathVariable(name = "id") long id) {
		if(iShopService.delateShop(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@PutMapping
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Shop> shopUpdate(@RequestBody Shop shop) {
		Shop s = iShopService.updateShop(shop);
		if(s!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(s);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@PutMapping (path ="/town")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Town> townUpdate(@RequestBody Town town) {
		Town t = iShopService.updateTown(town);
		if(t!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(t);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@GetMapping("/{id}")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Shop> getOneShop(@PathVariable(name = "id")  int id ){
		Shop shop = iShopService.getOneShop(id);
		 if(shop!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(shop);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@GetMapping("/town/{id}")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Town> getOneTown(@PathVariable(name = "id")  int id ){
		Town town =  iShopService.getOneTown(id);
		 if(town!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(town);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@GetMapping("/towns")
	public ResponseEntity<List<Town>> getAllTown(){
		List<Town> towns = iShopService.getAllTown();
		if(towns!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(towns);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@PostMapping("/add")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> addSellerToShop(@RequestBody UrlData urlData) {
		 if(iShopService.addSellerToShop(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@PostMapping("/renove")
	//@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> renoveSellerToShop(@RequestBody UrlData urlData) {
		 if(iShopService.renoveSellerToShop(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@GetMapping("/sellers/{id}")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<UserApp>> allSellerOfShop(@PathVariable(name = "id")long idShop){
		List<UserApp> users = iShopService.allSellerOfShop(idShop);
		if(users!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(users);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
