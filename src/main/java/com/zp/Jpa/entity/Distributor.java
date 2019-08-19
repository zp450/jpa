package com.zp.Jpa.entity;
import java.sql.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Distributor.java
    * @创建时间：2019-08-19 14:41:20
    * @创  建  人：zp 
    * @文件描述：Distributor 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Distributor")
public class Distributor{
	@Id
	@GeneratedValue
	private Integer uid;
	private String name;
	private String shoperName;
	private String cardType;
	private String iDcard;
	private String mobile;
	private String fax;
	private String email;
	private String contacts;
	private String addr;
	private String cardPic;
	private Integer province;
	private Integer city;
	private String remark;
	private Integer serviceLevel;
	private String tenpayCode;
	private Integer tenpayStates;
	private Integer iDCardStates;
	private Integer mobileStates;
	private Long supCose;
	private Date startTime;
	private Date endTime;
	private Date registTime;
	private Integer status;
	private Integer isCredit;
	private String business;
	private Integer isDel;
	private Integer oNumber;
	private Integer bUid;
	private String supplyList;
	private Integer opener;
	
}

