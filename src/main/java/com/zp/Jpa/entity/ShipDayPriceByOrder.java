package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /** 传
    * @文件名称：ShipDayPriceByOrder.java
    * @创建时间：2019-06-10 16:38:31
    * @创  建  人：zp 
    * @文件描述：ShipDayPriceByOrder 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipDayPriceByOrder")
public class ShipDayPriceByOrder{
	@Id
	@GeneratedValue
	private Integer lDPOid;
	private Integer lDPid;
	private Integer sid;
	private Integer lPid;
	private String priceName;
	private Date outTime;
	private Long basePrice;
	private Long childBasePrice;
	private Long rackrate;
	private Long childRackrate;
	private Long sellPrice;
	private Long childSellPrice;
	private Long integration;
	private Long childIntegration;
	private Long bonus;
	private Long childBonus;
	private Long price;
	private Long childPrice;
	private Long insurance;
	private Integer number;
	private Long roomPrice;
	private Integer usedNum;
	private Integer isDel;
	private Long oldBasePrice;
	private Long oldRackrate;
	private Long oldSellPrice;
	private Long oldIntegration;
	private Long oldBonus;
	private Long oldPrice;
	
}

