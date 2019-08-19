package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.equity.EquityUser;
import com.zp.Jpa.entity.search.EquityUserSearch;

public interface EquityUserService {
	EquityUser addEquityUser(EquityUser equityUser);// 增

	Integer deleteEquityUser(Integer euid );// 删

	EquityUser saveEquityUser(EquityUser equityUser);// 改

	Page<EquityUser> queryEquityUser(Integer page, Integer size, EquityUserSearch search);// 查
	
	List<EquityUser> findAll();
	
	List<EquityUser> findByEId(Integer eId);//查询一级下线
}
