package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.ShipOrder;

public interface ShipOrderRepository extends JpaRepository<ShipOrder, String>,JpaSpecificationExecutor<ShipOrder>{
	@Query(value = "   select * from shiporder where ordercn =?1",nativeQuery = true)
	List<ShipOrder> findByOrderCn(String orderNo);

	

}
