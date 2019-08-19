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

import com.zp.Jpa.entity.Group;
import com.zp.Jpa.entity.GroupCustomSumission;
import com.zp.Jpa.entity.search.GroupCustomSumissionSearch;
import com.zp.Jpa.repository.GroupCustomSumissionRepository;
import com.zp.Jpa.repository.GroupRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.GroupCustomSumissionService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class GroupCustomSumissionServiceImpl implements GroupCustomSumissionService{
	@Autowired
	private GroupCustomSumissionRepository repository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private UsersRepository usersRepository;
	Map<String, Object> map = new HashMap<String, Object>();
	@Override
	public GroupCustomSumission addGroupCustomSumission(GroupCustomSumission groupCustomSumission) {
		// TODO Auto-generated method stub
		System.out.println("传入的groupCustomSumission=>" + groupCustomSumission.toString());
		
		//设置下单的时间
		Date ctime = new Date();
		groupCustomSumission.setAddTime(ctime);// 下单时间
		groupCustomSumission.setEditTime(ctime);
//		UniqueOrderGenerate idWorker = new UniqueOrderGenerate(0, 0);// 订单号生成
//		if (StringUtils.isEmpty(groupCustomSumission.getOrderCn())) {
//			//获得下一个ID
//			long orderNo = idWorker.nextId();
//			groupCustomSumission.setOrderCn(String.valueOf(orderNo));
//		}

//		Users users = usersRepository.findOne(groupCustomSumission.getUid());
		//给此订单的订单信息设置改用户信息
//		if (!StringUtils.isEmpty(users.getUserName()) && !StringUtils.isEmpty(users.getTrueName())
//				&& !StringUtils.isEmpty(users.getMobile())) {
//			groupCustomSumission.setTrueName(users.getUserName());
//			//order.setOrderUserName(users.getTrueName());
//			groupCustomSumission.setMobile(Integer.parseInt(users.getMobile().trim()));
//			//groupCustomSumission.setPhone(users.getMobile());
//		} else {
//			String mString = "请先完善用户的个人信息,例如 用户的账户,用户的真实姓名,用户的手机号";
//			map.put("msg", mString);
//		} 
		// 订单支付状态 0是未支付,1是支付
		groupCustomSumission.setPayStatus(0);
		//订单状态
		groupCustomSumission.setOrderStatus(1);
		groupCustomSumission.setIsDel(0);
		//审核状态
		groupCustomSumission.setAuditStatue(0);
		// 没有传备注就设置为无备注
		if (StringUtils.isEmpty(groupCustomSumission.getUserRemark())) {
			groupCustomSumission.setUserRemark("无备注");
		}
		
		return repository.save(groupCustomSumission);
	}

	@Override
	public Integer deleteGroupCustomSumission(Integer gOid) {
		// TODO Auto-generated method stub
		GroupCustomSumission groupCustomSumission=repository.findOne(gOid);
		groupCustomSumission.setIsDel(1);
		GroupCustomSumission groupCustomSumission2=repository.save(groupCustomSumission);
		if(StringUtils.isEmpty(groupCustomSumission2)) {return 1;}
		
		return 0;
	}

	@Override
	public GroupCustomSumission saveGroupCustomSumission(GroupCustomSumission groupCustomSumission) {
		// TODO Auto-generated method stub
		//修改,修改时间
		Date date=new Date();
		groupCustomSumission.setEditTime(date);
		//定制人数加一
		Group group=groupRepository.getOne(groupCustomSumission.getGid());
		Integer buyNum=group.getBuyNum();
		group.setBuyNum(buyNum);
		groupRepository.save(group);
		
		return repository.save(groupCustomSumission);
	}

	@Override
	public Page<GroupCustomSumission> queryGroupCustomSumission(Integer page, Integer size,
			GroupCustomSumissionSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "addTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return repository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<GroupCustomSumission> getWhereClause(final GroupCustomSumissionSearch search) {
		return new Specification<GroupCustomSumission>() {
			@Override
			public Predicate toPredicate(Root<GroupCustomSumission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("addTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("addTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getName()!= null && !" ".equals(search.getName().trim())) {
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
	public List<GroupCustomSumission> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
