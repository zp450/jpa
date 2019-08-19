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

import com.zp.Jpa.entity.Adinfo;
import com.zp.Jpa.entity.search.AdinfoSearch;
import com.zp.Jpa.myInterface.RedisHandel;
import com.zp.Jpa.repository.AdinfoRepository;
import com.zp.Jpa.service.AdinfoService;
import com.zp.Jpa.tools.StringUtils;

@Service
public class AdinfoServiceImpl implements AdinfoService {
	@Autowired
	private AdinfoRepository adinfoRepository;

	@Override
	// 多条件分页查询
	public Page<Adinfo> queryAdinfo(Integer page, Integer size, AdinfoSearch search) {
		// TODO Auto-generated method stub

		//构造sort,排序方式    根据什么排序
		Sort sort = new Sort(Sort.Direction.DESC, "addTime");

		// System.out.println(sort);
		//构造pageable  传入page  size  sort
		Pageable pageable = new PageRequest(page, size, sort);
		// System.out.println(pageable);
		return adinfoRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Adinfo> getWhereClause(final AdinfoSearch search) {
		return new Specification<Adinfo>() {
			@Override
			public Predicate toPredicate(Root<Adinfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				//判定是否为空
				if (search.getName() != null && !" ".equals(search.getName())) {
					//加上动态的查询语句
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("addTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("addTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}
				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public Adinfo saveAdinfo(Adinfo adinfo) {
		// TODO Auto-generated method stub
		return adinfoRepository.save(adinfo);
	}

	@Override
	public Adinfo findAdinfo(Integer ACid) {
		// TODO Auto-generated method stub
		return adinfoRepository.findOne(ACid);
	}

	@Override
	public Integer deleteAdinfo(int ACid) {
		// TODO Auto-generated method stub
		Adinfo adinfo=adinfoRepository.findOne(ACid);
		adinfo.setIsDel(1);
		Adinfo adinfo2=adinfoRepository.save(adinfo);
		if(StringUtils.isEmpty(adinfo2)) {return 1;}
		return 0;
	}
	
	

	    @RedisHandel(key = "kkkk" ,keyField = "param1")
	    public Object test(String abc,int i8,int i2, String str ,Integer i){
	        return "1";
	    
	}

	

}
