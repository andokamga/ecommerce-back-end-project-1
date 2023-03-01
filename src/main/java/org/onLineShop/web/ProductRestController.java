package org.onLineShop.web;

import java.util.List;

import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.Product;
import org.onLineShop.service.IProductService;
import org.onLineShop.service.from.UrlData;
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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@OpenAPIDefinition
@RequestMapping("/api/products")
public class ProductRestController{
	
	 @Autowired
	 public IProductService iProductService;
	 
	 @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	 @Operation(summary = "to get image, give the image id")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "200", description = "image is product present",content = {@Content(mediaType= "application/PNG")}),
		 @ApiResponse(responseCode = "400", description = "bad request",content = @Content),
		 @ApiResponse(responseCode = "500", description = "server problem",content = @Content)
	 })
	 public ResponseEntity<byte[]> image(@PathVariable(name = "id")long id) throws Exception {
		 byte[] image = iProductService.getImage(id);
		 if(image!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(image);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @PostMapping(path ="/image/{id}",produces = MediaType.IMAGE_PNG_VALUE)
	 @Operation(summary = "to save image give the file and  id product image")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "200", description = "page of product present",content = {@Content(mediaType= "application/PNG")}),
		 @ApiResponse(responseCode = "400", description = "bad request",content = @Content),
		 @ApiResponse(responseCode = "500", description = "server problem",content = @Content)
	 })
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<byte[]> uploadImage(MultipartFile file, @PathVariable(name = "id") long id) throws Exception{
		 byte[] image = iProductService.uploadPhoto(file, id);
		 return ResponseEntity.status(HttpStatus.OK).body(image);

	 }
	 @GetMapping(path ="/home",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 @Operation(summary = "to get this list we need to give the id shop")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "200", description = "page of product present",content = {@Content(mediaType= "application/Json")}),
		 @ApiResponse(responseCode = "204", description = "page of product is empty",content = @Content),
		 @ApiResponse(responseCode = "400", description = "bad request",content = @Content),
		 @ApiResponse(responseCode = "500", description = "server problem",content = @Content)
	 })
	 public ResponseEntity<Page<Product>> pageProduct(@RequestBody @Valid UrlData urlData){
		 PageRequest pageRequest = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"productName"));
		  Page<Product> products= iProductService.home(pageRequest);	
		  if(products!=null) {
			  return ResponseEntity.status(HttpStatus.OK).body(products);
		  }
		  return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 @GetMapping(path ="/search",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 @Operation(summary = "to search the product give the wordkey and shop id")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "200", description = "page of product present",content = {@Content(mediaType= "application/Json")}),
		 @ApiResponse(responseCode = "204", description = "page of product is empty",content = @Content),
		 @ApiResponse(responseCode = "400", description = "bad request",content = @Content),
		 @ApiResponse(responseCode = "500", description = "server problem",content = @Content)
	 })
	 public ResponseEntity<Page<Product>>pagesearchProduct(@RequestBody @Valid UrlData urlData){
		 PageRequest pageRequest = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"productName"));
		 Page<Product> products= iProductService.searchByShop(urlData,pageRequest);	
		  if(products!=null) {
			  return ResponseEntity.status(HttpStatus.OK).body(products);
		  }
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Page<Product>> getListProduct(@RequestBody @Valid UrlData urlData ){
		 PageRequest pageRequest = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"productName")); 
		Page<Product> products= iProductService.listProduct(urlData,pageRequest);	
		  if(products!=null) {
			  return ResponseEntity.status(HttpStatus.OK).body(products);
		  }
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 @Operation(summary = "to save product give product and require information")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "201", description = "product create",content = {@Content(mediaType= "application/Json")}),
		 @ApiResponse(responseCode = "400", description = "bad request",content = @Content),
		 @ApiResponse(responseCode = "500", description = "server problem",content = @Content)
	 })
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product product) {
		 Product produit = iProductService.addProduct(product);
		 return ResponseEntity.status(HttpStatus.CREATED).body(produit);
		
	 }
	 @PostMapping(path ="/brand",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 @PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Brand> saveBrand(@RequestBody @Valid Brand brand) {
		 Brand bran = iProductService.addBrand(brand);
		 if(bran!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(bran);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 @PostMapping(path ="/category",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Category> saveCategory(@RequestBody @Valid Category category) {
		 Category cat = iProductService.addCategory(category);
		 if(cat!=null) {
			 return ResponseEntity.status(HttpStatus.CREATED).body(cat);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 
	 @DeleteMapping (path ="/brand/{id}")
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Void>delateBrand(@PathVariable(name = "id") long id) {
		 if(iProductService.delateBrand(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @DeleteMapping (path ="/category/{id}")
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Void> delateCategory(@PathVariable(name = "id") long id) {
		 if(iProductService.delateCategory(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @DeleteMapping (path ="/{id}")
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Void> delateProduct(@PathVariable(name = "id") long id) throws Exception {
		 if(iProductService.delateProduct(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Product> productUpdate(@RequestBody @Valid Product product) {
		 Product produit = iProductService.updateProduct(product);
		 return ResponseEntity.status(HttpStatus.OK).body(produit);
	 }
	 @PutMapping (path ="/brand",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Brand> brandUpdate(@RequestBody Brand brand) {
		 Brand bran = iProductService.updateBrand(brand);
		 if(bran!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(bran);
		 }
		 return ResponseEntity.status(HttpStatus.FOUND).build();
	 }
	 @PutMapping (path ="/category",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Category> CategoryUpdate(@RequestBody @Valid Category Category) {
		 Category cat = iProductService.updateCategory(Category);
		 if(cat!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(cat);
		 }
		 return ResponseEntity.status(HttpStatus.FOUND).build();
	 }
	 @GetMapping (path ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Product> getOneProduct(@PathVariable(name = "id")long id) {
		 Product product = iProductService.getOneProduct(id);
		 if(product!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(product);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @GetMapping (path ="/brand/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Brand> getOneBrand(@PathVariable(name = "id")long id) {
		 Brand brand =  iProductService.getOneBrand(id);
		 if(brand!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(brand);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @GetMapping (path ="/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Category> getOneCategory(@PathVariable(name = "id") long id){
		 Category category = iProductService.getOneCategory(id);
		 if(category!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(category);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @GetMapping (path ="/categoryProducts",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Page<Product>> categoryShopProduct(@RequestBody UrlData urlData){
		 PageRequest pageShopProduct = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"productName"));
		 Page<Product> product = iProductService.categoryShopProduct(urlData, pageShopProduct);
		 if(product!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(product);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
		
	 @GetMapping (path ="/brandProducts",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Page<Product>> brandShopProduct(@RequestBody UrlData urlData){
		 PageRequest pageShopProduct = PageRequest.of( urlData.getPage(),urlData.getSize() , Sort.by(Direction.ASC ,"productName"));
		 Page<Product> product = iProductService.brandShopProduct(urlData, pageShopProduct);
		 if(product!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(product);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 
	 @GetMapping (path ="/brands/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<List<Brand>> allShopBrand(@PathVariable(name = "id")long id){
		 List<Brand> brand = iProductService.allShopBrand(id);
		 if(brand!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(brand);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 @GetMapping (path ="/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<List<Category>> allShopCategory(@PathVariable(name = "id")long id){
		 List<Category> categories = iProductService.allShopCategory(id);
		 if(categories!=null) {
			 return ResponseEntity.status(HttpStatus.OK).body(categories);
		 }
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 }
	 @PostMapping(path ="/stock",consumes = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Void> stockProduct(@RequestBody @Valid UrlData urlData) {
		 if(iProductService.stockProduct(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
	 @PostMapping (path ="/destock", produces = MediaType.APPLICATION_JSON_VALUE)
	 //@PostAuthorize("hasAuthority('SELLER')")
	 public ResponseEntity<Void> destockProduct(@RequestBody @Valid UrlData urlData) {
		 if(iProductService.destockProduct(urlData)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 }
}
