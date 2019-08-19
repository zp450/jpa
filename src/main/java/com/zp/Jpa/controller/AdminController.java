package com.zp.Jpa.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.util.concurrent.RateLimiter;
import com.zp.Jpa.entity.UserUser;
import com.zp.Jpa.entity.search.SysUserSearch;
import com.zp.Jpa.entity.sys.SysUser;
import com.zp.Jpa.entity.sys.Token;
import com.zp.Jpa.interceptor.MySessionContext;
import com.zp.Jpa.service.Impl.SysUserServiceImpl;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.RsaTest;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.sys.JwtToken;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping(value = "/admin", name = "管理员(微信)模块")
public class AdminController {
	public Map<String, Object> map = new HashMap<String, Object>();
	Map<Integer, String> uidSessionIdmap = new HashMap<Integer, String>();// uid, session.getId()
	@Autowired
	public SysUserServiceImpl sysUserServiceImpl;
	final RateLimiter rateLimiter = RateLimiter.create(5);// 大小为5的令牌桶
	// 第一秒会双倍缓存 10个;
	// http://localhost:8081/admin/killProduct??name=tom

	@RequestMapping(value="/killProduct", method = RequestMethod.GET)
	public String killProduct(String name) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
		// 定义特殊用户
		int i = 1;
		if (name.equals("jack")) {
			i = 5; // 每秒请求只能变成一次;
		}
		if (rateLimiter.tryAcquire(i)) {// 尝试从令牌桶中拿一个令牌 true
			// 如果拿到
			// 省略业务处理;;;
			System.out.println("秒杀时间=>" + simpleDateFormat.format(new Date()) + ",用户:" + name);
		} else {
			return "对不起,你的商品没有了";
		}

