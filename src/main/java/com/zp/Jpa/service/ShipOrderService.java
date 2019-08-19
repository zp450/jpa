package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.search.ShipOrderSerach;



public interface ShipOrderService {
  ShipOrder saveShipOrder(ShipOrder shipOrder);
  ShipOrder save(ShipOrder shipOrder);
  ShipOrder findShipOrder(String sOid);
List<ShipOrder> findByOrderCn(String orderNo);
 Integer addShioOrderPeopleInfo(ShipOrder shipOrder ,ShipOrder shipOrder2);

 
 Page<ShipOrder> queryShipOrder(Integer page, Integer size,ShipOrderSerach search);
 
 ShipOrder deleteShipOrder(String sOid) ;
 void removeShipOrder(String sOid) ;
 
}
