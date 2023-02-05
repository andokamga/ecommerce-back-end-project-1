package org.onLineShop.entity;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

@Projection(name ="o",types = Orde.class)
public interface OrderProjection {
	public Long getIdOrde();
	public Date getOrderDate();
	public double getOrderPrice();
	public EnumStatus getStatus();
	public  Payment getPayement();
	public Collection<ItemProduct> getItemProducts();
	public UserApp getUserApp();

}
