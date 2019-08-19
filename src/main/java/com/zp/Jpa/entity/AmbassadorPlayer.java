package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：AmbassadorPlayer.java
    * @创建时间：2019-07-03 11:02:54
    * @创  建  人：zp 
    * @文件描述：AmbassadorPlayer 实体类     形象大使参赛人员实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="AmbassadorPlayer")
public class AmbassadorPlayer{
	@Id
	@GeneratedValue
	private Integer aPid;
	private Integer aid;
	private Integer aGid;
	private Integer aDid;
	private String name;
	private String sex;
	private String placeOrigin;
	private String brithsday;
	private String iD;
	private String peopleTravel;
	private String occupation;
	private String address;
	private Integer province;
	private Integer city;
	private Integer education;
	private String mobile;
	private String email;
	private Integer isBroker;
	private Integer isMatch;
	private Integer isWebCast;
	private String introduce;
	private String picture;
	private Integer status;
	private Integer isDel;
	private String video;
	private Integer uid;
	private Integer popularity;
	
}

