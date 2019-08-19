package com.zp.Jpa.interceptor;

import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class MySessionContext {

    private static HashMap mymap = new HashMap();
   // private static HashMap uidSessionIdmap = new HashMap();

    public static synchronized void AddSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
       
        sysoMap();//打印mymap
        //将sessionId和uid绑定
//        if (session != null && uid != null) {
//        	uidSessionIdmap.put(uid, session.getId());
//        }
    }

    public static synchronized void DelSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
        }
        sysoMap();
      //将sessionId和uid绑定  清除
//        if (session != null && uid != null) {
//        	uidSessionIdmap.remove(uid);
//        }
    }
    public static synchronized void DelSessionBySessionId(String sessionId) {
        if (sessionId != null) {
            mymap.remove(sessionId);
        }
        sysoMap();

    }

    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null)
        return null;
        return (HttpSession) mymap.get(session_id);
    }
    public static void sysoMap() {
    	Set<String> set=mymap.keySet();
        for (String string : set) {
            System.out.println(string+" "+mymap.get(string));
         }
    }
    public static Integer getSessionSum() {
    	return mymap.size();
    }
}