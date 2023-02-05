package org.onLineShop.entity;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
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
public class Shop {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idShop;
	private String shopName;
	private double longitude, latitude,attitude;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Town town;
	@OneToMany(mappedBy = "shop",cascade = CascadeType.REMOVE)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<ShopSeller> sellers;
	@OneToMany(mappedBy = "shop")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Product> products ;
	

}
