package com.zp.Jpa.service;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Ship;
import com.zp.Jpa.entity.search.ShipSearch;

public interface ShipService {
	Page<Ship> queryShip(Integer page, Integer limit,ShipSearch search);
	Ship saveShip(Ship ship);
	Ship findShip(Integer sid);
	Map<String, Object> selectShipRoomBySidAndOutTime(Integer sid, Date outTime);
}
