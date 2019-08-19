package com.zp.Jpa.entity.search;

import lombok.Data;

@Data
public class ShipPortSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
   private String name;
}
