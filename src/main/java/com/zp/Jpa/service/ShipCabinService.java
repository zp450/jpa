package com.zp.Jpa.service;



import java.util.Date;
import java.util.List;

import com.zp.Jpa.entity.ShipCabin;



public interface ShipCabinService {
	List<ShipCabin> findAll();
	ShipCabin saveShipCabin(ShipCabin shipCabin);
	List<ShipCabin> findBySidAndOuttime(Integer sid ,Date outTime);
	ShipCabin findByScid(Integer scid);
}
