package com.zp.Jpa.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.search.SysRoleSearch;
import com.zp.Jpa.entity.sys.SysRole;

public interface SysRoleService {
	Map<String, Object> addSysRole(SysRole sysRole);// 增

	Integer deleteSysRole(Integer roleId );// 删

	Map<String, Object> saveSysRole(SysRole sysRole);// 改

	Page<SysRole> querySysRole(Integer page, Integer size, SysRoleSearch search);// 查
	
	//给角色设置权限
	Integer setRolePermissiion(Integer roleId,List<Integer> permissionIds);
//	
//	List<SysRole> findAll();
}
