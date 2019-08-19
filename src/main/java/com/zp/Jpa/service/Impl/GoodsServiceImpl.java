package com.zp.Jpa.service.Impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.Goods;
import com.zp.Jpa.entity.search.GoodsSearch;
import com.zp.Jpa.repository.GoodsRepository;
import com.zp.Jpa.service.GoodsService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class GoodsServiceImpl implements GoodsService{

	@Autowired
	private GoodsRepository goodsRepository;
	@Override
	public Goods addGoods(Goods goods) {
		// TODO Auto-generated method stub
		return goodsRepository.save(goods);
	}

	@Override
	public Integer deleteGoods(Integer gid) {
		// TODO Auto-generated method stub
		Goods goods=goodsRepository.findOne(gid);
		goods.setIsDel(1);
		Goods goods2=goodsRepository.save(goods);
		if(StringUtils.isEmpty(goods2)) {return 1;}
		return 0;
	}

	@Override
	public Goods saveGoods(Goods goods) {
		// TODO Auto-generated method stub
		return goodsRepository.save(goods);
	}

	@Override
	public Page<Goods> queryGoods(Integer page, Integer size, GoodsSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "LastUpdate");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return goodsRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Goods> getWhereClause(final GoodsSearch search) {
		return new Specification<Goods>() {
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("lastUpdate"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("lastUpdate").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}
				
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}
	@Override
	public List<Goods> findAll() {
		// TODO Auto-generated method stub
		return goodsRepository.findAll();
	}

}
