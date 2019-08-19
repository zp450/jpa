package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @鏂囦欢鍚嶇О锛�ShipCabin.java
    * @鍒涘缓鏃堕棿锛�2019-06-11 18:52:21
    * @鍒�  寤�  浜猴細zp 
    * @鏂囦欢鎻忚堪锛�ShipCabin 瀹炰綋绫籠r
    * @鏂囦欢鐗堟湰锛�V0.01 
    */ 

@Data
@Entity
@Table(name="ShipCabin")
public class ShipCabin{
	@Id
	@GeneratedValue
	private Integer sCid;
	private String name;
	private Integer status;
	private Integer sort;
	private Integer isDel;
	
	
}

