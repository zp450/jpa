package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**邮轮
    * @文件名称：Ship.java
    * @创建时间：2019-05-31 10:22:22
    * @创  建  人：zp 
    * @文件描述：Ship 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Ship")
public class Ship{
	@Id
	@GeneratedValue
	private Integer sid;
	private Integer uid;
	private String name;
	private String number;
	private Integer sRid;
	private Integer sPid;
	private Integer sCid;
	private Integer sNid;
	private Long rackrate;
	private Long basePrice;
	private Long sellPrice;
	private Long price;
	private String wordPath;
	private Integer schedulingDay;
	private Integer days;
	private String linesImage;
	private Integer sort;
	private Date addTime;
	private Date editTime;
	private Integer dateType;
	private Integer status;
	private Long seeNum;
	private Long buyNum;
	private Long favNum;
	private Integer validPlansNum;
	private Integer schedulingType;
	private Integer isNew;
	private Integer isHot;
	private Integer isPromote;
	private Integer recommend;
	private String features;
	private String remark;
	private Integer isDel;
	private Integer oNumber;
	private String supply;
	private String picComeFrom;
	private String remark2;
	private Date validPlansLastTime;
	private String subTitle;
	private String validPlansTimeList;
	
}

