package com.zp.Jpa.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Order;
import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.search.OrderSerach;

public interface OrderService {
   //微信爱邮卡充值订单,直接下单加充值
	Order addWeixinAiyouOrder(Order order, String ip);
	//
	Object balanceZhifu(Integer uid,String oid);
	
	Page<Order> queryOrder(Integer page, Integer size,OrderSerach search);
	
	Order deleteOrder(String oid);
	
	Map<String, Object> addOrderForShipOrder(ShipOrder shipOrder,ShipOrder shipOrder2);
	
	Order save(Order order);
}
