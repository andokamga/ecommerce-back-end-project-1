package org.onLineShop.dao;

import org.onLineShop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface BrandRepository extends JpaRepository<Brand, Long> {
	Brand findByBrandName(String brandName);

}
