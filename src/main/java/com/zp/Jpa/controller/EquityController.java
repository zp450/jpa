package com.zp.Jpa.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.http.HttpRequest;
import com.aliyuncs.http.HttpResponse;
import com.zp.Jpa.controller.father.FatherController;
import com.zp.Jpa.entity.Equity;
import com.zp.Jpa.entity.search.EquitySearch;
import com.zp.Jpa.service.EquityService;
import com.zp.Jpa.service.EquityUserService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/equity", name = "股份购买记录模块")
public class EquityController implements FatherController{
	@Override
	
	public Object pre(HttpRequest request,HttpResponse response,Object args) {
		return nullValue();
	};
	@Autowired
	public EquityService equityService;
	@Autowired
	public EquityUserService equityUserService;
	@Autowired
	public AdminController adminController;
	/**
	 * http://localhost:8081/equity/queryEquity?page=1&size=10&isDel=0
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@ApiOperation(value = "股份购买查询", notes = "股份购买查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"), })
	@RequestMapping(value = "queryEquity", name = "多条件查询", method = RequestMethod.GET)
	public Object queryEquity(EquitySearch equitySearch,HttpServletRequest request, HttpServletResponse response) {
		
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(equitySearch.getIsDel())) {
			equitySearch.setIsDel(0);
		}
		// 分页查询 page size 查询条件 page 第几页 size 多少条
		Page<Equity> pages = equityService.queryEquity(equitySearch.getPage() - 1, equitySearch.getSize(),
				equitySearch);
		return MapTool.page(pages);
	}

	@ApiOperation(value = "股份购买", notes = "股份购买,返回股份购买记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uId", value = "用户id", required = true, defaultValue = "6032", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "recommendId", value = "推荐人id", dataType = "int", required = false, defaultValue = "72", paramType = "query"),
			@ApiImplicitParam(name = "money", value = "金额", dataType = "int", required = true, defaultValue = "1000", paramType = "query"), })

	@RequestMapping(value = "addEquity", name = "购买股份", method = RequestMethod.POST)
	public Map<String, Object> addEquity(Equity equity,HttpServletRequest request, HttpServletResponse response) {
		if(adminController.getSessionId(equity.getUId(), request.getSession())) {
			return MapTool.saveObject(equityService.addEquity(equity), "购买股份");
		};
		return MapTool.returnMsg("您的账号已在别处登录,请考虑更改密码,或者联系管理员 QQ:123456789");
	}

}
