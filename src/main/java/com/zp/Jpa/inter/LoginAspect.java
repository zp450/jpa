//package com.zp.Jpa.inter;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//
//@Aspect//切面注解
//@Component//构成
//public class LoginAspect {
//	//设置在ManagerController的login方法的切点
//	//execution（* com.xyz.service.*.*（..））
//    @Pointcut("execution( * com.zp.Jpa.controller.*.*(..))")
//    public void login(){//切点映射，命名不规定
//        System.out.println("aspect");
//    }
//    //在本类的login执行之前
//    @Before("login()")
//    public void beforelogin()
//    {
//        System.out.println("before");
//    }
//    //在本类的login执行之后执行
//    @After("login()")
//    public void afterlogin(){System.out.println("after");}
////  //@Around是在函数运行的之前和之后，能决定是否运行函数
////    @Around("@annotation(NeedManagerPower)))")
////    public void checklogin()
////    {
////        System.out.println("around");
////    }
//
//    
////    /**
////     * 策略二，将整个ManagerController除了Public String login()方法全打上切面
////     * @param joinPoint
////     * @return
////     * @throws Throwable
////     */
////    //@Around("within(com.jieshao.evm.Controller.ManagerController)&&!execution(public String com.jieshao.evm.Controller.ManagerController.login(..))")
////    public String checklogin(ProceedingJoinPoint joinPoint) throws Throwable {
////        HttpSession session= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
////        if (session.getAttribute("username")!=null){
////            if(session.getAttribute("power").equals("666")) {
////                //通过登录和权限检测
////                //正常运行
////                return (String)joinPoint.proceed();
////            }else{
////                return "{\"errno\":\"-2\",\"errmsg\":\"管理员权限不足\"}";
////            }
////        }else{
////            return "{\"errno\":\"-1\",\"errmsg\":\"管理员未登录\"}";
////        }
////    }
//
//
//}
