package com.zp.Jpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipDayPrice;
import com.zp.Jpa.repository.ShipDayPriceRepository;
import com.zp.Jpa.service.ShipDayPriceService;
@Service
public class ShipDayPriceServiceImpl implements ShipDayPriceService{
@Autowired
private ShipDayPriceRepository shipDayPriceRepository;
	
	
	@Override
	public List<ShipDayPrice> selectBySid(Integer sid) {
		// TODO Auto-generated method stub
		return shipDayPriceRepository.findBySid(sid);
	}

	@Override
	public ShipDayPrice save(ShipDayPrice shipDayPrice) {
		// TODO Auto-generated method stub
		return shipDayPriceRepository.save(shipDayPrice);
	}

}
