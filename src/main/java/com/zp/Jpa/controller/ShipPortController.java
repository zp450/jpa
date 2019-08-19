package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.ShipPort;
import com.zp.Jpa.entity.search.ShipPortSearch;
import com.zp.Jpa.service.ShipPortService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/shipPort", name = "邮轮港口模块")
public class ShipPortController {

	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public ShipPortService shipPortService;
	
	@ApiOperation(value = "邮轮大全查询", notes = "查询")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"),
			
			 })
	@RequestMapping(value = "/queryShipPort", name = "邮轮大全查询", method = RequestMethod.GET)
	public Map<String, Object> queryShipPort(ShipPortSearch shipPortSearch){
		System.out.println("shipPortSearch=>"+shipPortSearch);
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(shipPortSearch.getIsDel())) {
			shipPortSearch.setIsDel(0);
		} 
		// 分页查询	
		Page<ShipPort> pages = shipPortService.queryShipPort(shipPortSearch.getPage() - 1, shipPortSearch.getSize(), shipPortSearch);
		return MapTool.page(pages);
	}
	
}
