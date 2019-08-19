package com.zp.Jpa.aop;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zp.Jpa.controller.AdminController;
import com.zp.Jpa.entity.sys.Token;
import com.zp.Jpa.exception.NoPermissionException;
import com.zp.Jpa.exception.UnLoginException;
import com.zp.Jpa.tools.sys.ExceptionHandle;
import com.zp.Jpa.tools.sys.JwtToken;
import com.zp.Jpa.tools.sys.Result;
import com.zp.Jpa.tools.sys.SysUtils;
 
@Aspect
@Component
public class LoggerAOP {
	public static final Map<String, Object> map = new HashMap<String, Object>();
	private Logger logger = LoggerFactory.getLogger(LoggerAOP.class);
	@Autowired
    private ExceptionHandle exceptionHandle;
	@Autowired
	public AdminController adminController;
    //匹配com.zkn.learnspringboot.web.controller包及其子包下的所有类的所有方法
	@Pointcut("execution(public * com.zp.Jpa.controller.Equity*.*(..))")  
    public void executeService(){
 
    }
	@Before("executeService()")
	public Object log(JoinPoint joinPoint) throws UnLoginException, NoSuchMethodException, SecurityException, NoPermissionException, IOException {
		
		logger.info("@Before####################请求开始#####################");
		logger.info("记录日志@Before"+joinPoint.getArgs());
		System.out.println("记录日志@Before"+joinPoint.getArgs());
		
		//打印参数+---------begin--------
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //url
        logger.info("url={}",request.getRequestURL());
        //method
        logger.info("method={}",request.getMethod());
        //ip
        logger.info("id={}",request.getRemoteAddr());
        //class_method
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
        //args[]
        logger.info("args={}",joinPoint.getArgs());
      //打印参数+---------end--------
        
        
		Object[] args=joinPoint.getArgs();

Signature signature = joinPoint.getSignature();  
MethodSignature methodSignature = (MethodSignature)signature;    
Method targetMethod = methodSignature.getMethod(); 
		Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes()); 
		
		//		if( realMethod instanceof HandlerMethod ){
//			
//		}
		String token="";//token为空
//		realMethod.getParameterTypes();
		HttpServletRequest  httpRequest ;
		Integer uid;
		for(Object arg:args) {
			System.out.println("arg=>"+arg.toString());
			String canshuname=arg.toString();
			if(canshuname.contains("Request")) {
				//获取request,获取token
				httpRequest=(HttpServletRequest) arg;
				token=httpRequest.getParameter("token");
				//拦截uid,
//				if(canshuname.contains("uId")) {
//					uid=(Integer) arg;
//					//判断请求是否还在session集合中,如果在,就证明登录过,否则就是被挤下去,或者压根就没登录
//					boolean login=adminController.getSessionId(uid, request.getSession());
//					if(!login) {
//						System.out.println("该用户未登录,或者在别处登录");
//						map.put("code",500077);//该用户未登录,或者在别处登录
//						return map;
//					}
//				}
			}
			
			
		}
		
		System.out.println("aop切面 登陆拦截器  tokenStr=>" + token);
		if(token == null || "".equals(token)){// 模拟登陆拦截器
			System.out.println("未登录或请求没有携带Token,就想访问业务控制器,不存在的,怎么说?!!!");
			map.put("code",50008);//非法token
			
			throw new UnLoginException("未登录或请求没有携带合法Token!");// 抛出用户未登录异常,交给Spring容器异常处理机制
			
		}
		boolean result=tokenTest(token,realMethod);
		
		if(!result) {
			throw new NoPermissionException("还未登录,就想访问业务控制器,不能!!!");//抛出无权访问异常,交给Spring容器异常处理机制
		}
		map.put("code",0);//非法token
		System.out.println("token验证通过!!");
		return map;
		
	}
	@Around("executeService()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Result result = null;
//        try {
//
//        } catch (Exception e) {
//            return exceptionHandle.exceptionGet(e);
//        }
        if(result == null){
            return proceedingJoinPoint.proceed();
        }else {
            return result;
        }
    }

    @AfterReturning(pointcut = "executeService()",returning = "object")//打印输出结果
    public void doAfterReturing(Object object){
    	logger.info("response={}",object.toString());
    }
	
	private boolean tokenTest(String token,Method realMethod) throws NoPermissionException, IOException {
		// TODO Auto-generated method stub
		List<String> pList = null;//当前用户所拥有的权限集合
		//从token中取出用户信息
		Token tokenObject = null;
				try {
					 tokenObject = JwtToken.unsign(token, Token.class);// token过期会抛出 TokenExpiredException,交给Spring容器异常处理机制;
				}catch (TokenExpiredException e) {
					System.out.println("token过期了");
					map.put("code", 50014);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(tokenObject != null){
					pList = tokenObject.getPermissionValueList();//取出token中隐藏的用户权限集合
				}
				System.out.println("当前登录用户的权限集合=>"+pList);
				if( pList == null ){//当前未登录
					System.out.println("还未登录,就想访问业务控制器,怎么说?!!!");
					throw new NoPermissionException("还未登录,就想访问业务控制器,不能!!!");//抛出无权访问异常,交给Spring容器异常处理机制
//					return false;//拦截
				}
//				HandlerMethod hMethod = (HandlerMethod) realMethod;//请求映射方法
				//当前请求映射方法需要拥有的权限对象字符串
				String permissionValue = SysUtils.method2PermissionValue(realMethod);//从请求映射方法上取出需要的权限对象
				if(permissionValue == null) return true;//当前请求的方法没有name属性,则该方法不需要权限就可以访问
				System.err.println(" VS 访问当前方法需要权限=>"+permissionValue);
				
				//当前用户没有访问当前方法的权限则拦截
				if( pList != null && !pList.contains( permissionValue ) ){
					System.out.println("想来访问你权限范围之外的控制器,相当系统超级管理员你做梦吧!!!");
					throw new NoPermissionException("你无权访问,请做一个遵纪守法的公民!!!");//抛出无权访问异常,交给Spring容器异常处理机制
//					return false;//拦截
				}
				return true;
	}
	@After("executeService()")
	public void after(JoinPoint joinPoint) {
		logger.info("after####################请求开始#####################");
		logger.info("记录日志@after"+joinPoint.getArgs());
		System.out.println("记录日志@after"+joinPoint.getArgs());
	}
	

	
}
