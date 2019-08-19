package com.zp.Jpa.service.Impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.UserEmailTest;
import com.zp.Jpa.repository.UserEmailTestRepository;
import com.zp.Jpa.tools.MailUtil;
import com.zp.Jpa.tools.StringUtils;

@Service
public class UserEmailTestService {
	@Autowired
	private UserEmailTestRepository userEmailTestRepository;
	
	public UserEmailTest addUserEmailTest(UserEmailTest userEmailTest) {
		userEmailTest.setState(0);
		String code= UUID.randomUUID().toString();
		userEmailTest.setCode(code);
		UserEmailTest userEmailTest2=userEmailTestRepository.save(userEmailTest);
		//利用正则表达式（可改进）验证邮箱是否符合邮箱的格式
	    if(!userEmailTest.getUserEmail().matches("^\\w+@(\\w+\\.)+\\w+$")){
	        return null;
	    }
		//boolean doRegister=doRegister(userEmailTest.getUserName(),userEmailTest.getUserPassWord(),userEmailTest.getUserEmail());
		
		if(!StringUtils.isEmpty(userEmailTest2)) {
			new Thread(new MailUtil(userEmailTest.getUserEmail(), code)).start();//添加数据库成功后,状态未激活,发送邮件
		}
		return userEmailTest2;
	}
	public void setUserEmailTestStateOneByCode(String code) {
		UserEmailTest userEmailTest2=userEmailTestRepository.findByCode(code);
		userEmailTest2.setState(1);//一代表激活
		UserEmailTest userEmailTest3=userEmailTestRepository.save(userEmailTest2);
		System.out.println("新保存的信息"+userEmailTest3.toString());
	}

	
}
