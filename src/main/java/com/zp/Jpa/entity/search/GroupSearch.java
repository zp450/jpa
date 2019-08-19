package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class GroupSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
	
	private String name;//名字
	private String number;//编号
	private String describe;//介绍
	private Integer status;//邮轮定制状态,进行时1已结束0
	
	private Date begin;
	private Date end;
	
}
