package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipOrderPeople;

public interface ShipOrderPeopleRepository extends JpaRepository<ShipOrderPeople, Integer>,JpaSpecificationExecutor<ShipOrderPeople>{

	List<ShipOrderPeople> findByOrderCn(String orderCn);

}
