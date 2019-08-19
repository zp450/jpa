package com.zp.Jpa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="Equity")
public class Equity {
	@Id
	@GeneratedValue
	private Integer eId;
	private Integer uId;
	private Integer recommendId;//推荐人id
	
	private BigDecimal money;
	private Integer type;//入股方式
	private BigDecimal directPush;//入股直推百分比
//	private BigDecimal commission;//所得佣金
//	private BigDecimal morePush;//越推所得佣金,孙子层所回报
	private String name;//姓名
	private String IDcardNumber;//身份证号
	private String bankNumber;//银行卡号
	private String mobile;//手机号
	  
	  @Column(name="createTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=false,  updatable = false )
	  private Date createTime;
	  @Column(name="updateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=true,  updatable = true )
	  private Date updateTime;
	  @Column(name="isDel" , columnDefinition = "int default 0" , nullable=false )
	  private Integer isDel;
	  public Equity(Integer uId, Integer recommendId,
			  BigDecimal money) {
			super();
			this.uId = uId;
			this.recommendId = recommendId;
			this.money = money;
		}
	  public Equity() {
			super();
		}
}
