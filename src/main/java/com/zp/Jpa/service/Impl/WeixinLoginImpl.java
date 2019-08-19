package com.zp.Jpa.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.Users;
import com.zp.Jpa.entity.sys.SysUser;
import com.zp.Jpa.entity.sys.SysUser_SysRole_tb;
import com.zp.Jpa.entity.sys.User_SysRole_tb;
import com.zp.Jpa.repository.SysUserRepository;
import com.zp.Jpa.repository.SysUserRoleRepository;
import com.zp.Jpa.repository.UserRoleRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.WeixinLogin;
import com.zp.Jpa.tools.GetAccessTokenUtil;
import com.zp.Jpa.tools.JsapiTicketUtil;
import com.zp.Jpa.tools.SHA1;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Suijishu;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;
import com.zp.tools.wexin.CommonUtil;

import net.sf.json.JSONObject;

@Service
public class WeixinLoginImpl implements WeixinLogin {
	// public final static String appid="wx42661ad619b7676b";
	// public final static String appsecret="55e6dad131ff56deb8da522587789c59";
	// 获取用户基本信息
	public final static String userInfoForOpenId = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	private static Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private UsersRepository user;
	@Autowired
	private SysUserRepository sysUserRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;
	
	@Autowired
	private SysUserServiceImpl sysUserServiceImpl;
	public Map<String, String> getWeixinLogin() {
		// TODO Auto-generated method stub
		String ticket = "";
		String url = "";
		// 获取token
		Map<String, String> token = GetAccessTokenUtil.getAccessToken(ConstantUtil.appid, ConstantUtil.appsecret);
		String accessToken = token.get("accessToken");
		// System.out.println("accessToken=>"+accessToken);
		url = token.get("url");
		// 获取ticket
		Map<String, String> ticketMap = JsapiTicketUtil.JsapiTicket(accessToken);
		ticket = ticketMap.get("ticket");
		String noncestr = Suijishu.getRandomString(23);// 随机字符串
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
		// 4获取url
		// 5、将参数排序并拼接字符串
		String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
		// 6、将字符串进行sha1加密
		String signature = SHA1.getSha1(str);
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", timestamp);
		map.put("accessToken", accessToken);
		map.put("ticket", ticket);
		map.put("noncestr", noncestr);
		map.put("signature", signature);
		return map;
	}

