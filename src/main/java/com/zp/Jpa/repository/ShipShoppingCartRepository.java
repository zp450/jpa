package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.zp.Jpa.entity.ShipShoppingCart;

public interface ShipShoppingCartRepository extends JpaRepository<ShipShoppingCart, Integer>,JpaSpecificationExecutor<ShipShoppingCart>{
	@Transactional
    @Modifying
    @Query(value = "delete from ShipShoppingCart where uid =?1 and goodsId =?2",nativeQuery = true)
	Integer deleteByUidAndGoodsId(Integer uid, Integer goodsId);

}
