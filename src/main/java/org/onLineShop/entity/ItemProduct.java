package org.onLineShop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name = "itemProduct")
public class ItemProduct /*implements Serializable*/ {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idItemProduct;
	@ManyToOne
	private Product product;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Orde orde;
	private int quatity;
	
}
