package com.zp.Jpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipExtensions;
import com.zp.Jpa.repository.ShipExtensionsRepository;
import com.zp.Jpa.service.ShipExtensionsService;
@Service
public class ShipExtensionsServiceImpl implements ShipExtensionsService{
@Autowired
private ShipExtensionsRepository shipExtensionsRepository;
@Override
public List<ShipExtensions> selectBySid(Integer sid) {
	// TODO Auto-generated method stub
	return shipExtensionsRepository.findBySid(sid);
}

	@Override
	public ShipExtensions save(ShipExtensions shipExtensions) {
		// TODO Auto-generated method stub
		return shipExtensionsRepository.save(shipExtensions);
	}

	

}
