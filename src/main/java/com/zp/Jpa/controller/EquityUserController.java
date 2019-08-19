package com.zp.Jpa.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.service.EquityUserService;
import com.zp.Jpa.tools.MapTool;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/equityUser", name = "股东模块")
public class EquityUserController {
	@Autowired
	public EquityUserService equityUserService;
	/**
	 * http://localhost:8081/equityUser/findByRecommendId?uid=6035
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@ApiOperation(value = "查询一级下线", notes = "查询一级下线")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "uid",value = "用户id",required=true,defaultValue="6032", dataType = "int", paramType = "query"),})
	@RequestMapping(value="findByRecommendId",name="查询一级下线",method = RequestMethod.GET)
	public Map<String, Object> findByRecommendId(Integer uid,HttpServletRequest request, HttpServletResponse response) {
		return MapTool.findS(equityUserService.findByEId(uid), "查询一级下线") ;
	}
}
