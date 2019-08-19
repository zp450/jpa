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

import com.zp.Jpa.entity.GroupOrder;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.entity.search.GroupOrderSearch;
import com.zp.Jpa.repository.GroupOrderRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.GroupOrderService;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Suijishu;
import com.zp.Jpa.tools.UniqueOrderGenerate;
@Service
public class GroupOrderServiceImpl implements GroupOrderService{
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private GroupOrderRepository  groupOrderRepository;
	@Override
	public GroupOrder addGroupOrder(GroupOrder groupOrder) {
		// TODO Auto-generated method stub
		System.out.println("传入的groupOrder=>" + groupOrder.toString());
		//设置oid
		String gOid = Suijishu.getUuid();
		groupOrder.setGOid(gOid);
		//设置下单的时间
		Date ctime = new Date();
		groupOrder.setOrderTime(ctime);// 下单时间
		groupOrder.setEditTime(ctime);
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, 15);// 订单支付截止时间为15分钟后
//		Date endPayTime = c.getTime();
//		order.setEndPayTime(endPayTime);// 截止支付时间
		UniqueOrderGenerate idWorker = new UniqueOrderGenerate(0, 0);// 订单号生成
		if (StringUtils.isEmpty(groupOrder.getOrderCn())) {
			//获得下一个ID
			long orderNo = idWorker.nextId();
			groupOrder.setOrderCn(String.valueOf(orderNo));
		}

		Users users = usersRepository.findOne(groupOrder.getUid());
		//给此订单的订单信息设置改用户信息
		if (!StringUtils.isEmpty(users.getUserName()) && !StringUtils.isEmpty(users.getTrueName())
				&& !StringUtils.isEmpty(users.getMobile())) {
			groupOrder.setOrderUserName(users.getUserName());
			//order.setOrderUserName(users.getTrueName());
			groupOrder.setMobile(users.getMobile());
			//groupOrder.setPhone(users.getMobile());
		} else {
			String mString = "请先完善用户的个人信息,例如 用户的账户,用户的真实姓名,用户的手机号";
			map.put("msg", mString);
		}
		// 订单支付状态 0是未支付,1是支付
		groupOrder.setPayStatus(0);
		//订单状态
		groupOrder.setOrderStatus(1);
		groupOrder.setIsDel(0);
		// 没有传备注就设置为无备注
		if (StringUtils.isEmpty(groupOrder.getUserRemark())) {
			groupOrder.setUserRemark("无备注");
		}
		
		
		return groupOrderRepository.save(groupOrder);
	}

	@Override
	public Integer deleteGroupOrder(String gOid) {
		// TODO Auto-generated method stub
		GroupOrder groupOrder=groupOrderRepository.findOne(gOid);
		groupOrder.setIsDel(1);
		GroupOrder groupOrder2=groupOrderRepository.save(groupOrder);
		if(StringUtils.isEmpty(groupOrder2)) {return 1;}
		
		return 0;
	}

	@Override
	public GroupOrder saveGroupOrder(GroupOrder groupOrder) {
		// TODO Auto-generated method stub
		return groupOrderRepository.save(groupOrder);
	}
	@Override
	public List<GroupOrder> findAll() {
		// TODO Auto-generated method stub
		return groupOrderRepository.findAll();
	}

	@Override
	public Page<GroupOrder> queryGroupOrder(Integer page, Integer size, GroupOrderSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "orderTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return groupOrderRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<GroupOrder> getWhereClause(final GroupOrderSearch search) {
		return new Specification<GroupOrder>() {
			@Override
			public Predicate toPredicate(Root<GroupOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("orderTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("orderTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getGroupName()!= null && !" ".equals(search.getGroupName().trim())) {
					exList.add(cb.like(root.<String>get("groupName"), "%" + search.getGroupName() + "%"));
				}
				
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}
	

}
