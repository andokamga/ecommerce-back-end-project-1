package org.onLineShop.entity;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Brand /*implements Serializable*/ {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idBrand;
	private String brandName;
	@OneToMany(mappedBy = "brand")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Product> products;

}
