package com.zp.Jpa.service;

import java.util.Map;

public interface WeixinLogin {
	Map<String, String>  getWeixinLogin();
	Map<String, Object> getUserInfoForScope(String code,Integer Recommend);
  
}
