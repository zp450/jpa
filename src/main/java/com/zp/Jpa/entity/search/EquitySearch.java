package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class EquitySearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String name;
   private Date addTime;
   
   private Date begin;
   private Date end;
}
