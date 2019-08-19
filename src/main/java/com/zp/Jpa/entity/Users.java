package com.zp.Jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zp.Jpa.entity.sys.SysRole;

import lombok.Data;

@Data
@Entity
@Table(name="Users")
public class Users implements Serializable{
	@Id
	@GeneratedValue
	private Integer uid;
	private Integer uTid;
	private String userName;
	private String email;
	private String mobile;
	private String passWord;
	private String trueName;
	private String recommend;
	private Integer sex;
	private String birthday;
	private String tel;
	private String pic;
	private Long balance;
	private Long recharge;
	private Long giftCharge;
	private Long freezeCharge;
	private Long vouchers;
	private Integer loginNum;
	private Date registTime;
	private Date loginTime;
	private Integer mobileStatus;
	private Integer emailStatus;
	private String registIP;
	private String lastLoingIP;
	private String qQID;
	private Short status;
	private Integer oNumber;
	private Integer dNumber;
	private Integer isDel;
	private String payPassWord;
	private String weiXinFromOpenID;
	private Integer wQid;
	private String remark;
	private String reid;
//	private String state;//用户激活状态：0表示未激活，1表示激活
//	private String code;//激活码
	//优惠券
//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name="user_coupon_tb",
//		joinColumns={@JoinColumn(name="uid",referencedColumnName="uid")},
//		inverseJoinColumns={@JoinColumn(name="couPonid",referencedColumnName="couPonId")})
//	@NotFound(action = NotFoundAction.IGNORE)
//	private Set<Coupon> Coupons = new HashSet<>();
	
//	@JsonIgnore
//	@ManyToMany
//	@Cascade(value= {CascadeType.ALL})//级联关系
//	@JoinTable(name="User_SysRole_tb",joinColumns= {@JoinColumn(name="uid")},inverseJoinColumns= {@JoinColumn(name="role_id")})
//	@NotFound(action=NotFoundAction.IGNORE)//找不到引用的外键数据时忽略
//	private List<SysRole> rolelist=new ArrayList<SysRole>();//角色集合
	
	
	//签到数据
//	@OneToMany(mappedBy = "users",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
//    //拥有mappedBy注解的实体类为关系被维护端
//     //mappedBy="author"中的author是Article中的author属性
//    private List<UserCheck> userCheckList;//文章列表
	
//	@JsonIgnore 
//	@OneToMany(mappedBy="users",fetch=FetchType.EAGER,cascade=CascadeType.ALL) 
//	private List<UserCheck> userCheckList ;
//	@JsonIgnore 
//	 @OneToMany(mappedBy = "users",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
//	    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
//	    //拥有mappedBy注解的实体类为关系被维护端
//	     //mappedBy="author"中的author是Article中的author属性
//	    private List<UserCheck> userCheckList;//文章列表
	
	
	@Transient
	private List<UserCheck> userCheckList;//文章列表
}
