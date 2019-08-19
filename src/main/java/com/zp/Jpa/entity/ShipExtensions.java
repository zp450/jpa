package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
    * @文件名称：ShipExtensions.java
    * @创建时间：2019-06-10 16:41:08
    * @创  建  人：zp 
    * @文件描述：ShipExtensions 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="ShipExtensions")
public class ShipExtensions{
	@Id
	@GeneratedValue
	private Integer lEid;
	private Integer sid;
	private String title;
	private String introduction;
	private Integer status;
	private Integer sort;
	private Integer isDel;
	public void setLEid(Integer lEid){
	this.lEid=lEid;
	}
	public Integer getLEid(){
		return lEid;
	}
	public void setSid(Integer sid){
	this.sid=sid;
	}
	public Integer getSid(){
		return sid;
	}
	public void setTitle(String title){
	this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setIntroduction(String introduction){
	this.introduction=introduction;
	}
	public String getIntroduction(){
		return introduction;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setSort(Integer sort){
	this.sort=sort;
	}
	public Integer getSort(){
		return sort;
	}
	public void setIsDel(Integer isDel){
	this.isDel=isDel;
	}
	public Integer getIsDel(){
		return isDel;
	}
}

