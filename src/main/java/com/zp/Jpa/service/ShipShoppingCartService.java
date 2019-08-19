package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.ShipShoppingCart;



public interface ShipShoppingCartService {
	List<ShipShoppingCart> findCart(Integer uid);
  ShipShoppingCart saveCart(ShipShoppingCart cart);
  Integer deleteCart(Integer uid,Integer goodsId);
  
}
