package org.onLineShop.dao;

import org.onLineShop.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	UserRole findByUserRoleNameContains(String userName);
}
