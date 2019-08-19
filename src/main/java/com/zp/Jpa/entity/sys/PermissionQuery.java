package com.zp.Jpa.entity.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="PermissionQuery")
public class PermissionQuery {
	@Id
	@GeneratedValue
	private Integer pQId;
  private String permissionValue;
  private String permissionModule;
  private String permissionName;
  @Transient //临时参数,不映射到数据库表字段
  private Integer page;
  @Transient //临时参数,不映射到数据库表字段
  private Integer rows;
  private Integer isDel;
  public String getPermissionValue() {
		return permissionValue;
	}
	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}
	public String getPermissionModule() {
		return permissionModule;
	}
	public Integer getpQId() {
		return pQId;
	}
	public void setpQId(Integer pQId) {
		this.pQId = pQId;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
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
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "PermissionQuery [permissionValue=" + permissionValue
				+ ", permissionModule=" + permissionModule + ", permissionName="
				+ permissionName + ", page=" + page + ", rows=" + rows + "]";
	}
	public PermissionQuery(String permissionValue, String permissionModule,
			String permissionName, Integer page, Integer rows) {
		super();
		this.permissionValue = permissionValue;
		this.permissionModule = permissionModule;
		this.permissionName = permissionName;
		this.page = page;
		this.rows = rows;
	}
	public PermissionQuery() {
		super();
	}
}
