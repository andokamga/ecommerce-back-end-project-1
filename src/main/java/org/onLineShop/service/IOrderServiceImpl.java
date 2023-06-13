package org.onLineShop.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import org.onLineShop.dao.ItemProductRepository;
import org.onLineShop.dao.OrdeRepository;
import org.onLineShop.dao.ProductRepository;
import org.onLineShop.dao.ShopRepository;
import org.onLineShop.dao.ShopSellerRepository;
import org.onLineShop.dao.UserAppRepository;
import org.onLineShop.entity.Brand;
import org.onLineShop.entity.EnumStatus;
import org.onLineShop.entity.ItemProduct;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Product;
import org.onLineShop.entity.Shop;
import org.onLineShop.entity.ShopSeller;
import org.onLineShop.entity.UserApp;
import org.onLineShop.service.from.OrderForme;
import org.onLineShop.service.from.UrlData;
import org.onLineShop.service.from.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class IOrderServiceImpl implements IOrderService {
	@Autowired
	public ShopRepository shopRepository;
	@Autowired
	public ItemProductRepository itemProductRepository;
	@Autowired
	public UserAppRepository userAppRepository;
	@Autowired
	public ProductRepository productRepository;
	@Autowired
	public OrdeRepository ordeRepository;
	@Autowired
	public ShopSellerRepository ShopSellerRepository;
	@Override
	public boolean deleteOrder(long id) {
		if(ordeRepository.findById(id).isPresent()) {
			Orde orde = ordeRepository.findById(id).get();
			ordeRepository.delete(orde);
			return true;
		}
		return false;
	}

	@Override
	public Orde addOrder(OrderForme orderForme) {
		Orde orde = new Orde();
		Timer timer = new Timer();
		LocalDateTime startTime = LocalDateTime.now().plusSeconds(10000);
		Date start = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				ordeRepository.findAll().forEach(ord->{
					if(orde.getIdOrde().equals(ord.getIdOrde())&&ord.getStatus().equals(EnumStatus.NOPAID)) {
						ordeRepository.delete(ord);
					}
				});
							
			}
			
		};
	
		orde.setOrderDate(new Date());
		orde.setStatus(EnumStatus.NOPAID);
		orde.setOrderPrice(orderForme.getTotalPrince());
		orde.setName(orderForme.getClient().getName());
		orde.setAddress(orderForme.getClient().getAddress());
		orde.setPhoneNumber(orderForme.getClient().getPhoneNumber());
		orde.setEmail(orderForme.getClient().getEmail());
		orde.setUserApp(userAppRepository.findById(orderForme.getIdUser()).get());
		ordeRepository.save(orde);
		orderForme.getOrdeLine().forEach(ordeLine->{
			ItemProduct itemProduct = new ItemProduct();
			Product product = productRepository.findById(ordeLine.getId()).get();
			itemProduct.setOrde(orde);
			itemProduct.setProduct(product);
			itemProduct.setQuatity(ordeLine.getQuatity());
			product.setProductQuantity(product.getProductQuantity()-ordeLine.getQuatity());
			itemProductRepository.save(itemProduct);
		});
	
		timer.schedule(task, start);
		return orde;
	}
	@Override
	public Page<Orde> listOrdeUser(Long id,PageRequest PageRequest) {
		UserApp userApp = userAppRepository.findById(id).get();
		return ordeRepository.findByUserApp(userApp,PageRequest);
				
	}

	@Override
	public Orde getOneOrder(long id) {
		if(ordeRepository.findById(id).isPresent()) {
			return ordeRepository.findById(id).get();
		}
		return null;
	}

	@Override
	public Page<Orde> getShopOrder(UrlData urlData,PageRequest PageRequest) {
		if(userAppRepository.findById(urlData.getIdUser()).isPresent()&&shopRepository.findById(urlData.getIdShop()).isPresent()) {
			UserApp user = userAppRepository.findById(urlData.getIdUser()).get();
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			List<Orde> ordes = new ArrayList<>();
			/*for(ShopSeller shopSeller:ShopSellerRepository.findAll()) {
				if(shopSeller.getShop().equals(shop)&&shopSeller.getSeller().equals(user)) {
					for(ItemProduct item: itemProductRepository.findAll()) {
						if(item.getProduct().getShop().equals(shop)) {
							ordes.add(item.getOrde());
						}
					}
				}
				return new PageImpl<Orde>(ordes, PageRequest, ordes.size());
			}*/
			/*Stream<ItemProduct> stream = itemProductRepository.findAll().stream();
			List<ItemProduct> ord =stream.filter(ite->ite.getProduct().getShop().getShopName()=="").toList();*/
					for(ItemProduct item: itemProductRepository.findAll()) {
						if(item.getProduct().getShop().equals(shop)) {
							Boolean find = false;
							for(Orde orde:ordes) {
								if(orde.equals(item.getOrde())) {
									find = true;
									//break;
								}
							}
							if(!find) {
								ordes.add(item.getOrde());
							}
						}
					}
				PagedListHolder<Orde> pagedListHolder = new PagedListHolder<Orde>(ordes);
				pagedListHolder.setSort(new MutableSortDefinition("orderDate", true,true));
				pagedListHolder.resort();
				pagedListHolder.setPage(PageRequest.getPageNumber());
				pagedListHolder.setPageSize(PageRequest.getPageSize());
				List<Orde> ordesSlice = pagedListHolder.getPageList();
				//PropertyComparator.sort(ordesSlice, new MutableSortDefinition("orderDate", true,true));
				return new PageImpl<Orde>(ordesSlice, PageRequest, ordes.size());
			
		}	
	
		return null;
	}

	@Override
	public void deleteNotPaidOrder() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(!ordeRepository.findAll().isEmpty()) {
					for(Orde orde:ordeRepository.findAll()) {
						if(orde.getLimitDate().compareTo(new Date())>0 && orde.getStatus().equals(EnumStatus.NOPAID)) {
							ordeRepository.delete(orde);
						}
						
					}
				}	
							
			}
			
		};
		timer.schedule(task,UtilDate.START_TIME ,UtilDate.REHEARSAL_TIME);
	}

}

