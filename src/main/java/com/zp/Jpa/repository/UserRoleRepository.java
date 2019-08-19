package com.zp.Jpa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.sys.User_SysRole_tb;

public interface UserRoleRepository extends JpaRepository<User_SysRole_tb, Integer>,JpaSpecificationExecutor<User_SysRole_tb>{

	@Query(value="SELECT * FROM [dbo].[User_SysRole_tb] where uid =?1 and role_id=?2",nativeQuery=true)
	@Modifying
	@Transactional
	public List<User_SysRole_tb> getUserSysRole(Integer uid,Integer roleId);
}
