package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.search.SysRoleSearch;
import com.zp.Jpa.entity.sys.SysRole;
import com.zp.Jpa.service.SysRoleService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/sysRole", name = "角色管理模块")
public class SysRoleController {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public SysRoleService sysRoleService;
	
	@ApiOperation(value = "角色查询", notes = "查询")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = false, defaultValue = "0", paramType = "query"),
			
			 })
	@RequestMapping(value = "/querySysRole", name = "角色查询", method = RequestMethod.GET)
	public Map<String, Object> querySysRole(SysRoleSearch sysRoleSearch){
		System.out.println("shipPortSearch=>"+sysRoleSearch);
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(sysRoleSearch.getIsDel())) {
			sysRoleSearch.setIsDel(0);
		} 
		// 分页查询	
		Page<SysRole> pages = sysRoleService.querySysRole(sysRoleSearch.getPage() - 1, sysRoleSearch.getSize(), sysRoleSearch);
		return MapTool.page(pages);
	}
	@ApiOperation(value = "设置角色的权限", notes = "设置角色的权限")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色id", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "permissionIds", value = "权限id集合", dataType = "int", required = false, paramType = "query"),
			 })
	@RequestMapping(value = "/setRolePermissiion", name = "设置角色的权限", method = RequestMethod.GET)
	public Integer setRolePermissiion(Integer roleId,@RequestParam("permissionIds")List<Integer> permissionIds) {
		System.out.println("接收的角色id："+roleId);
		System.out.println("接收的权限id"+permissionIds);
		return sysRoleService.setRolePermissiion(roleId, permissionIds);
	}
	
	
	
	@ApiOperation(value = "添加角色", notes = "添加角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleName", value = "角色名", required = true, defaultValue = "股东", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "roleExplain", value = "角色描述", dataType = "String", required = true, defaultValue = "投资方", paramType = "query"), })
	@RequestMapping(value = "addSysRole", name = "添加角色",  method = RequestMethod.POST)
	public Map<String, Object> addSysRole(SysRole sysRole) {
		return MapTool.saveObject(sysRoleService.addSysRole(sysRole), "添加角色");
	}
	@ApiOperation(value = "删除角色", notes = "删除角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色id", required = true, defaultValue = "1", dataType = "String", paramType = "query"),
			 })
	@RequestMapping(value = "deleteSysRole",name = "删除角色",  method = RequestMethod.POST)
	public Map<String, Object> deleteSysRole(Integer roleId) {
		return MapTool.saveObject(sysRoleService.deleteSysRole(roleId), "删除角色");
	}
	@ApiOperation(value = "修改角色信息", notes = "修改角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleName", value = "角色名", required = true, defaultValue = "股东", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "roleExplain", value = "角色描述", dataType = "String", required = true, defaultValue = "投资方", paramType = "query"), })
	@RequestMapping(value = "saveSysRole",name = "修改角色",  method = RequestMethod.POST)
	public Map<String, Object> saveSysRole(SysRole sysRole) {
		return MapTool.saveObject(sysRoleService.saveSysRole(sysRole), "修改角色");
	}
}
