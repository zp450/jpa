package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.ShipDayPrice;

public interface ShipDayPriceService {
  List<ShipDayPrice> selectBySid(Integer sid);
  ShipDayPrice save(ShipDayPrice shipDayPrice);
}
