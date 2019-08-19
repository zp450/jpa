package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;

@Data
public class SysRoleSearch {

	private Integer page;
	private Integer size;
	private Integer isDel;
   private String roleName;
   private Date roleCreateTime;
   
   private Date begin;
   private Date end;
}
