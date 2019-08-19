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

import com.zp.Jpa.entity.ShipCompany;
import com.zp.Jpa.entity.search.ShipCompanySearch;
import com.zp.Jpa.repository.ShipCompanyRepository;
import com.zp.Jpa.service.ShipCompanyService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class ShipCompanyServiceImpl implements ShipCompanyService{
	@Autowired
	private ShipCompanyRepository shipCompanyRepository;

	@Override
	public ShipCompany addShipCompany(ShipCompany shipCompany) {
		// TODO Auto-generated method stub
		return shipCompanyRepository.save(shipCompany);
	}

	@Override
	public Integer deleteShipCompany(Integer sCid) {
		// TODO Auto-generated method stub
		ShipCompany shipCompany=shipCompanyRepository.findOne(sCid);
		shipCompany.setIsDel(1);
		ShipCompany shipCompany2=shipCompanyRepository.save(shipCompany);
		if(StringUtils.isEmpty(shipCompany2)) {return 1;}
		return 0;
	}

	@Override
	public ShipCompany saveShipCompany(ShipCompany shipCompany) {
		// TODO Auto-generated method stub
		return shipCompanyRepository.save(shipCompany);
	}

	@Override
	public Page<ShipCompany> queryShipCompany(Integer page, Integer size, ShipCompanySearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "sort");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return shipCompanyRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<ShipCompany> getWhereClause(final ShipCompanySearch search) {
		return new Specification<ShipCompany>() {
			@Override
			public Predicate toPredicate(Root<ShipCompany> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
	public List<ShipCompany> findAll() {
		// TODO Auto-generated method stub
		return shipCompanyRepository.findAll();
	}

}
