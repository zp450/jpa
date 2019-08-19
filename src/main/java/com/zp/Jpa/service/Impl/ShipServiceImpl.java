package com.zp.Jpa.service.Impl;

import java.util.ArrayList;
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

import com.zp.Jpa.entity.Ship;
import com.zp.Jpa.entity.ShipCabin;
import com.zp.Jpa.entity.ShipName;
import com.zp.Jpa.entity.ShipPrice;
import com.zp.Jpa.entity.search.ShipSearch;
import com.zp.Jpa.repository.ShipNameRepository;
import com.zp.Jpa.repository.ShipRepository;
import com.zp.Jpa.service.ShipCabinService;
import com.zp.Jpa.service.ShipPriceService;
import com.zp.Jpa.service.ShipService;
import com.zp.Jpa.tools.StringUtils;

@Service
public class ShipServiceImpl implements ShipService {
	private  Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private ShipCabinService shipCabinService;
	@Autowired
	private ShipRepository shipRepository;
	@Autowired
	private ShipNameRepository shipNameRepository;
	@Autowired
	private ShipPriceService shipPriceService;

	@Override
	public Page<Ship> queryShip(Integer page, Integer limit, ShipSearch search) {
		// TODO Auto-generated method stub

		Sort sort = new Sort(Sort.Direction.DESC, "addTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, limit, sort);
		System.out.println(pageable);
		return shipRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Ship> getWhereClause(final ShipSearch search) {
		return new Specification<Ship>() {
			@Override
			public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Integer> shipids = new ArrayList<>();
				if (!StringUtils.isEmpty(search.getShipName())) {
					List<ShipName> names = shipNameRepository.findByNameLike(search.getShipName());
					if (!StringUtils.isEmpty(names)) {
						for (ShipName sh : names) {
							shipids.add(sh.getSNid());
						}
					}
				}

				search.setSNids(shipids);
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				if (search.getSNids() != null) {// 始发地
					for (Integer in : search.getSNids()) {
						exList.add(cb.equal(root.<Integer>get("sNid"), in));
					}
				}
				// if (search.getSPid() != null ) {//始发地
				// exList.add(cb.equal(root.<Integer>get("SPid"), search.getSPid()));
				// }
				////邮轮航线
				

				//判断查询的航线是没过期的
				if (search.getNowTime() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("validPlansLastTime"), search.getNowTime()));// 如果传现在时间,那么就判定查询没过期的航线
				}
				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("validPlansLastTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("validPlansLastTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}
				// 判断价格
				if (search.getBeginMoney() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Long>get("sellPrice"), (long) search.getBeginMoney()));// 大于等于起始价格
				}
				if (search.getEndMoney() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("sellPrice").as(Long.class), (long) search.getEndMoney()));// 小于等于截止价格
				}

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}
				if (search.getRecommend() != null) {
					exList.add(cb.equal(root.<Integer>get("recommend"), search.getRecommend()));
				}
				if (search.getIsHot() != null) {
					exList.add(cb.equal(root.<Integer>get("isHot"), search.getIsHot()));
				}
				if (search.getSRid() != null) {
					exList.add(cb.equal(root.<Integer>get("sRid"), search.getSRid()));
				}
				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public Ship saveShip(Ship ship) {
		// TODO Auto-generated method stub
		return shipRepository.save(ship);
	}

	@Override
	public Ship findShip(Integer sid) {
		// TODO Auto-generated method stub
		return shipRepository.findOne(sid);
	}

	@Override
	public Map<String, Object> selectShipRoomBySidAndOutTime(Integer sid, Date outTime) {
		// TODO Auto-generated method stub
		Map<String, Object> map2 = new HashMap<String, Object>();
		// 房间类型
		List<ShipCabin> cabin = new ArrayList<>();
		// 房间类型 id集合
		List<Integer> scids = new ArrayList<>();
		// 房间详情名字集合
		List<String> ShipPriceName = new ArrayList<>();
		System.out.println(sid);
		System.out.println(outTime);
		// 先根据id和出发时间查询出房间的详细名称 时间格式传03/18/2020
		// 数据库中可能会有重复的 shipDayPrice表中根据sid和出发时间 查询出lpid 然后根据lpid去shipPrice表中查询出房间详细名
		// 房间详细集合
		List<ShipPrice> ShipPrice = shipPriceService.selectBySidAndOutTime(sid, outTime);

		// 不为空判断
		if (!StringUtils.isEmpty(ShipPrice)) {
			//
			int sizea = ShipPrice.size();
			// 获取到
			for (int i = 0; i < sizea; i++) {
				Integer scid = ShipPrice.get(i).getSCid();// 航位类型 id 就是这个房间详细 是属于什么类型的房 是什么房间类型 shipCabin表
				// 不会重复的放入
				if (!scids.contains(scid)) {
					// 将房间类型id放入到 类型集合
					scids.add(scid);
					// 找到这个房间类型
					ShipCabin shipCabin = shipCabinService.findByScid(scid);
					// 加入到房间类型集合里
					cabin.add(shipCabin);
				}
				// 船舱详情名字
				String name = ShipPrice.get(i).getName().toString();
				// 将船舱详情名字加入到船舱详情名字集合
				ShipPriceName.add(name);
			}
			List<Integer> scids2 = new ArrayList<>();
			// 遍历船舱类型
			for (int i = 0; i < cabin.size(); i++) {
				// 详细数据集合
				List<ShipPrice> nameStr = new ArrayList<>();
				String cabinName = null;
				// 不为空判断
				if (!StringUtils.isEmpty(cabin)) {
					// 拿出船舱类型名字
					cabinName = cabin.get(i).getName().toString();
					// 拿出船舱类型名字对应的船舱类型id添加到类型id集合2
					scids2.add(cabin.get(i).getSCid());
				}
				// 遍历 shipPrice 准备建立所属关系 !
				for (int j = 0; j < ShipPrice.size(); j++) {
					// 如果shipPrice中的scid和 房间类型 的id是相同的
					// 拿到该id下的属于的船舱详细名称
					if (cabin.get(i).getSCid() == ShipPrice.get(j).getSCid()) {

						// nameStr[i] = ShipPrice.get(j).getName();
						// 只是传名称
						// nameStr.add(ShipPriceName.get(j).toString());
						// 传全部参数
						// 对应的shipprice添加到集合
						nameStr.add(ShipPrice.get(j));
					}
				}
				// System.out.println("nameStr ShipPricessssss "+nameStr);
				// 如果不为空,添加对应关系
				if (!StringUtils.isEmpty(nameStr)) {
					// 房间名称 : 房间详细集合
					map2.put(cabinName, nameStr);
				}
			}
		}
		return map2;
	}
}
