package org.onLineShop.entity;

import java.util.Collection;

import jakarta.persistence.Column;
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
public class Town  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idTown;
	@NotNull(message = "town shouldn't be null")
	@Column(unique = true, length = 20,nullable = false)
	private String townName;
	private int shopNumbers;
	@OneToMany(mappedBy = "town")
	//@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Shop> shops;

}
