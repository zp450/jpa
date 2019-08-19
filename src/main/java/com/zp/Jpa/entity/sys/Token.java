package com.zp.Jpa.entity.sys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 系统Token令牌对象:存储用户信息 和用户权限信息
 * 当登录成功以后把Token令牌信息响应给客户端,
 * 服务器每次通过token 对请求进行 登录拦截,权限拦截
 * @Description:   Token令牌对象
 * @author         Mashuai 
 * @Date           2018-5-17 上午12:12:35  
 * @Email          1119616605@qq.com
 */
@JsonInclude(Include.NON_NULL)	//如果该属性为NULL则不参与序列化
@Data
public class Token {
	
	/**
	 * 用户Id
	 */
	private Integer userId;
	/**
	 * 角色Id集合
	 */
	private List<Integer> roleIdList;
	/**
	 * 用户拥有的权限集合
	 */
	private List<String> permissionValueList;
	//账号密码
	private String userName;
	private String userPassWord;
	
	
	
	public Token(Integer userId, List<Integer> roleIdList, List<String> permissionValueList, String userName,
			String userPassWord) {
		// TODO Auto-generated constructor stub
		super();
		this.userId = userId;
		this.roleIdList = roleIdList;
		this.permissionValueList = permissionValueList;
		this.userName = userName;
		this.userPassWord = userPassWord;
	}
	public Token(Integer userId, List<Integer> roleIdList,
			List<String> permissionValueList) {
		super();
		this.userId = userId;
		this.roleIdList = roleIdList;
		this.permissionValueList = permissionValueList;
	}
	public Token(Integer userId, List<String> permissionValueList) {
		super();
		this.userId = userId;
		this.permissionValueList = permissionValueList;
	}
	public Token(List<String> permissionValueList) {
		super();
		this.permissionValueList = permissionValueList;
	}
	
	public Token() {
		super();
	}
	

}
