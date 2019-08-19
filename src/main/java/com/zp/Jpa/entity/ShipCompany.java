package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：ShipCompany.java
    * @创建时间：2019-07-02 14:05:56
    * @创  建  人：zp 
    * @文件描述：ShipCompany 实体类     邮轮公司
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipCompany")
public class ShipCompany{
	@Id
	@GeneratedValue
	private Integer sCid;
	private String name;
	private String eNname;
	private String pic;
	private String introduction;
	private Integer isHot;
	private Integer sort;
	private Integer status;
	private Integer isDel;
	
}

