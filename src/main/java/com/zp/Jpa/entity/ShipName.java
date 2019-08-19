package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：ShipName.java
    * @创建时间：2019-06-06 16:52:55
    * @创  建  人：zp 
    * @文件描述：ShipName 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipName")
public class ShipName{
	@Id
	@GeneratedValue
	private Integer sNid;
	private Integer scid;
	private String name;
	private String pic;
	private Integer sort;
	private Integer status;
	private Integer isDel;
	private String introduction;
	
}