	@Override
	public Map<String, Object> getUserInfoForScope(String code, Integer Recommend) {
		// Map<String, Object> myResult = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		// code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
		// 1.引导用户进入授权页面同意授权，获取code
		// 通过前台获取
		// 2、通过code换取网页授权access_token（与基础支持中的access_token不同）

		// String object="传入的code是null或者是'',或者code请求超过一次";
		String access_token = "";
		String openid = "";
		if (!StringUtils.isEmpty(code)) {
			Map<String, String> map = GetAccessTokenUtil.getAccessTokenForCode(ConstantUtil.appid,
					ConstantUtil.appsecret, code);
			access_token = map.get("access_token");
			openid = map.get("openid");
		}

		// 3、如果需要，开发者可以刷新网页授权access_token，避免过期

		// 4、通过网页授权access_token和openid获取用户基本信息（支持UnionID机制）

		if (access_token != null) {
			Map<String, Object> map2 = GetAccessTokenUtil.getUserInfoForAccessTokenAndOpenid(access_token, openid);

			// 调用只是用appid和appsecret换取的 获取公众号是否关注信息;;
			String token = this.getWeixinLogin().get("accessToken");
			String requestUrl = userInfoForOpenId.replace("OPENID", openid).replace("ACCESS_TOKEN", token);
			JSONObject result = CommonUtil.httpsRequestToJsonObject(requestUrl, "GET", null, false);
			map2.put("data", result);
			// myResult.put("data", result);

			Users users = user.findByWeiXinFromOpenID(map2.get("openid").toString());
			if (!StringUtils.isEmpty(users)) {

				// 如果数据库中已经存在此用户,就返回数据库中存有的用户信息,
				map2.put("user", users);
				
				// myResult.put("user", users);
			} else {
				// 否则进行添加用户

				Users neUsers = new Users();

				Integer uid = 1;
				neUsers.setUTid(uid);

				neUsers.setPic(map2.get("headimgurl").toString());

				neUsers.setUserName(map2.get("nickname").toString());

				neUsers.setTrueName(map2.get("nickname").toString());

				neUsers.setPassWord((String) "123456");

				Object sex = map2.get("sex");
				neUsers.setSex(Integer.parseInt(sex.toString()));
				System.out.println("Recommend=>" + Recommend);
				if (!StringUtils.isEmpty(Recommend)) {
					neUsers.setRecommend(Recommend.toString());
				}

				neUsers.setBalance((long) 0.00);
				neUsers.setRecharge((long) 0.00);

				neUsers.setGiftCharge((long) 0.00);
				neUsers.setFreezeCharge((long) 0.00);
				neUsers.setVouchers((long) 0.00);
				neUsers.setIsDel(0);
				Object WeiXinFromOpenID = map2.get("openid");
				neUsers.setWeiXinFromOpenID(WeiXinFromOpenID.toString());
				try {
					// 因为userName不能为空并且username是和主键有关系,先保存,获取主键,再进行对username重新修改
					Users users2 = user.save(neUsers);

					// 进行会员编号
					String userName = users2.getUid().toString();
					// 前缀
					String pre = "wx";
					// 后缀
					String suf = "00000000";
					// 从右边开始去掉i个字符
					String str = suf.substring(0, suf.length() - userName.length());
					// 后缀
					str = str + userName;
					String resultUserName = pre + str;
					// 保存编号
					neUsers.setUserName(resultUserName);
					Users users3 = user.save(neUsers);
					map2.put("user", users3);
					
					//获取token
					//同时添加sysUs
					// myResult.put("user", users3);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			insertUserRole(users.getUid(),1);//添加用户角色关系
			SysUser sysUser=getSysUserByWeiXinUid(users.getUid());
			if(!StringUtils.isEmpty(sysUser)) {//如果存在这个账号了,那么就直接返回了;
				return map2;
			}
			//新添加一个管理员账号
			Map<String, Object> map=addSysUser(users);
			if(map.get("status").equals(1)) {
				SysUser sysUser2=(SysUser) map.get("data");//新添加成功的管理员
				if(!StringUtils.isEmpty(sysUser2)) {
					insertSysUserRole(sysUser2.getUserId(),1);//添加管理员用户角色关系
					map2.put("sysUser", sysUser2);
				}
				
			}
			return map2;
		} else {
			map.put("msg", "access_token為空");
		}
		return map;
	}
	/** 
	* @Title：getSysUserByWeiXinUid 
	* @Description：TODO  通过微信uid查询SysUser表,查询对应的管理员
	* @param ：@param uid
	* @param ：@return 
	* @return ：SysUser 
	* @throws 
	*/
	public SysUser getSysUserByWeiXinUid(Integer uid){
		return sysUserRepository.findByWeixinUid(uid);
	}
	private Integer insertSysUserRole(Integer userId, Integer roleId) {
		// TODO Auto-generated method stub
		//判断添加是否重复
				try {
					System.out.println("uid=>"+userId+"   roleId=>"+roleId);
					List<SysUser_SysRole_tb> sysUser_SysRole_tbs=sysUserRoleRepository.getSysUserSysRole(userId,roleId);
					if(sysUser_SysRole_tbs.size()>0) {//如果这个用户角色关系已经建立了
						System.out.println("这个用户角色关系已经建立了");
						return null;
						
					}
				} catch (ConverterNotFoundException e) {
					// TODO: handle exception
					System.out.println("这个用户角色关系已经建立了");
					return null;
				}
				
				Integer ok=sysUserRepository.setSysUserSysRole(userId,roleId);
				System.out.println("添加管理员用户角色关系成功的条数:"+ok);
				return ok;
	}

	/** 
	* @Title：addSysUser 
	* @Description：TODO 微信登录成功的同时,添加一个管理员账号并与之绑定
	* @param ：@param uid 
	* @return ：void 
	* @throws 
	*/
	private Map<String, Object> addSysUser(Users users) {
		//构建用户名   微信开头加四位随机数字,加四位随机字母数字组合,加上微信名
		String userName="微信"+Suijishu.getRandom(4)+Suijishu.getRandomString(4)+users.getTrueName(); 
		// TODO Auto-generated method stub
		String userPassWord="123456";
		Integer weixinUid=users.getUid();
		SysUser sysUser=new SysUser(userName,userPassWord,weixinUid);
		Map<String, Object> map=sysUserServiceImpl.addSysUser(sysUser);
		return map;
	}

	/** 
	* @Title：insertUserRole 
	* @Description：TODO  添加普通用户角色关系
	* @param ： 
	* @return ：void 
	* @throws 
	*/
	public Integer insertUserRole(Integer uid,Integer roleId) {
		//判断添加是否重复
		try {
			System.out.println("uid=>"+uid+"   roleId=>"+roleId);
			List<User_SysRole_tb> user_SysRole_tbs=userRoleRepository.getUserSysRole(uid,roleId);
			if(user_SysRole_tbs.size()>0) {//如果这个用户角色关系已经建立了
				System.out.println("这个用户角色关系已经建立了");
				return null;
				
			}
		} catch (ConverterNotFoundException e) {
			// TODO: handle exception
			System.out.println("这个用户角色关系已经建立了");
			return null;
		}
		
		Integer ok=user.setUserSysRole(uid,roleId);
		System.out.println("添加普通用户角色关系成功的条数:"+ok);
		return ok;
	}

}
