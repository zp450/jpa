package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.ShipPrice;

public interface ShipPriceRepository extends JpaRepository<ShipPrice, Integer>,JpaSpecificationExecutor<ShipPrice>{
	@Query(value = " select * from ShipPrice where LPid in (select lpid from ShipDayPrice where Sid = ?1 and OutTime = ?2)",nativeQuery = true)
	List<ShipPrice> selectBySidAndOutTime(Integer sid, String outTime);

}
