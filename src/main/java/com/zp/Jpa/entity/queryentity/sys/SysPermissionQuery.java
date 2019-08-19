package com.zp.Jpa.entity.queryentity.sys;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zp.Jpa.entity.queryentity.entitybase.QueryBase;



public class SysPermissionQuery extends QueryBase{
	
	private Integer permissionId;//权限ID
	private String permissionValue;//权限
	private String permissionModule;//权限所属模块
	private String permissionName;//权限备注说明介绍
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	//接受字符串日期需要格式化为日期类型
	private Date permissionUpdateTimeStart;//权限修改起始日期时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	//接受字符串日期需要格式化为日期类型
	private Date permissionUpdateTimeEnd;//权限修改截至日期时间
	public Integer getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	public String getPermissionValue() {
		return permissionValue;
	}
	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}
	public String getPermissionModule() {
		return permissionModule;
	}
	public void setPermissionModule(String permissionModule) {
		this.permissionModule = permissionModule;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public Date getPermissionUpdateTimeStart() {
		return permissionUpdateTimeStart;
	}
	public void setPermissionUpdateTimeStart(Date permissionUpdateTimeStart) {
		this.permissionUpdateTimeStart = permissionUpdateTimeStart;
	}
	public Date getPermissionUpdateTimeEnd() {
		return permissionUpdateTimeEnd;
	}
	public void setPermissionUpdateTimeEnd(Date permissionUpdateTimeEnd) {
		this.permissionUpdateTimeEnd = permissionUpdateTimeEnd;
	}
	@Override
	public String toString() {
		return "SysPermissionQuery [permissionId=" + permissionId
				+ ", permissionValue=" + permissionValue
				+ ", permissionModule=" + permissionModule
				+ ", permissionName=" + permissionName
				+ ", permissionUpdateTimeStart=" + permissionUpdateTimeStart
				+ ", permissionUpdateTimeEnd=" + permissionUpdateTimeEnd + "]";
	}
	
	
	
	
	
	
	
	

}
