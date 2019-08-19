package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Adinfo.java
    * @创建时间：2019-05-31 14:31:32
    * @创  建  人：zp 
    * @文件描述：Adinfo 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Adinfo")
public class Adinfo{
	@Id
	@GeneratedValue
	private Integer aCid;
	private Integer aSid;
	private String name;
	private String filePath;
	private Integer adType;
	private String info;
	private String url;
	private Integer visible;
	private Date startTime;
	private Date endTime;
	private Integer sort;
	private Date addTime;
	private Integer isDel;
	private String tag;
	private Integer sTid;
	
}

