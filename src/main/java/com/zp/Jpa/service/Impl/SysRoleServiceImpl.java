package com.zp.Jpa.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.zp.Jpa.entity.search.SysRoleSearch;
import com.zp.Jpa.entity.sys.SysRole;
import com.zp.Jpa.repository.SysRoleRepository;
import com.zp.Jpa.service.SysRoleService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class SysRoleServiceImpl implements SysRoleService{
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	@Override
	public Map<String, Object> saveSysRole(SysRole sysRole) {
		// TODO Auto-generated method stub
		SysRole sysRole2 = findByRoleName(sysRole.getRoleName());
		System.out.println("数据库中存在的sysRole2=>" + sysRole2);
		// 如果可以查到该用户 那么用户名重复
		if (!StringUtils.isEmpty(sysRole2)) {
			map.put("status", 0);
			map.put("msg", "该角色名已经存在了,换一个吧");
			map.put("data", 00000);
		} else {
			// 添加用户
			SysRole SysRole3 = add(sysRole);
			map.put("status", 1);
			map.put("msg", "修改角色信息成功");
			map.put("data", SysRole3);
		}

		return map;
		
	}

	@Override
	public Integer deleteSysRole(Integer roleId) {
		// TODO Auto-generated method stub
		SysRole equity = sysRoleRepository.findOne(roleId);
		equity.setIsDel(1);
		SysRole equity2 = sysRoleRepository.save(equity);
		if (StringUtils.isEmpty(equity2)) {
			return 1;
		}
		return 0;
	}

	@Override
	public Map<String, Object> addSysRole(SysRole sysRole) {
		// TODO Auto-generated method stub
		// 添加之前需要判断
				// 用户名不可重复作判断 先根据用户名去数据库里查找
		SysRole sysRole2 = findByRoleName(sysRole.getRoleName());
				System.out.println("数据库中存在的sysRole2=>" + sysRole2);
				// 如果可以查到该用户 那么用户名重复
				if (!StringUtils.isEmpty(sysRole2)) {
					map.put("status", 0);
					map.put("msg", "该角色名已经存在了,换一个吧");
					map.put("data", 00000);
				} else {
					// 添加用户
					SysRole SysRole3 = add(sysRole);
					map.put("status", 1);
					map.put("msg", "添加新角色成功");
					map.put("data", SysRole3);
				}

				return map;
		
	}

	private SysRole add(SysRole sysRole) {
		// TODO Auto-generated method stub
		sysRole.setIsDel(0);
		sysRole.setRoleCreateTime(new Date());
		sysRole.setRoleLastUpdateTime(new Date());
		return sysRoleRepository.save(sysRole);
	}

	private SysRole findByRoleName(String roleName) {
		// TODO Auto-generated method stub
		return sysRoleRepository.findByRoleName(roleName);
	}

	@Override
	public Page<SysRole> querySysRole(Integer page, Integer size, SysRoleSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "createTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return sysRoleRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<SysRole> getWhereClause(final SysRoleSearch search) {
		return new Specification<SysRole>() {
			@Override
			public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("roleCreateTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("roleCreateTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getRoleName() != null && !" ".equals(search.getRoleName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getRoleName() + "%"));
				}

				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public Integer setRolePermissiion(Integer roleId, List<Integer> permissionIds) {
		// TODO Auto-generated method stub
		//先删除原来的关联
		sysRoleRepository.deleteByRoleId(roleId);
		int n=0;
		for (Integer integer : permissionIds) {
			 n=sysRoleRepository.setRoleModule(roleId, integer);
		}
		return n;
		
	}

}
