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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
	@Column(length = 20)
	private String firstName;
	@NotNull(message = "username shouldn't be null")
	@Column(unique = true, length = 20,nullable = false)
	private String userName;
	@Column(unique = true, length = 30)
	@Email(message = "invalid email")
	private String email;
	private String userImage;
	//@NotBlank(message = "password shouldn't be empty")
	//@NotNull(message = "password shouldn't be null")
	//@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,}$",message = "password size not less then 5" )
	@JsonProperty(access = Access.WRITE_ONLY) 
	private String password;
	@Column(unique = true, length = 20)
	@Pattern(regexp ="^(\\+\\d{1,4}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",message = "please enter the valid number" )
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
