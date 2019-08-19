package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


//优惠券表
/**
 * @author Administrator
 * 不为空参数,需要传title 名称, 
 */
@Data
@Entity
@Table(name="Coupon")
public class Coupon {
	@Id
	@GeneratedValue
	
  private Integer couPonId;
	@Column(name="title" , columnDefinition = "varchar(100)" , nullable=false )
  private String title;//优惠券标题
	@Column(name="icon" , columnDefinition = "varchar(100)" , nullable=true )
  private String  icon;
  //可用于：10店铺优惠券 11新人店铺券  20商品优惠券  30类目优惠券  60平台优惠券 61新人平台券',船票优惠;微信商城优惠,打车优惠
  private Integer usedWhere;
  
	@Column(name="type" , columnDefinition = "int default 1" , nullable=true )
  private Integer type;//1满减券 2叠加满减券 3无门槛券（需要限制大小）
 
	@Column(name="withSpecial" , columnDefinition = "int default 1" , nullable=true )
  private Integer withSpecial; //1不能 2 可用于特价商品 默认不能
  
	@Column(name="withSn" , columnDefinition = "varchar(100) default '爱邮吉游'" , nullable=true )
  private String withSn;//指定店铺或商品流水号
  
	@Column(name="withAmount" , columnDefinition = "money" , nullable=true )
  private Long withAmount;//满多少金额
  
	@Column(name="usedAmount" , columnDefinition = "money" , nullable=false )
  private Long usedAmount;//优惠券金额

  private Integer quantity;//数量

  private Integer takeCount;//已经领取的数量

  private Integer usedCount;//已经使用的优惠券数量
  
  private Date startTime;//发放开始时间

  private Date endTime;  //发放结束时间
  
  private Integer validType;//时效:1绝对时效（领取后XXX-XXX时间段有效）  2相对时效（领取后N天有效）'
  @Column(name="validStartTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=true,  updatable = true )
  private Date validStartTime; //使用开始时间
  
  private Date validEndTime;//使用结束时间
  
  private Integer validDays;//自领取之日起有效天数
  @Column(name="status" , columnDefinition = "int default 1" , nullable=false )
  private Integer status;//1生效 2失效 3已结束',
  @Column(name="createUser" , columnDefinition = "varchar(10) " , nullable=true )
  private String createUser;
  @Column(name="createTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=false,  updatable = false )
  private Date createTime;
  @Column(name="updateUser" , columnDefinition = "varchar(20) " , nullable=false )
  private String updateUser;
  @Column(name="updateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=true,  updatable = true )
  private Date updateTime;
  @Column(name="isDel" , columnDefinition = "int default 0" , nullable=false )
  private Integer isDel;

//  @JsonIgnore
//  @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
//  @JoinTable(name="user_coupon_tb",
//		joinColumns={@JoinColumn(name="couPonid",referencedColumnName="couPonId")},
//		inverseJoinColumns={@JoinColumn(name="uid",referencedColumnName="uid")})
//	@NotFound(action = NotFoundAction.IGNORE)
//	private Set<Users> Users = new HashSet<Users>();

}
