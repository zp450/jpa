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

import com.zp.Jpa.entity.Group;
import com.zp.Jpa.entity.search.GroupSearch;
import com.zp.Jpa.repository.GroupRepository;
import com.zp.Jpa.service.GroupService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class GroupServiceImpl implements GroupService{

	@Autowired
	private GroupRepository GroupRpository;

	@Override
	public Group addGroup(Group Group) {
		// TODO Auto-generated method stub
	

		return GroupRpository.save(Group);
	}

	@Override
	public Group deleteGroup(Integer gid) {
		// TODO Auto-generated method stub
		Group Group2 = GroupRpository.findOne(gid);
		if(StringUtils.isEmpty(Group2)) {
			Group2.setIsDel(1);
		}
		return GroupRpository.save(Group2);
	}

	@Override
	public Group saveGroup(Group Group) {
		// TODO Auto-generated method stub
		return GroupRpository.save(Group);
	}

	@Override
	public Page<Group> queryGroup(Integer page, Integer size, GroupSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "addTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return GroupRpository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Group> getWhereClause(final GroupSearch search) {
		return new Specification<Group>() {
			@Override
			public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("userCreateTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("userCreateTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}
				 
				
				if (search.getDescribe() != null&& !" ".equals(search.getDescribe().trim())) {
					exList.add(cb.equal(root.<Integer>get("describe"), search.getDescribe()));
				}
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				if (search.getStatus() != null ) {
					exList.add(cb.equal(root.<Integer>get("status"), search.getStatus()));
				}
				return predicate;
			}
		};
	}

	@Override
	public List<Group> findAll() {
		// TODO Auto-generated method stub
		return GroupRpository.findAll();
	}

	

	public Group findByUserName(String name) {
		// TODO Auto-generated method stub
		return GroupRpository.findByName(name);
	}


}
