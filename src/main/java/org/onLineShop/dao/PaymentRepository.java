package org.onLineShop.dao;

import org.onLineShop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin("*")
@RepositoryRestResource
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
