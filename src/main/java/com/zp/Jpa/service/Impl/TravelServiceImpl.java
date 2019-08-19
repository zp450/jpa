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

import com.zp.Jpa.entity.Travel;
import com.zp.Jpa.entity.search.TravelSearch;
import com.zp.Jpa.repository.TravelRepository;
import com.zp.Jpa.service.TravelService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class TravelServiceImpl implements TravelService{
	@Autowired
	private TravelRepository travelRepository;

	@Override
	public Travel addTravel(Travel travel) {
		// TODO Auto-generated method stub
		return travelRepository.save(travel);
	}

	@Override
	public Integer deleteTravel(Integer tId) {
		// TODO Auto-generated method stub
		Travel shipRoute=travelRepository.findOne(tId);
		shipRoute.setIsDel(1);
		Travel shipRoute2=travelRepository.save(shipRoute);
		if(StringUtils.isEmpty(shipRoute2)) {return 1;}
		
		return 0;
	}

	@Override
	public Travel saveTravel(Travel travel) {
		// TODO Auto-generated method stub
		return travelRepository.save(travel);
	}

	@Override
	public Page<Travel> queryTravel(Integer page, Integer size, TravelSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "aTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return travelRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Travel> getWhereClause(final TravelSearch search) {
		return new Specification<Travel>() {
			@Override
			public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				

				if (search.getTName() != null && !" ".equals(search.getTName().trim())) {
					exList.add(cb.like(root.<String>get("tName"), "%" + search.getTName() + "%"));
				}
				
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public List<Travel> findAll() {
		// TODO Auto-generated method stub
		return travelRepository.findAll();
	}

}
