package org.onLineShop.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity 
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Orde  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrde;
	@Temporal(TemporalType.DATE)
	private Date orderDate;
	private Date limitDate;
	private double orderPrice;
	private EnumStatus status;
	private String name;
	private String  email ;
	private String address;
	private String phoneNumber;
	@OneToOne(mappedBy = "orde",cascade = CascadeType.REMOVE)
	private  Payment payement;
	@OneToMany(mappedBy = "orde",cascade = CascadeType.REMOVE)
	//@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<ItemProduct> itemProducts=new ArrayList<>();
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private UserApp userApp;

}
