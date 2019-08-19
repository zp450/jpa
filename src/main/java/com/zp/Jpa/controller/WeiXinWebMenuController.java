package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.WeiXinWebMenu;
import com.zp.Jpa.entity.search.WeiXinWebMenuSearch;
import com.zp.Jpa.service.WeiXinWebMenuService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/weiXinWebMenuController", name = "首页导航分页多条件查询模块")
public class WeiXinWebMenuController {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public WeiXinWebMenuService weiXinWebMenuService;

	@ApiOperation(value = "首页导航多条件查询", notes = "微信首页导航多条件查询page=1&size=10&isDel=0,")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好,", required = true,defaultValue="1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", required = true,defaultValue="10",  dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "isDel", value = "是否删除,不填默认给0,未删除", required = true,defaultValue="0",  dataType = "integer", paramType = "query")})
	@RequestMapping(value = "queryWeixinWebMenu", name = "多条件查询首页导航", method = RequestMethod.GET)
	public Object queryWeixinWebMenu( WeiXinWebMenuSearch search) {
		if(StringUtils.isEmpty(search.getIsDel())) {
			search.setIsDel(0);
		}
		//日常分页查询
		Page<WeiXinWebMenu> pages = weiXinWebMenuService.queryWeiXinWebMenu(search.getPage() - 1, search.getSize(), search);
		return MapTool.page(pages);
	}
}
