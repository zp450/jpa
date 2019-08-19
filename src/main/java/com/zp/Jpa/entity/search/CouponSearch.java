package com.zp.Jpa.entity.search;

import lombok.Data;

@Data
public class CouponSearch {
	private Integer page;
	private Integer size;
	private String title;//优惠券标题
	//private Long usedAmount;//优惠券金额
	private Long beginAmount;//金额初始范围
	private Long endAmount;
	private Integer isDel;
}
