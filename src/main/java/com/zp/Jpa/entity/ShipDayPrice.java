package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：ShipDayPrice.java
    * @创建时间：2019-06-10 17:05:22
    * @创  建  人：zp 
    * @文件描述：ShipDayPrice 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipDayPrice")
public class ShipDayPrice{
	@Id
	@GeneratedValue
	private Integer lDPid;
	private Integer sid;
	private Integer lPid;
	private Integer sendType;
	private Integer sendDay;
	private Date outTime;
	private Long basePrice;
	private Long childBasePrice;
	private Long oldBasePrice;
	private Long sellPrice;
	private Long childSellPrice;
	private Long oldSellPrice;
	private Long price;
	private Long childPrice;
	private Long oldPrice;
	private Long rackrate;
	private Long childRackrate;
	private Long integration;
	private Long childIntegration;
	private Long bonus;
	private Long childBonus;
	private Integer number;
	private Integer planType;
	private Integer sort;
	private Long roomPrice;
	private Integer usedNum;
	private Integer isDel;
	private Integer isWd;
	private Long oldRackrate;
	private Long oldIntegration;
	private Long oldBonus;
	
}

