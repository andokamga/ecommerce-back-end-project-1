package org.onLineShop.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.onLineShop.entity.UserApp;
import org.onLineShop.entity.UserRole;
import org.onLineShop.service.IAccountService;
import org.onLineShop.service.from.UrlData;
import org.onLineShop.service.security.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounts")
public class AccountRestController {
	@Autowired
	public IAccountService iAccountService;
	@Autowired
	public RefreshToken RefreshToken;
	@PostMapping(path ="/signup")
	//@PostMapping(path ="/user")
	public ResponseEntity<UserApp> saveUser(@RequestBody @Valid UserApp user) {
		UserApp userApp = iAccountService.addUser(user);
		if(userApp!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(userApp);
		 }
		 return ResponseEntity.status(HttpStatus.FOUND).build();
	}
	@PostMapping(path ="/role")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserRole> saveRole(@RequestBody @Valid UserRole role) {
		UserRole userRole = iAccountService.addRole(role);
		if(userRole!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(userRole);
		 }
		 return ResponseEntity.status(HttpStatus.FOUND).build();
	}
	@PostMapping(path ="/add")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> saveRoleToUser(@RequestBody @Valid UrlData urlData ) {
		if(iAccountService.addRoleToUser(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@PostMapping(path ="/remove")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> removeRoleToUser(@RequestBody @Valid UrlData urlData) {
		if(iAccountService.removeRoleUser(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping("/user/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<UserApp> getOneUser(@PathVariable(name = "id")  int id ){
		UserApp user = iAccountService.getOneUser(id);
		 if(user!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(user);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping("/role/{id}")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserRole> getOneRole(@PathVariable(name = "id")  int id ){
		UserRole role = iAccountService.getOneRole(id) ; 
		if(role!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(role);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@DeleteMapping (path ="user/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Void> delateuser(@PathVariable(name = "id") long id) throws IOException{
		if(iAccountService.deleteUser(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@DeleteMapping (path ="/role/{id}")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> delateRole(@PathVariable(name = "id") long id) {
		if(iAccountService.deleteRole(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@PutMapping (path ="/user")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<UserApp> userUpdate(@RequestBody @Valid UserApp user) {
		UserApp userApp = iAccountService.updateUser(user);
		if(userApp!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(userApp);
		}
		return ResponseEntity.status(HttpStatus.FOUND).build();
	}
	@PutMapping (path ="/role")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserRole> roleUpdate(@RequestBody @Valid UserRole role) {
		UserRole userRole = iAccountService.updateRole(role);
		if(userRole!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(userRole);
		}
		return ResponseEntity.status(HttpStatus.FOUND).build();
	} 
	@PostMapping(path ="/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<byte[]> uploadImage(MultipartFile file, @PathVariable(name = "id") long id) throws Exception{
		byte[] image = iAccountService.uploadPhoto(file, id);
		if(image!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(image);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<byte[]> image(@PathVariable(name = "id")long id) throws Exception {
		byte[] image = iAccountService.getImage(id);
		if(image!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(image);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	@GetMapping(path = "/refreshToken")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(RefreshToken.jwtRefreshToken( request, response)){
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping(path = "/profile")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<UserApp> profile(Principal principal) {
		UserApp user = iAccountService.loadUserByUsername(principal.getName());
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	@PostMapping(path ="verify")
	//@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<UserApp> verifyUsername(@RequestBody UrlData UrlData) {
		UserApp user = iAccountService.verifyUsername(UrlData);
		if(user!=null){
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@PostMapping(path = "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Page<UserApp>> getAlluser(@RequestBody UrlData UrlData){
		PageRequest pageRequest = PageRequest.of( UrlData.getPage(),UrlData.getSize() , Sort.by(Direction.ASC ,"userName"));
		Page<UserApp> users = iAccountService.getAlluser(pageRequest);
		if(users!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(users);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@GetMapping(path = "/roles")
	@PostAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<UserRole>> getAllRole( ){
		List<UserRole> Roles = iAccountService.getAllRole();
		if(Roles!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(Roles);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@DeleteMapping(path ="/image/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public ResponseEntity<Void> delateImage(@PathVariable(name = "id")long id) throws IOException {
		if(iAccountService.deletePhotoIfExist(id)){
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping(path = "/facebook")
	public ResponseEntity<Principal> facebook(Principal principal) {
		return ResponseEntity.status(HttpStatus.OK).body(principal);
	}
	
}
