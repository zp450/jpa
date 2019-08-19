package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：ShipRoute.java
    * @创建时间：2019-07-02 09:28:03
    * @创  建  人：zp 
    * @文件描述：ShipRoute 实体类  全球航区
    * @文件版本：V0.01 
    */ 

@Entity
@Data
@Table(name="ShipRoute")
public class ShipRoute{
	@Id
	@GeneratedValue
	private Integer sRid;
	private String name;
	private String pic;
	private Integer isHot;
	private Integer sort;
	private Integer status;
	private Integer isDel;
	
}

