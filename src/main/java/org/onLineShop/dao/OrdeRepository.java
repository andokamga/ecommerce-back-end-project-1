package org.onLineShop.dao;

import org.onLineShop.entity.Orde;
import org.onLineShop.entity.UserApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface OrdeRepository extends JpaRepository<Orde, Long> {

	Page<Orde> findByUserApp(UserApp userApp, PageRequest pageRequest);
	
}
