package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Groups.java
    * @创建时间：2019-06-25 09:23:42
    * @创  建  人：zp 
    * @文件描述：Groups 实体类  邮轮定制 邮轮定制 邮轮定制 邮轮定制 邮轮定制 邮轮定制
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="\"Group\"")
public class Group{
	@Id
	@GeneratedValue
	private Integer gid;
	private Integer isRelevance;
	private Integer goodsKey;
	private Integer goodsId;
	private String name;
	private String shortName;
	private String number;
	private Integer cid;
	private Integer groupType;
	private String gCid;
	private Integer buyType;
	private String pic;
	private Long costPrice;
	private Long groupPrice;
	private Long sellPrice;
	private Long price;
	private Long integration;
	private Long bonus;
	private Integer inventory;
	private Integer pNum;
	private Integer vNum;
	private Integer lNum;
	private Integer mNum;
	private Integer isRepeatedly;
	private Date startTime;
	private Date endTime;
	private Integer isCoupon;
	private Integer dealType;
	private Date validityStartTime;
	private Date validityEndTime;
	private Integer isSyncretic;
	private String linkName;
	private String linkTel;
	private String linkAddress;
	private String mapX;
	private String mapY;
	private String describe;
	private String info;
	private Integer recommend;
	private Integer isNew;
	private Integer isHot;
	private Integer isACancel;
	private Integer isPCancel;
	private Integer status;
	private Integer sort;
	private Date addTime;
	private Date editTime;
	private Integer seeNum;
	private Integer buyNum;
	private Integer favNum;
	private Integer isDel;
	private String merchantRemark;
	private Integer uid;
	private Integer oNumber;
	private String declareInfo;
	
}

