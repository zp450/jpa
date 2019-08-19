package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.annotate.SysControllerLog;
import com.zp.Jpa.entity.Adinfo;
import com.zp.Jpa.entity.queryentity.sys.DeleteObjects;
import com.zp.Jpa.entity.search.AdinfoSearch;
import com.zp.Jpa.myInterface.RedisHandel;
import com.zp.Jpa.service.AdinfoService;
import com.zp.Jpa.service.Impl.AdinfoServiceImpl;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;

//http://localhost:8081/swagger-ui.html   swagger2的api文档
@RestController
@CrossOrigin
@RequestMapping(value = "/adinfo", name = "首页轮播图邮轮旅游广告模块(微信)")
public class AdinfoController {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public AdinfoService adinfoService;
	@Autowired
	public AdinfoServiceImpl adinfoServiceImpl;
    @GetMapping("/hello")
   public  String hello(){
        //这个会失效
        meTest("德玛西亚",1);
        //这个通过Autowird service层的方法就不会失效   @Autowired  Test test;
        adinfoServiceImpl.test("德邦总管",1,2,"zzp",3); 
        return "hello";
   }

   @RedisHandel(key = "kkkk" ,keyField = "param1")
   private Object meTest(String str ,Integer i){
       System.out.println(str);
       System.out.println(i);
	return i;
   }

	/**
	 * http://localhost:8081/adinfo/queryAdinfo?page=1&size=10&isDel=0
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@ApiOperation(value = "多条件查询首页轮播图邮轮旅游广告模块", notes = "轮播图")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"), })
	@SysControllerLog(description="轮播图")
	@RequestMapping(value = "queryAdinfo", name = "多条件查询", method = RequestMethod.GET)
	public Map<String, Object> queryAdinfo(AdinfoSearch adinfoSearch) {
		// 为空 就是给0 未删除
				if (StringUtils.isEmpty(adinfoSearch.getIsDel())) {
					adinfoSearch.setIsDel(0);
				} 
		//分页查询  page size  查询条件     page 第几页  size 多少条
		Page<Adinfo> pages = adinfoService.queryAdinfo(adinfoSearch.getPage() - 1, adinfoSearch.getSize(),
				adinfoSearch);
		return MapTool.page(pages);
	}
	@ApiOperation(value = "批量删除首页轮播图邮轮旅游广告模块", notes = "批量删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "objects", value = "传id数组", required = true,  dataType = "int", paramType = "query"),
			 })
	//@SysControllerLog(description="轮播图")
	@RequestMapping(value = "deleteAdinfos", name = "批量删除", method = RequestMethod.POST)
	
	public Map<String, Object> deleteAdinfos(@RequestBody DeleteObjects objects){
		Object ids= objects.getIds();
		System.out.println(ids.toString());
		// 使用JSONArray
				int success = 0;
				JSONArray jsonArray = JSONArray.fromObject(ids);
				System.out.println(jsonArray.toString());
				
//				@SuppressWarnings("unchecked")
//				ArrayList<ShipOrderPeoples> datas = (ArrayList<ShipOrderPeoples>) JSONArray.toCollection(jsonArray,
//						ShipOrderPeoples.class);
				List<String> idStrings=(List<String>) JSONArray.toCollection(jsonArray,String.class);
		for(String soid:idStrings) {
			
			Integer adinfo=adinfoService.deleteAdinfo(Integer.parseInt(soid));
			if(adinfo>0) {success+=1;}
		}
		if(success>0) {
			map.put("status", 1);
			map.put("msg", "成功删除"+success+"个旅游订单");
		}else {
			map.put("status", 0);
			map.put("msg", "删除失败");
		}
		return map;
	}
	
	
}
