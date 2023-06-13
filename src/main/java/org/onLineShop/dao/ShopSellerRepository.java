package org.onLineShop.dao;

import org.onLineShop.entity.ShopSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin("*")
@RepositoryRestResource
public interface ShopSellerRepository extends JpaRepository<ShopSeller, Long> {

}
