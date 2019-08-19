package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.Travel;
import com.zp.Jpa.entity.search.TravelSearch;
import com.zp.Jpa.service.TravelService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value="/travel" ,name="游记模块")
public class TravelController {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public TravelService travelService;
	
	@ApiOperation(value = "游记查询", notes = "查询")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = false, defaultValue = "0", paramType = "query"),
			
			 })
	@RequestMapping(value = "/queryTravel", name = "游记查询", method = RequestMethod.GET)
	public Map<String, Object> queryTravel(TravelSearch travelSearch){
		System.out.println("shipPortSearch=>"+travelSearch);
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(travelSearch.getIsDel())) {
			travelSearch.setIsDel(0);
		} 
		// 分页查询	
		Page<Travel> pages = travelService.queryTravel(travelSearch.getPage() - 1, travelSearch.getSize(), travelSearch);
		return MapTool.page(pages);
	}
}
