package com.zp.Jpa.entity.sys;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @Description:   
 * @author         Mashuai 
 * @Date           2018-5-16 下午10:50:29  
 * @Email          1119616605@qq.com
 * 

CREATE TABLE `roletb` (
  `roleId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `roleName` varchar(20) NOT NULL COMMENT '角色名称',
  `roleExplain` varchar(100) DEFAULT NULL COMMENT '角色说明',
  `roleCreateTime` datetime DEFAULT NULL COMMENT '角色创建时间',
  `roleLastUpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '角色最近修改时间',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8
 */
@JsonInclude(Include.NON_NULL)	//如果该属性为NULL则不参与序列化
@Entity
@Table(name="SysRole")
@Data
public class SysRole {

	@Id
	@GeneratedValue
	private Integer roleId;
	@Column(columnDefinition = "varchar(20) ")
	private String roleName;//角色名称

	@Column(columnDefinition = "varchar(100)")
	private String roleExplain;//角色说明

	@Column(name="roleCreateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=false,  updatable = false )
	private Date roleCreateTime;//角色创建时间

	@Column(name="roleLastUpdateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=true,  updatable = true )
	private Date roleLastUpdateTime;
	
	@JsonIgnore
	@ManyToMany
	@Cascade(value= {CascadeType.ALL})//级联关系 
	@JoinTable(name = "SysRole_SysModule_tb", joinColumns = { @JoinColumn(name = "role_id") },inverseJoinColumns = { @JoinColumn(name = "module_id") })
	@NotFound(action = NotFoundAction.IGNORE)
	private List<SysModule> modulelist;
	
	
	@JsonIgnore
	@ManyToMany
	@Cascade(value= {CascadeType.ALL})//级联关系 
	@JoinTable(name = "SysRole_SysPermission_tb", joinColumns = { @JoinColumn(name = "role_id") },inverseJoinColumns = { @JoinColumn(name = "permission_id")})
	@NotFound(action = NotFoundAction.IGNORE)
	private List<SysPermission> pList;
	@Column(name="isDel" , columnDefinition = "int default 0" , nullable=true )
	private Integer isDel;
	

}
