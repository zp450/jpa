package com.zp.Jpa.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zp.Jpa.entity.sys.Token;
import com.zp.Jpa.exception.NoPermissionException;
import com.zp.Jpa.exception.UnLoginException;
import com.zp.Jpa.sys.result.Result;
import com.zp.Jpa.tools.sys.JwtToken;
import com.zp.Jpa.tools.sys.SysUtils;

/**
 * 是否登录过滤
 * 
 * @Description: 登录过滤拦截器
 * @author Mashuai
 * @Date 2018-5-10 下午6:39:06
 * @Email 1119616605@qq.com
 */
public class TokenLoginInterceptor extends HandlerInterceptorAdapter {

	/**
	 * RequestMapping 执行之前,执行preHandle
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		List<String> pList = null;//当前用户所拥有的权限集合
		System.out.println("TokenLoginInterceptor preHandle 被拦截的方法是----------handler => " + handler);
		String path = request.getServletPath();// 获取请求的当前项目下的资源 , 匹配得到: 当前项目/xxx/**/xxx.xxx 例如 /sys/login
		System.out.println("LoginInterceptor拦截器 获取请求的当前项目下的资源=>" + path);
		if (path.matches(SysUtils.NO_INTERCEPTOR_PATH)) {// 不拦截的URL正则表达式
			System.out.println("不拦截的URL正则表达式匹配成功,放行 ^_^ ");
			return true;
		}
		response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域访问
		if( handler instanceof HandlerMethod ){
		// 模拟登录成功
		// String user = "我是张三，我的权限在这里";

		// String token = JwtToken.sign(user, 3000);
		// System.out.println("token1=>"+token);
		String token = request.getParameter("token");
		System.out.println("TokenLoginInterceptor 登陆拦截器  tokenStr=>" + token);
		if(token == null || "".equals(token)){// 模拟登陆拦截器
			System.out.println("未登录或请求没有携带Token,就想访问业务控制器,不存在的,怎么说?!!!");
			throw new UnLoginException("未登录或请求没有携带合法Token!");// 抛出用户未登录异常,交给Spring容器异常处理机制
		}

		
		//从token中取出用户信息
		Token tokenObject = JwtToken.unsign(token, Token.class);// token过期会抛出 TokenExpiredException,交给Spring容器异常处理机制
		
		if(tokenObject != null){
			pList = tokenObject.getPermissionValueList();//取出token中隐藏的用户权限集合
		}
		System.out.println("当前登录用户的权限集合=>"+pList);
		if( pList == null ){//当前未登录
			System.out.println("还未登录,就想访问业务控制器,怎么说?!!!");
			throw new NoPermissionException("还未登录,就想访问业务控制器,不能!!!");//抛出无权访问异常,交给Spring容器异常处理机制
//			return false;//拦截
		}
		HandlerMethod hMethod = (HandlerMethod) handler;//请求映射方法
		//当前请求映射方法需要拥有的权限对象字符串
		String permissionValue = SysUtils.method2PermissionValue(hMethod);//从请求映射方法上取出需要的权限对象
		if(permissionValue == null) return true;//当前请求的方法没有name属性,则该方法不需要权限就可以访问
		System.err.println(" VS 访问当前方法需要权限=>"+permissionValue);
		
		//当前用户没有访问当前方法的权限则拦截
		if( pList != null && !pList.contains( permissionValue ) ){
			System.out.println("想来访问你权限范围之外的控制器,相当系统超级管理员你做梦吧!!!");
			throw new NoPermissionException("你无权访问,请做一个遵纪守法的公民!!!");//抛出无权访问异常,交给Spring容器异常处理机制
//			return false;//拦截
		}
		// Thread.currentThread().sleep(5000);//当前主线程睡眠5秒

		// 模拟过期
		/*
		 * String expiredUser = null; try { expiredUser = JwtToken.unsign(token,
		 * String.class); }catch (TokenExpiredException e) { System.out.println("过期了");
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 * System.out.println("expiredUser=>"+expiredUser);
		 */
		}else{
			System.out.println("LoginInterceptor preHandle 被拦截的非控制器资源是----------handler => "+handler);
		}
		return true;
	}

	/**
	 * RequestMapping 执行之后,执行postHandle,此时并未真正分发视图
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// System.out.println("TokenLoginInterceptor postHandle ----------modelAndView
		// => "+modelAndView);
		// modelAndView.setViewName("");//可以控制分发的视图
		response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域访问
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	}

	/**
	 * RequestMapping 执行之后,执行postHandle,视图也分发完毕. 1. 相当于 finally 2.方法执行异常
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("TokenLoginInterceptor afterCompletion ----------ex => " + ex);
		response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域访问

		// 如果有异常信息
		if (ex != null) {
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter print = null;
			try {
				print = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("Tokenlogin ex异常出现了,然后我就给设置无权限访问了");
				print.print(new ObjectMapper().writeValueAsString(new Result(3)));// 3无权访问
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

}
