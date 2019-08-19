package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.Action;

public interface ActionService {

	Action addAction(Action action);
	void deleteAction(Integer aid);
	List<Action> queryAction();
	
	
//	GroupOrder addGroupOrder(GroupOrder groupOrder);// 增
//
//	Integer deleteGroupOrder(String gOid );// 删
//
//	GroupOrder saveGroupOrder(GroupOrder groupOrder);// 改
//
//	Page<GroupOrder> queryGroupOrder(Integer page, Integer size, GroupOrderSearch search);// 查
//	
//	List<GroupOrder> findAll();
}
