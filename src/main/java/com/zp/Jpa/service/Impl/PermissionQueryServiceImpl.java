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

import com.zp.Jpa.entity.search.PermisssionQuerySearch;
import com.zp.Jpa.entity.sys.SysPermission;
import com.zp.Jpa.repository.PermissionRepository;
import com.zp.Jpa.service.PermissionQueryService;
@Service
public class PermissionQueryServiceImpl implements PermissionQueryService{


	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public Page<SysPermission> queryPermissionQuery(PermisssionQuerySearch search) {
		// TODO Auto-generated method stub

		Sort sort = new Sort(Sort.Direction.DESC, "permissionLastUpdateTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(search.getPage(), search.getRows() , sort);
		System.out.println(pageable);
		return permissionRepository.findAll(this.getWhereClause(search), pageable);
	}
	private Specification<SysPermission> getWhereClause(final PermisssionQuerySearch search) {
		return new Specification<SysPermission>() {
			@Override
			public Predicate toPredicate(Root<SysPermission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				if (search.getPermissionValue() != null && !" ".equals(search.getPermissionValue())) {
					exList.add(cb.like(root.<String>get("permissionValue"), "%" + search.getPermissionValue() + "%"));
				}
				if (search.getPermissionModule() != null && !" ".equals(search.getPermissionModule())) {
					exList.add(cb.like(root.<String>get("permissionModule"), "%" + search.getPermissionModule() + "%"));
				}
				if (search.getPermissionName() != null && !" ".equals(search.getPermissionName())) {
					exList.add(cb.like(root.<String>get("permissionName"), "%" + search.getPermissionName() + "%"));
				}
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}


}
