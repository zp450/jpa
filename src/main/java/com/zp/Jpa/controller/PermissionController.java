package com.zp.Jpa.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.zp.Jpa.entity.search.PermisssionQuerySearch;
import com.zp.Jpa.entity.sys.SysPermission;
import com.zp.Jpa.service.PermissionService;

/**
 * 
 * @Description: 权限控制器
 * @author zp
 */
@RestController
@RequestMapping(value = "/permission", name = "权限模块")
public class PermissionController {

	private Map<String, Object> map = new HashMap<String, Object>();

	@Autowired
	private ApplicationContext applicationContext;
	/**
	 * springmvc在启动时候将所有贴有请求映射标签：RequestMapper方法收集起来封装到该对象中
	 */
	// @Autowired
	// private RequestMappingHandlerMapping handlerMapping;//SpringMVC所有控制器中的请求映射集合
	@Autowired
	private PermissionService service;

	/**
	 * http://localhost:8081/permission/updatePermission 更新系统权限信息
	 */
	@RequestMapping(value = "/updatePermission", name = "更新系统权限")
	public Object updatePermission() {
		System.out.println("更新系统中所有权限...");
		int k = this.updateSysPermission();// 收集系统中所有权限数据更新到数据库
		System.out.println("系统中所有权限全部" + k + "条更新完毕 ^_^ ");

		return k;
	}

	/**
	 * 收集系统中所有权限数据更新到数据库
	 */
	public int updateSysPermission() {
		// 查询出数据库中现有的所有权限数据集合
		List<String> ownList = service.queryAll();
		//控制器
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		// 获取url与类和方法的对应信息
//		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		// SpringMVC所有控制器中的请求映射集合
		Map<RequestMappingInfo, HandlerMethod> requestMap = mapping.getHandlerMethods();
		// 获取所有controller中所有带有@RequestMapper注解的方法
		Collection<HandlerMethod> handlerMethods = requestMap.values();
		// 成功更新0条数据
		if (handlerMethods == null || handlerMethods.size() < 1)
			return 0;
		// 收集到的待新增的权限集合
		List<SysPermission> pList = new ArrayList<SysPermission>();
		// 待添加的权限对象
		SysPermission permission = null;
		// 遍历所有带有@RequestMapper注解的方法
		for (HandlerMethod method : handlerMethods) {
			// 从控制器映射方法上取出@RequestMapper注解对象
			RequestMapping anno = method.getMethodAnnotation(RequestMapping.class);
			// @RequestMapper注解有没有name备注是作为一个权限的标志
			// @RequestMapper注解写了name属性才做权限收集：所以@RequestMapper注解有没有name备注是作为一个权限的标志
			if (!"".equals(anno.name())) {
				// 带有@RequestMapper注解的方法所在控制器类上的RequestMapping注解对象
				RequestMapping namespaceMapping = method.getBeanType().getAnnotation(RequestMapping.class);
				// 得到RequestMapping注解的value值,即命名空间,即模块
				String namespace = namespaceMapping.value()[0];
				// 得到权限 ,例如：user:delete 用户模块的删除权限
				String permissionValue = (namespace + ":" + anno.value()[0]).replace("/", "");
				System.out.println("得到权限 ,例如：user:delete 用户模块的删除权限=>" + permissionValue + "权限说明:" + anno.name());
				// 如果数据库已经存储有这个注解权限,则忽略不收集
				if (ownList.contains(permissionValue))
					continue;
				// if( pList.contains(permissionValue) )continue;//如果已经收集到该权限,则忽略不再重复收集
				// 构造权限对象,三个参数:权限,模块说明,权限说明
				// 把权限控制注解@Permission解析为权限控制对象SysPermission,方便插入数据库权限表
				permission = new SysPermission(permissionValue, namespaceMapping.name(), anno.name());
				permission.setIsDel(0);//设置删除状态
				// 把数据库没有存储的,收集容器中也没有收集到的的权限加入收集容器中.
				pList.add(permission);
			}
		}
		return pList.size() > 0 ? service.batchInsert(pList) : 0;
	}


	/**
	 * localhost:8081/permission/getUrl
	 * 原始获取url
	 * @return
	 */
	@RequestMapping(value = "/getUrl", method = RequestMethod.GET)
	public Object getUrl() {
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		// 获取url与类和方法的对应信息
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		List<String> urlList = new ArrayList<>();
		for (RequestMappingInfo info : map.keySet()) {
			// 获取url的Set集合，一个方法可能对应多个url
			Set<String> patterns = info.getPatternsCondition().getPatterns();
			for (String url : patterns) {
				urlList.add(url);
			}
		}
		return urlList;
	}

	/**
	 * http://localhost:8081/permission/queryNode?roleId=1
	 * 查看权限树
	 */
	@RequestMapping(value = "/queryNode", name = "查看权限树")
	public Object queryNode(int roleId) {
		System.out.println("roleId=>" + roleId);
		return service.queryPermissionTree(roleId);
	}

	/**
	 * http://localhost:8081/permission/queryPermission?page=1&rows=10 查看全体权限
	 */
	@RequestMapping(value = "/queryPermission", name = "权限查询")
	public Map<String, Object> queryPermission(String permissionValue, String permissionModule, String permissionName, Integer page,
			Integer rows) {
		// 例如page=1 rows=10
		PermisssionQuerySearch pquery = new PermisssionQuerySearch(permissionValue, permissionModule, permissionName,
				(page - 1) * rows, rows);
		Page<SysPermission> page2=service.queryPermissionPage(pquery);
		List<SysPermission> aList = page2.getContent();
		Integer total = (int) page2.getTotalElements();
		//也可以这种拿取,但会请求两次,影响熟读
//		List<SysPermission> aList = service.queryPermission(pquery);
//		Integer total = service.totalPermission(pquery);
		map.put("total", total);
		map.put("rows", aList);

		return map;
	}

	@RequestMapping(value = "deletePermission", name = "从数据库中删除权限")
	public Integer deletePermission(Integer permissionId) {
		Integer deletePermission = service.deletePermission(permissionId);
		return deletePermission;
	}

	// @RequestMapping(value="/addPermission",name="权限添加")
	// public Integer addPermission(SysPermission permission){
	// return service.addPermission(permission);
	// }
	//
	/*
	 * RequestMapping接口的源码如下，里面定义了七个属性 public interface RequestMapping extends
	 * Annotation { // 指定映射的名称 public abstract String name(); // 指定请求路径的地址 public
	 * abstract String[] value(); // 指定请求的方式，是一个RequsetMethod数组，可以配置多个方法 public
	 * abstract RequestMethod[] method(); // 指定参数的类型 public abstract String[]
	 * params();
	 * 
	 * public abstract String[] headers(); // 指定数据请求的格式 public abstract String[]
	 * consumes(); // 指定返回的内容类型 public abstract String[] produces();
	 */

}
