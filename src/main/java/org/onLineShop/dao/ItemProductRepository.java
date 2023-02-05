package org.onLineShop.dao;

import org.onLineShop.entity.ItemProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface ItemProductRepository extends JpaRepository<ItemProduct, Long> {

}
