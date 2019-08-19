package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Goods;
import com.zp.Jpa.entity.search.GoodsSearch;

public interface GoodsService {
	
	Goods addGoods(Goods goods);// 增

	Integer deleteGoods(Integer gid );// 删

	Goods saveGoods(Goods goods);// 改

	Page<Goods> queryGoods(Integer page, Integer size, GoodsSearch search);// 查
	
	List<Goods> findAll();
}
