package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.ShipCompany;
import com.zp.Jpa.entity.search.ShipCompanySearch;

public interface ShipCompanyService {
	ShipCompany addShipCompany(ShipCompany shipCompany);// 增

	Integer deleteShipCompany(Integer sCid );// 删

	ShipCompany saveShipCompany(ShipCompany shipCompany);// 改

	Page<ShipCompany> queryShipCompany(Integer page, Integer size, ShipCompanySearch search);// 查
	
	List<ShipCompany> findAll();
}
