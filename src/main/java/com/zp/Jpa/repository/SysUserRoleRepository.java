package com.zp.Jpa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.sys.SysUser_SysRole_tb;

public interface SysUserRoleRepository extends JpaRepository<SysUser_SysRole_tb, Integer>,JpaSpecificationExecutor<SysUser_SysRole_tb>{
	@Query(value="SELECT * FROM [dbo].[SysUser_SysRole_tb] where user_id =?1 and role_id=?2",nativeQuery=true)
	@Modifying
	@Transactional
	public List<SysUser_SysRole_tb> getSysUserSysRole(Integer userId,Integer roleId);
}
