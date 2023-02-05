package org.onLineShop.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.onLineShop.dao.BrandRepository;
import org.onLineShop.dao.CategoryRepository;
import org.onLineShop.dao.ItemProductRepository;
import org.onLineShop.dao.OrdeRepository;
import org.onLineShop.dao.PaymentRepository;
import org.onLineShop.dao.ProductRepository;
import org.onLineShop.dao.ShopRepository;
import org.onLineShop.dao.ShopSellerRepository;
import org.onLineShop.dao.TownRepository;
import org.onLineShop.dao.UserAppRepository;
import org.onLineShop.dao.UserRoleRepository;
import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.EnumStatus;
import org.onLineShop.entity.ItemProduct;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Payment;
import org.onLineShop.entity.Product;
import org.onLineShop.entity.Shop;
import org.onLineShop.entity.ShopSeller;
import org.onLineShop.entity.Town;
import org.onLineShop.entity.UserApp;
import org.onLineShop.entity.UserRole;
import org.onLineShop.service.from.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ShopInitServiceImpl implements IShopInitService {
	@Autowired
	public TownRepository townRepository;
	@Autowired
	public ShopRepository shopRepository;
	@Autowired
	public BrandRepository brandRepository;
	@Autowired
	public CategoryRepository categoryRepository;
	@Autowired
	public ProductRepository productRepository;
	@Autowired
	public UserRoleRepository userRoleRepository;
	@Autowired
	public UserAppRepository userAppRepository;
	@Autowired
	public OrdeRepository ordeRepository;
	@Autowired
	public ItemProductRepository itemProductRepository;
	@Autowired
	public PaymentRepository PaymentRepository;
	@Autowired
	public ShopSellerRepository shopSellerRepository;
	@Autowired
	public PasswordEncoder PasswordEncoder;
	@Override
	public void initTown() {
	Stream.of("yaounde","douala","kribe","bandjoun").forEach(townName->{
		Town town = new Town();
		town.setTownName(townName);
		town.setShopNumbers(3+(int)(Math.random()*3));
		townRepository.save(town);
		});
		
	}
    
	@Override
	public void initShop() {
		townRepository.findAll().forEach(town->{
			for(int i=0; i<town.getShopNumbers(); i++) {
				Shop shop = new Shop();
				shop.setShopName("shop "+(i+1));
				shop.setTown(town);
				shopRepository.save(shop);
				}	
			});
		
		
	}

	@Override
	public void initBrand() {
		Stream.of("hp","dell","lenovo","itel","aplle").forEach(brandName->{
			Brand brand = new Brand();
			brand.setBrandName(brandName);
			brandRepository.save(brand);
			});
		
	}

	@Override
	public void initCategory() {
		Stream.of("ordinateur","tele","phone","usb","camera").forEach(categoryName->{
			Category category = new Category();
			category.setCategoryName(categoryName);
			categoryRepository.save(category);
			});
		
	}

	@Override
	public void initRole() {
		Stream.of("admin","seller","user").forEach(userName->{
			UserRole role = new UserRole();
			role.setUserRoleName(userName.toUpperCase());
			userRoleRepository.save(role);
			});
	}

	@Override
	public void initUser() {
		List<UserRole> roles = userRoleRepository.findAll();
		Stream.of("kamga","sado","andre","delon","nguigne","mervelle").forEach(userName->{
			UserApp user = new UserApp();
			user.setUserName(userName);
			user.setActive(true);
			user.setPassword(PasswordEncoder.encode("123"));
			user.setUserImage("UnKnown.jpg");
			for(int i=0; i<(1+(int)(Math.random()*2)); i++) {
				UserRole role = roles.get(new Random().nextInt(roles.size()));
				user.getUserRoles().add(role);
				role.getUserApps().add(user);
			};
			
			userAppRepository.save(user);
		});		
		
	}

	@Override
	public void initProduct() {
		double[] price = new double[] {1000,2500,7550,800,400};
		townRepository.findAll().forEach(town->{
			town.getShops().forEach(shop->{
				Stream.of("ordinateur 1","ordinateur 2","camera 1","telephone 1",
						"usb 1","ordinateur 3","tele 1","ordinateur 4","camera 2","telephone 2",
						"usb 2","ordinateur 5","ordinateur 6","camera 3","telephone 3",
						"tele 2","ordinateur 7","ordinateur 8","tele 3","ordinateur 9","ordinateur 10").forEach(productName->{
							List<Category> category=categoryRepository.findAll();
							List<Brand> brand=brandRepository.findAll();
							Product product=new Product();
							product.setProductName(productName);
							product.setProductImage(productName.replaceAll(" ", "")+".jpg");
							product.setProductPrice(price[new Random().nextInt(price.length)]);
							product.setProductQuantity(10+(int)(Math.random()*11));
							product.setAvailable(true);
							product.setShop(shop);
							product.setCategory(category.get(new Random().nextInt(category.size())));
							product.setBrand(brand.get(new Random().nextInt(brand.size())));
							productRepository.save(product);
						});
			});
			
		});
		
		
	}

	@Override
	public void initOrde() {
		List<UserApp>  userApp= userAppRepository.findAll();
		LocalDateTime startTime = LocalDateTime.now().plusHours(UtilDate.LIMIT_DATE);
		Date limitDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
		  townRepository.findAll().forEach(town->{
			  shopRepository.findAll().forEach(shop->{
	              for(int i=0;i<(5+(int)(Math.random()*10));i++) {
	            	Orde orde = new Orde();
	      			orde.setOrderDate(new Date());
	      			orde.setLimitDate(limitDate);
	      			orde.setOrderPrice(1000+Math.random()*7000);
	      			orde.setUserApp(userApp.get(new Random().nextInt(userApp.size())));
	      			orde.setStatus(EnumStatus.NOPAID);
	      			ordeRepository.save(orde); 
				  }
		  });
		  
			
		});
				
		
	}

	@Override
	public void initItemProduct() {
		List<Product> product = productRepository.findAll();
		ordeRepository.findAll().forEach(orde->{
			for(int i=0; i<(3+(int)(Math.random()*3)); i++) {
				ItemProduct item = new ItemProduct();
				item.setOrde(orde);
				item.setProduct(product.get(new Random().nextInt(product.size())));
				item.setQuatity((3+(int)Math.random()*3));
				itemProductRepository.save(item);
				}	
			});
		
	}

	@Override
	public void initPayment() {
	
		List<Orde> ordes = ordeRepository.findAll();
		
			 for(int i=0; i< ordes.size(); i++) {
				if(i%2==0) {
					Payment payment = new Payment();
					Optional<Orde> ordeOptional = ordeRepository.findById((long)i);
					if(ordeOptional.isPresent()){
						Orde orde = ordeOptional.get();	
						payment.setOrde(orde);
						payment.setPaymentDate(new Date());
						payment.setPaymentOperator("MTN");
						payment.setPaymentRef("hjgyjhj");
						orde.setStatus(EnumStatus.PAID);
						PaymentRepository.save(payment);
					}
					
				}
				
			}
			
			
		
	}

	@Override
	public void initShopSeller() {
		boolean trouve;
		for(Shop shop:shopRepository.findAll()) {
			List<UserApp> sellers = userAppRepository.findAll();
			UserApp seller = sellers.get(new Random().nextInt(sellers.size()));
			trouve = false;
			for(ShopSeller shopSeller :shopSellerRepository.findAll()) {
				if(shopSeller.getSeller().equals(seller)) {
					trouve = true;
					break;
				}
							
			}			
			if(!trouve) {
				ShopSeller shopSelle = new ShopSeller();
				shopSelle.getWorkDay().add("Lundi");
				shopSelle.setShop(shop);
				shopSelle.setSeller(seller);
				shopSellerRepository.save(shopSelle);
			}
			
		}
		
	}


}
