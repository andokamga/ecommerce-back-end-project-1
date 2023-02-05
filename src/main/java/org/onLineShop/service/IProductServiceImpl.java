package org.onLineShop.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.onLineShop.dao.BrandRepository;
import org.onLineShop.dao.CategoryRepository;
import org.onLineShop.dao.ItemProductRepository;
import org.onLineShop.dao.OrdeRepository;
import org.onLineShop.dao.ProductRepository;
import org.onLineShop.dao.ShopRepository;
import org.onLineShop.dao.TownRepository;
import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.Product;
import org.onLineShop.entity.Shop;
import org.onLineShop.service.from.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class IProductServiceImpl implements IProductService {
	@Autowired
	public ProductRepository productRepository;
	@Autowired
	public TownRepository townRepository;
	@Autowired
	public ShopRepository shopRepository;
	@Autowired
	public BrandRepository brandRepository;
	@Autowired
	public CategoryRepository categoryRepository;
	@Autowired
	public ItemProductRepository itemProductRepository;
	@Autowired
	public OrdeRepository ordeRepository;
	@Autowired
	public IOrderService iOrderService;
	@Override
	public String uploadDirectory() {
		return System.getProperty("user.home")+"/shop/products";
	}
	public void deletePhotoIfExist(long id) throws IOException {
		if(productRepository.findById(id).isPresent()) {
			String filename = productRepository.findById(id).get().getProductImage();
			Path filenamePath = Paths.get(uploadDirectory(),filename);
			Files.deleteIfExists(filenamePath);
		}
		
	}
	@Override
	public byte[] getImage(long id) throws Exception {
		if(productRepository.findById(id).isPresent()) {
			Product product = productRepository.findById(id).get();
			String ProductImage = product.getProductImage();
			File file = new File(uploadDirectory()+"/"+ProductImage);
			Path path = Paths.get(file.toURI());
			return Files.readAllBytes(path);
		}
		return null;
	}
	@Override
	public byte[] uploadPhoto(MultipartFile file, Long id) throws Exception {
		if(productRepository.findById(id).isPresent()) {
			deletePhotoIfExist(id);
			String filename = id+ file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
			Product product = productRepository.findById(id).get();
			product.setProductImage(filename);
			Path filenamePath = Paths.get(uploadDirectory(),filename);
			Files.write(filenamePath, file.getBytes());
			productRepository.save(product);
			return getImage(id);
		}
		return null;
			
	}

	@Override
	public Page<Product> home(PageRequest PageRequest) {
		if(!shopRepository.findAll().isEmpty()) {
			List <Shop> shops = shopRepository.findAll();
			Shop shop = shops.get(new Random().nextInt(shops.size())); 
			Page<Product> products =  productRepository.findByShop(shop,PageRequest );
			return products;
		}
		
		return null;
	}
	@Override
	public Page<Product> searchByShop(UrlData urlData, PageRequest pageRequest) {
		if(shopRepository.findById(urlData.getIdShop()).isPresent()) {
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			return  productRepository.findByShopAndProductNameContains(shop,urlData.getSearch(),pageRequest);
		}
		return null;
	}		
	@Override
	public Brand addBrand(Brand brand) {
		return brandRepository.save(brand);
		
	}
	@Override
	public Brand updateBrand(Brand brand) {
		return brandRepository.save(brand);
		
	}
	@Override
	public Category addCategory(Category category) {
		return categoryRepository.save(category);
		
	}
	@Override
	public Category updateCategory(Category category) {
		return categoryRepository.save(category);
		
	}
	@Override
	public Product addProduct(Product product) {
			return productRepository.save(product);
	
	}
	@Override
	public Product updateProduct(Product product) {
		return productRepository.save(product);
		
	}
	@Override
	public boolean delateBrand(Long id) {
		if(brandRepository.findById(id).isPresent()) {
			brandRepository.delete(brandRepository.findById(id).get());
			return true;
		}
		return false;
	}
	@Override
	public boolean delateCategory(Long id) {
		if(categoryRepository.findById(id).isPresent()) {
			categoryRepository.delete(categoryRepository.findById(id).get());
			return true;
	    }
		return false;
	}
	@Override
	public boolean delateProduct(Long id) throws IOException {
		
		if(productRepository.findById(id).isPresent()) {
			itemProductRepository.findAll().forEach(itemProduct-> {
				if(itemProduct.getProduct().getIdProduct()==id ) {
					itemProductRepository.findAll().forEach(item->{
						if(itemProduct.equals(item)) {
							iOrderService.deleteOrder(item.getOrde().getIdOrde());
						}
					});
						
				}
				
			});
			deletePhotoIfExist(id);
		    productRepository.delete(productRepository.findById(id).get());
		    return true;
	    }
		return false;
	}
	@Override
	public Page<Product> listProduct(UrlData urlData,PageRequest pageRequest) {
		if(shopRepository.findById(urlData.getIdShop()).isPresent()) {
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			return productRepository.findByShop(shop,pageRequest);
		}
		return null;
	}
	@Override
	public Product getOneProduct(long id) {
		if(productRepository.findById(id).isPresent()) {
			return productRepository.findById(id).get();
		}
		return null;
	}
	@Override
	public Brand getOneBrand(long id) {
		if(brandRepository.findById(id).isPresent()) {
			return brandRepository.findById(id).get();
		}
		return null;
	}
	@Override
	public Category getOneCategory(long id) {
		if(categoryRepository.findById(id).isPresent()) {
			return categoryRepository.findById(id).get();
		}
		return null;
	}
	@Override
	public Page<Product> categoryShopProduct(UrlData urlData, PageRequest pageRequest) {
		if(shopRepository.findById(urlData.getIdShop()).isPresent()) {
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			Category category = categoryRepository.findById(urlData.getIdCat()).get();
			return productRepository.findByShopAndCategory(shop,category,pageRequest);
		}
		return null;
	}
	@Override
	public Page<Product> brandShopProduct(UrlData urlData, PageRequest pageRequest) {
		if(shopRepository.findById(urlData.getIdShop()).isPresent()) {
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			Brand brand = brandRepository.findById(urlData.getIdBrand()).get();
			return productRepository.findByShopAndBrand(shop,brand,pageRequest);
		}
		return null;
		
	}
	@Override
	public List<Brand> allShopBrand(long id) {
		if(shopRepository.findById(id).isPresent()) {
			List<Brand> brands = new ArrayList<>();
			Shop shop = shopRepository.findById(id).get();
			Page<Product>  products = productRepository.findByShop(shop, null);
			products.forEach(product->{
				Boolean find = false;
				for(Brand brand:brands) {
					if(product.getBrand().equals(brand)) {
						find = true;
						break;
					}
				}
				if(!find) {
					brands.add(product.getBrand());
				}
			});
			return brands;
		}
		return null;
	}
	@Override
	public List<Category> allShopCategory(long id) {
		if(shopRepository.findById(id).isPresent()) {
			List<Category> categories = new ArrayList<>();
			Shop shop = shopRepository.findById(id).get();
			Page<Product>  products = productRepository.findAllByShop(shop, null);
			products.forEach(product->{
				Boolean find = false;
				for(Category category:categories) {
					if(product.getCategory().equals(category)) {
						find = true;
						break;
					}
				}
				if(!find) {
					categories.add(product.getCategory());
				}
			});
			return categories;
		}
		return null;
	
	}
	@Override
	public boolean stockProduct(UrlData urlData) {
		if(productRepository.findById(urlData.getIdProduct()).isPresent()) {
			Product product = productRepository.findById(urlData.getIdProduct()).get();
			product.setProductQuantity(product.getProductQuantity()+urlData.getQte());
			productRepository.save(product);
			return true;
		}
		return false;
	}
	@Override
	public boolean destockProduct(UrlData urlData) {
		if(productRepository.findById(urlData.getIdProduct()).isPresent()) {
			Product product = productRepository.findById(urlData.getIdProduct()).get();
			if(product.getProductQuantity()>urlData.getQte()) {
				product.setProductQuantity(product.getProductQuantity()-urlData.getQte());
				productRepository.save(product);
				return true;
			}
			
		}
		return false;
	}

}
