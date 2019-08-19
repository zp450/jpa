package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.entity.UserEmailTest;
import com.zp.Jpa.service.Impl.UserEmailTestService;

@CrossOrigin
@RestController
@RequestMapping(value="/userEmailTest" )
public class UserEmailTestContoller {
	public Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	public UserEmailTestService userEmailTestImpl;
	//http://localhost:8081/userEmailTest/addUserEmailTest?userName=tom&userEmail=1987896027@qq.com&userPassWord=123456
	@RequestMapping(value = "/addUserEmailTest", method = RequestMethod.GET)
	public Object addUserEmailTest(UserEmailTest userEmailTest) {
		return userEmailTestImpl.addUserEmailTest(userEmailTest);
		
	}
	//http://localhost:8081/userEmailTest/changeUserEmailTestState?code=eed437d5-fa16-4dd6-af1a-4ee3e0964974
	@RequestMapping(value = "/changeUserEmailTestState", method = RequestMethod.GET)
	public void changeUserEmailTestState(String  code) {
		System.out.println("code=>"+code+"有新用户验证邮箱");
		 userEmailTestImpl.setUserEmailTestStateOneByCode(code);
		
	}
}
