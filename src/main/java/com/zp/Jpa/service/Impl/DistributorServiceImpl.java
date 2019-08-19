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

import com.zp.Jpa.entity.Distributor;
import com.zp.Jpa.entity.equity.EquityUser;
import com.zp.Jpa.entity.search.DistributorSearch;
import com.zp.Jpa.entity.search.EquityUserSearch;
import com.zp.Jpa.repository.DistributorRepository;
import com.zp.Jpa.service.DistributorService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class DistributorServiceImpl implements DistributorService{
	@Autowired
	private DistributorRepository distributorRepository;
	@Override
	public Distributor addDistributor(Distributor distributor) {
		// TODO Auto-generated method stub
		return distributorRepository.save(distributor);
	}

	@Override
	public Integer deleteDistributor(Integer uid) {
		// TODO Auto-generated method stub
		Distributor equity = distributorRepository.findOne(uid);
		equity.setIsDel(1);
		Distributor equity2 = distributorRepository.save(equity);
		if (StringUtils.isEmpty(equity2)) {
			return 1;
		}
		return 0;
	}

	@Override
	public Distributor saveDistributor(Distributor distributor) {
		// TODO Auto-generated method stub
		return distributorRepository.save(distributor);
	}

	@Override
	public Page<Distributor> queryDistributor(Integer page, Integer size, DistributorSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "registTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return distributorRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Distributor> getWhereClause(final DistributorSearch search) {
		return new Specification<Distributor>() {
			@Override
			public Predicate toPredicate(Root<Distributor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("registTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("registTime").as(Date.class), search.getEnd()));// 小于等于截止日期
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
	public List<Distributor> findAll() {
		// TODO Auto-generated method stub
		return distributorRepository.findAll();
	}

}
