package com.zp.Jpa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.sys.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Integer>,JpaSpecificationExecutor<SysUser>{

	List<SysUser> findByUserNameLike(String userName);

	SysUser findByUserNameAndUserPassWord(String userName, String newPassWord);

	SysUser findByUserName(String userName);
//	@Query(value="select * from syspermission where permissionid = (SELECT permissionId FROM role_permission_tb WHERE roleId = ?1)", nativeQuery = true)
//	List<SysPermission> queryPermissionIdsByRoleIds(int roleId);
	
	@Query(value="select roleId from SysRole where roleId in (SELECT role_id FROM SysUser_SysRole_tb WHERE user_id = ?1)", nativeQuery = true)
	List<Integer> queryRoleIdByUserId(Integer userId);
	@Query(value="select permissionValue from SysPermission where permissionId in (SELECT permission_Id FROM SysRole_SysPermission_tb WHERE role_id in(SELECT role_id FROM SysUser_SysRole_tb WHERE user_id = ?1) )", nativeQuery = true)
	List<String> queryPermissionIdByUserId(Integer userId);
	//查询普通用户所拥有的角色
	@Query(value="select roleId from SysRole where roleId in (SELECT role_id FROM User_SysRole_tb WHERE uid = ?1)", nativeQuery = true)
	List<Integer> queryRoleIdByUserIdUsers(Integer userId);
	//查询普通用户所拥有的权限
	@Query(value="select permissionValue from SysPermission where permissionId in (SELECT permission_id FROM SysRole_SysPermission_tb WHERE role_id in(SELECT role_id FROM User_SysRole_tb WHERE uid = ?1) )", nativeQuery = true)
	List<String> queryPermissionIdByUserIdUsers(Integer userId);
	//查询普通用户所拥有的角色
		@Query(value="select roleName from SysRole where roleId in (SELECT role_id FROM User_SysRole_tb WHERE uid = ?1)", nativeQuery = true)
	List<String> queryRoleByUserId(Integer userId);
		
		//查询管理员用户所拥有的角色
				@Query(value="select roleName from SysRole where roleId in (SELECT role_id FROM SysUser_SysRole_tb WHERE user_id = ?1)", nativeQuery = true)
			List<String> queryRoleBySysUserId(Integer userId);
				
				//设置
				@Query(value="INSERT INTO SysUser_SysRole_tb (user_id,role_id) VALUES (?1,?2)",nativeQuery=true)
				@Modifying
				@Transactional
				public int setSysUserSysRole(Integer userId,Integer role);

				SysUser findByWeixinUid(Integer uid);

}
