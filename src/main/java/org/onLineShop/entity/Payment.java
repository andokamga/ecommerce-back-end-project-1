package org.onLineShop.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString 
public class Payment  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPayment;
	private String paymentRef;
	private String paymentOperator;
	@Temporal(TemporalType.DATE)
	private Date paymentDate;
	@OneToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Orde orde;
}
