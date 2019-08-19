package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.GroupOrder;
import com.zp.Jpa.entity.search.GroupOrderSearch;
import com.zp.Jpa.service.GroupOrderService;
import com.zp.Jpa.tools.MapTool;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/groupOrder", name = "邮轮定制订单模块")
public class GroupOrderController {
	 Map<String, Object> map = new HashMap<String, Object>();
		@Autowired
		public GroupOrderService groupOrderService;
		/**
		 * @param groupSearch
		 * @return
		 */
		@ApiOperation(value = "邮轮定制订单多条件查询", notes = "邮轮定制订单模块")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "page",value = "页数,如果是前台页面显示,传1就好",required=true,defaultValue="1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size",value = "要多少条", dataType = "int",required=true,defaultValue="10", paramType = "query"),
			@ApiImplicitParam(name = "isDel",value = "是否已经删除", dataType = "integer",required=true,defaultValue="0", paramType = "query"),})
			@RequestMapping(value="queryGroupOrder",name="多条件查询",method = RequestMethod.GET)
			public Object queryGroupOrder( GroupOrderSearch groupOrderSearch) {
			//分页查询  page size  查询条件     page 第几页  size 多少条
				Page<GroupOrder> pages = groupOrderService.queryGroupOrder(groupOrderSearch.getPage()-1, groupOrderSearch.getSize(),groupOrderSearch);		
				return MapTool.page(pages);
			}
		
		@ApiOperation(value = "增加邮轮定制订单", notes = "增加邮轮定制订单")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "GroupOrder", required=true, dataType = "GroupOrder", paramType = "query"),
			})
		@RequestMapping(value="addGroupOrder",name="增加邮轮定制订单",method = RequestMethod.POST)
		public GroupOrder addGroupOrder(GroupOrder groupOrder) {
			return groupOrderService.addGroupOrder(groupOrder);
		}
		@ApiOperation(value = "删除邮轮定制订单", notes = "删除邮轮定制订单")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "gOid",value = "主键",required=true, dataType = "int", paramType = "query"),
			})
		@RequestMapping(value="deleteGroup",name="删除邮轮定制订单",method = RequestMethod.POST)
		public Map<String, Object> deleteGroup(String gOid) {
			Integer success=groupOrderService.deleteGroupOrder(gOid);
			if(success>0) {
				map.put("status", 1);
				map.put("msg", "删除成功");
			}
			
			return map;
		}
}
