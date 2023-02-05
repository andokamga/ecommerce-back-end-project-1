package org.onLineShop.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.onLineShop.dao.ItemProductRepository;
import org.onLineShop.dao.OrdeRepository;
import org.onLineShop.dao.ProductRepository;
import org.onLineShop.dao.UserAppRepository;
import org.onLineShop.entity.ItemProduct;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Product;
import org.onLineShop.service.from.InfoBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class IBillServiceImpl implements IBillService {
	@Autowired
	public ItemProductRepository itemProductRepository;
	@Autowired
	public UserAppRepository userAppRepository;
	@Autowired
	public ProductRepository productRepository;
	@Autowired
	public OrdeRepository ordeRepository;
	@Override
	public InfoBill billOrde(long id) {
		InfoBill infoBill = new InfoBill();
		LocalDateTime time =  LocalDateTime.now();
		DateTimeFormatter dateprint = DateTimeFormatter.ofPattern("dd MMM YYYY - HH:mm");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
		String formadate = formatter.format(time);
		if(ordeRepository.findById(id).isPresent()) {
			Collection<ItemProduct> items = itemProductRepository.findAll();
			for(int i=1; i<items.size(); i++) {
				if(itemProductRepository.findById((long) i).isPresent()) {
					ItemProduct item = itemProductRepository.findById((long) i).get();
					if(item.getOrde().getIdOrde()==id) {
						Orde orde = ordeRepository.findById(id).get();
						Product product = item.getProduct();
						String shop = product.getShop().getShopName();
						String town = product.getShop().getTown().getTownName();
						infoBill.setShop(shop);
						infoBill.setTown(town);
						infoBill.setOrde(orde);
						infoBill.setCode((formadate+shop.toUpperCase()+orde.getIdOrde()).replaceAll(" ",""));
						infoBill.setDatePring(dateprint.format(time));
						break;
					}
					
				}
			}
		}
		return  infoBill;
		
	}

}
