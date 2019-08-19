package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.sys.SysPermission;


public interface PermissionRepository extends JpaRepository<SysPermission, Integer>,JpaSpecificationExecutor<SysPermission>{
	@Query(value="select * from syspermission where permissionid = (SELECT permissionId FROM role_permission_tb WHERE roleId = ?1)", nativeQuery = true)
	List<SysPermission> queryPermissionIdsByRoleIds(int roleId);
	@Query(value="SELECT permissionValue FROM SysPermission", nativeQuery = true)
	List<String> findAllPermissionValue();
//	@Query(value=" SELECT  "+
//	"p.permissionid  as id,"+
//			"p.permissionMoudle as label "+
//	" p.childrens as SysPermisssionTree FROM SysPermission as p", nativeQuery = true)
//	List<SysPermisssionTree> findAllBydistinctModule();

	List<SysPermission> findByPermissionModule(String permissionModule);
	@Query(value="SELECT  distinct permissionModule   FROM SysPermission", nativeQuery = true)
	List<String> findModuleName();
	

	

}
