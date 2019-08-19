package com.zp.Jpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipOrderPeople;
import com.zp.Jpa.repository.ShipOrderPeopleRepository;
import com.zp.Jpa.service.ShipOrderPeopleService;
@Service
public class ShipOrderPeopleServiceImpl implements ShipOrderPeopleService{
	@Autowired
	private ShipOrderPeopleRepository sopRepository;
       public List<ShipOrderPeople> findByOrderCn(String orderCn){
    	   return sopRepository.findByOrderCn(orderCn);
       }
}
