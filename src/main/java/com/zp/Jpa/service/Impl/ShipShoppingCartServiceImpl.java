package com.zp.Jpa.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipShoppingCart;
import com.zp.Jpa.repository.ShipShoppingCartRepository;
import com.zp.Jpa.service.ShipShoppingCartService;



@Service
public class ShipShoppingCartServiceImpl implements ShipShoppingCartService{
@Autowired
private ShipShoppingCartRepository  cartRepository;
	@Override
	public List<ShipShoppingCart> findCart(Integer uid) {
		List<Integer> ids=new ArrayList<>();
		ids.add(uid);
		// TODO Auto-generated method stub
		return cartRepository.findAll(ids);
	}

	@Override
	public ShipShoppingCart saveCart(ShipShoppingCart cart) {
		// TODO Auto-generated method stub
		return cartRepository.save(cart);
	}

	@Override
	public Integer deleteCart(Integer uid, Integer goodsId) {
		// TODO Auto-generated method stub
		return cartRepository.deleteByUidAndGoodsId(uid,goodsId);
	}

}
