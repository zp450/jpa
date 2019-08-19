package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

   /**
    * @鏂囦欢鍚嶇О锛�ShipPrice.java
    * @鍒涘缓鏃堕棿锛�2019-06-12 09:57:36
    * @鍒�  寤�  浜猴細zp 
    * @鏂囦欢鎻忚堪锛�ShipPrice 瀹炰綋绫籠r
    * @鏂囦欢鐗堟湰锛�V0.01 
    */ 

@Entity
@Data
@Table(name="ShipPrice")
public class ShipPrice{
	@Id
	@GeneratedValue
	private Integer lPid;
	private Integer sid;
	private Integer sCid;
	private String name;
	private Integer minAdvance;
	private Integer maxAdvance;
	private Date checkInDate;
	private Date checkOutDate;
	private Integer dateType;
	private Integer roomNum;
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
	private Long insurance;
	private Integer planType;
	private Long roomPrice;
	private String explan;
	private Integer sort;
	private Integer status;
	private Integer isDel;
	private Long oldRackrate;
	private Long oldIntegration;
	private Long oldBonus;
	@Transient
	private String selected;
}

