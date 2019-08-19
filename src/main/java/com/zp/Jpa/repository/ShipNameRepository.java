package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipName;

public interface ShipNameRepository extends JpaRepository<ShipName, Integer>,JpaSpecificationExecutor<ShipName>{

	List<ShipName> findByNameLike(String shipName);

}
