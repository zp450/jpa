package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.Group;
import com.zp.Jpa.entity.search.GroupSearch;
import com.zp.Jpa.service.GroupService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/group", name = "邮轮定制模块")
public class GroupController {
	 private Map<String, Object> map = new HashMap<String, Object>();
		@Autowired
		public GroupService groupService;
		/**
		 * @param groupSearch
		 * @return
		 */
		@ApiOperation(value = "邮轮定制模块", notes = "邮轮定制模块")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "page",value = "页数,如果是前台页面显示,传1就好",required=true,defaultValue="1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size",value = "要多少条", dataType = "int",required=true,defaultValue="10", paramType = "query"),
			@ApiImplicitParam(name = "isDel",value = "是否已经删除", dataType = "integer",required=true,defaultValue="0", paramType = "query"),})
			@RequestMapping(value="queryGroup",name="多条件查询",method = RequestMethod.GET)
			public Object queryGroup( GroupSearch groupSearch) {
			//分页查询  page size  查询条件     page 第几页  size 多少条
			// 为空 就是给0 未删除
			if (StringUtils.isEmpty(groupSearch.getIsDel())) {
				groupSearch.setIsDel(0);
			} 
				Page<Group> pages = groupService.queryGroup(groupSearch.getPage()-1, groupSearch.getSize(),groupSearch);		
				return MapTool.page(pages);
			}
		
		@ApiOperation(value = "增加邮轮定制", notes = "增加邮轮定制")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "Group", required=true, dataType = "Group", paramType = "query"),
			})
		@RequestMapping(value="addGroup",name="增加邮轮定制",method = RequestMethod.POST)
		public Group addGroup(Group group) {
			return groupService.addGroup(group);
		}
		@ApiOperation(value = "删除邮轮定制", notes = "删除邮轮定制")
		@ApiImplicitParams({
			@ApiImplicitParam(name = "gid",value = "主键",required=true, dataType = "int", paramType = "query"),
			})
		@RequestMapping(value="deleteGroup",name="删除邮轮定制",method = RequestMethod.POST)
		public Map<String, Object> deleteGroup(Integer gid) {
			Group group=groupService.deleteGroup(gid);
			if(StringUtils.isEmpty(group)) {
				map.put("status", 1);
				map.put("msg", "删除成功");
			}
			
			return map;
		}
		
}
