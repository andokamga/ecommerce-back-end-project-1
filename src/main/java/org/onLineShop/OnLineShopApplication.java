package org.onLineShop;

import java.io.File;

import org.onLineShop.entity.ItemProduct;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Shop;
import org.onLineShop.service.IAccountService;
import org.onLineShop.service.IOrderService;
import org.onLineShop.service.IProductService;
import org.onLineShop.service.IShopInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

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
		iShopInitService.initItemProduct();
		iShopInitService.initPayment();
		iShopInitService.initShopSeller();
		
	}

}
