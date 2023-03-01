package org.onLineShop.service.from;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UrlData {
	private Long idUser;
	private Long idRole;
	private Long idShop;
	private Long idProduct;
	private Long idCat;
	private Long idBrand;
	private String search;
	private int qte;
	private String Username;
	@NotNull
	private int page;
	@NotNull
	private int size;
}
