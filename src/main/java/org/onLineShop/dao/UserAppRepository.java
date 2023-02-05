package org.onLineShop.dao;

import org.onLineShop.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface UserAppRepository extends JpaRepository<UserApp, Long>{

	UserApp findByUserName(String username);

}
