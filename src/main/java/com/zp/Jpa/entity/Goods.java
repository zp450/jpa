package com.zp.Jpa.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

   /**
    * @文件名称：Goods.java
    * @创建时间：2019-06-03 16:49:33
    * @创  建  人：zp 
    * @文件描述：Goods 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="Goods")
public class Goods{
	@Id
	@GeneratedValue
	private Integer gid;
	private Integer cid;
	private String number;
	private String name;
	private Integer brandId;
	private Integer amount;
	private Long weight;
	private Long rackrate;
	private Long basePrice;
	private Long sellPrice;
	private Long integration;
	private Long price;
	private String img;
	private String brief;
	private String description;
	private Integer isReal;
	private Integer isSeal;
	private Integer sort;
	private Integer isDelete;
	private Integer isBest;
	private Integer isNew;
	private Integer isHot;
	private Integer isPromote;
	private Date lastUpdate;
	private Integer gTid;
	private Integer click;
	private Integer orderNum;
	private String sellerNote;
	private Integer isDel;
	private Integer uid;
	private Integer oNumber;
	private String standard;
	
//	@ManyToMany
//	private Set<ShipShoppingCart> ShipShoppingCarts = new HashSet<ShipShoppingCart>();
	
}

