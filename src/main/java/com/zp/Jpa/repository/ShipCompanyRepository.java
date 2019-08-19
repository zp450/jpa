package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.ShipCompany;

public interface ShipCompanyRepository extends JpaRepository<ShipCompany, Integer>,JpaSpecificationExecutor<ShipCompany>{


}
