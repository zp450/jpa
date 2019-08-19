package com.zp.Jpa.service.Impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zp.Jpa.entity.search.SysUserSearch;
import com.zp.Jpa.entity.sys.SysUser;
import com.zp.Jpa.entity.sys.Token;
import com.zp.Jpa.repository.SysUserRepository;
import com.zp.Jpa.service.SysUserService;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.MD5.PasswordMD5;
import com.zp.Jpa.tools.sys.JwtToken;

@Service
public class SysUserServiceImpl implements SysUserService {
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private SysUserRepository sysUserRepository;

	/**
	 * @Title：addSysUser @Description：TODO 添加管理员账号 @param ：@param uid @return
	 * ：void @throws
	 */
	@Override
	public Map<String, Object> addSysUser(SysUser sysUser) {
		// 添加之前需要判断
		// 用户名不可重复作判断 先根据用户名去数据库里查找
		SysUser sysUser2 = findByUserName(sysUser.getUserName());
		System.out.println("数据库中存在的sysUser2=>" + sysUser2);
		// 如果可以查到该用户 那么用户名重复
		if (!StringUtils.isEmpty(sysUser2)) {
			map.put("status", 0);
			map.put("msg", "该用户名已经有人用过了,换一个吧");
			map.put("data", 00000);
		} else {
			// 添加用户
			SysUser sysUser3 = add(sysUser);
			map.put("status", 1);
			map.put("msg", "注册成功,赶快去登录吧");
			map.put("data", sysUser3);
		}

		return map;
	}

	public SysUser add(SysUser sysUser) {

		sysUser.setIsDel(0);
		sysUser.setUserIsLockout(0);
		sysUser.setUserLevel(0);
		System.out.println("sysUser=>" + sysUser);
		// TODO Auto-generated method stub
		// 密码加密
		String newpassWord = PasswordMD5.getNewPassWord(sysUser.getUserName(), sysUser.getUserPassWord());
		sysUser.setUserPassWord(newpassWord);
		sysUser.setUserIsLockout(0);
		Date date = new Date();
		sysUser.setUserUpdateTime(date);
		sysUser.setUserCreateTime(date);
		sysUser.setUserPassWrongCout(0);

		return sysUserRepository.save(sysUser);
	}

	@Override
	public SysUser deleteSysUser(SysUser sysUser) {
		// TODO Auto-generated method stub
		SysUser sysUser2 = sysUserRepository.findOne(sysUser.getUserId());
		sysUser2.setIsDel(1);
		return sysUserRepository.save(sysUser2);
	}

	@Override
	public Map<String, Object>  saveSysUser(SysUser sysUser) {
		// TODO Auto-generated method stub
		SysUser sysUser2=sysUserRepository.findOne(sysUser.getUserId());
		// 修改之前需要判断
				// 用户名不可重复作判断 先根据用户名去数据库里查找
				SysUser sysUser3 = findByUserName(sysUser.getUserName());
				System.out.println("修改的时候数据库中存在的sysUser3=>" + sysUser3);
				// 如果可以查到该用户 那么用户名重复
				if (!StringUtils.isEmpty(sysUser2)) {
					map.put("status", 0);
					map.put("msg", "该用户名已经有人用过了,换一个吧");
					map.put("data", 00000);
				} else {
					// 修改用户
					//前端不能控制的值
					sysUser.setUserPassWord(sysUser.getUserPassWord());
					sysUser.setUserUpdateTime(new Date());
					SysUser sysUser4 = sysUserRepository.save(sysUser);
					map.put("status", 1);
					map.put("msg", "修改成功");
					map.put("data", sysUser4);
				}

				return map;
		
	}

	public SysUser findByUserName(String userName) {

		return sysUserRepository.findByUserName(userName);
	}

