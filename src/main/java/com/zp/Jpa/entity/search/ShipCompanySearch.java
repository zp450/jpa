package com.zp.Jpa.entity.search;

import lombok.Data;

@Data
public class ShipCompanySearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String name;
}
