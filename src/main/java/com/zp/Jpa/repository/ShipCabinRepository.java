package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipCabin;

public interface ShipCabinRepository extends JpaRepository<ShipCabin, Integer>,JpaSpecificationExecutor<ShipCabin>{
	
	//List<ShipCabin> findBySidAndOuttime(Integer sid, Date outTime);

	ShipCabin findBySCid(Integer scid);

}
