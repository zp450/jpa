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

import com.zp.Jpa.entity.Coupon;
import com.zp.Jpa.entity.search.CouponSearch;
import com.zp.Jpa.repository.CouponRepository;
import com.zp.Jpa.service.CouponService;
@Service
public class CouponServiceImpl implements CouponService{
	@Autowired
	private CouponRepository couponRepository;
	@Override
	public Page<Coupon> queryCoupon(Integer page, Integer size, CouponSearch search) {
		// TODO Auto-generated method stub

		Sort sort = new Sort(Sort.Direction.DESC, "createTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size , sort);
		System.out.println(pageable);
		return couponRepository.findAll(this.getWhereClause(search), pageable);
}
private Specification<Coupon> getWhereClause(final CouponSearch search) {
return new Specification<Coupon>() {
	@Override
	public Predicate toPredicate(Root<Coupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate predicate = cb.conjunction();// 动态SQL表达式
		List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
		
		if (search.getBeginAmount() != null) {
			exList.add(cb.greaterThanOrEqualTo(root.<Long>get("usedAmount"), search.getBeginAmount()));// 大于等于起始金额
		}
		if (search.getEndAmount() != null) {
			exList.add(cb.lessThanOrEqualTo(root.get("usedAmount").as(Long.class), search.getEndAmount()));// 小于等于截止金额
		}
		if (search.getIsDel() != null ) {
			exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
		}
		return predicate;
	}
};
}

	@Override
	public Coupon saveCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		return couponRepository.save(coupon);
	}

	@Override
	public Coupon findCoupin(Integer couPonId) {
		// TODO Auto-generated method stub
		return couponRepository.findOne(couPonId);
	}

}
