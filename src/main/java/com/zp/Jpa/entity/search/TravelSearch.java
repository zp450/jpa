package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class TravelSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String tName;
   
   
   private Date begin;
   private Date end;
}
