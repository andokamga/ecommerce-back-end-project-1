package org.onLineShop.dao;

import org.onLineShop.entity.Brand;
import org.onLineShop.entity.Category;
import org.onLineShop.entity.Product;
import org.onLineShop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin("*")
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product>findByShop(Shop shop,Pageable page);

	Page<Product> findAllByShop(Shop shop, PageRequest pageRequest);

	Page<Product> findByShopAndProductNameContains(Shop shop, String search,Pageable page);

	Page<Product> findByShopAndBrand(Shop shop, Brand brand, Pageable page);

	Page<Product> findByShopAndCategory(Shop shop, Category category, Pageable page);
}
