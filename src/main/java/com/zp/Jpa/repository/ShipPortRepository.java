package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipPort;

public interface ShipPortRepository extends JpaRepository<ShipPort, Integer>,JpaSpecificationExecutor<ShipPort>{

}
