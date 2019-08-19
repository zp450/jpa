package com.zp.Jpa.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipCabin;
import com.zp.Jpa.repository.ShipCabinRepository;
import com.zp.Jpa.service.ShipCabinService;
@Service
public class ShipCabinServiceImpl implements ShipCabinService{
	@Autowired
    private ShipCabinRepository shipCabinRepository;
	@Override
	public List<ShipCabin> findAll() {
		// TODO Auto-generated method stub
		return shipCabinRepository.findAll();
	}

	@Override
	public ShipCabin saveShipCabin(ShipCabin shipCabin) {
		// TODO Auto-generated method stub
		return shipCabinRepository.save(shipCabin);
	}

	@Override
	public List<ShipCabin> findBySidAndOuttime(Integer sid, Date outTime) {
		// TODO Auto-generated method stub
//		return shipCabinRepository.findBySidAndOuttime(sid,outTime);
		return null;
	}

	@Override
	public ShipCabin findByScid(Integer scid) {
		// TODO Auto-generated method stub
		return shipCabinRepository.findBySCid(scid);
	}

}
