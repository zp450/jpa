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

import com.zp.Jpa.entity.ShipPort;
import com.zp.Jpa.entity.search.ShipPortSearch;
import com.zp.Jpa.repository.ShipPortRepository;
import com.zp.Jpa.service.ShipPortService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class ShipPortServiceImpl implements ShipPortService{
	@Autowired
	private ShipPortRepository  shipPortRepository;

	@Override
	public ShipPort addShipPort(ShipPort shipPort) {
		// TODO Auto-generated method stub
		return shipPortRepository.save(shipPort);
	}

	@Override
	public Integer deleteShipPort(Integer sPid) {
		// TODO Auto-generated method stub
		ShipPort shipPort=shipPortRepository.findOne(sPid);
		shipPort.setIsDel(1);
		ShipPort shipPort2=shipPortRepository.save(shipPort);
		if(StringUtils.isEmpty(shipPort2)) {return 1;}
		return 0;
	}

	@Override
	public ShipPort saveShipPort(ShipPort shipPort) {
		// TODO Auto-generated method stub
		return shipPortRepository.save(shipPort);
	}

	@Override
	public Page<ShipPort> queryShipPort(Integer page, Integer size, ShipPortSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "sort");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return shipPortRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<ShipPort> getWhereClause(final ShipPortSearch search) {
		return new Specification<ShipPort>() {
			@Override
			public Predicate toPredicate(Root<ShipPort> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
	public List<ShipPort> findAll() {
		// TODO Auto-generated method stub
		return shipPortRepository.findAll();
	}

}
