package com.zp.Jpa.entity.sys;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @Description:   

 * 
 * 

CREATE TABLE `usertb` (
  `userId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `userName` varchar(20) DEFAULT NULL COMMENT '用户登录名',
  `userPassWord` varchar(20) DEFAULT NULL COMMENT '用户密码',
  `userIsLockout` tinyint(1) DEFAULT '0' COMMENT '用户是否锁定:默认不锁定',
  `userUpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户信息最后一次被修改时间:数据库自己维护记录的修改时间',
  `userCreateTime` datetime DEFAULT NULL COMMENT '用户账号创建时间',
  `userLastLoginTime` datetime NOT NULL COMMENT '用户上一次登录时间',
  `userLastLoginIp` varchar(20) DEFAULT NULL COMMENT '用户上一次登录实际IP地址',
  `userPassWrongCout` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '用户输入密码错误次数',
  `userLockoutTime` datetime DEFAULT NULL COMMENT '用户输入密码错误次数达到上限锁定时间',
  `userEmail` varchar(50) DEFAULT NULL COMMENT '用户密保邮箱',
  `userTelephone` varchar(20) DEFAULT NULL COMMENT '用户密保电话',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8

 */
@Entity
@Table(name="SysUser")
@Data
@JsonInclude(Include.NON_NULL)	//如果该属性为NULL则不参与序列化
public class SysUser {
	@Id
	@GeneratedValue
	private Integer userId;
	private String userName;
	private Integer weixinUid;//跟微信用户绑定
	private String userPassWord;
	private Integer userIsLockout;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")	//日期格式化为中国的时区 东8区 
	private Date userUpdateTime;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")	//日期格式化为中国的时区 东8区
	private Date userCreateTime;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")	//日期格式化为中国的时区 东8区
	private Date userLastLoginTime;
	private String userLastLoginIp;
	private Integer userPassWrongCout = 0;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")	//日期格式化为中国的时区 东8区
	private Date userLockoutTime;
	private String userEmail;
	private String userTelephone;
	
	@JsonIgnore
	@ManyToMany
	@Cascade(value= {CascadeType.ALL})//级联关系
	@JoinTable(name="SysUser_SysRole_tb",joinColumns= {@JoinColumn(name="user_id")},inverseJoinColumns= {@JoinColumn(name="role_id")})
	@NotFound(action=NotFoundAction.IGNORE)//找不到引用的外键数据时忽略
	private List<SysRole> rolelist=new ArrayList<SysRole>();//角色集合
	@Column( columnDefinition = "int default 1" , nullable=true )
	private Integer isDel;
	@Column( columnDefinition = "int default 0" , nullable=true )
	private Integer userLevel;//一级权限最牛逼  二级次之  ,随后更垃圾
	@Column(name="loginStates" , columnDefinition = "int default 0" , nullable=true )
	private Integer loginStates;//登录状态 0是未登录  1是已登录
	public SysUser(String userName, String userPassWord,String userLastLoginIp) {
		super();
		this.userName = userName;
		this.userPassWord = userPassWord;
		this.userLastLoginIp = userLastLoginIp;
	}
	public SysUser() {
		super();
	}
	public SysUser(String userName, String userPassWord,Integer weixinUid) {
		// TODO Auto-generated constructor stub
		super();
		this.userName = userName;
		this.userPassWord = userPassWord;
		this.weixinUid = weixinUid;
		
	}
	

}
