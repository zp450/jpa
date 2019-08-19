package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class NewsSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String title;
   
   
   private Date begin;
   private Date end;
}
