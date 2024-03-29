package org.onLineShop;

import java.io.File;

import org.onLineShop.entity.ItemProduct;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Shop;
import org.onLineShop.service.IAccountService;
import org.onLineShop.service.ICourierService;
import org.onLineShop.service.IOrderService;
import org.onLineShop.service.IProductService;
import org.onLineShop.service.IShopInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class OnLineShopApplication implements CommandLineRunner {
	@Autowired
	private IShopInitService iShopInitService;
	@Autowired
	private RepositoryRestConfiguration restConfiguration;
	@Autowired
	private IAccountService iAccountService;
	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private IProductService iProductService;
	@Autowired
	private ICourierService iCourierService;
	
	public static void main(String[] args) {
		SpringApplication.run(OnLineShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		new File(iAccountService.uploadDirectory()).mkdir();
		new File(iProductService.uploadDirectory()).mkdir();
		iOrderService.deleteNotPaidOrder();
		restConfiguration.exposeIdsFor(Shop.class,Orde.class,ItemProduct.class);
		iShopInitService.initTown();
		iShopInitService.initShop();
		iShopInitService.initCategory();
		iShopInitService.initBrand();
		iShopInitService.initProduct();
		iShopInitService.initRole();
		iShopInitService.initUser();
		iShopInitService.initOrde();
		//iShopInitService.initItemProduct();
		iShopInitService.initPayment();
		iShopInitService.initShopSeller();
		//iCourierService.sendHtmlEmail();
		iCourierService.sendSMS("+23793266410", "en fin reusir la vie");
		
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			 @Override
			public void addCorsMappings(CorsRegistry registry) {
				// TODO Auto-generated method stub
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:4200")
					.allowedMethods("*")
	                .maxAge(3600L)
	                .allowedHeaders("*")
	                .exposedHeaders("Authorization");
	                //.allowCredentials(true);
				 /*registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT")
	                .allowedHeaders("*")
	                .exposedHeaders("*")
	                .allowCredentials(true).maxAge(3600);*/
			}
		};
	}

}
