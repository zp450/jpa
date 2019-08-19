package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Distributor;

public interface DistributorRepository extends JpaRepository<Distributor, Integer>,JpaSpecificationExecutor<Distributor>{

}
