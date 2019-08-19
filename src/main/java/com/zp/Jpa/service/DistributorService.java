package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Distributor;
import com.zp.Jpa.entity.search.DistributorSearch;

public interface DistributorService {
	Distributor addDistributor(Distributor distributor);// 增

	Integer deleteDistributor(Integer uid );// 删

	Distributor saveDistributor(Distributor distributor);// 改

	Page<Distributor> queryDistributor(Integer page, Integer size, DistributorSearch search);// 查
	
	List<Distributor> findAll();
}
