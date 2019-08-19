package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>,JpaSpecificationExecutor<Order>{

	List<Order> findByUidAndOrderType(Integer uid, Integer orderType);

	Object findByOid(String oid);

	List<Order> findByUid(Integer uid);
	
	

}
