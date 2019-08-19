package com.zp.Jpa.service.Impl;


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

import com.zp.Jpa.entity.WeiXinWebMenu;
import com.zp.Jpa.entity.search.WeiXinWebMenuSearch;
import com.zp.Jpa.repository.WeiXinWebMenuRepository;
import com.zp.Jpa.service.WeiXinWebMenuService;
@Service
public class WeiXinWebMenuServiceImpl implements WeiXinWebMenuService{
	@Autowired
	private WeiXinWebMenuRepository weiXinWebMenuRepository;
	@Override
	public WeiXinWebMenuService saveWeiXinWebMenu(WeiXinWebMenuService weiXinWebMenu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<WeiXinWebMenu> queryWeiXinWebMenu(Integer page, Integer size, WeiXinWebMenuSearch search) {
		// TODO Auto-generated method stub

				Sort sort = new Sort(Sort.Direction.ASC, "sort");

				System.out.println(sort);
				Pageable pageable = new PageRequest(page, size , sort);
				System.out.println(pageable);
				return weiXinWebMenuRepository.findAll(this.getWhereClause(search), pageable);
	}
	private Specification<WeiXinWebMenu> getWhereClause(final WeiXinWebMenuSearch search) {
		return new Specification<WeiXinWebMenu>() {
			@Override
			public Predicate toPredicate(Root<WeiXinWebMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				if (search.getTitle() != null && !" ".equals(search.getTitle())) {
					exList.add(cb.like(root.<String>get("title"), "%" + search.getTitle() + "%"));
				}
				if (search.getStatus() != null ) {
					exList.add(cb.equal(root.<Integer>get("status"), search.getStatus()));
				}
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}
}
