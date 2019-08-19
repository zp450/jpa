package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Travel;
import com.zp.Jpa.entity.search.TravelSearch;

public interface TravelService {
	Travel addTravel(Travel travel);// 增

	Integer deleteTravel(Integer tId);// 删

	Travel saveTravel(Travel travel);// 改

	Page<Travel> queryTravel(Integer page, Integer size, TravelSearch search);// 查
	
	List<Travel> findAll();
}
