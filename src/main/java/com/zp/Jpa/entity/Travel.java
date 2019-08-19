package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：Travel.java
    * @创建时间：2019-07-02 16:54:24
    * @创  建  人：zp 
    * @文件描述：Travel 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Travel")
public class Travel{
	@Id
	@GeneratedValue
	private Integer tId;
	private String tName;
	private String tAuthor;
	private String tContent;
	private Integer tState;
	private Integer tNewest;
	private Integer tHot;
	private Integer tAtla;
	private String tTime;
	private Integer tIike;
	private Integer tCollect;
	private String tT;
	private String tTr;
	private String tTra;
	private String tTral;
	private String aTime;
	private Integer isDel;
	
}

