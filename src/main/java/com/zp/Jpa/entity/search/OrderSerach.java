package com.zp.Jpa.entity.search;

import lombok.Data;

@Data
public class OrderSerach {
	private Integer page;
	private Integer size;
	private Integer uid;
	private Integer orderType;
	private Integer isDel;
}