		return "支付成功";
	}

	/**
	 * http://localhost:8081/admin/query?page=1&size=10&isDel=0
	 * 
	 * @param sysUserSearch
	 * @return
	 */
	@ApiOperation(value = "分页多条件查询管理员", notes = "查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数,如果是前台页面显示,传1就好", required = true, defaultValue = "1", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "要多少条", dataType = "int", required = true, defaultValue = "10", paramType = "query"),
			@ApiImplicitParam(name = "isDel", value = "是否已经删除", dataType = "integer", required = true, defaultValue = "0", paramType = "query"), })
	@RequestMapping(value = "querySysUser", name = "多条件查询", method = RequestMethod.GET)
	public Map<String, Object> querySysUser(SysUserSearch sysUserSearch) {
		// 分页查询 page size 查询条件 page 第几页 size 多少条
		// 为空 就是给0 未删除
		if (StringUtils.isEmpty(sysUserSearch.getIsDel())) {
			sysUserSearch.setIsDel(0);
		}
		Page<SysUser> pages = sysUserServiceImpl.querySysUser(sysUserSearch.getPage() - 1, sysUserSearch.getSize(),
				sysUserSearch);
		// int i=0;
		// i=1/0;
		return MapTool.page(pages);
	}

	@ApiOperation(value = "管理员注册添加", notes = "添加管理员")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userName", value = "用户名", required = true, defaultValue = "tom", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "userPassWord", value = "密码", dataType = "String", required = true, defaultValue = "000000", paramType = "query"), })
	@RequestMapping(value = "addSysUser", method = RequestMethod.POST)
	public Map<String, Object> addSysUser(SysUser sysUser) {

		return sysUserServiceImpl.addSysUser(sysUser);
	}

	// *****************登录模块*****************************************
	// @NeedManagerPower//自定义的注解
	// {"userName":"admin","userPassWord":"K7a+SR18euUV+Lfm8tSrVbRh9YJ5pI9nH9tRkIN9YEZZU5KL762KVCzgEvf5H4t/yp1FaagZM5ZkhD0+HZEYBojpzYh2E2gUCqButprv0YGMWaUk4stGkTzPCkLrN5mVlUroAIXKvjM0D25gIbRoy50sBM7R0c+t0SwNahu/Uv8="}
	@ApiOperation(value = "管理员登录", notes = "管理员登录(传Token)")
	@ApiImplicitParams({
			// @ApiImplicitParam(name = "userName", value = "用户名", required = false,
			// defaultValue = "tom", dataType = "String", paramType = "query"),
			// @ApiImplicitParam(name = "userPassWord", value = "密码", dataType = "String",
			// required = false, defaultValue = "000000", paramType = "query"),
			@ApiImplicitParam(name = "token", value = "json字符串", required = false, defaultValue = "{\"userName\":\"admin\",\"userPassWord\":\"K7a+SR18euUV+Lfm8tSrVbRh9YJ5pI9nH9tRkIN9YEZZU5KL762KVCzgEvf5H4t/yp1FaagZM5ZkhD0+HZEYBojpzYh2E2gUCqButprv0YGMWaUk4stGkTzPCkLrN5mVlUroAIXKvjM0D25gIbRoy50sBM7R0c+t0SwNahu/Uv8=\"}", dataType = "String", paramType = "query"), })
	// @SysControllerLog(description="管理员登录")
	@RequestMapping(value = "loginSysUser", method = RequestMethod.POST)
	public Map<String, Object> loginSysUser(@RequestParam(required=true) String token, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("接收的token=>" + token);// 接受到的token
		// 转换成对象
		// JSONObject jsonObject=JSONObject.parseObject(jsonString);
		// UserUser user = (UserUser)
		// JSONObject.toJavaObject(jsonObject,UserUser.class);
		// 从token中取出用户信息
		Token tokenObject = null;
		try {
			tokenObject = JwtToken.unsign(token, Token.class);// token过期会抛出 TokenExpiredException,交给Spring容器异常处理机制;
			System.out.println("解析过的token=>"+tokenObject);
		} catch (TokenExpiredException e) {
			System.out.println("token过期了");
			map.put("code", 50014);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String userPassWord = tokenObject.getUserPassWord();
		String userName = tokenObject.getUserName();
		// RSA解密
//		String decryptData = RsaTest.decrypt(userPassWord, RsaTest.getPrivateKey(ConstantUtil.RSAsecret));
//		System.out.println("解密后内容:" + decryptData);

		// 如果前端没有传ip,那么就用request获取ip地址
		String ipInteger = request.getRemoteAddr();
		SysUser sysUser = new SysUser(userName, userPassWord, !StringUtils.isEmpty(ipInteger) ? ipInteger : "0");
		System.out.println("token 登录rsa解密后传入参数=>" + sysUser);

		Map<String, Object> map = sysUserServiceImpl.getLoginMap(sysUser);
		Integer uid;
		if (!StringUtils.isEmpty(map.get("uid"))) {
			uid = (Integer) map.get("uid");
			System.out.println("登录后传入uid=>"+uid+"   进行添加session");
			addSession(uid, request, response);
			String sessionId = (String)uidSessionIdmap.get(uid);
			System.out.println("登录成功的时候√√√√√√√uidSessionIdmap中"+uid+"对应的sessionId=>"+sessionId);
			map.put("sessionId", sessionId);
		}
		
		
		//不用传新生成的token了,业务改变
		map.remove("token");
		return map;
	}

	@ApiOperation(value = "获取管理员登录Token", notes = "管理员登录Token")
	@ApiImplicitParams({
			// @ApiImplicitParam(name = "userName", value = "用户名", required = false,
			// defaultValue = "tom", dataType = "String", paramType = "query"),
			// @ApiImplicitParam(name = "userPassWord", value = "密码", dataType = "String",
			// required = false, defaultValue = "000000", paramType = "query"),
			@ApiImplicitParam(name = "jsonString", value = "json字符串", required = false, defaultValue = "{\"userName\":\"admin\",\"userPassWord\":\"K7a+SR18euUV+Lfm8tSrVbRh9YJ5pI9nH9tRkIN9YEZZU5KL762KVCzgEvf5H4t/yp1FaagZM5ZkhD0+HZEYBojpzYh2E2gUCqButprv0YGMWaUk4stGkTzPCkLrN5mVlUroAIXKvjM0D25gIbRoy50sBM7R0c+t0SwNahu/Uv8=\"}", dataType = "String", paramType = "query"), })
	// @SysControllerLog(description="管理员登录")
	@RequestMapping(value = "getLoginSysUserToken", method = RequestMethod.POST)
	public String getLoginSysUserToken(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("接收的=>" + jsonString);
		// 转换成对象
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		UserUser user = (UserUser) JSONObject.toJavaObject(jsonObject, UserUser.class);
		// RSA解密
		String decryptData = RsaTest.decrypt(user.getUserPassWord(), RsaTest.getPrivateKey(ConstantUtil.RSAsecret));
		System.out.println("解密后内容:" + decryptData);

		// 如果前端没有传ip,那么就用request获取ip地址
		String ipInteger = request.getRemoteAddr();
		SysUser sysUser = new SysUser(user.getUserName(), decryptData,
				!StringUtils.isEmpty(ipInteger) ? ipInteger : "0");
		System.out.println("登录rsa解密后传入参数=>" + sysUser);

		Map<String, Object> map = sysUserServiceImpl.getLoginMap(sysUser);
		Integer uid;
		if (!StringUtils.isEmpty(map.get("uid"))) {
			uid = (Integer) map.get("uid");
			//账号密码=>token=>解析token=>账号密码=>登录,这里不添加session
//			addSession(uid, request, response);
		}

		System.out.println("返回给前端的token=>"+(String)map.get("token"));
		return map.get("token").toString();
	}

	// 注销
	@ApiOperation(value = "管理员注销", notes = "管理员注销")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户id", required = false, defaultValue = "2", dataType = "int", paramType = "query"), })
//	@SysControllerLog(description = "管理员注销")
	@RequestMapping(value = "loginOut", method = RequestMethod.POST)
	public void loginOut( @RequestParam(required=true)String userId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("注销收到的userId=>"+userId);
//		JSONObject jsonObject = JSONObject.parseObject(jsonString);
//		UserUser user = (UserUser) JSONObject.toJavaObject(jsonObject, UserUser.class);
//		Integer uid = user.getUserId();
		// 更改这个人的状态
		Integer uid=Integer.parseInt(userId);
		sysUserServiceImpl.setUnLogin(uid);// 更改登录状态为登出 0
		//
		delSession(uid, request, response);// 清除关系和session
		System.out.println("用户id"+uid+"已注销");

	}

	@ApiOperation(value = "删除管理员", notes = "删除管理员")
	@ApiImplicitParam(name = "userId", value = "用户名", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "deleteSysUser", name = "删除管理员", method = RequestMethod.GET)
	public Integer deleteSysUser(SysUser sysUser) {
		SysUser sysUser2 = sysUserServiceImpl.deleteSysUser(sysUser);
		if (sysUser2 != null) {
			return 1;
		}
		return 0;
	}

	
	
	@ApiOperation(value = "获取当前会话状态(轮询调用)", notes = "获取当前会话状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, defaultValue = "3", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "sessionId", value = "sessionId", dataType = "String", required = true, defaultValue = "BE0E8044AEFC0A46F1F103B149EE0409", paramType = "query"), })
	@RequestMapping(value = "getSessionStates", name = "获取当前session(会话)是否还存在", method = RequestMethod.GET)
	public Integer getSessionStates(@RequestParam(value = "userId") String userId,@RequestParam(value = "sessionId") String sessionId) {
		
		System.out.println("轮询轮询获取当前session(会话)是否还存在的userId=>"+userId+"   sessionId=>"+sessionId);
		//就是看看别人有没有登录我的号,把我连接服务器的session清空掉,
		Integer uid=Integer.parseInt(userId);
		String sessionIded = (String)uidSessionIdmap.get(uid);
		System.out.println("uidSessionIdmap中"+userId+"对应的sessionId(已经存在的)=>"+sessionIded);
		 // 1. entrySet遍历，在键和值都需要时使用（最常用）
        for (Map.Entry<Integer, String> entry : uidSessionIdmap.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            
        }
        //如果传进来的sessionId,还能对应后台中存储的sessionId 那么就证明没有被挤下去
        if (sessionId.equals(sessionIded) ) {
			System.out.println("轮询获取当前会话状态session会话还存在");
			return 0;// session会话还存在
			
		}
		System.out.println("轮询获取当前会话状态session会话已消失");
		return 1;// session会话已清除,
	}
	@ApiOperation(value = "保存管理员个人信息", notes = "保存管理员个人信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sysUser", value = "sysUser", required = false, defaultValue = "3", dataType = "String", paramType = "query"),
		 })
	@RequestMapping(value = "saveSysUserInfo", name = "保存管理员个人信息", method = RequestMethod.POST)
	public Map<String, Object> saveSysUserInfo(SysUser sysUser) {
		return sysUserServiceImpl.saveSysUser(sysUser);
	}

	// http://localhost:8081/admin/getOnline
	@RequestMapping(value = "getOnline", name = "获取当前在线人数", method = RequestMethod.GET)
	public Integer getOnline() {
		MySessionContext.sysoMap();
		return MySessionContext.getSessionSum();
	}

	// http://localhost:8081/admin/addSession
	@RequestMapping(value = "addSession", method = RequestMethod.GET)
	public void addSession(Integer uid, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		System.out.println("sessionId=>" + session.getId());
		// 修改键值对
		// 如果已存在用户进行登录,用户已经登录了
		Set<Integer> set = uidSessionIdmap.keySet();
		for (Integer uu : set) {
			System.out.println(uu + ":" + uidSessionIdmap.get(uu));
			if (!StringUtils.isEmpty(uid)) {
				if (uu.equals(uid) && uidSessionIdmap.get(uu) != session.getId()) {// 如果在绑定过的map中已经存在这个用户id,那么就进行清除原有的session和原有的绑定键值对//挤下去原来登录的账号原理
					String sessionId = uidSessionIdmap.get(uu).toString();
					// 清除以前存在的关系
					MySessionContext.DelSessionBySessionId(sessionId);// 清除session集合中该
																		// uid对应的sessionid对应的session会话;---开始清除存储的会话
					uidSessionIdmap.remove(uu);// 清除绑定关系uid-sessionId
					System.out.println("该用户的原有session信息已清空,提示原有登录成功的用户进行修改登录密码");
				}
			}
		}
		// sysUserServiceImpl.setUnLogin(uid);//更改用户登录状态 //用户还是不变的,用户的状态都是登录
		System.out.println("添加的uid=>"+uid+"   添加的sessionId=>"+session.getId());
		uidSessionIdmap.put(uid, session.getId());// 将用户id和当前的sessionid进行绑定 ///覆盖和添加uid和sessionId对应的关系
		MySessionContext.AddSession(session);// 添加sessionid和session对应 //添加会话
		System.out.println("当前连接session数量=>" + MySessionContext.getSessionSum());
	}

	// 判断请求是否还在session集合中,如果在,就证明登录过,否则就是被挤下去,或者压根就没登录
	public boolean getSessionId(Integer uid, HttpSession session) {
		String sessionId = uidSessionIdmap.get(uid).toString();
		return sessionId.equals(session.getId());
	}

	// http://localhost:8081/admin/delSession
	@RequestMapping(value = "delSession", method = RequestMethod.GET)
	public void delSession(Integer uid, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		System.out.println("sessionId=>" + session.getId());
		sysUserServiceImpl.setUnLogin(uid);// 更改为登出状态
		MySessionContext.DelSession(session);// 清除session
		// 清除以前存在的关系
		uidSessionIdmap.remove(uid);// 清除绑定关系uid-sessionId
		System.out.println("当前连接session数量=>" + MySessionContext.getSessionSum());
	}
	@ApiOperation(value = "根据微信uid来获取管理员", notes = "根据微信uid来获取管理员")
	@ApiImplicitParam(name = "weixinUid", value = "微信uid", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "getSysUserByWeixinUid", method = RequestMethod.GET)
	public SysUser getSysUserByWeixinUid(Integer weixinUid) {
		
		return  sysUserServiceImpl.getSysUserByWeixinUid(weixinUid);
		
	}
	
	
	/**
	 * @param uid
	 * @param oldPayPassWord
	 * @param newPayPassWord
	 * @return
	 */
	@ApiOperation(value = "管理员修改自己的登录密码", notes = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "管理员用户id ", required = true, dataType = "string", paramType = "query", defaultValue = "3"),
			@ApiImplicitParam(name = "oldPassWord", value = "用户老密码", required = true, dataType = "string", paramType = "query", defaultValue = "123564"),
			@ApiImplicitParam(name = "newPassWord", value = "用户新密码", required = true, dataType = "string", paramType = "query", defaultValue = "111111"),
			 })
	@RequestMapping(value = "changeSysUserPassWord", name = "用户修改密码", method = RequestMethod.POST)
	public Object changeSysUserPassWord(String userId,String oldPassWord ,String newPassWord) {
		Integer uid=Integer.parseInt(userId);
		 return sysUserServiceImpl.changeSysUserPassWord(uid,oldPassWord,newPassWord);
	}

	// public Object changePassWord(SysUser sysUser) {
	// return sysUserServiceImpl.changePassWord(sysUser);
	// }

}
