package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.ShipOrderPeople;

public interface ShipOrderPeopleService {
	List<ShipOrderPeople> findByOrderCn(String orderCn);
}
