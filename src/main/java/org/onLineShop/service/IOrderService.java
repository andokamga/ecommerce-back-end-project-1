package org.onLineShop.service;

import org.onLineShop.entity.Orde;
import org.onLineShop.service.from.OrderForme;
import org.onLineShop.service.from.UrlData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
	public boolean deleteOrder(long id);
	public Orde addOrder( OrderForme orderForme );
	public Page<Orde> listOrdeUser(Long id,PageRequest PageRequest);
	public Orde getOneOrder(long id);
	public Page<Orde> getShopOrder(UrlData urlData,PageRequest PageRequest);
	public void deleteNotPaidOrder();
}

