package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.ShipRoute;
import com.zp.Jpa.entity.search.ShipRouteSearch;

public interface ShipRouteService {
	ShipRoute addShipRoute(ShipRoute shipRoute);// 增

	Integer deleteShipRoute(Integer sRid );// 删

	ShipRoute saveShipRoute(ShipRoute shipRoute);// 改

	Page<ShipRoute> queryShipRoute(Integer page, Integer size, ShipRouteSearch search);// 查
	
	List<ShipRoute> findAll();
}
