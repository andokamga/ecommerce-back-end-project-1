package org.onLineShop.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
//@Table(name = "userRole")
public class UserRole {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUserRole;
	@Column(name = "role_name",unique = true, length =10)
	private String userRoleName;
	@ManyToMany(fetch = FetchType.EAGER)
	//@JoinTable(name = "userAppRole")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<UserApp> userApps = new ArrayList<>();
}
