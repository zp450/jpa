package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class GroupOrderSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String groupName;
   
   
   private Date begin;
   private Date end;
}
