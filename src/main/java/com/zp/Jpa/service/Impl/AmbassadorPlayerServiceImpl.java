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

import com.zp.Jpa.entity.AmbassadorPlayer;
import com.zp.Jpa.entity.search.AmbassadorPlayerSearch;
import com.zp.Jpa.repository.AmbassadorPlayerRepository;
import com.zp.Jpa.service.AmbassadorPlayerService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class AmbassadorPlayerServiceImpl implements AmbassadorPlayerService{
	@Autowired
	private AmbassadorPlayerRepository repository;
	@Override
	public AmbassadorPlayer addAmbassadorPlayer(AmbassadorPlayer ambassadorPlayer) {
		// TODO Auto-generated method stub
		return repository.save(ambassadorPlayer);
	}

	@Override
	public Integer deleteAmbassadorPlayer(Integer aPid) {
		// TODO Auto-generated method stub
		AmbassadorPlayer shipPort=repository.findOne(aPid);
		shipPort.setIsDel(1);
		AmbassadorPlayer shipPort2=repository.save(shipPort);
		if(StringUtils.isEmpty(shipPort2)) {return 1;}
		return 0;
	}

	@Override
	public AmbassadorPlayer saveAmbassadorPlayer(AmbassadorPlayer ambassadorPlayer) {
		// TODO Auto-generated method stub
		return repository.save(ambassadorPlayer);
	}

	@Override
	public Page<AmbassadorPlayer> queryAmbassadorPlayer(Integer page, Integer size, AmbassadorPlayerSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "aPid");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return repository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<AmbassadorPlayer> getWhereClause(final AmbassadorPlayerSearch search) {
		return new Specification<AmbassadorPlayer>() {
			@Override
			public Predicate toPredicate(Root<AmbassadorPlayer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}
				if (search.getAGid() != null ) {
					exList.add(cb.equal(root.<Integer>get("aGid"), search.getAGid()));
				}
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}
	@Override
	public List<AmbassadorPlayer> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
