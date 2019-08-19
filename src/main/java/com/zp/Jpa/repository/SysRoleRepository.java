package com.zp.Jpa.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.sys.SysRole;

public interface SysRoleRepository extends JpaRepository<SysRole, Integer>,JpaSpecificationExecutor<SysRole>{
	//设置之前先删除  本角色关联的所有权限
		@Query(value="DELETE FROM SysRole_SysPermission_tb WHERE role_id=?1",nativeQuery=true)
		@Modifying
		@Transactional
		public int deleteByRoleId(Integer roleId);
		
		//设置
		@Query(value="INSERT INTO SysRole_SysPermission_tb (role_id,permission_id) VALUES (?1,?2)",nativeQuery=true)
		@Modifying
		@Transactional
		public int setRoleModule(Integer roleId,Integer permissionId);

		public SysRole findByRoleName(String roleName);
		
		

}
