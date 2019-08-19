package com.zp.Jpa.service;

import java.util.Date;
import java.util.List;

import com.zp.Jpa.entity.ShipPrice;



public interface ShipPriceService {
	List<ShipPrice> selectBySidAndOutTime(Integer sid,Date outTime);
	List<ShipPrice> findAll();
	ShipPrice save(ShipPrice shipPrice);
}
