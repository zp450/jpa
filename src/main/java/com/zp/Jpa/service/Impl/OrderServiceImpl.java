package com.zp.Jpa.service.Impl;

import java.util.Calendar;
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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zp.Jpa.controller.ShipOrderController;
import com.zp.Jpa.entity.Order;
import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.entity.search.OrderSerach;
import com.zp.Jpa.repository.OrderRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.OrderService;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Suijishu;
import com.zp.Jpa.tools.UniqueOrderGenerate;
import com.zp.Jpa.tools.MD5.PasswordMD5;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;
import com.zp.tools.wexin.Weixin;

@Service
public class OrderServiceImpl implements OrderService {
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	@Autowired
	private ShipOrderController shipOrderController;
	@Autowired
	private ShipOrderServiceImpl shipOrderServiceImpl;

	@Override
	// 事务
	@Transactional(rollbackFor = Exception.class)
	// 基于类的代理模式
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Order addWeixinAiyouOrder(Order order, String ip) {
		try {

			Long ptring = order.getTotalPrice();
			// 如果本次充值订单超过999元
			if (ptring >= ConstantUtil.HUIYUANPRICE) {
				Users users = usersRepository.findOne(Integer.valueOf(order.getUid()));//
				// 就将本次的用户会员等级提升;
				if (!StringUtils.isEmpty(users)) {
					users.setUTid(7);
					usersRepository.save(users);
				}

			}
			System.out.println("传入的order=>" + order.toString());
			//设置oid
			String oid = Suijishu.getUuid();
			order.setOid(oid);
			//设置下单的时间
			Date ctime = new Date();
			order.setOrderTime(ctime);// 下单时间
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 15);// 订单支付截止时间为15分钟后
			Date endPayTime = c.getTime();
			order.setEndPayTime(endPayTime);// 截止支付时间
			UniqueOrderGenerate idWorker = new UniqueOrderGenerate(0, 0);// 订单号生成
			if (StringUtils.isEmpty(order.getOrderNo())) {
				//获得下一个ID
				long orderNo = idWorker.nextId();
				order.setOrderNo(String.valueOf(orderNo));
			}

			Users users = usersRepository.findOne(order.getUid());
			//给此订单的订单信息设置改用户信息
			if (!StringUtils.isEmpty(users.getUserName()) && !StringUtils.isEmpty(users.getTrueName())
					&& !StringUtils.isEmpty(users.getMobile())) {
				order.setUserName(users.getUserName());
				order.setOrderUserName(users.getTrueName());
				order.setMobile(users.getMobile());
				order.setPhone(users.getMobile());
			} else {
				String mString = "请先完善用户的个人信息,例如 用户的账户,用户的真实姓名,用户的手机号";
				map.put("msg", mString);
			}
			// 订单支付状态 0是未支付,1是支付
			order.setPayStatus(0);
			//订单状态
			order.setOrderStatus(1);
			order.setIsDel(0);
			// 没有传备注就设置为无备注
			if (StringUtils.isEmpty(order.getRemark())) {
				order.setRemark("无备注");
			}
			Order order2 = orderRepository.save(order);
			return order2;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("添加微信订单    在执行本次事务中出现意外异常,数据已回滚");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 如果updata2()抛了异常,updata()会回滚,不影响事物正常执行

		}
		return null;
	}

	/**
	 * 对订单进行支付 必传项pid,oid,uid 微信支付必传openid,ip 余额支付必传payPassWord
	 * 
	 * @param order
	 *            oid,uid,ip,pid,password,openid
	 * @return
	 */
	// 事务
	@Transactional(rollbackFor = Exception.class)
	// 基于类的代理模式
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Map<String, Object> pay(Order order) {
		System.out.println("pay方法   准备对订单进行支付");
		System.out.println("==========支付訂單參數驗證 begin==========");
		System.out.println("支付平台pid不為空=>" + order.getPid());
		System.out.println("訂單id oid不為空=>" + order.getOid());
		System.out.println("用戶id uid不為空=>" + order.getUid());
		System.out.println("==========微信必傳==========");
		System.out.println("微信openid不為空=>" + order.getOpenid());
		System.out.println("訪問ip不為空=>" + order.getIp());
		System.out.println("==========余额必傳==========");
		System.out.println("payPassWord 支付密碼不為空=>" + order.getPayPassWord());
		System.out.println("==========支付訂單參數驗證 over==========");

		// 判断支付方式进行//余额支付
		if (1 == (order.getPid())) {
			try {
				System.out.println("order=>" + order);
				//拿到改用户,对支付密码作出判断
				Integer uid = order.getUid();
				Users user2 = usersRepository.findOne(uid);
				//如果uid找不到,直接抛出结果
				if (StringUtils.isEmpty(user2)) {
					map.put("status", 0);
					map.put("msg", "提供的uid无法查询到该用户");
					return map;
				}
				// 用户名为盐,md5加密
				String newpassWord = PasswordMD5.getNewPassWord(user2.getUserName(), order.getPayPassWord());
				// 如果加密后密码与数据库中加密后密码相同就继续
				if (newpassWord.equals(user2.getPayPassWord())) {
					//余额支付
					Map<String, Object> balanceZhifuResult = balanceZhifu(order.getUid(), order.getOid());

					map.put("status", balanceZhifuResult.get("status"));
					map.put("msg", balanceZhifuResult.get("msg"));
					map.put("data", balanceZhifuResult.get("data"));

				} else {
					map.put("status", 0);
					map.put("msg", "支付失败,支付密码错误");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("余额支付    在执行本次事务中出现意外异常,数据已回滚");
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 如果updata2()抛了异常,updata()会回滚,不影响事物正常执行
			}
		} else if (9 == order.getPid()) {
			try {
				// 微信支付
				Map<String, Object> result = Weixin.wxPayApi(order, ConstantUtil.appid, ConstantUtil.mch_id,
						order.getIp(), order.getOpenid());
				map.put("msg", result.get("msg"));
				map.put("status", 1);
				order.setPayStatus(1);// 更改支付状态,订单状态
				order.setOrderStatus(2);
				//状态只能在        老系统的lg状态
//				order.setPayStatus(0);// 更改支付状态,订单状态
//				order.setOrderStatus(0);
				orderRepository.save(order);
			} catch (Exception e) {
				// 出错会滚状态
				System.out.println("微信支付 在执行本次事务中出现意外异常,数据已回滚");
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 如果updata2()抛了异常,updata()会回滚,不影响事物正常执行
				map.put("status", 0);
				map.put("msg", "微信支付异常");
				System.out.println("微信支付异常");
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {

			map.put("status", 0);
			map.put("msg", "payType为无状态,不是余额支付,也不是微信支付");
			return map;
		}

		return map;
	}

	@Override
	// 事务
	@Transactional(rollbackFor = Exception.class)
	// 基于类的代理模式
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Map<String, Object> balanceZhifu(Integer uid, String oid) {
		System.out.println("balanceZhifu方法   准备对订单进行余额支付");
		// TODO Auto-generated method stub
		Users users = usersRepository.findOne(uid);
		long balance = users.getBalance();
		Order order = orderRepository.findOne(oid);

		long price = order.getTotalPrice();
		// 修改过的订单id;以此为判断来进行结果输出,如果为null,就是支付失败,如果下面进行了修改,就不为空,代表支付成功;
		String oid2 = null;
		// 如果余额充足就减去价格保存
		if (balance > price) {
			balance = balance - price;
			users.setBalance(balance);
			try {
				// 保存减去余额的用户
				usersRepository.save(users);
				// 保存已经修改订单状态的订单
				order.setPayStatus(1);
				order.setOrderStatus(2);
				Order order2 = orderRepository.save(order);
				oid2 = order2.getOid();
				System.out.println("余额支付完成   !!!!!!!!!!!!");
				usersRepository.save(users);
				map.put("msg", "余额支付完成");
				map.put("data", oid2);
				map.put("status", 1);

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("在执行本次事务中出现意外异常,数据已回滚");
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 如果updata2()抛了异常,updata()会回滚,不影响事物正常执行
				map.put("msg", "本次余额出现异常,请联系官方微信,或稍后再试");
				map.put("status", 0);
				return map;
			}
		} else {
			map.put("msg", "余额不足,请充值后再试");
			map.put("data", oid2);
			map.put("status", 0);
		}
		return map;
	}

	public Order findOneByOid(String oid) {
		// TODO Auto-generated method stub
		return orderRepository.findOne(oid);
	}

	@Override
	public Page<Order> queryOrder(Integer page, Integer size, OrderSerach search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "orderTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return orderRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Order> getWhereClause(final OrderSerach search) {
		return new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合
				if (search.getUid() != null) {
					exList.add(cb.equal(root.<Integer>get("uid"), search.getUid()));
				}
				if (search.getOrderType() != null) {
					exList.add(cb.equal(root.<Integer>get("orderType"), search.getOrderType()));
				}

				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	public Order deleteOrder(String oid) {
		// TODO Auto-generated method stub

		Order order = orderRepository.findOne(oid);
		order.setIsDel(1);// 更改删除状态
		return orderRepository.save(order);
	}

	@Override // 添加订单 用航线订单
	public Map<String, Object> addOrderForShipOrder(ShipOrder shipOrder, ShipOrder shipOrder2) {
		// TODO Auto-generated method stub
		Order order1 = new Order();
		// 订单图片
		order1.setOrderImage(shipOrder2.getShipOrderImage());

		order1.setName(shipOrder2.getLinesName());
		order1.setUid(shipOrder2.getUid());// uid
		order1.setOrderType(10);// 订单类型为邮轮旅游
		order1.setTotalPrice(shipOrder2.getTotlePrice());
		// order1.setOid(shipOrder2.getSOid());
		order1.setOrderNo(shipOrder2.getOrderCn());
		order1.setPid(shipOrder2.getPayType());
		order1.setMobile(shipOrder2.getMobile());
		order1.setOpenid(shipOrder2.getOpenid());
		order1.setPayPassWord(shipOrder2.getPayPassWord());
		order1.setOrderStatus(1);// 1.已下单,未支付
		Order order2 = orderServiceImpl.addWeixinAiyouOrder(order1, shipOrder2.getIP());

		if (!StringUtils.isEmpty(shipOrder2.getPayType())) {
			order2.setPid(shipOrder2.getPayType());// pid
			order2.setIp(shipOrder2.getIP());
			order2.setPayPassWord(shipOrder2.getPayPassWord());
			System.out.println("准备支付的订单详情=>" + order2);
			map.put("order", order2);

			// map.put("pay", payResult);
		}
		//
		Integer success = shipOrderServiceImpl.addShioOrderPeopleInfo(shipOrder, shipOrder2);
		if (success > 0) {
			map.put("peopleNumberSuccess", success);
		}

		return map;
	}

	@Override
	public Order save(Order order) {
		// TODO Auto-generated method stub
		return orderRepository.save(order);
	}
}
