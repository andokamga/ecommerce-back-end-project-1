package org.onLineShop.service;

import java.util.List;

import org.onLineShop.entity.Shop;
import org.onLineShop.entity.Town;
import org.onLineShop.entity.UserApp;
import org.onLineShop.service.from.UrlData;

public interface IShopService {
	public Town addTown(Town town);
	public Shop addShop(Shop shop);
	public boolean delateTown(Long id);
	public boolean delateShop(Long id);
	public Shop updateShop( Shop shop);
	public Shop getOneShop(long id);
	public Town updateTown(Town Town);
	public Town getOneTown(long id);
	public List<Town> getAllTown();
	public boolean addSellerToShop(UrlData urlData);
	public List<UserApp> allSellerOfShop(long idShop);
	public boolean renoveSellerToShop(UrlData urlData);
	public Town findTownByTownName(String TownName);
}
