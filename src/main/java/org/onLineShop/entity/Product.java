package org.onLineShop.entity;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Product  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProduct;
	private String productName;
	private String description;
	private String productImage;
	private double productPrice;
	private boolean available;
	private double reductionPrince;
	private int like;
	private	int unLike;
	private int productQuantity;
	@ManyToOne
	//@JsonProperty(access = Access.WRITE_ONLY)
	private Shop shop;
	@ManyToOne
	//@JsonProperty(access = Access.WRITE_ONLY)
	private Category category;
	@ManyToOne
	//@JsonProperty(access = Access.WRITE_ONLY)
	private Brand brand;
	@OneToMany(mappedBy = "product")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<ItemProduct> itemProducts;
}
