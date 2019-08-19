package com.zp.Jpa.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.Ship;
import com.zp.Jpa.entity.ShipDayPrice;
import com.zp.Jpa.entity.ShipExtensions;
import com.zp.Jpa.entity.ShipPrice;
import com.zp.Jpa.entity.search.ShipSearch;
import com.zp.Jpa.service.ShipCabinService;
import com.zp.Jpa.service.ShipDayPriceService;
import com.zp.Jpa.service.ShipExtensionsService;
import com.zp.Jpa.service.ShipPriceService;
import com.zp.Jpa.service.ShipService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Date.DateUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/ship", name = "邮轮航线商城模块(微信)")
public class ShipController {
	public  final Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public  ShipService shipService;
	@Autowired
	public  ShipExtensionsService shipExService;
	@Autowired
	public  ShipDayPriceService shipDayPriceService;
	@Autowired
	public  ShipCabinService shipCabinService;
	@Autowired
	public  ShipPriceService shipPriceService;

	/**
	 * http://localhost:8081/ship/queryShip?page=1&size=10&recommend=1&isDel=0&sRid=1
	 * http://localhost:8081/ship/queryShip?page=1&size=10&isDel=0
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@ApiOperation(value = "航线多条件查询", notes = "获取邮轮航线多个信息page=1&size=10&isDel=0")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好,", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "size", value = "要多少条", required = true, dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "isDel", value = "isDel,代表未删除的,不传默认为0", required = false, dataType = "Integer", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "sRid", value = "航线地区id", required = false, dataType = "Integer", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "search", value = "条件", required = false, dataType = "int", paramType = "ShipSearch"),})
	@RequestMapping(value = "queryShip", name = "航线多条件查询", method = RequestMethod.GET)
	public Object queryShip(ShipSearch search) {
		// 算出本月时间区间
		Integer month = search.getGoMonth();
		if (!StringUtils.isEmpty(month)) {
			Map<String, Date> mo = DateUtil.getMonthBeginAndEnd(month);
			search.setBegin(mo.get("begin"));
			search.setEnd(mo.get("end"));
		}
		//根据查询条件中的isdel来判断前台和后台,,如果有isDel这个参数,那么就判断是前台
		// 未删除数据判断
		if (StringUtils.isEmpty(search.getIsDel())) {
			//给定现在时间  ,用户就会查看不到已过期的航线
			search.setNowTime(new Date());
			search.setIsDel(0);
		}
		
		
		
		// 分页查询 page size 查询条件 page 第几页 size 多少条
		Page<Ship> pages = shipService.queryShip(search.getPage() - 1, search.getSize(), search);
//		List<Ship> s=pages.getContent();
//		for(Ship ship:s) {
//			System.out.println(ship.getValidPlansLastTime());
//		}
		
		return MapTool.page(pages);
	}

	/**
	 * http://localhost:8081/ship/selectShip?sid=4
	 * 
	 * @param sid
	 * @return
	 */
	@ApiOperation(value = "航线查询详情", notes = "获取邮轮航线信息详情")
	@ApiImplicitParam(name = "sid", value = "邮轮id", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "selectShip", name = "航线轮船查询详情", method = RequestMethod.GET)
	public  Ship selectShip(Integer sid) {
		return shipService.findShip(sid);
	}

	/**
	 * http://localhost:8081/ship/selectById?sid=272
	 * 
	 * @param sid
	 * @return
	 */
	@ApiOperation(value = "航线基本信息说明详情", notes = "费用,材料,特殊要求之类的信息")
	@ApiImplicitParam(name = "sid", value = "邮轮id", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "selectById", name = "航线轮船查询详情", method = RequestMethod.GET)
	public  List<ShipExtensions> selectById(Integer sid) {
		return shipExService.selectBySid(sid);
	}

	/**
	 * http://localhost:8081/ship/selectPriceById?sid=2
	 * 
	 * @param sid
	 * @return
	 */
	@ApiOperation(value = "航线房间信息价格详情", notes = "费用,材料,特殊要求之类的信息")
	@ApiImplicitParam(name = "sid", value = "邮轮id", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "selectPriceById", name = "航线轮船查询详情", method = RequestMethod.GET)
	public  List<ShipDayPrice> selectPriceById(Integer sid) {
		return shipDayPriceService.selectBySid(sid);
	}

	/**
	 * http://localhost:8081/ship/selectShipRoom?sid=2489&outTime=2020-03-18
	 * 00:00:00.000
	 * 
	 * @param sid
	 * @param outTime
	 * @return
	 * @throws ParseException
	 *             2020-03-18 00:00:00.000 03/18/2020
	 *             http://localhost:8081/ship/selectShipRoom?sid=2489&outTime=03/18/2020
	 * @throws InterruptedException
	 */
	@ApiOperation(value = "航线船舱查询", notes = "客舱类型")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sid", value = "邮轮id", required = true, dataType = "int", paramType = "query", defaultValue = "2489"),
			@ApiImplicitParam(name = "outTime", value = "邮轮出发时间", required = true, dataType = "date", paramType = "query", defaultValue = ""), })
	@RequestMapping(value = "selectShipRoom", name = "航线船舱查询", method = RequestMethod.GET)
	public  Object selectShipRoom(Integer sid, Date outTime) throws ParseException, InterruptedException {
		
		return shipService.selectShipRoomBySidAndOutTime(sid, outTime);
	}

	/**
	 * http://localhost:8081/ship/getLDPid?sid=2489&outTime=03/18/2020
	 * 
	 * @param sid
	 * @param outTime
	 * @return
	 */
	@ApiOperation(value = "线路日期价格", notes = "获取线路日期价格 LDPid")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sid", value = "邮轮id", required = true, dataType = "int", paramType = "query", defaultValue = "2489"),
			@ApiImplicitParam(name = "outTime", value = "邮轮出发时间", required = true, dataType = "date", paramType = "query", defaultValue = ""), })
	@RequestMapping(value = "getLDPid", name = "航线船舱查询   线路日期价格id", method = RequestMethod.GET)
	public Integer getLDPid(Integer sid, Date outTime) {
		List<ShipPrice> ShipPrice = shipPriceService.selectBySidAndOutTime(sid, outTime);
		Integer lpid = ShipPrice.get(0).getLPid();// 线路日期价格id
		return lpid;
	}
}