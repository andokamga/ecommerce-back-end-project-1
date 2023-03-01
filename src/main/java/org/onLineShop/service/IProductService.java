package org.onLineShop.service;

import java.io.IOException;
import java.util.List;

import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.Product;
import org.onLineShop.service.from.UrlData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
	public String uploadDirectory();
	public byte[] getImage(long id) throws Exception;
	public byte[] uploadPhoto(MultipartFile file,Long id) throws Exception;
	public Page<Product> home(PageRequest pageRequest);
	public Page<Product> searchByShop(UrlData urlData, PageRequest pageRequest);
	public Brand addBrand(Brand brand);
	public boolean delateBrand(Long id);
	public Brand updateBrand( Brand brand);
	public Category addCategory(Category category);
	public boolean delateCategory(Long id);
	public Category updateCategory(Category category);
	public Product addProduct(Product product);
	public boolean delateProduct(Long id)throws IOException;
	public Product updateProduct(Product product);
	public Page<Product> listProduct(UrlData urlData,PageRequest pageRequest);
	public Product getOneProduct(long id);
	public Brand getOneBrand(long id);
	public Category getOneCategory(long id);
	public Page<Product> categoryShopProduct(UrlData urlData, PageRequest pageRequest);
	public Page<Product> brandShopProduct(UrlData urlData,PageRequest pageRequest);
	public List<Brand> allShopBrand(long id);
	public List<Category> allShopCategory(long id);
	public boolean stockProduct(UrlData urlData);
	public boolean destockProduct(UrlData urlData);
	public Brand findBrandByBrandName(String brandName);
	public Category findCategoryByCategoryName(String categoryName);
 }