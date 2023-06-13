package org.onLineShop.service.from;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class OrderForme{
	private Long idUser;
	private double totalPrince;
	private List<OrdeLine> ordeLine= new ArrayList<>();
	private Client client;
}