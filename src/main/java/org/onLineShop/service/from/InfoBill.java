package org.onLineShop.service.from;

import org.onLineShop.entity.Orde;

import lombok.Data;
@Data
public class InfoBill {
	private String code;
	private String datePring;
	private String town;
	private String shop;
	private Orde orde;
}
