package com.zp.Jpa.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @文件名称：Order.java
 * @创建时间：2019-05-21 15:04:35
 * @创  建  人：zp 
 * @文件描述：Orders 实体类
 * @文件版本：V0.01 
 * 本框架是springdatajpa ,实体类属性开头需要要小写
 * 如果数据库中存在该Order表,属性还是会对应上,不会影响数据库中的字段,如果没有,就会添加字段,
 * 属性开头要小写
 */ 

@Data
@Entity
@Table(name="\"Order\"")
public class Order {
	//public class Order implements Serializable{
	@Id
//	@GeneratedValue
	
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name="system-uuid", strategy="uuid")
	
	//@Column(name="Oid" , columnDefinition = "nvarchar(32)" , nullable=false , length=64)
	//@Column(name="Oid" , columnDefinition = "uniqueidentifier(222)" , nullable=false )指定列宽,不可指定
	@Column(name="Oid" , columnDefinition = "nvarchar(32)" , nullable=false )
	
	private String oid; 
	private String name;
	private Integer pid;
	private Integer uid;
	private String userName;
	private String orderNo;
	private Integer orderType;
	private String baseNo;
	private String outNo;
	private Long totalPrice;
	private String orderUserName;
	private String mobile;
	private String phone;
	private Integer orderStatus;
	private Integer payStatus;
//	老系统订单状态
//	PayStatus
//	有效订单  
//	所有订单 -1
//	未支付 0
//	已支付 1
//	已取消 2
//	待发货 4
//	已发货 5
	private Date orderTime;
	private Date payTime;
	private Date endPayTime;
	private String remark;
	private Integer isDel;
	private Integer sNumber;
	private Integer dNumber;
	private Integer oNumber;
	private Long receivedMoney;
	private Integer spliteStatus;
	private Long rebate;
	//@Transient
	private String payPassWord;
	//@Transient
	private String ip;
	//@Transient
	private String openid;
	private String orderImage;
	private Integer sid;//航线id;商品  id
	
	private Integer equityFatherId;//股票父id
}

