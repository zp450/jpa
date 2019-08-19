package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Ship;

public interface ShipRepository extends JpaRepository<Ship, Integer>,JpaSpecificationExecutor<Ship>{


}
