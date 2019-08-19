package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：News.java
    * @创建时间：2019-07-02 15:18:02
    * @创  建  人：zp 
    * @文件描述：News 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="News")
public class News{
	@Id
	@GeneratedValue
	private Integer nid;
	private String title;
	private String nNid;
	private String pic;
	private String introduction;
	private String tag;
	private Integer status;
	private Integer click;
	private Integer recommend;
	private String url;
	private String comeFrom;
	private String author;
	private Date addTime;
	private Short type;
	private Integer isDel;
	private String video;
	private Integer isVideo;
	
}

