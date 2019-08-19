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

import com.zp.Jpa.entity.LinkMan;
import com.zp.Jpa.entity.Order;
import com.zp.Jpa.entity.ShipOrder;
import com.zp.Jpa.entity.ShipOrderPeople;
import com.zp.Jpa.entity.search.ShipOrderSerach;
import com.zp.Jpa.service.LinkManService;
import com.zp.Jpa.service.ShipOrderService;
import com.zp.Jpa.service.Impl.OrderServiceImpl;
import com.zp.Jpa.service.Impl.ShipOrderPeopleServiceImpl;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Date.DateUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/shipOrder", name = "邮轮航线订单模块")
public class ShipOrderController {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public ShipOrderService shipOrderService;
	@Autowired
	public LinkManService linkManService;

	@Autowired
	public OrderServiceImpl orderServiceImpl;
	@Autowired
	ShipOrderPeopleServiceImpl shipOrderPeopleServiceImpl;

	@ApiOperation(value = "添加邮轮旅游订单", notes = "下單加支付   微信手机端邮轮旅游下单  pid oid uid 必传 餘額 支付      passowrd  要么    微信支付  openid 和ip地址")
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pid", value = "支付方式  1余额支付 2线下支付 9微信支付 ", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "oid", value = "订单id", required = true, dataType = "string", paramType = "query", defaultValue = "10026FD2-8483-46C9-9E3E-086F06421063"),
		@ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "int", paramType = "query", defaultValue = "830"),
		@ApiImplicitParam(name = "payPassWord", value = "余额支付密码", dataType = "int", paramType = "query", defaultValue = "123456"),
		@ApiImplicitParam(name = "ip", value = "支付ip地址", dataType = "string", paramType = "query", defaultValue = "123456"),
		@ApiImplicitParam(name = "openid", value = "微信openid", dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "shipOrder", value = "邮轮旅游订单", required = true, dataType = "ShipOrder", paramType = "query")})
	
	@RequestMapping(value = "addShipOrder", name = "添加邮轮旅游订单", method = RequestMethod.POST)
	public Map<String, Object> addShipOrder(ShipOrder shipOrder, HttpServletRequest request,
			HttpServletResponse response) {
		//要求订单的手机号不能为空,,,,如果出现16666666666的手机号,该用户就没有手机号mobile字段
		if (StringUtils.isEmpty(shipOrder.getMobile())) {
			shipOrder.setMobile("16666666666");
			map.put("wromgMsg", "提交訂單是為提供手機號");
		}
		//ip地址获取,如果没有传的话,就从request中获取
		if (StringUtils.isEmpty(shipOrder.getIP())) {
			String ip = request.getRemoteAddr();
			shipOrder.setIP(ip);// 实际请求ip
		}
		// 先添加邮轮订单表
		ShipOrder shipOrder2 = shipOrderService.saveShipOrder(shipOrder);
		if (shipOrder2 != null) {
			map.put("msg", "下单成功");
			map.put("status", 1);
		}
		if (shipOrder2.getPayPassWord() == null || shipOrder2.getOpenid()==null) {
			map.put("msg", "下单成功,但缺少支付信息,密碼或微信openid為空");
			map.put("status", 1);
		}
		if (StringUtils.isEmpty(shipOrder2)) {return map;}
		// 再添加订单表   order中要包含所有的訂單信息    
		Map<String, Object> orderAddResult = orderServiceImpl.addOrderForShipOrder(shipOrder, shipOrder2);
		//对订单进行支付
		Map<String, Object> payResult=shipOrderPay((Order)orderAddResult.get("order"));
		map.put("status", payResult.get("status"));
		map.put("msg", payResult.get("return_msg"));
		map.put("oid", payResult.get("oid"));
		
		map.put("peopleNumberSuccess", orderAddResult.get("peopleNumberSuccess"));

		return map;
	}

	/**
	 * @param order2
	 * @return
	 */
	@ApiOperation(value = "航线订单支付", notes = "pid oid uid 必传  支付      passowrd  要么    微信支付  openid 和ip地址")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pid", value = "支付方式  1余额支付 2线下支付 9微信支付 ", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "oid", value = "订单id", required = true, dataType = "string", paramType = "query", defaultValue = "10026FD2-8483-46C9-9E3E-086F06421063"),
			@ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "int", paramType = "query", defaultValue = "830"),
			@ApiImplicitParam(name = "payPassWord", value = "余额支付密码", dataType = "int", paramType = "query", defaultValue = "123456"),
			@ApiImplicitParam(name = "ip", value = "支付ip地址", dataType = "string", paramType = "query", defaultValue = "123456"),
			@ApiImplicitParam(name = "openid", value = "微信openid", dataType = "string", paramType = "query") })
	@RequestMapping(value = "shipOrderPay", name = "航线订单支付", method = RequestMethod.GET)
	public Map<String, Object> shipOrderPay(Order order2) {
		Order order = orderServiceImpl.findOneByOid(order2.getOid());
		if (order.getPayStatus() == 1) {
			map.put("oid", order2.getOid());
			map.put("return_msg", "无须重复支付,支付失败");
			map.put("status", 0);
			return map;
		}
		Map<String, Object> payResult = orderServiceImpl.pay(order2);
		map.put("oid", order2.getOid());
		map.put("return_msg", payResult.get("msg"));
		map.put("status", payResult.get("status"));
		// order表里的是orderno shiporder里的是ordercn 脑子瓦特了?
		// 更改子订单,就是订单分支状态shioOrder
		List<ShipOrder> shipOrder = shipOrderService.findByOrderCn(order.getOrderNo());
		System.out.println("准备更改状态的shipOrder=>" + shipOrder);
		if (shipOrder.size() > 0) {
			shipOrder.get(0).setPayStatus(1);
			shipOrder.get(0).setOrderStatus(2);
			// ShipOrder shipOrder2 = shipOrderService.save(shipOrder.get(0));
			shipOrderService.save(shipOrder.get(0));
		} else {
			map.put("remark", "shioOrder状态未更改");
		}

		return map;
	}

	/**
	 * http://localhost:8081/shipOrder/selectShipOrderForSoid?soid=677C4A21-480B-4FC2-A025-00A5E053D7FD
	 * 
	 * @param soid
	 * @return
	 */
	@ApiOperation(value = "查询邮轮旅游订单详情", notes = "微信手机端邮轮旅游查单")
	@ApiImplicitParam(name = "soid", value = "订单id", required = true, dataType = "String", defaultValue = "677C4A21-480B-4FC2-A025-00A5E053D7FD", paramType = "query")
	@RequestMapping(value = "selectShipOrderForSoid", name = "查询邮轮旅游订单详情", method = RequestMethod.GET)
	public ShipOrder selectShipOrderForSoid(String soid) {
		return shipOrderService.findShipOrder(soid);
	}

	@ApiOperation(value = "邮轮订单查询", notes = "查询")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"),
			
			 })
	@RequestMapping(value = "/queryShipOrder", name = "邮轮订单查询", method = RequestMethod.GET)
	public Object queryShipOrder(ShipOrderSerach shipOrderSerach) {
		//Date begin=shipOrderSerach.getBegin();
		System.out.println("shipOrderSerach=>"+shipOrderSerach);
		if(!StringUtils.isEmpty(shipOrderSerach.getBegin())) {
			shipOrderSerach.setBegin(DateUtil.getLingLingLing(shipOrderSerach.getBegin()));
		}
		if(!StringUtils.isEmpty(shipOrderSerach.getEnd())) {
		shipOrderSerach.setEnd(DateUtil.getErsanWujiuWujiu(shipOrderSerach.getEnd()));}
		System.out.println("shipOrderSerach=>"+shipOrderSerach);
		if (StringUtils.isEmpty(shipOrderSerach.getIsDel())) {
			shipOrderSerach.setIsDel(0);
		} // 为空 就是给0 未删除
		// 分页查询
		
		Page<ShipOrder> pages = shipOrderService.queryShipOrder(shipOrderSerach.getPage() - 1, shipOrderSerach.getSize(), shipOrderSerach);
		
		List<ShipOrder> shipOrders=pages.getContent();
		for(ShipOrder s:shipOrders) {
			//拿到船员
			String orderCn=s.getOrderCn();
			List<ShipOrderPeople> shipOrderPeopleList=shipOrderPeopleServiceImpl.findByOrderCn(orderCn);
			//遍历这个订单下的这群人,
			for(ShipOrderPeople people:shipOrderPeopleList) {
				Integer lkId=people.getLmId();
				LinkMan linkMan=linkManService.findOne(lkId);
				people.setLinkMan(linkMan);
			}
			
			s.setShipOrderPeopleList(shipOrderPeopleList);
			//shipOrderService.save(s);
		}
		//System.out.println("pages.data="+pages.getContent());
		
		map.put("count", pages.getTotalElements());
		map.put("data", shipOrders);
		return map;
	}
	//http://localhost:8080/major/deleteAll?objs[]=3&objs[]=4
	@ApiOperation(value = "批量删除旅游订单", notes = "删除旅游订单")
	@ApiImplicitParam(name = "soids", value = "订单id", required = true, dataType = "String", defaultValue = "soids[]=3&soids[]=4", paramType = "query")
	@RequestMapping(value = "deleteOrders", name = "删除旅游订单", method = RequestMethod.POST)
	public Map<String, Object> deleteOrders(@RequestParam(value="soids",required=false) String soids) {
		System.out.println("soids="+soids);
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(soids);
		stringBuilder.replace(0, 1, "");
		stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(), "");
		String aString=stringBuilder.toString();
		String[]  ss=aString.split(",");
		int success=0;
		for(String soid:ss) {
			StringBuilder stringBuilder1=new StringBuilder();
			stringBuilder1.append(soid);
			stringBuilder1.replace(0, 1, "");
			stringBuilder1.replace(stringBuilder1.length()-1,stringBuilder1.length(), "");
			ShipOrder shipOrder=shipOrderService.deleteShipOrder(stringBuilder1.toString());
			if(!StringUtils.isEmpty(shipOrder)) {success+=1;}
		}
		if(success>0) {
			map.put("status", 1);
			map.put("msg", "成功删除"+success+"个旅游订单");
		}else {
			map.put("status", 0);
			map.put("msg", "删除失败");
		}
		
		return map;
	}
	@ApiOperation(value = "删除旅游订单", notes = "删除旅游订单(用户看不到,但是数据库中存在)")
	@ApiImplicitParam(name = "soid", value = "订单id", required = true, dataType = "String", defaultValue = "677C4A21-480B-4FC2-A025-00A5E053D7FD", paramType = "query")
	@RequestMapping(value = "deleteOrder", name = "删除旅游订单", method = RequestMethod.POST)
	public Map<String, Object> deleteOrder(String soid) {
		ShipOrder shipOrder=shipOrderService.deleteShipOrder(soid);
		 Map<String, Object> map2 = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(shipOrder)) {
			map2.put("status", 1);
			map2.put("msg", "成功删除旅游订单");
			return map2;
		}
		return map2;
	}
	@ApiOperation(value = "移除旅游订单", notes = "删除旅游订单 ,从数据库 中删除,真正的删除")
	@ApiImplicitParam(name = "soid", value = "订单id", required = true, dataType = "String", defaultValue = "677C4A21-480B-4FC2-A025-00A5E053D7FD", paramType = "query")
	@RequestMapping(value = "removeOrder", name = "移除删除旅游订单", method = RequestMethod.POST)
	public void removeOrder(String soid) {
		shipOrderService.removeShipOrder(soid);
	}
	
}
