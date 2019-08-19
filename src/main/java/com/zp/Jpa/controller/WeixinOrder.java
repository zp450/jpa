package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.Order;
import com.zp.Jpa.entity.Ship;
import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.ShipOrderPeople;
import com.zp.Jpa.entity.search.OrderSerach;
import com.zp.Jpa.repository.OrderRepository;
import com.zp.Jpa.service.Impl.OrderServiceImpl;
import com.zp.Jpa.service.Impl.ShipOrderPeopleServiceImpl;
import com.zp.Jpa.service.Impl.ShipOrderServiceImpl;
import com.zp.Jpa.service.Impl.ShipServiceImpl;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONException;

@RestController
@CrossOrigin
@RequestMapping(value = "/weixinOrder", name = "微信订单模块")
public class WeixinOrder {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl orderServiceImpl;
	@Autowired
	public ShipOrderServiceImpl shipOrderServiceImpl;
	@Autowired
	public ShipOrderPeopleServiceImpl shipOrderPeopleServiceImpl;
	@Autowired
	public ShipServiceImpl shipServiceImpl;

	/**
	 * http://localhost:8081/weixinOrder/addOrder?totalPrice=213&uid=6032&orderType=9&name=充值订单&ip=180.155.223.182&openid=oFwxwtzxw40O4ZlYerRFuvXtJSDI
	 * 微信下单
	 * 
	 * @param uid
	 *            request response
	 * 
	 * @return users
	 * @throws JSONException
	 */
	@ApiOperation(value = "微信添加订单", notes = "添加订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "order", value = "订单信息", required = true, dataType = "Order"),
			@ApiImplicitParam(name = "ip", value = "访问机器ip地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "openid", value = "用户携带的openid", required = true, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/addOrder", name = "微信添加订单", method = RequestMethod.POST)
	public Object addOrder(Order order, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 保存商品图片
		if (StringUtils.isEmpty(order.getIp())) {
			// 实际请求ip
			String ip = request.getRemoteAddr();
			order.setIp(ip);
		}
		// 如果没有传pid那么就给pid设为9 微信支付方式
		if (StringUtils.isEmpty(order.getPid())) {
			order.setPid(9);
		} 
		// 添加微信爱游卡支付订单
		Order order2 = orderServiceImpl.addWeixinAiyouOrder(order, order.getIp());
		//支付  对生成的订单进行支付
		Object object = orderServiceImpl.pay(order2);
		if (StringUtils.isEmpty(object)) {
			map.put("oid", order2.getOid());
			map.put("return_msg", "支付失败");
			return map;
		} // 支付成功的订单id
		map.put("oid", order2.getOid());
		map.put("return_msg", object);
		//支付成功后修改订单状态
		order2.setOrderStatus(2);
		order2.setPayStatus(1);
		orderServiceImpl.save(order2);
		return map;
	}

	// public Object addWeixinOrder(Order order, String ip, String openid,
	// HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// // 实际请求ip
	// ip = request.getRemoteAddr();
	// Order order2 = orderServiceImpl.addWeixinAiyouOrder(order, ip, openid);
	// if (order2 != null && !StringUtils.isEmpty(ip)) {
	// map.put("msg", "添加订单成功");
	// Map<String, Object> object = Weixin.wxPayApi(order2, ConstantUtil.appid,
	// ConstantUtil.mch_id, ip, openid);
	// //支付成功的订单id
	// map.put("oid", order2.getOid());
	// map.put("return_msg", object);
	// return map;
	// }
	// return "添加订单失败";
	// }
	/**
	 * http://localhost:8081/weixinOrder/getOrderForUidAndOrderType?uid=6032&orderType=9&page=1&size=10
	 * 
	 * @param uid
	 *            orderType订单类型 类型表是ordermodule
	 * @return List<Order>
	 */
	@ApiOperation(value = "微信查询某个用户的订单", notes = "查询")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"),
			@ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "orderType", value = "订单类型  充值类型是9", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getOrderForUidAndOrderType", name = "微信查询订单", method = RequestMethod.GET)
	public Object getOrderForUidAndOrderType(OrderSerach orderSerach) {
		if (StringUtils.isEmpty(orderSerach.getIsDel())) {
			orderSerach.setIsDel(0);
		} // 为空 就是给0 未删除
		// 分页查询
		Page<Order> pages = orderServiceImpl.queryOrder(orderSerach.getPage() - 1, orderSerach.getSize(), orderSerach);
		return MapTool.page(pages);
	}

	/**
	 * http://localhost:8081/weixinOrder/getOrderInfoForOid?oid=1435957C-A8DF-49CF-B7B2-6B88D14E7D0B
	 * 
	 * @param oid
	 * @return
	 */
	@ApiOperation(value = "微信查询订单详情", notes = "详细信息")
	@ApiImplicitParam(name = "oid", value = "订单id", required = true, paramType = "path")

	@RequestMapping(value = "/getOrderInfoForOid", name = "微信查询订单详情", method = RequestMethod.GET)
	public Map<String, Object> getOrderInfoForOid(@RequestParam(value = "oid", required = true) String oid) {
		Map<String, Object> result = new HashMap<String, Object>();
		//查询出该订单
		Order order = orderRepository.findOne(oid);
		result.put("order", order);// 订单
		//orderCn是与其他订单表关联的
		String orderCn = order.getOrderNo();// 订单表和其他表的关联
		//试着根据ordercn查询出shipOrder 
		List<ShipOrder> shipOrder = shipOrderServiceImpl.findByOrderCn(orderCn);
		//一般是一个ordercn对应一个数据库  
		//防止空指针
		if(shipOrder.size()>0||!StringUtils.isEmpty(shipOrder.get(0))) {
			Integer sid = shipOrder.get(0).getSid();
			//拿到ship   航线   ,这个数据库不是我设计的,
			Ship ship = shipServiceImpl.findShip(sid);
			result.put("ship", ship);
		}
		
		if (shipOrder.size() > 0) {
			// 邮轮航线订单
			result.put("shipOrder", shipOrder.get(0));
		} 
		//放入订单航线对应的用户
		List<ShipOrderPeople> shipOrderPeoples = shipOrderPeopleServiceImpl.findByOrderCn(orderCn);
		result.put("shipOrderPeoples", shipOrderPeoples);// 邮轮订单人员

		map.put("stats", 1);
		map.put("msg", "查询结果");
		map.put("data", result);
		return map;
	}

	/**
	 * http://localhost:8081/weixinOrder/deleteOrderByOid?oid=53AF233E-460F-4634-A601-0092480B02FF
	 * 
	 * @param oid
	 * @return
	 */
	@ApiOperation(value = "删除订单", notes = "删除")
	@ApiImplicitParam(name = "oid", value = "订单id", required = true, dataType = "String", paramType = "path")

	@RequestMapping(value = "/deleteOrderByOid", name = "微信删除订单", method = RequestMethod.GET)
	public Integer deleteOrderByOid(String oid) {
		Order order = orderServiceImpl.deleteOrder(oid);
		if (order != null) {
			return 1;
		}
		return 0;
	}
}