	@Override
	public Page<SysUser> querySysUser(Integer page, Integer size, SysUserSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "userCreateTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return sysUserRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<SysUser> getWhereClause(final SysUserSearch search) {
		return new Specification<SysUser>() {
			@Override
			public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("userCreateTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("userCreateTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getUserName() != null && !" ".equals(search.getUserName().trim())) {
					exList.add(cb.like(root.<String>get("userName"), "%" + search.getUserName() + "%"));
				}
				if (search.getUserIsLockout() != null) {
					exList.add(cb.equal(root.<Integer>get("userIsLockout"), search.getUserIsLockout()));
				}
				if (search.getUserLastLoginIp() != null && !" ".equals(search.getUserName().trim())) {
					exList.add(cb.like(root.<String>get("userLastLoginIp"), "%" + search.getUserLastLoginIp() + "%"));
				}
				if (search.getUserEmail() != null && !" ".equals(search.getUserEmail().trim())) {
					exList.add(cb.like(root.<String>get("userEmail"), "%" + search.getUserEmail() + "%"));
				}
				if (search.getUserTelephone() != null && !" ".equals(search.getUserTelephone().trim())) {
					exList.add(cb.like(root.<String>get("userTelephone"), "%" + search.getUserTelephone() + "%"));
				}
				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public List<SysUser> findAll() {
		// TODO Auto-generated method stub
		return sysUserRepository.findAll();
	}

	public Map<String, Object> getLoginMap(SysUser sysUser) {
		// 登录用户
		SysUser sysUser2 = loginSysUser(sysUser);
		// 如果该用户的是已删除和已经锁定的状态,就返回 错误信息
		if (!StringUtils.isEmpty(sysUser2)) {
			if (sysUser2.getIsDel() == 1 || sysUser2.getUserIsLockout() == 1) {
				map.put("status", 2);
				map.put("msg", "用户已被锁定或删除,请联系管理员tom  QQ:123456789");
				map.put("data", null);
				return map;

			}
		} // 如果该用户在数据库中能查询到且不为空
		if (!StringUtils.isEmpty(sysUser2)) {
			Integer loginStates = sysUser2.getLoginStates();

			map.put("status", 1);
			map.put("msg", "登录成功");
			map.put("data", sysUser2);
			// 登录成功,错误密码次数清零
			sysUser2.setUserPassWrongCout(0);

			Integer uid = sysUser2.getUserId();
			insertSysUserRole(uid);// 添加关系

			// 如果登录状态显示已登录,提示用户修改密码
			if (!StringUtils.isEmpty(loginStates) && loginStates.equals(1)) {
				// 如果是已登录的状态
				map.put("code", 50009);
				map.put("msg", "您的账号已在别处登录,考虑修改密码");
			}
			SysUser userAdmin = sysUserRepository.findByUserName(sysUser.getUserName());
			String password = userAdmin.getUserPassWord();// 先保存一下这个密码,后面会在保存用户信息的时候防止加密前密码覆盖
			// 因为要传过去需要登录用的账号密码,c 更改密码为加密前的
			userAdmin.setUserPassWord(sysUser.getUserPassWord());
			// 如果登录成功,赋予token token组成由 用户信息,用户权限信息,用户模块信息组成
			Map<String, Object> token = getTokenAndList(userAdmin);
			sysUser2.setUserPassWord(password);// 取消用户的错误登录次数
			saveSysUser(sysUser2);
			// 固定格式
			map.put("code", 0);
			map.put("roles", token.get("roles"));
			map.put("name", sysUser2.getUserName());// 当前登录用户名
			// token令牌判定,每次前端请求,都会携带这个token,然后后端解析出该用户的权限,和访问方法权限作为比较,以此来作为权限限制,
			map.put("token", token.get("token"));// 响应给客户端的token令牌
			// 登录成功后改变管理员的登录状态;
			setLogined(sysUser2.getUserId());// 1为已登录

			map.put("uid", sysUser2.getUserId());

			map.put("roleIdList", token.get("roleIdList"));// 角色集合
			map.put("permissionValueList", token.get("permissionValueList"));// 响应给客户端的当前用户权限

		} else {
			SysUser sysUser3 = sysUserRepository.findByUserName(sysUser.getUserName());
			// 如果该用户的是已删除和已经锁定的状态,就返回 错误信息
			if (!StringUtils.isEmpty(sysUser3)) {
				if (sysUser3.getIsDel() == 1 || sysUser3.getUserIsLockout() == 1) {
					map.put("code", 1);
					map.put("status", 0);
					map.put("msg", "用户已被锁定或删除,请联系管理员tom  QQ:123456789");
					map.put("data", null);
					return map;

				}
			}
			map.put("status", 0);
			map.put("msg", "登录名或密码错误");
			map.put("data", sysUser2);

		}
		return map;
	}

	/**
	 * @Title：insertSysUserRole @Description：TODO 登录成功的同时,向用户角色表中添加关系; @param
	 * ：@param uid @return ：void @throws
	 */
	private void insertSysUserRole(Integer uid) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取token
	 * 
	 * @param sysUser
	 * @return
	 */
	public Map<String, Object> getTokenAndList(SysUser sysUser) {
		Integer uid = sysUser.getUserId();
		Map<String, Object> map = new HashMap<String, Object>();
		// 根据用户Id查询出该用户的所有角色Id
		List<Integer> roleIdList = queryRoleIdByUserId(uid);
		// System.out.println(sysUser.getUserName()+"的角色ids=>"+roleIdList);

		// 角色们
		List<String> roles = queryRoleBySysUserId(uid);
		// 根据用户Id查询出该用户的所有权限
		List<String> permissionValueList = queryPermissionValueByUserId(uid);
		// 构造一个token对象,存储用户和权限信息

		// 将用户的登录名和密码封装到token中
		String userName = sysUser.getUserName();
		String userPassWord = sysUser.getUserPassWord();

		Token tokenObj = new Token(uid, roleIdList, permissionValueList, userName, userPassWord);
		String token = null;
		try {
			token = JwtToken.sign(tokenObj, 4 * 60 * 60 * 1000);// 4*60*60*1000 四个小时有效期的token
			System.out.println("生成token大小=>" + token.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		map.put("token", token);
		map.put("roleIdList", roleIdList);
		map.put("roles", roles);

		map.put("permissionValueList", permissionValueList);

		return map;
	}

	/**
	 * 获取token
	 * 
	 * @param User
	 * @return
	 */
	public Map<String, Object> getTokenAndListUsers(Integer uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 根据普通用户Id查询出该用户的所有角色Id
		List<Integer> roleIdList = queryRoleIdByUserIdUsers(uid);
		System.out.println("的角色ids=>" + roleIdList);

		// 根据用户Id查询出该用户的所有权限
		List<String> permissionValueList = queryPermissionValueByUserIdUsers(uid);
		for (String per : permissionValueList) {
			System.out.println(per.toString());
		}
		// 构造一个token对象,存储用户和权限信息
		Token tokenObj = new Token(uid, roleIdList, permissionValueList);
		String token = null;
		try {
			token = JwtToken.sign(tokenObj, 4 * 60 * 60 * 1000);// 4*60*60*1000 四个小时有效期的token
			System.out.println("生成token大小=>" + token.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		map.put("token", token);
		map.put("roleIdList", roleIdList);
		map.put("permissionValueList", permissionValueList);
		return map;
	}

	/**
	 * 管理员登录
	 * 
	 * @param sysUser
	 * @return
	 */
	public SysUser loginSysUser(SysUser sysUser) {
		// TODO Auto-generated method stub
		String newPassWord = PasswordMD5.getNewPassWord(sysUser.getUserName(), sysUser.getUserPassWord());
		SysUser sysUser2 = sysUserRepository.findByUserNameAndUserPassWord(sysUser.getUserName(), newPassWord);
		Date date = new Date();
		// 登录名或密码错误
		System.out.println("sysUser2=>" + sysUser2);
		// 进行 密码错误次数判定
		if (StringUtils.isEmpty(sysUser2)) {
			System.out.println("登录名或密码错误=========对用户名进行锁定,密码错误次数判断");
			SysUser sysUser3 = sysUserRepository.findByUserName(sysUser.getUserName());
			if (!StringUtils.isEmpty(sysUser3)) {
				Integer wrongCount = sysUser3.getUserPassWrongCout();
				// 如果错误次数大于10次请联系管理员
				if (wrongCount > 10) {
					sysUser3.setUserIsLockout(1);

					sysUser3.setUserLockoutTime(date);
					sysUserRepository.save(sysUser3);
					return sysUser2;
				}
				sysUser3.setUserPassWrongCout(wrongCount + 1);
				sysUserRepository.save(sysUser3);
				return sysUser2;
			}

		} else {
			// 成功登录
			sysUser2.setUserLastLoginTime(date);// 更新上次成功登录时间
			sysUserRepository.save(sysUser2);
		}
		return sysUser2;

	}

	public Map<String, Object> isLockOrIsDel(SysUser sysUser3) {
		if (!StringUtils.isEmpty(sysUser3)) {
			if (sysUser3.getIsDel() == 1 || sysUser3.getUserIsLockout() == 1) {
				map.put("status", 0);
				map.put("msg", "用户已被锁定或删除,请联系管理员tom  QQ:123456789");
				map.put("data", null);
				return map;

			}
		}
		return map;
	};

	public SysUser findByName(String userName) {
		// TODO Auto-generated method stub
		return sysUserRepository.findByUserName(userName);
	}

	/**
	 * 根据管理员用户Id查询出该用户的所有角色Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> queryRoleIdByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserRepository.queryRoleIdByUserId(userId);
	}

	/**
	 * 根据管理员用户Id查询出该用户的所有角色
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryRoleBySysUserId(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserRepository.queryRoleBySysUserId(userId);
	}

	/**
	 * 根据普通用户Id查询出该用户的所有角色Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> queryRoleIdByUserIdUsers(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserRepository.queryRoleIdByUserIdUsers(userId);
	}

	public List<String> queryPermissionValueByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserRepository.queryPermissionIdByUserId(userId);
	}

	// 普通用户查询权限
	public List<String> queryPermissionValueByUserIdUsers(Integer userId) {
		// TODO Auto-generated method stub
		return sysUserRepository.queryPermissionIdByUserIdUsers(userId);
	}

	@Override
	public void logout(Integer userId) {
		// TODO Auto-generated method stub
		SysUser sysUser = sysUserRepository.findOne(userId);
		sysUser.setLoginStates(0);// 0代表未登录
		sysUserRepository.save(sysUser);
	}

	// 更改用户的登录装状态为已登录
	public void setLogined(Integer userId) {
		// TODO Auto-generated method stub
		SysUser sysUser = sysUserRepository.findOne(userId);
		sysUser.setLoginStates(1);
		sysUserRepository.save(sysUser);
	}

	// 更改用户的登录装状态为已下线
	public void setUnLogin(Integer userId) {
		// TODO Auto-generated method stub
		SysUser sysUser = sysUserRepository.findOne(userId);
		if (!StringUtils.isEmpty(sysUser)) {
			sysUser.setLoginStates(0);
			sysUserRepository.save(sysUser);
		}

	}
	/** 通过微信uid 来查询出对应的管理员
	* @Title：getSysUserByWeixinUid 
	* @Description：TODO
	* @param ：@param weixinUid
	* @param ：@return 
	* @return ：SysUser 
	* @throws 
	*/
	public SysUser getSysUserByWeixinUid(Integer weixinUid) {
		return sysUserRepository.findByWeixinUid(weixinUid);
	}
	
	public Map<String, Object> changeSysUserPassWord(Integer uid,String oldPassWord,String newPassWord) {
		SysUser sysUser=sysUserRepository.findOne(uid);
		String passWord=sysUser.getUserPassWord();//拿到数据库中加密过的 原来的登录密码
		//如果用户没有设置过支付密码
		if(StringUtils.isEmpty(passWord)) {
			//用加密后的新密码
			String newPassWordMD5=PasswordMD5.getNewPassWord(sysUser.getUserName(), newPassWord);
			//进行对用户信息更新
			sysUser.setUserPassWord(newPassWordMD5);
			//保存用户信息
			SysUser sysUser2=sysUserRepository.save(sysUser);
			if(!StringUtils.isEmpty(sysUser2)) {
				map.put("status", 1);
				map.put("msg", "设置新密码成功");
			}else {
				map.put("status", 0);
				map.put("msg", "密码设置失败,用户信息保存出错,请稍后再试");
			}
			return map;
		}
		
		//如果老密码和新密码相同返回失败
		if(newPassWord.equals(oldPassWord)) {
			map.put("status", 0);
			map.put("msg", "老密码和新密码相同,登录密码修改失败");
		}else {
			//验证用户输入的老密码是否正确
			String pass=PasswordMD5.getNewPassWord(sysUser.getUserName(), oldPassWord);
			
			if(pass.equals(passWord)) {
				//用加密后的新密码
				String newPassWordMD5=PasswordMD5.getNewPassWord(sysUser.getUserName(), newPassWord);
				//进行对用户信息更新
				sysUser.setUserPassWord(newPassWordMD5);
				//保存用户信息
				SysUser SysUser3=sysUserRepository.save(sysUser);
				if(!StringUtils.isEmpty(SysUser3)) {
					map.put("status", 1);
					map.put("msg", "密码修改成功");
					map.put("data", "您的新密码:"+newPassWord);
				}else {
					map.put("status", 0);
					map.put("msg", "密码修改失败,用户信息保存出错,请稍后再试");
				}
			}else {
				map.put("status", 0);
				map.put("msg", "密码修改失败,用户原有密码验证失败");
			}
		}
		return map;
	}
	
	// /**用户修改mi
	// * @param sysUser
	// * @return
	// */
	// public Object changePassWord(SysUser sysUser) {
	// // TODO Auto-generated method stub
	//
	// return null;
	// }

}
