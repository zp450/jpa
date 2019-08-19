package com.zp.Jpa.service;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.search.PermisssionQuerySearch;
import com.zp.Jpa.entity.sys.SysPermission;

public interface PermissionQueryService {
	  
	Page<SysPermission> queryPermissionQuery(PermisssionQuerySearch search);
}
