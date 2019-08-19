package com.zp.Jpa.service;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Adinfo;
import com.zp.Jpa.entity.search.AdinfoSearch;

public interface AdinfoService {
	Page<Adinfo> queryAdinfo(Integer page, Integer size,AdinfoSearch search);
	Adinfo saveAdinfo(Adinfo adinfo);
	Adinfo findAdinfo(Integer ACid);
	Integer deleteAdinfo(int ACid );// 删
	
//	GroupOrder addGroupOrder(GroupOrder groupOrder);// 增
//
	
//
//	GroupOrder saveGroupOrder(GroupOrder groupOrder);// 改
//
//	Page<GroupOrder> queryGroupOrder(Integer page, Integer size, GroupOrderSearch search);// 查
//	
//	List<GroupOrder> findAll();
}
