package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Travel;

public interface TravelRepository extends JpaRepository<Travel, Integer>,JpaSpecificationExecutor<Travel>{


}
