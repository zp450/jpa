package com.zp.Jpa.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Equity;
import com.zp.Jpa.entity.search.EquitySearch;

public interface EquityService {

	Map<String, Object> addEquity(Equity equity);// 增

	Integer deleteEquity(Integer eId );// 删

	Equity saveEquity(Equity equity);// 改

	Page<Equity> queryEquity(Integer page, Integer size, EquitySearch search);// 查
	
	
	
	
}
