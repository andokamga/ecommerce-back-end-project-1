package org.onLineShop.dao;

import org.onLineShop.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface TownRepository extends JpaRepository<Town, Long>{

	Town findByTownName(String townName);

}
