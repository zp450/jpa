package com.zp.Jpa.service;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Coupon;
import com.zp.Jpa.entity.search.CouponSearch;

public interface LineService {
	Page<Coupon> queryCoupon(Integer page, Integer size,CouponSearch search);
}
