package com.zp.Jpa.entity;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Customize.java
    * @创建时间：2019-06-20 09:12:40
    * @创  建  人：zp 
    * @文件描述：Customize 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Customize")
public class Customize{
	@Id
	@GeneratedValue
	private Integer cid;
	private String address;
	private String adult;
	private String baby;
	private String budget;
	private String children;
	private String city;
	private String contact;
	private String contacttime;
	private String email;
	private String guides;
	private String leader;
	private String sex;
	private String startdate;
	private String stay;
	private String tel;
	private String text;
	private String traveldays;
	private String cabin;
	private String ship;
	private String shiptype;
	private String train;
	private String traintype;
	private String vehicle;
	private String air;
	private Date addTime;
	private Integer status;
	private Integer uid;
	private Integer admin;
	private Integer dNumber;
	private Integer oNumber;
	private Integer isDel;
	
}

