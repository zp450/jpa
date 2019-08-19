package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipDayPrice;


public interface ShipDayPriceRepository extends JpaRepository<ShipDayPrice, Integer>,JpaSpecificationExecutor<ShipDayPrice>{

	List<ShipDayPrice> findBySid(Integer sid);

}
