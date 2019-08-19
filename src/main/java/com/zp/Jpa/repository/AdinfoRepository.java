package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Adinfo;

public interface AdinfoRepository extends JpaRepository<Adinfo, Integer>,JpaSpecificationExecutor<Adinfo>{
//	@Query(value="select * from syspermission where permissionid = (SELECT permissionId FROM role_permission_tb WHERE roleId = ?1)", nativeQuery = true)
//	List<SysPermission> queryPermissionIdsByRoleIds(int roleId);
}
