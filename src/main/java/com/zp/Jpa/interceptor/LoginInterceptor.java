//package com.zp.Jpa.interceptor;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zp.Jpa.entity.sys.Token;
//import com.zp.Jpa.exception.NoPermissionException;
//import com.zp.Jpa.exception.UnLoginException;
//import com.zp.Jpa.sys.result.Result;
//import com.zp.Jpa.tools.HttpsUrlConnection.HttpUtil;
//import com.zp.Jpa.tools.sys.JwtToken;
//import com.zp.Jpa.tools.sys.SysUtils;
//
//
///**
// * 单点登录过滤
// * 
// * @Description: 单点登录过滤拦截器
// * @author zhangpu
// */
//public class LoginInterceptor extends HandlerInterceptorAdapter {}
