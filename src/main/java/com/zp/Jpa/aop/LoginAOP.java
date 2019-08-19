package com.zp.Jpa.aop;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import com.zp.Jpa.myInterface.RedisHandel;

@Aspect
@Configuration
public class LoginAOP {
//
    //定义切点方法/Jpa/src/main/java/com/zp/Jpa/myInterface/RedisHandel.java
    @Pointcut("@annotation(com.zp.Jpa.myInterface.AdminLogin)")
    public  void pointCut(){}

    //环绕通知
    @Around("pointCut()")
    public void around(ProceedingJoinPoint joinPoint) {
        try{
            //1.获取到所有的参数值的数组
            Object[] args = joinPoint.getArgs();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            //2.获取到方法的所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();
            Method method = methodSignature.getMethod();
            System.out.println("---------------参数列表开始-------------------------");
            for (int i =0 ,len=parameterNames.length;i < len ;i++){
                System.out.println("参数名："+ parameterNames[i] + " = " +args[i]);
            }
            System.out.println("---------------参数列表结束-------------------------");
            RedisHandel redis=(RedisHandel)method.getAnnotation(RedisHandel.class);
            System.out.println("自定义注解 key:" + redis.key());
            System.out.println("自定义注解 keyField:" + redis.keyField());
            Class cla=method.getClass();
            if(cla.isAnnotationPresent(RedisHandel.class)){
                RedisHandel redisHandel =(RedisHandel)cla.getAnnotation(RedisHandel.class);
                String key=redisHandel.key();
                String keyField=redisHandel.keyField();
                System.out.println("key = " + key);
                System.out.println("keyField = " + keyField);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
