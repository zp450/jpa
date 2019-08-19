package com.zp.Jpa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

   /**
    * @文件名称：ShipOrder.java
    * @创建时间：2019-05-31 15:59:04
    * @创  建  人：zp 
    * @文件描述：ShipOrder 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipOrder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipOrder {
	@Id
	
	private String sOid;
	private String orderCn;
	private String linesName;
	private Integer uid;
	private Integer uTid;
	private Integer lDPid;
	private Integer sid;
	private Date outTime;
	private Long totlePrice;
	private Long basePrice;
	private Long sellPrice;
	private Long price;
	private Integer amount;
	private Integer childAmount;
	private Long integration;
	private Long vouchers;
//	1.已下单,未支付
//	2.已下单,已支付
//	3.已发货
//	4.已收货,未评论
//	5.已退款,未退货
//	6.已退款,已退货
//	7.订单超时,订单已取消
//	8.订单已删除
//	9.已收货,已评论
	private Integer orderStatus;

//0.未支付 1.已支付
	private Integer payStatus;
	private String iP;
	private String userRemark;
	private String orderUserName;
	private String mobile;
	private String email;
	private Integer payType;
	private Date orderTime;
	private Date editTime;
	private Long insurance;
	private Integer iId;
	private Long insurancePrice;
	private String insuranceOrder;
	private String insuranceNumber;
	private Integer insuranceStatus;
	private String insuranceRemark;
	private Long roomPrice;
	private Integer singleRoomCount;
	private Long otherTip;
	private Integer status;
	private Integer isDel;
	private Integer dNumber;
	private Integer oNumber;
	private Integer sNumber;
	private Integer oldAmount;
	private Integer orderType;
//	@JsonIgnore
//	@OneToMany(mappedBy="shipOrder",fetch=FetchType.EAGER,cascade=CascadeType.ALL)	
//	private List<ShipOrderPeople> ShipOrderPeopleList;
	
//	@ManyToMany(mappedBy = "ShipOrderList")
	@Transient
	private List<ShipOrderPeople> ShipOrderPeopleList;
	private Integer srpId;	
	@Transient
    private Object plist;
//	private Object list;
//	@Transient
//	private List<String> list;
//	   private List<String> price;
//	   private List<String> uid;
//	   private List<String> number;
//	   private List<String> lpid;//是shipprice中的主键;
//	   private List<Room> rooms;
	//@Transient
	private String payPassWord;
	//@Transient
	private String openid;
	//图片
	private String shipOrderImage;
	
}

