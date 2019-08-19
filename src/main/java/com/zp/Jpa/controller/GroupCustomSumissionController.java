package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.GroupCustomSumission;
import com.zp.Jpa.entity.search.GroupCustomSumissionSearch;
import com.zp.Jpa.service.GroupCustomSumissionService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/groupCustomSumission", name = "邮轮定制提交模块")
public class GroupCustomSumissionController {
	Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public GroupCustomSumissionService service;
	/**
	 * @param groupSearch
	 * @return
	 */
	@ApiOperation(value = "邮轮定制提交多条件查询", notes = "邮轮定制提交查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page",value = "页数,如果是前台页面显示,传1就好",required=true,defaultValue="1", dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "size",value = "要多少条", dataType = "int",required=true,defaultValue="10", paramType = "query"),
		@ApiImplicitParam(name = "isDel",value = "是否已经删除", dataType = "integer",required=true,defaultValue="0", paramType = "query"),})
		@RequestMapping(value="queryGroupOrder",name="邮轮定制提交多条件查询",method = RequestMethod.GET)
		public Object queryGroupOrder( GroupCustomSumissionSearch search) {
		// 为空 就是给0 未删除
					if (StringUtils.isEmpty(search.getIsDel())) {
						search.setIsDel(0);
					}
		//分页查询  page size  查询条件     page 第几页  size 多少条
			Page<GroupCustomSumission> pages = service.queryGroupCustomSumission(search.getPage()-1, search.getSize(),search);		
			return MapTool.page(pages);
		}
	
	@ApiOperation(value = "增加邮轮定制提交", notes = "增加邮轮定制提交")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "GroupCustomSumission", required=true, dataType = "GroupCustomSumission", paramType = "query"),
		})
	@RequestMapping(value="addGroupCustomSumission",name="增加邮轮定制提交",method = RequestMethod.POST)
	public Map<String, Object> addGroupCustomSumission(GroupCustomSumission groupCustomSumission) {
		GroupCustomSumission groupCustomSumission2=service.addGroupCustomSumission(groupCustomSumission);
		
		return MapTool.saveObject(groupCustomSumission2, "邮轮定制提交"); 
	}
	@ApiOperation(value = "删除邮轮定制提交", notes = "删除邮轮定制订提交")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "csId",value = "主键",required=true, dataType = "int", paramType = "query"),
		})
	@RequestMapping(value="deleteGroupCustomSumission",name="删除邮轮定制提交",method = RequestMethod.POST)
	public Map<String, Object> deleteGroupCustomSumission(Integer csId) {
		Integer success=service.deleteGroupCustomSumission(csId);
		if(success>0) {
			map.put("status", 1);
			map.put("msg", "删除成功");
		}
		
		return map;
	}
	@ApiOperation(value = "修改邮轮定制提交", notes = "修改邮轮定制提交更改一些审核状态,什么玩意儿的")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "GroupCustomSumission", required=true, dataType = "GroupCustomSumission", paramType = "query"),
		})
	@RequestMapping(value="saveGroupCustomSumission",name="修改邮轮定制提交",method = RequestMethod.POST)
	public Map<String, Object> saveGroupCustomSumission(GroupCustomSumission groupCustomSumission) {
		GroupCustomSumission groupCustomSumission2=service.saveGroupCustomSumission(groupCustomSumission);
		
		return MapTool.saveObject(groupCustomSumission2, "邮轮定制提交 修改完成");
	}
}
