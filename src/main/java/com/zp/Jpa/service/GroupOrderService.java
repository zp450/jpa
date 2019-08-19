package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.GroupOrder;
import com.zp.Jpa.entity.search.GroupOrderSearch;



public interface GroupOrderService {
	GroupOrder addGroupOrder(GroupOrder groupOrder);// 增

	Integer deleteGroupOrder(String gOid );// 删

	GroupOrder saveGroupOrder(GroupOrder groupOrder);// 改

	Page<GroupOrder> queryGroupOrder(Integer page, Integer size, GroupOrderSearch search);// 查
	
	List<GroupOrder> findAll();
}
