package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：ShipPort.java
    * @创建时间：2019-07-02 10:38:04
    * @创  建  人：zp 
    * @文件描述：ShipPort 实体类
    * @文件版本：V0.01 
    */ 

@Entity
@Data
@Table(name="ShipPort")
public class ShipPort{
	@Id
	@GeneratedValue
	private Integer sPid;
	private String name;
	private String pic;
	private String introduction;
	private String characteristic;
	private String news;
	private Integer sort;
	private Integer isHot;
	private Integer status;
	private Integer isDel;
	
}

