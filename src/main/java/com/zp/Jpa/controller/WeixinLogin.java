package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.weixinAppUtil.ConstantUtil;
import com.zp.tools.wexin.Weixin;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONException;
@RestController
@CrossOrigin
@RequestMapping(value = "/WeixinLogin", name = "微信登录模块")
public class WeixinLogin{
	//public Map<String, Object> map = new HashMap<String, Object>();
	
	/**http://120.26.71.141:8081/WeixinLogin/getWeixinToken
	 * http://2a49921k65.zicp.vip:55938/WeixinLogin/getWeixinToken
	 * http://localhost:8081/WeixinLogin/getWeixinToken
	 * 获得微信token
	 * @param url
	 * @return 
	 * timestamp,accessToken,ticket,noncestr,signature  map形式
	 * @throws JSONException 
	 */
	@ApiOperation(value = "获得微信token", notes = "获得微信token")
	@ApiImplicitParam(name = "url", value = "url", required = false, dataType = "String", paramType = "query")
	@RequestMapping(value = "/getWeixinToken", name = "获得微信token", method = RequestMethod.GET)
	public Object getWeixinToken(String url) throws JSONException {
		
		Map<String, Object> map=new HashMap<>();
		Map<String, String> result=new HashMap<>();
		Map<String, String> signature=new HashMap<>();
		map.put("msg", "获取微信token失败,fail");
		try {
			//获取token
			Map<String, String> tokenMap=Weixin.getToken(ConstantUtil.appid, ConstantUtil.appsecret);
			//jsapi_ticket和access token为7200的有效时间。7200后要重新获取
			String  token=tokenMap.get("access_token");
			//获取ticket
			Map<String, String> ticketMap=Weixin.getTicket(token);
			String ticket=ticketMap.get("ticket");
			result.put("token", token);			
			result.put("ticket", ticket);			
			//System.out.println("/getWeixinToken接受到的url==>"+url);
			//算出签名
			signature=Weixin.sign(ticket, url);
			
			map.put("signature", signature);
			map.put("data", result);
			map.remove("msg");
			map.put("msg", "获取信息成功,success");
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		return map;
	}

	
}
