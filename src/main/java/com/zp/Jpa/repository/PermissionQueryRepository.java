package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.sys.PermissionQuery;

public interface PermissionQueryRepository extends JpaRepository<PermissionQuery, Integer>,JpaSpecificationExecutor<PermissionQuery>{

}
