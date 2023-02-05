package org.onLineShop.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class ShopSeller {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idShopSeller;
	@OneToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private UserApp seller;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Shop shop;
	private List<String> workDay = new ArrayList<>();

}
