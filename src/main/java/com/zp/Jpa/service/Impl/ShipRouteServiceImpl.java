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

import com.zp.Jpa.entity.ShipRoute;
import com.zp.Jpa.entity.search.ShipRouteSearch;
import com.zp.Jpa.repository.ShipRouteRepository;
import com.zp.Jpa.service.ShipRouteService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class ShipRouteServiceImpl implements ShipRouteService{

	@Autowired
	private ShipRouteRepository shipRouteRepository;
	@Override
	public ShipRoute addShipRoute(ShipRoute shipRoute) {
		// TODO Auto-generated method stub
		return shipRouteRepository.save(shipRoute);
	}

	@Override
	public Integer deleteShipRoute(Integer sRid) {
		// TODO Auto-generated method stub
		ShipRoute shipRoute=shipRouteRepository.findOne(sRid);
		shipRoute.setIsDel(1);
		ShipRoute shipRoute2=shipRouteRepository.save(shipRoute);
		if(StringUtils.isEmpty(shipRoute2)) {return 1;}
		
		return 0;
		
	}

	@Override
	public ShipRoute saveShipRoute(ShipRoute shipRoute) {
		// TODO Auto-generated method stub
		return shipRouteRepository.save(shipRoute);
	}

	@Override
	public Page<ShipRoute> queryShipRoute(Integer page, Integer size, ShipRouteSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "sort");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return shipRouteRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<ShipRoute> getWhereClause(final ShipRouteSearch search) {
		return new Specification<ShipRoute>() {
			@Override
			public Predicate toPredicate(Root<ShipRoute> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				

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
	public List<ShipRoute> findAll() {
		// TODO Auto-generated method stub
		return shipRouteRepository.findAll();
	}

}
