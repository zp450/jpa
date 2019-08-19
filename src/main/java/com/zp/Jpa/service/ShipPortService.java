package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.ShipPort;
import com.zp.Jpa.entity.search.ShipPortSearch;

public interface ShipPortService {
	ShipPort addShipPort(ShipPort shipPort);// 增

	Integer deleteShipPort(Integer sPid );// 删

	ShipPort saveShipPort(ShipPort shipPort);// 改

	Page<ShipPort> queryShipPort(Integer page, Integer size, ShipPortSearch search);// 查
	
	List<ShipPort> findAll();
}
