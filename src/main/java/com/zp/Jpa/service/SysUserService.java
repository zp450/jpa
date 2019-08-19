package com.zp.Jpa.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.search.SysUserSearch;
import com.zp.Jpa.entity.sys.SysUser;

public interface SysUserService {
	Map<String, Object> addSysUser(SysUser sysUser);// 增

	SysUser deleteSysUser(SysUser sysUser);// 删

	Map<String, Object>  saveSysUser(SysUser sysUser);// 改

	Page<SysUser> querySysUser(Integer page, Integer size, SysUserSearch search);// 查
	
	List<SysUser> findAll();
	
	SysUser loginSysUser(SysUser sysUser);
	
	Map<String,Object> getLoginMap(SysUser sysUser);
	
	Map<String, Object> isLockOrIsDel(SysUser sysUser3);
	
	void logout(Integer userId);
}
