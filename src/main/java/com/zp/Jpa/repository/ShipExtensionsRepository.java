package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipExtensions;


public interface ShipExtensionsRepository extends JpaRepository<ShipExtensions, Integer>,JpaSpecificationExecutor<ShipExtensions>{

	

	List<ShipExtensions> findBySid(Integer sid);

}
