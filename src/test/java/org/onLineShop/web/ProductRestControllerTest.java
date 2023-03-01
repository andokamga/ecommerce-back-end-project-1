package org.onLineShop.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.Product;
import org.onLineShop.service.IProductService;
import org.onLineShop.service.from.UrlData;
import org.onLineShop.service.from.UtilDate;
import org.onLineShop.service.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
class ProductRestControllerTest {
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private AppUserDetails appUserDetails;
	@Autowired
	private IProductService iProductService;
	private MockMvc mockMvc;
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				 .webAppContextSetup(context)
				 .apply(springSecurity())
				 .build();
		
	}
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	    
	}
	public String obtainToken(String username, String password) throws Exception {
		 
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("userName", username);
	    params.add("password", password);

	    ResultActions result  = mockMvc.perform(post("/login")
	        .params(params)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .accept(MediaType.APPLICATION_JSON))
	    	.andExpect(status().isOk());
	   return result.andReturn().getResponse().getContentAsString();

	}
	public String accessToken(String username, String password) throws Exception {
		String token =obtainToken(username,password);
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(token).get("access-token").toString();
	}
	public String refreshToken(String username, String password) throws Exception {
		String token =obtainToken(username,password);
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(token).get("access-token").toString();
	}

	/*@Test
	void testImage() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/products/image/{id}",3)
				                              .contentType(MediaType.IMAGE_PNG_VALUE)
				                              .accept(MediaType.IMAGE_PNG_VALUE));
		result.andExpect(status().isOk());		                      
		
	}

	@Test
	void testUploadImage() throws Exception {
		//given -setup or precondition
		String accessToken = accessToken("sado","123");
		File file = new File(UtilDate.PROFIL_IMAGE +"Unknown.jpg");
		Path path = Paths.get(file.toURI());
		MockMultipartFile image = new MockMultipartFile("file","Unknown.jpg", null,Files.readAllBytes(path));
		//when -action
		ResultActions result = mockMvc.perform(multipart("/api/products/image/{id}",1)
				                               .file(image)
				                               .header("Authorization", "Bearer " +accessToken)
                                               .contentType(MediaType.IMAGE_PNG_VALUE)
                                               .accept(MediaType.IMAGE_PNG_VALUE));
        //then -verify output    
        result.andExpect(status().isOk());	
		
	}
	@Test
	void testPageProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setPage(0);
		urlData.setSize(2);
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/home")
                .content(asJsonString(urlData))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
		        .andExpect(jsonPath("size", CoreMatchers.is( urlData.getSize())))
		        .andExpect(jsonPath("number", CoreMatchers.is(urlData.getPage()))); 
	}

	@Test
	void testPagesearchProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setPage(0);
		urlData.setSize(2);
		urlData.setIdShop((long) 3);
		urlData.setSearch("ordina");
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/search")
                .content(asJsonString(urlData))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
		        .andExpect(jsonPath("size", CoreMatchers.is( urlData.getSize())))
		        .andExpect(jsonPath("number", CoreMatchers.is(urlData.getPage())))
		        .andExpect(jsonPath("content[1].productName", CoreMatchers.is( containsString(urlData.getSearch()))));
	}

	@Test
	void testGetListProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setPage(0);
		urlData.setSize(2);
		urlData.setIdShop((long) 3);
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products")
		                               .content(asJsonString(urlData))
		                                .contentType(MediaType.APPLICATION_JSON)
		                                .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
			  .andExpect(jsonPath("size", CoreMatchers.is( urlData.getSize())))
			  .andExpect(jsonPath("number", CoreMatchers.is(urlData.getPage())));
		
	}

	@Test
	void testSaveProduct() throws Exception {
		//given -setup or precondition
		Product product = new Product();
		product.setProductName("ordinateur 11");
		product.setAvailable(true);
		product.setBrand(iProductService.getOneBrand(3));
		product.setCategory(iProductService.getOneCategory(3));
		//when -action
		ResultActions result = mockMvc.perform(post("/api/products")
		                             .content(asJsonString(product))
		                             .with(user("andre").authorities(new SimpleGrantedAuthority("SELLER")))
		                             .contentType(MediaType.APPLICATION_JSON)
		                             .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isCreated())
				        .andExpect(jsonPath("productName", CoreMatchers.is( product.getProductName())));
		
	}

	@Test
	void testSaveBrand() throws Exception {
		//given -setup or precondition
		Brand brand = new Brand();
		brand.setBrandName("itel A 56");
		//when -action
		ResultActions result = mockMvc.perform(post("/api/products/brand")
		                              .content(asJsonString(brand))
		                              .with(user(appUserDetails.loadUserByUsername("kamga")))
		                              .contentType(MediaType.APPLICATION_JSON)
		                              .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isCreated())
			  .andExpect(jsonPath("brandName", CoreMatchers.is( brand.getBrandName())));
				        
		
	}

	@Test
	void testSaveCategory() throws Exception {
		//given -setup or precondition
		Category category = new Category();
		category.setCategoryName("habit");
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		String token =obtainToken("kamga","123");
		String accessToken = jsonParser.parseMap(token).get("access-token").toString();
		//when -action
		ResultActions result = mockMvc.perform(post("/api/products/category")
				                               .header("Authorization", "Bearer " + accessToken)
		                                       .content(asJsonString(category))
		                                       .contentType(MediaType.APPLICATION_JSON)
		                                       .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isCreated())
			  .andExpect(jsonPath("categoryName", CoreMatchers.is( category.getCategoryName())));
		
	}

	@Test
	void testDelateBrand() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(delete("/api/products/brand/{id}",2));
				                          
				//then -verify output
		result.andExpect(status().isOk());

		
	}

	@Test
	void testDelateCategory() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(delete("/api/products/category/{id}",2));
				               
		//then -verify output
		result.andExpect(status().isOk());			
		
	}

	@Test
	void testDelateProduct() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(delete("/api/products/{id}",2));
				                        
				//then -verify output
		result.andExpect(status().isOk());				        
		
	}

	@Test
	void testProductUpdate() throws Exception {
		//given -setup or precondition
		Product product = iProductService.getOneProduct(3);
		product.setProductName("ordinateur 11");
		product.setAvailable(true);
		product.setBrand(iProductService.getOneBrand(3));
		product.setCategory(iProductService.getOneCategory(3));
		//when -action
		ResultActions result = mockMvc.perform(put("/api/products")
		                             .content(asJsonString(product))
		                             .contentType(MediaType.APPLICATION_JSON)
		                             .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
				        .andExpect(jsonPath("productName", CoreMatchers.is( product.getProductName())));
	}

	@Test
	void testBrandUpdate() throws Exception {
		//given -setup or precondition
		Brand brand = iProductService.getOneBrand(3);
		brand.setBrandName("itel A 56");
		//when -action
		ResultActions result = mockMvc.perform(put("/api/products/brand")
		                              .content(asJsonString(brand))
		                              .contentType(MediaType.APPLICATION_JSON)
		                              .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
			  .andExpect(jsonPath("brandName", CoreMatchers.is( brand.getBrandName())));
	}

	@Test
	void testCategoryUpdate() throws Exception {
	   //given -setup or precondition
		Category category = iProductService.getOneCategory(3);
		category.setCategoryName("habit");
		//when -action
		ResultActions result = mockMvc.perform(put("/api/products/category")
		                .content(asJsonString(category))
		                .contentType(MediaType.APPLICATION_JSON)
		                .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
			  .andExpect(jsonPath("categoryName", CoreMatchers.is( category.getCategoryName())));
		
	}

	@Test
	void testGetOneProduct() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/{id}",3)
				                       .contentType(MediaType.APPLICATION_JSON)
				                        .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk());
		
	}

	@Test
	void testGetOneBrand() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/brand/{id}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
         //then -verify output
         result.andExpect(status().isOk());
	}

	@Test
	void testGetOneCategory() throws Exception {
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/category/{id}",3)
				                .contentType(MediaType.APPLICATION_JSON)
				                .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk());
	
	}

	@Test
	void testCategoryShopProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setPage(0);
		urlData.setSize(2);
		urlData.setIdShop((long) 3);
		urlData.setIdCat((long) 3);
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/categoryProducts")
				                       .content(asJsonString(urlData))
				                       .contentType(MediaType.APPLICATION_JSON)
				                       .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
			  .andExpect(jsonPath("size", CoreMatchers.is( urlData.getSize())))
			  .andExpect(jsonPath("number", CoreMatchers.is(urlData.getPage())));
		
	}

	@Test
	void testBrandShopProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setPage(0);
		urlData.setSize(2);
		urlData.setIdShop((long) 3);
		urlData.setIdBrand((long) 3);
		//when -action
		ResultActions result = mockMvc.perform(get("/api/products/brandProducts")
				                       .content(asJsonString(urlData))
				                       .contentType(MediaType.APPLICATION_JSON)
				                       .accept(MediaType.APPLICATION_JSON));
		//then -verify output
		result.andExpect(status().isOk())
			  .andExpect(jsonPath("size", CoreMatchers.is( urlData.getSize())))
			  .andExpect(jsonPath("number", CoreMatchers.is(urlData.getPage())));
	}

	@Test
	void testAllShopBrand() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/products/brand/{id}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then -verify output
        result.andExpect(status().isOk());
		
	}

	@Test
	void testAllShopCategory() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/products/category/{id}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then -verify output
       result.andExpect(status().isOk());
		
	}

	@Test
	void testStockProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setIdProduct((long) 3);
		urlData.setQte(2);
		ResultActions result = mockMvc.perform(post("/api/products/stock")
				.content(asJsonString(urlData))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then -verify output
        result.andExpect(status().isOk());
	}

	@Test
	void testDestockProduct() throws Exception {
		//given -setup or precondition
		UrlData urlData = new UrlData();
		urlData.setIdProduct((long) 3);
		urlData.setQte(2);
		ResultActions result = mockMvc.perform(post("/api/products/destock")
				.content(asJsonString(urlData))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then -verify output
        result.andExpect(status().isOk());
		
	}*/

}
