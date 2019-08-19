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

import com.zp.Jpa.entity.equity.EquityUser;
import com.zp.Jpa.entity.search.EquityUserSearch;
import com.zp.Jpa.repository.EquityUserRepository;
import com.zp.Jpa.service.EquityUserService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class EquityUserServiceImpl implements EquityUserService{
	@Autowired
	private EquityUserRepository equityUserRepository;

	@Override
	public EquityUser addEquityUser(EquityUser equityUser) {
		// TODO Auto-generated method stub
		return equityUserRepository.save(equityUser);
	}

	@Override
	public Integer deleteEquityUser(Integer euid) {
		// TODO Auto-generated method stub
		EquityUser equity = equityUserRepository.findOne(euid);
		equity.setIsDel(1);
		EquityUser equity2 = equityUserRepository.save(equity);
		if (StringUtils.isEmpty(equity2)) {
			return 1;
		}
		return 0;
	}

	@Override
	public EquityUser saveEquityUser(EquityUser equityUser) {
		// TODO Auto-generated method stub
		return equityUserRepository.save(equityUser);
	}

	@Override
	public Page<EquityUser> queryEquityUser(Integer page, Integer size, EquityUserSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "createTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return equityUserRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<EquityUser> getWhereClause(final EquityUserSearch search) {
		return new Specification<EquityUser>() {
			@Override
			public Predicate toPredicate(Root<EquityUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("createTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}

				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}


	@Override
	public List<EquityUser> findAll() {
		// TODO Auto-generated method stub
		return equityUserRepository.findAll();
	}
	
	@Override
	public List<EquityUser> findByEId(Integer eId) {
		// TODO Auto-generated method stub
		return equityUserRepository.findByRecommendId(eId);
	}

	public EquityUser findByUId(Integer uid) {
		// TODO Auto-generated method stub
		return equityUserRepository.findByUId(uid);
	}

}
