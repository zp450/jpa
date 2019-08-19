package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：GroupOrder.java
    * @创建时间：2019-07-01 15:44:49
    * @创  建  人：zp 
    * @文件描述：GroupOrder 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="GroupOrder")
public class GroupOrder{
	@Id
	//@GeneratedValue
	private String gOid;
	private Integer uid;
	private Integer uTid;
	private String iP;
	private String orderUserName;
	private String mobile;
	private String email;
	private String userRemark;
	private Integer gid;
	private String groupName;
	private String orderCn;
	private Integer number;
	private Long groupPrice;
	private Long totlePrice;
	private Long otherTip;
	private Long price;
	private Long bonus;
	private Long integration;
	private Date validityTime;
	private Integer voucherNum;
	private Date orderTime;
	private Date editTime;
	private Integer status;
	private Integer payStatus;
	private Integer orderStatus;
	private Integer payType;
	private String oPRemark;
	private Integer isDel;
	private Integer sNumber;
	private Integer dNumber;
	private Integer oNumber;
	
	
	
	
	
	
	
}

