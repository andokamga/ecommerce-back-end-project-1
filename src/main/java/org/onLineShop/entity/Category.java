package org.onLineShop.entity;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Category  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategory;
	@NotNull(message = "category shouldn't be null")
	//@Column(unique = true, length = 20,nullable = false)
	private String categoryName;
	@OneToMany(mappedBy = "category")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Product> products;
}
