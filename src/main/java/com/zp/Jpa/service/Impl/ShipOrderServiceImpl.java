package com.zp.Jpa.service.Impl;

import java.util.ArrayList;
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

import com.zp.Jpa.entity.Ship;
import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.ShipOrderPeople;
import com.zp.Jpa.entity.queryentity.sys.RoomChoice;
import com.zp.Jpa.entity.queryentity.sys.ShipOrderPeoples;
import com.zp.Jpa.entity.search.ShipOrderSerach;
import com.zp.Jpa.repository.ShipOrderPeopleRepository;
import com.zp.Jpa.repository.ShipOrderRepository;
import com.zp.Jpa.service.ShipOrderService;
import com.zp.Jpa.service.ShipService;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Suijishu;
import com.zp.Jpa.tools.Date.DateUtil;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;

import net.sf.json.JSONArray;

@Service
public class ShipOrderServiceImpl implements ShipOrderService {
	@Autowired
	private ShipOrderRepository shipOrderRepository;
	@Autowired
	private ShipOrderPeopleRepository shipOrderPeopleRepository;
	@Autowired
	private ShipService shipService;

	@Override
	public ShipOrder saveShipOrder(ShipOrder shipOrder) {
		// 保存订单图片
		Ship ship = shipService.findShip(shipOrder.getSid());
		String img = ship.getLinesImage();
		shipOrder.setShipOrderImage(img);

		System.out.println("shioOrder====>" + shipOrder);
		shipOrder.setInsurancePrice(ConstantUtil.BAOXIAN);
		// shipOrder.setPrice(shipOrder.getTotlePrice()-shipOrder.getVouchers());
		shipOrder.setSellPrice(shipOrder.getBasePrice());// 同行价格暂时为单价
		shipOrder.setChildAmount(0);// 取消儿童人数
		shipOrder.setIsDel(0);
		shipOrder.setOtherTip((long) 0);
		shipOrder.setStatus(0);

		shipOrder.setPrice(shipOrder.getPrice() / 100);
		shipOrder.setTotlePrice(shipOrder.getTotlePrice() / 100);
 
		// TODO Auto-generated method stub
		System.out.println("传入的order=>" + shipOrder.toString());
		String SOid = Suijishu.getUuid();
		shipOrder.setSOid(SOid);
		// 生成随机数字
		String OrderCn = Suijishu.getRandomString(10);
		// 当前时间
		String date = DateUtil.getCurrDate("yyyyMMddHHmmss");
		OrderCn = "s" + OrderCn + date;// 如果在一秒钟内有100000000人下单,可能会出现重复;
		shipOrder.setOrderCn(OrderCn);
		shipOrder.setOrderStatus(1);
		shipOrder.setPayStatus(0);

		Date OrderTime = new Date();
		shipOrder.setOrderTime(OrderTime);// 下单时间
		shipOrder.setEditTime(OrderTime);// 修改时间

		return shipOrderRepository.save(shipOrder);
	}

	@Override
	public ShipOrder findShipOrder(String sOid) {
		// TODO Auto-generated method stub
		return shipOrderRepository.findOne(sOid);
	}

	@Override
	public List<ShipOrder> findByOrderCn(String orderNo) {
		// TODO Auto-generated method stub
		return shipOrderRepository.findByOrderCn(orderNo);
	}

	@Override
	public ShipOrder save(ShipOrder shipOrder) {
		// TODO Auto-generated method stub
		return shipOrderRepository.save(shipOrder);
	}

	public Integer addShioOrderPeopleInfo(ShipOrder shipOrder, ShipOrder shipOrder2) {
		Integer uid = shipOrder.getUid();// 他们的负责人id
		Object peoples = shipOrder.getPlist();
		
		// 使用JSONArray
		int success = 0;
		JSONArray jsonArray = JSONArray.fromObject(peoples);
		@SuppressWarnings("unchecked")
		ArrayList<ShipOrderPeoples> datas = (ArrayList<ShipOrderPeoples>) JSONArray.toCollection(jsonArray,
				ShipOrderPeoples.class);
		for (ShipOrderPeoples shipOrderPeople2 : datas) {

			System.out.println("shipOrderPeople2==>" + shipOrderPeople2);
			Object sp2 = shipOrderPeople2.getRooms();
			JSONArray jsonArray2 = JSONArray.fromObject(sp2);
			@SuppressWarnings("unchecked")
			ArrayList<RoomChoice> roomdata = (ArrayList<RoomChoice>) JSONArray.toCollection(jsonArray2,
					RoomChoice.class);
			for (RoomChoice arr2 : roomdata) {
				System.out.println("arr2==>" + arr2);

				// 新建一个登船人员
				ShipOrderPeople shipOrderPeople = new ShipOrderPeople();
				// sp1信息保存
				shipOrderPeople.setParentId(uid);
				shipOrderPeople.setShipOrderPeopleName(shipOrderPeople2.getName());
				shipOrderPeople.setTotalprice(Integer.parseInt(shipOrderPeople2.getPrice()));
				shipOrderPeople.setLPid(Integer.parseInt(shipOrderPeople2.getLpid()));
				shipOrderPeople.setNumber(Integer.parseInt(shipOrderPeople2.getNumber()));
				shipOrderPeople.setOrderCn(shipOrder2.getOrderCn());
				shipOrderPeople.setShipRoomName(shipOrderPeople2.getName());
				shipOrderPeople.setLDPid(shipOrder2.getLDPid());

				// room2信息保存
				// shipOrderPeople.setNumber(Integer.parseInt(rooms.getRoomNuber()));
				// roomChoice信息保存s
				if (!StringUtils.isEmpty(arr2)) {
					shipOrderPeople.setLmId(Integer.parseInt(arr2.getLmId()));
					shipOrderPeople.setShipOrderPeopleName(arr2.getName());
				}

				System.out.println("新添加的登船人员===>" + shipOrderPeople.toString());
				// ShipOrderPeople People = shipOrderPeopleRepository.save(shipOrderPeople);
				shipOrderPeopleRepository.save(shipOrderPeople);
				if (!StringUtils.isEmpty(peoples)) {
					success += 1;
				}
			}

		}
		return success;

	}

	@Override
	public Page<ShipOrder> queryShipOrder(Integer page, Integer size, ShipOrderSerach search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "orderTime");

		Pageable pageable = new PageRequest(page, size, sort);

		return shipOrderRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<ShipOrder> getWhereClause(final ShipOrderSerach search) {
		return new Specification<ShipOrder>() {
			@Override
			public Predicate toPredicate(Root<ShipOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				
				if (search.getLinesName() != null && !" ".equals(search.getLinesName())) {
					//加上动态的查询语句
					exList.add(cb.like(root.<String>get("linesName"), "%" + search.getLinesName() + "%"));
				}
				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("orderTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("orderTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public ShipOrder deleteShipOrder(String sOid) {
		// TODO Auto-generated method stub
		ShipOrder shipOrder=shipOrderRepository.findOne(sOid);
		shipOrder.setIsDel(1);
		ShipOrder shipOrder3=shipOrderRepository.save(shipOrder);
		return shipOrder3;
	}

	@Override
	public void removeShipOrder(String sOid) {
		// TODO Auto-generated method stub
		shipOrderRepository.delete(sOid);
		
	}

	
}
