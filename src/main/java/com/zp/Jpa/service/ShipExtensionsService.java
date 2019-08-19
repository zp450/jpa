package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.ShipExtensions;

public interface ShipExtensionsService {
  List<ShipExtensions> selectBySid(Integer sid);
  ShipExtensions save(ShipExtensions shipExtensions);
  
}
