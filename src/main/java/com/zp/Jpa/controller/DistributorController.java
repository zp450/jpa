package com.zp.Jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.Distributor;
import com.zp.Jpa.entity.search.DistributorSearch;
import com.zp.Jpa.service.DistributorService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/distributor", name = "网络分销商模块")
public class DistributorController{
	@Autowired
	public DistributorService distributorService;
	//http://localhost:8081/distributor/queryDistributor?page=1&size=10&isDel=0
	@ApiOperation(value = "网络分销商查询", notes = "网络分销商查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page",value = "页数,如果是前台页面显示,传1就好",required=true,defaultValue="1", dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "size",value = "要多少条", dataType = "int",required=true,defaultValue="10", paramType = "query"),
		@ApiImplicitParam(name = "isDel",value = "是否已经删除", dataType = "integer",required=true,defaultValue="0", paramType = "query"),})
		@RequestMapping(value="queryDistributor",name="多条件查询",method = RequestMethod.GET)
		public Object queryDistributor( DistributorSearch distributorSearch) {
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(distributorSearch.getIsDel())) {
			distributorSearch.setIsDel(0);
		} 
		//分页查询  page size  查询条件     page 第几页  size 多少条
			Page<Distributor> pages = distributorService.queryDistributor(distributorSearch.getPage()-1, distributorSearch.getSize(),distributorSearch);		
			return MapTool.page(pages);
		}
}
