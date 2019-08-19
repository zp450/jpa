package com.zp.Jpa.entity.sys;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @Description:   权限实体类
 * @author         zp        
 */

@JsonInclude(Include.NON_NULL)	//如果该属性为NULL则不参与序列化
@Entity
@Table(name="SysPermission")
@Data
public class SysPermission {
	@Id
	@GeneratedValue
	private Integer permissionId;//权限ID
	private String permissionValue;//权限
	@ManyToOne(fetch=FetchType.LAZY)
	private SysPermission parent;
	private String permissionModule;//权限所属模块
	private String permissionName;//权限备注说明介绍
	//@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")	//日期格式化为中国的时区 东8区
	private Date permissionLastUpdateTime;//权限修改日期时间
	@Transient //临时参数,不映射到数据库表字段
	private Integer page;
	@Transient //临时参数,不映射到数据库表字段
	private  Integer rows;
	@Transient //临时参数,不映射到数据库表字段
	private boolean include;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="parent")
	@Transient 
	private Set<SysPermission> childrens=new HashSet<SysPermission>(0);
	private Integer isDel;
	public SysPermission(String permissionValue, String permissionModule,
			String permissionName) {
		super();
		this.permissionValue = permissionValue;
		this.permissionModule = permissionModule;
		this.permissionName = permissionName;
	}
	public SysPermission() {
		super();
	}
	//private List<SysPermission> childrens;
//	@ManyToMany
//	private Set<SysRole> sysRoles = new HashSet<SysRole>();
	
	
	
}
