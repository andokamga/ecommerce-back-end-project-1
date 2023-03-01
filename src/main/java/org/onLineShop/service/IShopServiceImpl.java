package org.onLineShop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.onLineShop.dao.ProductRepository;
import org.onLineShop.dao.ShopRepository;
import org.onLineShop.dao.ShopSellerRepository;
import org.onLineShop.dao.TownRepository;
import org.onLineShop.dao.UserAppRepository;
import org.onLineShop.entity.Shop;
import org.onLineShop.entity.ShopSeller;
import org.onLineShop.entity.Town;
import org.onLineShop.entity.UserApp;
import org.onLineShop.service.from.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class IShopServiceImpl implements IShopService {
	@Autowired
	public ShopSellerRepository shopSellerRepository;
	@Autowired
	public UserAppRepository userAppRepository;
	@Autowired
	public ProductRepository productRepository;
	@Autowired
	public TownRepository townRepository;
	@Autowired
	public ShopRepository shopRepository;
	@Autowired
	public IProductService iProductService;
	@Override
	public Town addTown(Town town) {
		if(findTownByTownName(town.getTownName())!=null) {
			return null;
		}
		return townRepository.save(town);
		
	}

	@Override
	public Shop addShop(Shop shop) {
		return shopRepository.save(shop);
		
	}

	@Override
	public boolean delateTown(Long id) {
		if(townRepository.findById(id).isPresent()) {
			shopRepository.findAll().forEach(shop->{
				if(shop.getTown().getIdTown()==id) {
					delateShop(shop.getIdShop());
				}
			});
			townRepository.delete(townRepository.findById(id).get());
			return true;
		}
		return false;
	}

	@Override
	public boolean delateShop(Long id) {
		if(shopRepository.findById(id).isPresent()) {
			productRepository.findAll().forEach(product->{
				if(product.getShop().getIdShop()==id) {
					try {
						iProductService.delateProduct(product.getIdProduct());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			shopRepository.delete(shopRepository.findById(id).get());
			return true;
		}
		return false;
	}

	@Override
	public Shop updateShop(Shop shop) {
		
		return shopRepository.save(shop);
	}

	@Override
	public Shop getOneShop(long id) {
		if(shopRepository.findById(id).isPresent()) {
			return shopRepository.findById(id).get();
		}
		return null;
	}

	@Override
	public Town updateTown(Town town) {
		if(findTownByTownName(town.getTownName())!=null) {
			return null;
		}
		return townRepository.save(town);
	}

	@Override
	public Town getOneTown(long id) {
		if(townRepository.findById(id).isPresent()) {
			return townRepository.findById(id).get();
		}
		
		return null;
	}

	@Override
	public List<Town> getAllTown() {
		return townRepository.findAll();
	}

	@Override
	public boolean addSellerToShop(UrlData urlData) {
		if(userAppRepository.findById(urlData.getIdUser()).isPresent()) {
			ShopSeller shopSeller = new ShopSeller();
			UserApp seller = userAppRepository.findById(urlData.getIdUser()).get();
			Shop shop = shopRepository.findById(urlData.getIdShop()).get();
			shopSeller.setSeller(seller);
			shopSeller.setShop(shop);
			shopSellerRepository.save(shopSeller);
			return true;
		}
		return false;
	}

	@Override
	public List<UserApp> allSellerOfShop(long idShop) {
		List<UserApp> seller = new ArrayList<>();
		for(ShopSeller shopSeller:shopSellerRepository.findAll()) {
			if(shopSeller.getShop().getIdShop()==idShop) {
				seller.add(shopSeller.getSeller());
				return seller;
			}
			
		}
		return null;
	}

	@Override
	public boolean renoveSellerToShop(UrlData urlData) {
		for(ShopSeller seller : shopSellerRepository.findAll()) {
			if(seller.getShop().getIdShop()==urlData.getIdShop()&&seller.getSeller().getIdUserApp()==urlData.getIdUser()) {
				shopSellerRepository.delete(seller);
				return true;
			}
		}
		return false;
	}

	@Override
	public Town findTownByTownName(String TownName) {
			return townRepository.findByTownName(TownName);
	}

}
