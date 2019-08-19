package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Lines.java
    * @创建时间：2019-06-06 14:41:55
    * @创  建  人：zp 
    * @文件描述：Lines 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Lines")
public class Lines{
	@Id
	@GeneratedValue
	private Integer lid;
	private Integer uid;
	private String name;
	private String number;
	private Integer linesType;
	private Integer linesNature;
	private String lNid;
	private String subject;
	private Long rackrate;
	private Long basePrice;
	private Long sellPrice;
	private Long price;
	private String wordPath;
	private String lCid;
	private Integer days;
	private Integer city;
	private String endCity;
	private Integer dateType;
	private String traffic;
	private String linesImage;
	private Integer sort;
	private Date addTime;
	private Date editTime;
	private Integer status;
	private Long seeNum;
	private Long buyNum;
	private Long favNum;
	private Integer validPlansNum;
	private Integer schedulingType;
	private Integer schedulingDay;
	private Integer isNew;
	private Integer isHot;
	private Integer isPromote;
	private Integer recommend;
	private String features;
	private String remark;
	private Integer isDel;
	private Integer oNumber;
	private String cityList;
	private String supply;
	private String picComeFrom;
	private String remark2;
	private String tuNiuScheduling;
	private Date validPlansLastTime;
	private String subTitle;
	private String validPlansTimeList;
}

