package com.zp.Jpa.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.queryentity.sys.SysPermissionQuery;
import com.zp.Jpa.entity.search.PermisssionQuerySearch;
import com.zp.Jpa.entity.sys.Node;
import com.zp.Jpa.entity.sys.SysPermission;



public interface PermissionService {
	
	
	/**
	 * 条件分页查询
	 * @param permission
	 * @return
	 */
	public List<SysPermission> query(SysPermissionQuery permission);
	
	/**
	 * 查询所有权限集合
	 * @return 权限value集合
	 */
	public List<String> queryAll();
	
	/**
	 * 批量插入权限数据
	 * @param pList
	 * @return 成功插入的权限数据条数
	 */
	public int batchInsert(List<SysPermission> pList);
	
	/**
	 * 查询所有权限操作
	 * @return
	 */
	public List<Node> queryNode();
	/**
	 * 根据角色Ids查询所有权限操作
	 * @return
	 */
//	public List<SysPermission> queryRoleSetPermission(Integer roleId);
	
	/**权限树
	 * @param roleId
	 * @return
	 */
	public Map<String, Object> queryPermissionTree(Integer roleId);
	public List<SysPermission> queryPermission(PermisssionQuerySearch pquery);
	public Page<SysPermission> queryPermissionPage(PermisssionQuerySearch pquery);
	
	public Integer addPermission(SysPermission permission);

	public Integer deletePermission(Integer permissionId);

	public Integer totalPermission(PermisssionQuerySearch pquery);

}
