package com.zp.Jpa.entity.search;

import lombok.Data;

@Data
public class PermisssionQuerySearch {
	private Integer isDel;
   private String permissionValue;
   private String permissionModule;
   private String permissionName;
   private Integer page;
   private Integer rows;
   public PermisssionQuerySearch(String permissionValue, String permissionModule,
			String permissionName, Integer page, Integer rows) {
		super();
		this.permissionValue = permissionValue;
		this.permissionModule = permissionModule;
		this.permissionName = permissionName;
		this.page = page;
		this.rows = rows;
	}
}
