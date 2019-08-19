package com.zp.Jpa.interceptor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener {
	
	@Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		httpSessionEvent.getSession().setMaxInactiveInterval(60*30);
    //会有自动过期时间,这里设置为30分钟后,如果session没反应就自动清除
		//在一个电脑的同一个浏览器,访问,只会存在一个session
		MySessionContext.AddSession(httpSessionEvent.getSession());
   
    }
	@Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        MySessionContext.DelSession(session);    
    }
}