package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.GroupOrder;

public interface GroupOrderRepository extends JpaRepository<GroupOrder, String>,JpaSpecificationExecutor<GroupOrder>{

}
