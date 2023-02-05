package org.onLineShop.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
//@Table(name = "userApp")
public class UserApp {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUserApp;
	@Column(unique = true, length = 20)
	private String userName;
	@Column(unique = true, length = 20)
	private String email;
	private String userImage;
	@JsonProperty(access = Access.WRITE_ONLY) 
	private String password;
	private String phoneNumber;
	private boolean active;
	@OneToOne(mappedBy = "seller",cascade = CascadeType.REMOVE)
	private ShopSeller shopSeller;
	@OneToMany(mappedBy = "userApp",cascade = CascadeType.REMOVE)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Orde> ordes;
	@ManyToMany(mappedBy = "userApps",fetch = FetchType.EAGER)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<UserRole> userRoles=new ArrayList<>();
}
