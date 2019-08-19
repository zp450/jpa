package com.zp.Jpa.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：WeiXinWebMenu.java
    * @创建时间：2019-05-31 16:38:11
    * @创  建  人：zp 
    * @文件描述：WeiXinWebMenu 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="WeiXinWebMenu")
public class WeiXinWebMenu{
	@Id
	@GeneratedValue
	private Integer sMid;
	private String mark;
	private String title;
	private String url;
	private String ico;
	private String bgColor;
	private String fontColor;
	private Integer menuType;
	private String description;
	private Integer sort;
	private Integer status;
	private Integer isDel;
	
}

