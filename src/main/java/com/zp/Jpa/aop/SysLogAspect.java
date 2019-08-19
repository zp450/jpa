//package com.zp.Jpa.aop;
//
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.spi.LoggerFactory;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.google.common.base.Splitter;
//import com.zp.Jpa.annotate.SysControllerLog;
//import com.zp.Jpa.annotate.SysServiceLog;
//import com.zp.Jpa.entity.Action;
//import com.zp.Jpa.entity.Users;
//import com.zp.Jpa.entity.sys.SysUser;
//import com.zp.Jpa.service.ActionService;
//import com.zp.Jpa.service.Impl.SysUserServiceImpl;
//import com.zp.Jpa.tools.StringUtils;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.lang.reflect.Method;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
// 
///**
// * Title: SysControllerLog
// * @date 2018年8月31日
// * @version V1.0
// * Description: 切点类
// */
//@Aspect
//@Component
////@SuppressWarnings("all")
//public class SysLogAspect {
//	
//    //注入Service用于把日志保存数据库，实际项目入库采用队列做异步
//    @Autowired
//    private ActionService actionService;
//    @Autowired
//    private SysUserServiceImpl sysUserServiceImpl;
//    //本地异常日志记录对象
//    Logger logger=Logger.getLogger(SysLogAspect.class);
////    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
////   // private Logger logger = LoggerFactory.getLogger(LoggerAOP.class);
//
//    //匹配com.zkn.learnspringboot.web.controller包及其子包下的所有类的所有方法
//	@Pointcut("execution(public * com.zp.Jpa.controller.loginSysUser(..))")  
//    public void executeService(){
// 
//    }
////	@Before("executeService()")
////	public void log(JoinPoint joinPoint) {
////		
////		logger.info("@Before####################请求开始#####################");
////		logger.info("记录日志@Before"+joinPoint.getArgs());
////		System.out.println("记录日志@Before"+joinPoint.getArgs());
////	}
////	@After("executeService()")
////	public void after(JoinPoint joinPoint) {
////		logger.info("after####################请求开始#####################");
////		logger.info("记录日志@after"+joinPoint.getArgs());
////		System.out.println("记录日志@after"+joinPoint.getArgs());
////	}
//	
//    //Service层切点
////    @Pointcut("@annotation(com.zp.Jpa.annotate.SysServiceLog)")
////    public void serviceAspect(){
////    }
//// 
////    //Controller层切点
////    @Pointcut("@annotation(com.zp.Jpa.annotate.SysControllerLog)")
////    public void controllerAspect(){
////    }
//	public static String getParam(String url, String name) {
//	    String params = url.substring(url.indexOf("?") + 1, url.length());
//	    Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
//	    return split.get(name);
//	}
//    /**
//     * @Description  前置通知  用于拦截Controller层记录用户的操作
//     * @date 2018年9月3日 10:38
//     */
// 
//    @Before("executeService()")
//    public void doBefore(JoinPoint joinPoint){
//    	System.out.println("记录日志@Before"+joinPoint.getArgs());
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//       byte[] name=request.getRequestURI().getBytes();
//        //读取session中的用户
//       Object[] args=joinPoint.getArgs();
//   	SysUser users =null;
//   	if(StringUtils.isEmpty(args[0])) {
//   		users=(SysUser)args[0];
//   	
//     users=sysUserServiceImpl.findByUserName(users.getUserName());
//   	}
//    String ip = request.getRemoteAddr();
//        
// 
//       
//        try {
//            //*========控制台输出=========*//
//            System.out.println("==============前置通知开始==============");
//            System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
//            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
//            System.out.println("请求ip："+ip);
//            System.out.println("请求人："+users.getUserName());
//            System.out.println("请求人参数："+users.toString());
//            
//    		logger.info("@Before####################请求开始#####################");
//    		logger.info("记录日志@Before"+joinPoint.getArgs());
//    		
//            //*========数据库日志=========*//
//            Action action = new Action();
//            action.setActionDes(getControllerMethodDescription(joinPoint));
//            action.setActionType("0");
//            action.setActionIp(ip);
//            action.setUserId(users.getUserId());
//            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            action.setActionTime(simpleDateFormat.format(new Date()));
//            //保存数据库
//            actionService.addAction(action);
// 
//        }catch (Exception e){
//            //记录本地异常日志
//            logger.error("==前置通知异常==");
//            //logger.error("异常信息：{}",e.getMessage());
//        }
//    }
// 
////    /**
////     * @Description  异常通知 用于拦截service层记录异常日志
////     * @date 2018年9月3日 下午5:43
////     */
////    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
////    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        HttpSession session = request.getSession();
////        //读取session中的用户
////        Users users = (Users) session.getAttribute("users");
////        //获取请求ip
////        String ip = request.getRemoteAddr();
////        //获取用户请求方法的参数并序列化为JSON格式字符串
////        String params = "";
////        
////        if (!StringUtils.isEmpty(joinPoint.getArgs())){
////            for (int i = 0; i < joinPoint.getArgs().length; i++) {
////                params+= (joinPoint.getArgs()[i]).toString()+";";
////            }
////        }
////        try{
////            /*========控制台输出=========*/
////            System.out.println("=====异常通知开始=====");
////            System.out.println("异常代码:" + e.getClass().getName());
////            System.out.println("异常信息:" + e.getMessage());
////            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
////            System.out.println("方法描述:" + getServiceMethodDescription(joinPoint));
////            System.out.println("请求人:" + users.getUserName());
////            System.out.println("请求IP:" + ip);
////            System.out.println("请求参数:" + params);
////            /*==========数据库日志=========*/
////            Action action = new Action();
////            action.setActionDes(getServiceMethodDescription(joinPoint));
////            action.setActionType("1");
////            action.setUserId(users.getUid());
////            action.setActionIp(ip);
////            action.setActionTime(new Date());
////            //保存到数据库
////            actionService.addAction(action);
////        }catch (Exception ex){
////            //记录本地异常日志
////            logger.error("==异常通知异常==");
////            logger.error("异常信息:{}", ex.getMessage());
////        }
////    }
//// 
// 
//    /**
//     * @Description  获取注解中对方法的描述信息 用于service层注解
//     * @date 2018年9月3日 下午5:05
//     */
//    public static String getServiceMethodDescription(JoinPoint joinPoint)throws Exception{
//        String targetName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] arguments = joinPoint.getArgs();
//		Class targetClass = Class.forName(targetName);
//        Method[] methods = targetClass.getMethods();
//        String description = "";
//        for (Method method:methods) {
//            if (method.getName().equals(methodName)){
//                Class[] clazzs = method.getParameterTypes();
//                if (clazzs.length==arguments.length){
//                    description = method.getAnnotation(SysServiceLog.class).description();
//                    break;
//                }
//            }
//        }
//        return description;
//    }
// 
// 
// 
//    /**
//     * @author changyaofang
//     * @Description  获取注解中对方法的描述信息 用于Controller层注解
//     * @date 2018年9月3日 上午12:01
//     */
//    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
//        String targetName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();//目标方法名
//        Object[] arguments = joinPoint.getArgs();
//        Class targetClass = Class.forName(targetName);
//        Method[] methods = targetClass.getMethods();
//        String description = "";
//        for (Method method:methods) {
//            if (method.getName().equals(methodName)){
//                Class[] clazzs = method.getParameterTypes();
//                if (clazzs.length==arguments.length){
//                    description = method.getAnnotation(SysControllerLog.class).description();
//                    break;
//                }
//            }
//        }
//        return description;
//    }
//}
