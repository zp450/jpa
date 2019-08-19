package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;
@Data
public class AmbassadorPlayerSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String name;
   private Integer aGid;//所属分组
   
   private Date begin;
   private Date end;
}
