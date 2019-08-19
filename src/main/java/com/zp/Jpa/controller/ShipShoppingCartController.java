package com.zp.Jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.ShipShoppingCart;
import com.zp.Jpa.service.ShipShoppingCartService;
@RestController
@CrossOrigin
@RequestMapping(value = "/shipCart", name = "微信购物车模块")
public class ShipShoppingCartController {
	//public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public ShipShoppingCartService cartService;
	@RequestMapping(value = "/findCart", name = "根据用户id查询购物车")
	public Object findCart(Integer uid) {
	List<ShipShoppingCart> list=cartService.findCart(uid);
	return list;
	}
	@RequestMapping(value = "/addCart", name = "添加商品购物车")
	public Object addCart(ShipShoppingCart cart) {
	return	cartService.saveCart(cart);
	}
	@RequestMapping(value = "/deleteCart", name = "删除商品购物车")
	public Object deleteCart(Integer uid,Integer goodsId) {
		return	cartService.deleteCart(uid,goodsId);
	};
}
